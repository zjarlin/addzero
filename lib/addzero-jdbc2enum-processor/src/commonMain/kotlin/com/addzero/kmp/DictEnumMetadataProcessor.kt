package com.addzero.kmp
import com.addzero.kmp.util.PinYin4JUtils
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties
import kotlin.math.absoluteValue

/**
 * 字典枚举元数据处理器提供者
 *
 * 功能：提供字典枚举元数据处理器，用于生成数据库字典表和字典项表相关的枚举类
 */
class DictEnumMetadataProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return DictEnumMetadataProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}

/**
 * 字典枚举元数据处理器
 *
 * 功能：处理数据库字典表和字典项表的元数据，生成对应的枚举类
 */
class DictEnumMetadataProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {
    // 数据库连接配置
    private val jdbcDriver = options["jdbcDriver"] ?: "org.postgresql.Driver"
    private val jdbcUrl = options["jdbcUrl"] ?: throw IllegalArgumentException("jdbcUrl is required")
    private val jdbcUsername = options["jdbcUsername"] ?: throw IllegalArgumentException("jdbcUsername is required")
    private val jdbcPassword = options["jdbcPassword"] ?: throw IllegalArgumentException("jdbcPassword is required")
    private val outputPackage = options["enumOutputPackage"] ?: "com.addzero.kmp.generated.enums"

    // 字典表配置
    private val dictTableName = options["dictTableName"] ?: "sys_dict"
    private val dictIdColumn = options["dictIdColumn"] ?: "id"
    private val dictCodeColumn = options["dictCodeColumn"] ?: "dict_code"
    private val dictNameColumn = options["dictNameColumn"] ?: "dict_name"

    // 字典项表配置
    private val dictItemTableName = options["dictItemTableName"] ?: "sys_dict_item"
    private val dictItemForeignKeyColumn = options["dictItemForeignKeyColumn"] ?: "dict_id"
    private val dictItemCodeColumn = options["dictItemCodeColumn"] ?: "item_value"
    private val dictItemNameColumn = options["dictItemNameColumn"] ?: "item_text"

    override fun process(resolver: Resolver): List<KSAnnotated> {

        try {
            // 提取字典数据并生成枚举类
            logger.info("开始处理字典枚举数据...")
            try {
                extractDictEnumData()
                logger.info("成功从数据库字典表生成枚举类")
            } catch (e: Exception) {
                logger.warn("⚠️ 无法连接到数据库或提取字典数据: ${e.message}")
                logger.warn("跳过字典枚举生成过程")
                when (e) {
                    is ClassNotFoundException -> logger.warn("找不到JDBC驱动: $jdbcDriver")
                    is SQLException -> logger.warn("SQL错误: ${e.message}, 错误代码: ${e.errorCode}, SQL状态: ${e.sqlState}")
                    else -> e.printStackTrace()
                }
            }
            return emptyList()
        } catch (e: Exception) {
            logger.warn("处理字典数据失败: ${e.message}")
        }
        return emptyList()
    }

    /**
     * 提取字典数据并生成枚举类
     * @throws ClassNotFoundException 如果找不到JDBC驱动
     * @throws SQLException 如果数据库连接或查询失败
     */
    private fun extractDictEnumData() {
        // 注册JDBC驱动
        Class.forName(jdbcDriver)

        logger.info("正在连接数据库: $jdbcUrl")
        // 创建数据库连接 - 设置连接超时(5秒)
        val props = Properties().apply {
            setProperty("user", jdbcUsername)
            setProperty("password", jdbcPassword)
            setProperty("connectTimeout", "5")
        }

        DriverManager.getConnection(jdbcUrl, props).use { connection ->
            logger.info("数据库连接成功")

            // 构建SQL查询
            val sql = """
                SELECT 
                    d.$dictIdColumn as dict_id,
                    d.$dictCodeColumn as dict_code,
                    d.$dictNameColumn as dict_name,
                    i.$dictItemCodeColumn as item_code,
                    i.$dictItemNameColumn as item_desc
                FROM 
                    $dictTableName d
                LEFT JOIN 
                    $dictItemTableName i ON d.$dictIdColumn = i.$dictItemForeignKeyColumn
                ORDER BY 
                    d.$dictCodeColumn, i.$dictItemCodeColumn
            """.trimIndent()

            try {
                // 执行查询并收集结果
                connection.createStatement().use { statement ->
                    statement.executeQuery(sql).use { resultSet ->
                        // 用于按字典分组的映射
                        val dictMap = mutableMapOf<String, MutableList<DictItem>>()
                        val dictNameMap = mutableMapOf<String, String>()
                        // 生成的类名集合，用于检测重复
                        val generatedClassNames = mutableSetOf<String>()

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
                            logger.warn("未找到任何字典数据,请检查数据库表 $dictTableName 和 $dictItemTableName 是否存在数据")
                            return
                        }

                        logger.info("从数据库读取到 ${dictMap.size} 个字典")

                        // 为每个字典生成枚举类
                        dictMap.forEach { (dictCode, items) ->
                            try {
                                // 检查是否有字典项
                                if (items.isEmpty()) {
                                    logger.warn("字典'$dictCode'没有字典项,跳过枚举生成")
                                    return@forEach
                                }

                                // 转换为类名
                                val enumName = toCamelCase(dictCode, true)

                                // 检查类名是否重复
                                if (enumName in generatedClassNames) {
                                    logger.warn("字典'$dictCode'生成的枚举类名'$enumName'重复,跳过")
                                    return@forEach
                                }

                                // 记录类名
                                generatedClassNames.add(enumName)

                                // 生成枚举类
                                generateEnumClass(dictCode, dictNameMap[dictCode] ?: "", items, enumName)
                                logger.info("为字典'$dictCode'生成枚举类: Enum$enumName")
                            } catch (e: Exception) {
                                logger.warn("为字典'$dictCode'生成枚举类失败: ${e.message}")
                            }
                        }
                        dictMap.clear()

                    }
                }
            } catch (e: Exception) {
                logger.warn("执行SQL查询失败: ${e.message}")
                throw e
            }
        }
    }

    /**
     * 生成枚举类
     *
     * @param dictCode 字典编码
     * @param dictName 字典名称
     * @param items 字典项列表
     * @param enumName 枚举类名（不含前缀）
     */
    private fun generateEnumClass(dictCode: String, dictName: String, items: List<DictItem>, enumName: String) {
        // 枚举类名加上"Enum"前缀
        val fullEnumName = "Enum$enumName"

        // 构建枚举类内容
        val enumContent = """
            package $outputPackage
            
            /**
             * $dictName
             * 
             * 数据库字典编码: $dictCode
             * 自动生成的枚举类，不要手动修改
             */
            enum class $fullEnumName(
                val code: String,
                val desc: String
            ) {
                ${generateEnumEntries(items)};
                
                companion object {
                    /**
                     * 根据编码获取枚举值
                     * 
                     * @param code 编码
                     * @return 对应的枚举值，如果不存在则返回null
                     */
                    fun fromCode(code: String): $fullEnumName? {
                        return entries.find { it.code == code }
                    }
                    
                    /**
                     * 根据描述获取枚举值
                     * 
                     * @param desc 描述
                     * @return 对应的枚举值，如果不存在则返回null
                     */
                    fun fromDesc(desc: String): $fullEnumName? {
                        return entries.find { it.desc == desc }
                    }
                }
            }
        """.trimIndent()

        // 创建枚举类文件
        val dependencies = Dependencies(aggregating = true)
        codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName = outputPackage,
            fileName = fullEnumName // 文件名也使用前缀
        ).use { output ->
            output.write(enumContent.toByteArray())
        }

    }

    /**
     * 生成枚举项
     *
     * @param items 字典项列表
     * @return 枚举项字符串
     */
    private fun generateEnumEntries(items: List<DictItem>): String {
        return items.joinToString(",\n    ") { item ->
            val enumEntryName = toEnumEntryName(item.desc, item.code)
            """$enumEntryName("${item.code}", "${item.desc}")"""
        }
    }

    /**
     * 转换为驼峰命名
     *
     * @param str 输入字符串
     * @param capitalizeFirst 是否将首字母大写
     * @return 驼峰命名字符串
     */
    private fun toCamelCase(str: String, capitalizeFirst: Boolean): String {
        // 将下划线、中划线等分隔符替换为空格
        val words = str.replace(Regex("[_\\-\\s]+"), " ")
            .split(" ")
            .filter { it.isNotEmpty() }

        if (words.isEmpty()) return ""

        return words.mapIndexed { index, word ->
            if (index == 0 && !capitalizeFirst) {
                word.lowercase()
            } else {
                word.capitalize()
            }
        }.joinToString("")
    }


    /**
     * 转换为合法的枚举项名称
     *
     * @param desc 字典项描述
     * @param code 字典项编码，用于desc为空时的备选
     * @return 合法的枚举项名称
     */
    private fun toEnumEntryName(desc: String, code: String): String {
        // 如果描述为空，则直接使用code
        if (desc.isBlank()) {
            return PinYin4JUtils.sanitize(code)
        }

        try {
            // 1. 尝试将中文转为拼音
            val pinyin = PinYin4JUtils.hanziToPinyin(desc, "_")

            // 2. 如果拼音转换成功，将其处理为合法变量名并返回
            if (pinyin.isNotBlank()) {
                return PinYin4JUtils.sanitize(pinyin)
            }
        } catch (e: Exception) {
            logger.warn("Failed to convert '${desc}' to pinyin: ${e.message}")
        }

        // 3. 如果拼音转换失败，直接尝试将原始描述处理为合法变量名
        val sanitizedDesc = PinYin4JUtils.sanitize(desc)

        // 4. 如果处理后的描述不为空，返回它
        if (sanitizedDesc != "CODE") {
            return sanitizedDesc
        }

        // 5. 最后兜底：使用code生成变量名
        return PinYin4JUtils.sanitize(code)
    }

    /**
     * 将字符串处理为合法的枚举名
     *
     * @param input 输入字符串
     * @return 处理后的枚举名
     */
    private fun processToEnumName(input: String): String {
        // 直接使用PinYin4JUtils的sanitize方法
        return PinYin4JUtils.sanitize(input, "CODE_${input.hashCode().absoluteValue}")
    }

    /**
     * 字典项数据类
     */
    private data class DictItem(
        val code: String,
        val desc: String
    )
}
