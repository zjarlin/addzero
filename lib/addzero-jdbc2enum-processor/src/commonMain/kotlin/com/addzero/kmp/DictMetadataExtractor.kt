package com.addzero.kmp

import com.google.devtools.ksp.processing.KSPLogger
import java.sql.DriverManager
import java.sql.SQLException
import java.util.*

/**
 * 字典元数据抽取器
 *
 * 负责从数据库中抽取字典表和字典项表的元数据
 */
class DictMetadataExtractor(
    private val logger: KSPLogger,
    private val config: DictConfig
) {

    /**
     * 字典配置
     */
    data class DictConfig(
        val jdbcDriver: String,
        val jdbcUrl: String,
        val jdbcUsername: String,
        val jdbcPassword: String,
        val dictTableName: String,
        val dictIdColumn: String,
        val dictCodeColumn: String,
        val dictNameColumn: String,
        val dictItemTableName: String,
        val dictItemForeignKeyColumn: String,
        val dictItemCodeColumn: String,
        val dictItemNameColumn: String
    )

    /**
     * 字典项数据类
     */
    data class DictItem(
        val code: String,
        val desc: String
    )

    /**
     * 字典元数据
     */
    data class DictMetadata(
        val dictCode: String,
        val dictName: String,
        val items: List<DictItem>
    )

    /**
     * 从数据库提取字典元数据
     *
     * @return 字典元数据列表
     * @throws ClassNotFoundException 如果找不到JDBC驱动
     * @throws SQLException 如果数据库连接或查询失败
     */
    fun extractDictMetadata(): List<DictMetadata> {
        // 注册JDBC驱动
        Class.forName(config.jdbcDriver)

        logger.info("正在连接数据库: ${config.jdbcUrl}")

        // 创建数据库连接 - 设置连接超时(5秒)
        val props = Properties().apply {
            setProperty("user", config.jdbcUsername)
            setProperty("password", config.jdbcPassword)
            setProperty("connectTimeout", "5")
        }

        return DriverManager.getConnection(config.jdbcUrl, props).use { connection ->
            logger.info("数据库连接成功")

            // 构建SQL查询
            val sql = """
                SELECT 
                    d.${config.dictIdColumn} as dict_id,
                    d.${config.dictCodeColumn} as dict_code,
                    d.${config.dictNameColumn} as dict_name,
                    i.${config.dictItemCodeColumn} as item_code,
                    i.${config.dictItemNameColumn} as item_desc
                FROM 
                    ${config.dictTableName} d
                LEFT JOIN 
                    ${config.dictItemTableName} i ON d.${config.dictIdColumn} = i.${config.dictItemForeignKeyColumn}
                ORDER BY 
                    d.${config.dictCodeColumn}, i.${config.dictItemCodeColumn}
            """.trimIndent()

            try {
                // 执行查询并收集结果
                connection.createStatement().use { statement ->
                    statement.executeQuery(sql).use { resultSet ->
                        // 用于按字典分组的映射
                        val dictMap = mutableMapOf<String, MutableList<DictItem>>()
                        val dictNameMap = mutableMapOf<String, String>()

                        // 遍历结果集
                        while (resultSet.next()) {
                            val dictCode = resultSet.getString("dict_code")
                            val dictName = resultSet.getString("dict_name")
                            val itemCode = resultSet.getString("item_code")
                            val itemDesc = resultSet.getString("item_desc")

                            // 保存字典名称
                            dictNameMap[dictCode] = dictName

                            // 如果是有效的字典项，则添加到映射中
                            if (itemCode != null && itemDesc != null) {
                                val items = dictMap.getOrPut(dictCode) { mutableListOf() }
                                items.add(DictItem(itemCode, itemDesc))
                            }
                        }

                        if (dictMap.isEmpty()) {
                            logger.warn("未找到任何字典数据,请检查数据库表 ${config.dictTableName} 和 ${config.dictItemTableName} 是否存在数据")
                            return@use emptyList()
                        }

                        logger.info("从数据库读取到 ${dictMap.size} 个字典")

                        // 转换为字典元数据列表
                        dictMap.map { (dictCode, items) ->
                            DictMetadata(
                                dictCode = dictCode,
                                dictName = dictNameMap[dictCode] ?: "",
                                items = items
                            )
                        }.filter { it.items.isNotEmpty() } // 过滤掉没有字典项的字典
                    }
                }
            } catch (e: Exception) {
                logger.warn("执行SQL查询失败: ${e.message}")
                throw e
            }
        }
    }
}
