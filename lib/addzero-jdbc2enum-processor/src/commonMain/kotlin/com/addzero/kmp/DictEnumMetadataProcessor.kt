package com.addzero.kmp

import com.addzero.kmp.context.SettingContext
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import java.sql.SQLException

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
 * 采用两阶段处理：process阶段收集元数据，finish阶段生成代码
 */
class DictEnumMetadataProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    // 字典元数据抽取器
    private val metadataExtractor = DictMetadataExtractor(
        logger = logger,
        config = DictMetadataExtractor.DictConfig(
            jdbcDriver = options["jdbcDriver"] ?: "org.postgresql.Driver",
            jdbcUrl = options["jdbcUrl"] ?: throw IllegalArgumentException("jdbcUrl is required"),
            jdbcUsername = options["jdbcUsername"] ?: throw IllegalArgumentException("jdbcUsername is required"),
            jdbcPassword = options["jdbcPassword"] ?: throw IllegalArgumentException("jdbcPassword is required"),
            dictTableName = options["dictTableName"] ?: "sys_dict",
            dictIdColumn = options["dictIdColumn"] ?: "id",
            dictCodeColumn = options["dictCodeColumn"] ?: "dict_code",
            dictNameColumn = options["dictNameColumn"] ?: "dict_name",
            dictItemTableName = options["dictItemTableName"] ?: "sys_dict_item",
            dictItemForeignKeyColumn = options["dictItemForeignKeyColumn"] ?: "dict_id",
            dictItemCodeColumn = options["dictItemCodeColumn"] ?: "item_value",
            dictItemNameColumn = options["dictItemNameColumn"] ?: "item_text"
        )
    )

    // 字典枚举代码生成器
    private val enumCodeGenerator = DictEnumCodeGenerator(
        codeGenerator = this.codeGenerator,
        logger = logger,
    )

    // 收集到的字典元数据
    private var dictMetadataList: List<DictMetadataExtractor.DictMetadata> = emptyList()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        SettingContext.initialize(options)
        // process 阶段只收集元数据，不生成代码
        if (dictMetadataList.isEmpty()) {
            try {
                logger.info("开始收集字典元数据...")
                dictMetadataList = metadataExtractor.extractDictMetadata()
                logger.info("成功收集到 ${dictMetadataList.size} 个字典的元数据")
            } catch (e: Exception) {
                logger.warn("⚠️ 无法连接到数据库或提取字典数据: ${e.message}")
                logger.warn("跳过字典枚举生成过程")
                when (e) {
                    is ClassNotFoundException -> logger.warn("找不到JDBC驱动")
                    is SQLException -> logger.warn("SQL错误: ${e.message}, 错误代码: ${e.errorCode}, SQL状态: ${e.sqlState}")
                    else -> logger.warn("未知错误: ${e.message}")
                }
            }
        }
        return emptyList()
    }

    override fun finish() {
        // finish 阶段生成代码
        if (dictMetadataList.isNotEmpty()) {
            try {
                logger.info("开始生成字典枚举类...")
                enumCodeGenerator.generateEnumClasses(dictMetadataList)
                logger.info("成功生成 ${dictMetadataList.size} 个字典枚举类")
            } catch (e: Exception) {
                logger.warn("生成字典枚举类失败: ${e.message}")
            }
        }
    }
}
