import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.entity.analysis.model.EntityMetadata
import com.addzero.kmp.entity.analysis.processor.BaseJimmerProcessor
import com.google.devtools.ksp.processing.*
import generator.IsoCodeGenerator
import java.io.File

/**
 * 实体转同构体处理器提供者
 */
class Entity2IsoProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Entity2IsoProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}

/**
 * 实体转同构体处理器
 *
 * 专门负责生成同构体类，被 shared 模块依赖
 * 基于 BaseJimmerProcessor，使用统一的实体分析逻辑
 *
 * 生成目录：shared/src/commonMain/kotlin/com/addzero/kmp/isomorphic/
 */
class Entity2IsoProcessor(
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
    options: Map<String, String>
) : BaseJimmerProcessor(codeGenerator, logger, options) {

    // 同构体代码生成器
    private val isoCodeGenerator = IsoCodeGenerator(logger)

    // 跟踪已生成的同构体，避免重复生成
    private val generatedIsoClasses = mutableSetOf<String>()

    override fun processEntities(entities: List<EntityMetadata>) {
        logger.warn("开始生成同构体类...")

        // 从 Settings 中获取配置（outputDir 由扩展属性计算）
        val packageName = SettingContext.settings.isomorphicPackageName
        val outputDir = SettingContext.settings.isomorphicOutputDir

        entities.forEach { metadata ->
            try {
                // 检查是否已经生成过
                if (generatedIsoClasses.contains(metadata.qualifiedName)) {
                    logger.info("跳过已生成的同构体: ${metadata.className}Iso")
                    return@forEach
                }

                // 生成同构体代码
                val isoCode = isoCodeGenerator.generateIsoCode(metadata, packageName)

                // 写入文件
                val fileName = "${metadata.className}Iso.kt"
                val file = File(outputDir, fileName)
                file.writeText(isoCode)

                generatedIsoClasses.add(metadata.qualifiedName)

                logger.info("生成同构体: ${metadata.className}Iso")
            } catch (e: Exception) {
                logger.error("生成同构体失败: ${metadata.className}, 错误: ${e.message}")
            }
        }

        logger.warn("同构体类生成完成，共生成 ${generatedIsoClasses.size} 个")
    }
}
