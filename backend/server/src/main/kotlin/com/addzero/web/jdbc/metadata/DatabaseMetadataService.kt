package com.addzero.web.jdbc.metadata

import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.sql.Connection
import java.sql.DatabaseMetaData
import javax.sql.DataSource

@Service
class DatabaseMetadataService(private val dataSource: DataSource) {

    fun getPrimaryKeysMetadata(schema: String = "public"): List<PrimaryKeyMetadata> {
        return dataSource.connection.use { connection ->
            val metadata = connection.metaData
            val primaryKeys = metadata.getPrimaryKeys(null, schema, null)
            val result = mutableListOf<PrimaryKeyMetadata>()

            while (primaryKeys.next()) {
                result.add(
                    PrimaryKeyMetadata(
                        tableName = primaryKeys.getString("TABLE_NAME"),
                        columnName = primaryKeys.getString("COLUMN_NAME"),
                        keySeq = primaryKeys.getShort("KEY_SEQ"),
                        pkName = primaryKeys.getString("PK_NAME")
                    )
                )
            }

            result.sortedWith(compareBy({ it.tableName }, { it.keySeq }))
        }
    }

    fun getForeignKeysMetadata(schema: String = "public"): List<ForeignKeyMetadata> {
        return dataSource.connection.use { connection ->
            val metadata = connection.metaData
            val foreignKeys = metadata.getExportedKeys(null, schema, null)
            val result = mutableListOf<ForeignKeyMetadata>()

            while (foreignKeys.next()) {
                result.add(
                    ForeignKeyMetadata(
                        pkTableName = foreignKeys.getString("PKTABLE_NAME"),
                        pkColumnName = foreignKeys.getString("PKCOLUMN_NAME"),
                        fkTableName = foreignKeys.getString("FKTABLE_NAME"),
                        fkColumnName = foreignKeys.getString("FKCOLUMN_NAME"),
                        keySeq = foreignKeys.getShort("KEY_SEQ"),
                        fkName = foreignKeys.getString("FK_NAME"),
                        pkName = foreignKeys.getString("PK_NAME")
                    )
                )
            }

            result.sortedWith(compareBy({ it.pkTableName }, { it.fkTableName }, { it.keySeq }))
        }
    }

    fun getColumnsMetadata(schema: String = "public"): List<ColumnMetadata> {
        return dataSource.connection.use { connection ->
            val metadata = connection.metaData
            val columns = metadata.getColumns(null, schema, null, null)
            val result = mutableListOf<ColumnMetadata>()

            while (columns.next()) {
                val tableName = columns.getString("TABLE_NAME")
                val columnName = columns.getString("COLUMN_NAME")
                val dataType = columns.getString("TYPE_NAME")
                val columnSize = columns.getInt("COLUMN_SIZE")
                val nullable = columns.getInt("NULLABLE")

                // 生成MD5哈希ID
                val id = generateMd5("$tableName.$columnName")

                // 转换数据类型
                val normalizedType = normalizeDataType(dataType)

                // 转换可空标志
                val nullableFlag = if (nullable == DatabaseMetaData.columnNoNulls) "NO" else "YES"

                result.add(
                    ColumnMetadata(
                        md5 = id,
                        tableName = tableName,
                        columnName = columnName,
                        columnType = normalizedType,
                        columnLength = if (normalizedType in listOf("varchar", "char")) columnSize else null,
                        nullableFlag = nullableFlag
                    )
                )
            }

            // 按表名和列的位置排序
            result.sortedWith(compareBy({ it.tableName }, { getColumnPosition(connection, schema, it.tableName, it.columnName) }))
        }
    }

    private fun normalizeDataType(dataType: String): String {
        return when (dataType.lowercase()) {
            "character varying" -> "varchar"
            "varchar" -> "varchar"
            "character" -> "char"
            else -> dataType.lowercase()
        }
    }

    private fun generateMd5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        val digest = md.digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    private fun getColumnPosition(connection: Connection, schema: String, tableName: String, columnName: String): Int {
        connection.createStatement().use { statement ->
            val query = """
                SELECT ordinal_position 
                FROM information_schema.columns 
                WHERE table_schema = '$schema' 
                AND table_name = '$tableName' 
                AND column_name = '$columnName'
            """.trimIndent()

            statement.executeQuery(query).use { rs ->
                if (rs.next()) {
                    return rs.getInt(1)
                }
            }
        }
        return Int.MAX_VALUE
    }
}
