package com.addzero.ai.mcp

import org.springframework.ai.tool.annotation.Tool
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 🗄️ 数据库操作 MCP 服务
 *
 * 提供数据库查询、DDL执行、表结构查询等功能的 MCP 工具集合。
 * 使用 Spring Boot 3.2+ 的 JdbcClient 进行数据库操作。
 */
@Service
@Transactional(readOnly = true)
class JdbcService(
    private val jdbcClient: JdbcClient
) {

    /**
     * 📊 查询表结构信息
     *
     * @param tableName 表名，如果为空则查询所有表
     * @return 表结构信息的格式化字符串
     */
    @Tool(description = "查询数据库表结构信息，包括列名、数据类型、约束等详细信息")
    fun queryTableStructure(tableName: String = ""): String {
        return try {
            if (tableName.isBlank()) {
                // 查询所有表
                val tables = jdbcClient.sql(
                    """
                    SELECT table_name, table_comment
                    FROM information_schema.tables
                    WHERE table_schema = 'public'
                    ORDER BY table_name
                """
                )
                    .query { rs, _ ->
                        "${rs.getString("table_name")} - ${rs.getString("table_comment") ?: "无注释"}"
                    }
                    .list()

                "数据库中的表 (${tables.size} 个):\n" + tables.joinToString("\n")
            } else {
                // 查询指定表的结构
                val columns = jdbcClient.sql(
                    """
                    SELECT column_name, data_type, is_nullable, column_default,
                           character_maximum_length, column_comment
                    FROM information_schema.columns
                    WHERE table_name = :tableName
                    ORDER BY ordinal_position
                """
                )
                    .param("tableName", tableName)
                    .query { rs, _ ->
                        val columnName = rs.getString("column_name")
                        val dataType = rs.getString("data_type")
                        val maxLength = rs.getInt("character_maximum_length")
                        val nullable = rs.getString("is_nullable")
                        val defaultValue = rs.getString("column_default")
                        val comment = rs.getString("column_comment")

                        val typeInfo = if (maxLength > 0) "$dataType($maxLength)" else dataType
                        val nullableInfo = if (nullable == "YES") "NULL" else "NOT NULL"
                        val defaultInfo = if (defaultValue != null) " DEFAULT $defaultValue" else ""
                        val commentInfo = if (comment != null) " -- $comment" else ""

                        "$columnName $typeInfo $nullableInfo$defaultInfo$commentInfo"
                    }
                    .list()

                if (columns.isEmpty()) {
                    "表 '$tableName' 不存在或无权限访问"
                } else {
                    "表 '$tableName' 的结构 (${columns.size} 列):\n" + columns.joinToString("\n")
                }
            }
        } catch (e: Exception) {
            "查询表结构失败: ${e.message}"
        }
    }

    /**
     * 🔍 执行自定义SQL查询
     *
     * @param sql SQL查询语句（仅支持SELECT语句）
     * @param limit 结果限制数量，默认50条
     * @return 查询结果的格式化字符串
     */
    @Tool(description = "执行自定义SQL查询语句，仅支持SELECT查询，返回格式化的查询结果")
    fun executeQuery(sql: String, limit: Int = 50): String {
        return try {
            // 安全检查：只允许SELECT语句
            val trimmedSql = sql.trim().uppercase()
            if (!trimmedSql.startsWith("SELECT")) {
                return "安全限制：只允许执行SELECT查询语句"
            }

            // 添加LIMIT限制
            val finalSql = if (trimmedSql.contains("LIMIT")) {
                sql
            } else {
                "$sql LIMIT $limit"
            }

            val results = jdbcClient.sql(finalSql)
                .query { rs, _ ->
                    val metaData = rs.metaData
                    val columnCount = metaData.columnCount
                    val row = mutableMapOf<String, Any?>()

                    for (i in 1..columnCount) {
                        val columnName = metaData.getColumnName(i)
                        row[columnName] = rs.getObject(i)
                    }
                    row
                }
                .list()

            if (results.isEmpty()) {
                "查询无结果"
            } else {
                val headers = results.first().keys
                val headerLine = headers.joinToString(" | ")
                val separator = headers.joinToString(" | ") { "-".repeat(it.length.coerceAtLeast(3)) }

                val dataLines = results.take(limit).map { row ->
                    headers.joinToString(" | ") { header ->
                        row[header]?.toString()?.take(20) ?: "NULL"
                    }
                }

                "查询结果 (${results.size} 行):\n$headerLine\n$separator\n" +
                        dataLines.joinToString("\n")
            }
        } catch (e: Exception) {
            "SQL查询执行失败: ${e.message}"
        }
    }

    /**
     * 🔧 执行DDL语句（创建表等）
     *
     * @param ddl DDL语句
     * @return 执行结果
     */
    @Tool(
        description = """
     执行DDL语句，如CREATE TABLE、ALTER TABLE等数据库结构操作,
    要包含id, update_by ,create_by ,create_time, update_time四个字段
    """
    )
    @Transactional(readOnly = false)
    fun executeDDL(ddl: String): String {
        return try {
            // 安全检查：只允许特定的DDL语句
            val trimmedDdl = ddl.trim().uppercase()
            val allowedDdlKeywords = listOf("CREATE", "ALTER", "DROP", "COMMENT")

            if (!allowedDdlKeywords.any { trimmedDdl.startsWith(it) }) {
                return "安全限制：只允许执行CREATE、ALTER、DROP、COMMENT等DDL语句"
            }

            jdbcClient.sql(ddl).update()
            "DDL语句执行成功: ${ddl.take(100)}${if (ddl.length > 100) "..." else ""}"

        } catch (e: Exception) {
            "DDL语句执行失败: ${e.message}"
        }
    }

    /**
     * 🔍 根据列名查询所在表
     *
     * @param columnName 列名（支持模糊匹配）
     * @param exactMatch 是否精确匹配，默认false（模糊匹配）
     * @return 包含该列的表信息
     */
    @Tool(description = "根据列名查询该列存在于哪些表中，支持精确匹配和模糊匹配")
    fun findTablesByColumn(columnName: String, exactMatch: Boolean = false): String {
        return try {
            if (columnName.isBlank()) {
                return "请提供要查询的列名"
            }

            val searchCondition = if (exactMatch) {
                "c.column_name = :columnName"
            } else {
                "c.column_name ILIKE :searchPattern"
            }

            val searchValue = if (exactMatch) columnName else "%$columnName%"

            val results = jdbcClient.sql(
                """
                SELECT
                    c.table_name,
                    c.column_name,
                    c.data_type,
                    c.is_nullable,
                    c.column_default,
                    c.character_maximum_length,
                    c.numeric_precision,
                    c.numeric_scale,
                    c.ordinal_position,
                    pd.description AS column_comment
                FROM
                    information_schema.columns c
                        LEFT JOIN
                    pg_catalog.pg_statio_all_tables st ON
                        c.table_schema = st.schemaname AND
                        c.table_name = st.relname
                        LEFT JOIN
                    pg_catalog.pg_description pd ON
                        pd.objoid = st.relid AND
                        pd.objsubid = c.ordinal_position
                WHERE
                    c.table_schema = 'public'
                  AND $searchCondition
                ORDER BY
                    c.table_name, c.ordinal_position
            """
            )
                .param(if (exactMatch) "columnName" else "searchPattern", searchValue)
                .query { rs, _ ->
                    mapOf(
                        "tableName" to rs.getString("table_name"),
                        "columnName" to rs.getString("column_name"),
                        "dataType" to rs.getString("data_type"),
                        "isNullable" to rs.getString("is_nullable"),
                        "columnDefault" to rs.getString("column_default"),
                        "maxLength" to rs.getInt("character_maximum_length"),
                        "numericPrecision" to rs.getInt("numeric_precision"),
                        "numericScale" to rs.getInt("numeric_scale"),
                        "comment" to rs.getString("column_comment"),
                        "position" to rs.getInt("ordinal_position")
                    )
                }
                .list()

            if (results.isEmpty()) {
                val matchType = if (exactMatch) "精确匹配" else "模糊匹配"
                "未找到包含列名 '$columnName' 的表 ($matchType)"
            } else {
                val groupedByTable = results.groupBy { it["tableName"] as String }
                val matchType = if (exactMatch) "精确匹配" else "模糊匹配"

                """
                找到 ${results.size} 个列在 ${groupedByTable.size} 个表中 ($matchType '$columnName'):

                ${
                    groupedByTable.entries.joinToString("\n\n") { (tableName, columns) ->
                        """
                    📋 表: $tableName (${columns.size} 个匹配列)
                    ${
                            columns.joinToString("\n") { column ->
                                val dataType = column["dataType"] as String
                                val maxLength = column["maxLength"] as Int
                                val precision = column["numericPrecision"] as Int
                                val scale = column["numericScale"] as Int
                                val nullable = if ((column["isNullable"] as String) == "YES") "NULL" else "NOT NULL"
                                val defaultValue = (column["columnDefault"] as String?)?.let { " DEFAULT $it" } ?: ""
                                val comment = (column["comment"] as String?)?.let { " -- $it" } ?: ""
                                val position = column["position"] as Int

                                val typeInfo = when {
                                    maxLength > 0 -> "$dataType($maxLength)"
                                    precision > 0 && scale > 0 -> "$dataType($precision,$scale)"
                                    precision > 0 -> "$dataType($precision)"
                                    else -> dataType
                                }

                                "  [$position] ${column["columnName"]} $typeInfo $nullable$defaultValue$comment"
                            }
                        }
                    """.trimIndent()
                    }
                }
                """.trimIndent()
            }
        } catch (e: Exception) {
            "查询列信息失败: ${e.message}"
        }
    }

    /**
     * 🔗 查询表之间的关联关系
     *
     * @param tableName 表名，如果为空则查询所有外键关系
     * @return 表关联关系信息
     */
    @Tool(description = "查询表之间的外键关联关系，了解数据库表结构设计")
    fun findTableRelations(tableName: String = ""): String {
        return try {
            val whereClause = if (tableName.isNotBlank()) {
                "AND (tc.table_name = :tableName OR ccu.table_name = :tableName)"
            } else {
                ""
            }

            val sql = """
                SELECT
                    tc.table_name as source_table,
                    kcu.column_name as source_column,
                    ccu.table_name as target_table,
                    ccu.column_name as target_column,
                    tc.constraint_name,
                    rc.update_rule,
                    rc.delete_rule
                FROM information_schema.table_constraints tc
                JOIN information_schema.key_column_usage kcu
                    ON tc.constraint_name = kcu.constraint_name
                    AND tc.table_schema = kcu.table_schema
                JOIN information_schema.constraint_column_usage ccu
                    ON ccu.constraint_name = tc.constraint_name
                    AND ccu.table_schema = tc.table_schema
                LEFT JOIN information_schema.referential_constraints rc
                    ON tc.constraint_name = rc.constraint_name
                    AND tc.table_schema = rc.constraint_schema
                WHERE tc.constraint_type = 'FOREIGN KEY'
                  AND tc.table_schema = 'public'
                  $whereClause
                ORDER BY tc.table_name, tc.constraint_name
            """

            val query = if (tableName.isNotBlank()) {
                jdbcClient.sql(sql).param("tableName", tableName)
            } else {
                jdbcClient.sql(sql)
            }

            val relations = query.query { rs, _ ->
                mapOf(
                    "sourceTable" to rs.getString("source_table"),
                    "sourceColumn" to rs.getString("source_column"),
                    "targetTable" to rs.getString("target_table"),
                    "targetColumn" to rs.getString("target_column"),
                    "constraintName" to rs.getString("constraint_name"),
                    "updateRule" to rs.getString("update_rule"),
                    "deleteRule" to rs.getString("delete_rule")
                )
            }.list()

            if (relations.isEmpty()) {
                val scope = if (tableName.isNotBlank()) "表 '$tableName'" else "数据库"
                "$scope 中未找到外键关联关系"
            } else {
                val scope = if (tableName.isNotBlank()) "表 '$tableName'" else "数据库"
                """
                $scope 的外键关联关系 (${relations.size} 个):

                ${
                    relations.joinToString("\n") { relation ->
                        val updateRule = relation["updateRule"] as String?
                        val deleteRule = relation["deleteRule"] as String?
                        val rules = listOfNotNull(
                            updateRule?.let { "UPDATE $it" },
                            deleteRule?.let { "DELETE $it" }
                        ).joinToString(", ")
                        val rulesInfo = if (rules.isNotEmpty()) " [$rules]" else ""

                        "🔗 ${relation["sourceTable"]}.${relation["sourceColumn"]} → " +
                                "${relation["targetTable"]}.${relation["targetColumn"]}$rulesInfo"
                    }
                }
                """.trimIndent()
            }
        } catch (e: Exception) {
            "查询表关联关系失败: ${e.message}"
        }
    }
}
