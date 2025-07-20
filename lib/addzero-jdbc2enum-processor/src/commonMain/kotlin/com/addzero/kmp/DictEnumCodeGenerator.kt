package com.addzero.kmp

import com.addzero.kmp.context.SettingContext.settings
import com.addzero.kmp.util.PinYin4JUtils
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger

/**
 * 字典枚举代码生成器
 *
 * 负责根据字典元数据生成枚举类代码
 */
class DictEnumCodeGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) {

    /**
     * 生成所有字典的枚举类
     *
     * @param dictMetadataList 字典元数据列表
     */
    fun generateEnumClasses(dictMetadataList: List<DictMetadataExtractor.DictMetadata>) {
        // 生成的类名集合，用于检测重复
        val generatedClassNames = mutableSetOf<String>()

        dictMetadataList.forEach { dictMetadata ->
            try {
                // 检查是否有字典项
                if (dictMetadata.items.isEmpty()) {
                    logger.warn("字典'${dictMetadata.dictCode}'没有字典项,跳过枚举生成")
                    return@forEach
                }

                // 转换为类名
                val enumName = toCamelCase(dictMetadata.dictCode, true)

                // 检查类名是否重复
                if (enumName in generatedClassNames) {
                    logger.warn("字典'${dictMetadata.dictCode}'生成的枚举类名'$enumName'重复,跳过")
                    return@forEach
                }

                // 记录类名
                generatedClassNames.add(enumName)

                // 生成枚举类
                generateEnumClass(dictMetadata, enumName)
//                logger.info("为字典'${dictMetadata.dictCode}'生成枚举类: Enum$enumName")
            } catch (e: Exception) {
                logger.warn("为字典'${dictMetadata.dictCode}'生成枚举类失败: ${e.message}")
            }
        }
    }

    /**
     * 生成单个枚举类
     *
     * @param dictMetadata 字典元数据
     * @param enumName 枚举类名（不含前缀）
     */
    private fun generateEnumClass(dictMetadata: DictMetadataExtractor.DictMetadata, enumName: String) {
        // 枚举类名加上"Enum"前缀
        val fullEnumName = "Enum$enumName"

        // 构建枚举类内容
        val enumOutputPackage = settings.enumOutputPackage
        val enumOutputDir = settings.enumOutputDir
//        logger.warn("枚举类输出目录为: $enumOutputDir")
//        logger.warn("枚举输出包为: $enumOutputPackage")
        val enumContent = """
            package $enumOutputPackage
            
            /**
             * ${dictMetadata.dictName}
             * 
             * 数据库字典编码: ${dictMetadata.dictCode}
             * 自动生成的枚举类，不要手动修改
             */
            enum class $fullEnumName(
                val code: String,
                val desc: String
            ) {
                ${generateEnumEntries(dictMetadata.items)};
                
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
        // 使用手动 File IO 生成到指定目录
        writeEnumToFile(fullEnumName, enumContent, enumOutputDir)
    }

    /**
     * 生成枚举项
     *
     * @param items 字典项列表
     * @return 枚举项字符串
     */
    private fun generateEnumEntries(items: List<DictMetadataExtractor.DictItem>): String {
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
     * 使用手动 File IO 写入枚举文件
     */
    private fun writeEnumToFile(fileName: String, content: String, outputDir: String) {


        val enumOutputDir = settings.enumOutputDir

        // 创建目录
        val dir = java.io.File(enumOutputDir)
        dir.mkdirs()

        // 写入文件
        val file = java.io.File(dir, "$fileName.kt")
        file.writeText(content)

        logger.info("Generated enum class: $fileName at ${file.absolutePath}")
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
}
