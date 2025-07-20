package com.addzero.kmp

import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.entity.analysis.model.EntityMetadata
import com.addzero.kmp.entity.analysis.processor.BaseJimmerProcessor
import com.addzero.kmp.generator.FormCodeGenerator
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * 实体转表单处理器提供者
 */
class Entity2FormProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Entity2FormProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}

/**
 * 实体转表单处理器
 *
 * 专门负责生成表单代码，被 backend 模块依赖
 * 基于 BaseJimmerProcessor，使用统一的实体分析逻辑
 *
 * 生成目录：composeApp/build/generated/ksp/commonMain/kotlin/com/addzero/kmp/forms/
 */
class Entity2FormProcessor(
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
    options: Map<String, String>
) : BaseJimmerProcessor(codeGenerator, logger, options) {

    // 表单代码生成器
    private val formCodeGenerator = FormCodeGenerator(logger)

    // 跟踪已生成的表单，避免重复生成
    private val generatedFormClasses = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        logger.warn("开始生成表单类（process阶段）...")

        // 在 process 阶段直接生成表单，避免 KSP 生命周期问题
        val entitySymbols = resolver.getSymbolsWithAnnotation("org.babyfish.jimmer.sql.Entity")
            .filterIsInstance<KSClassDeclaration>()

        // 从 Settings 中获取配置
        val packageName = SettingContext.settings.formPackageName
        val outputDir = SettingContext.settings.formOutputDir

        entitySymbols.forEach { entity ->
            try {
                // 检查是否已经生成过
                val qualifiedName = entity.qualifiedName?.asString() ?: return@forEach
                if (generatedFormClasses.contains(qualifiedName)) {
                    logger.info("跳过已生成的表单: ${entity.simpleName.asString()}Form")
                    return@forEach
                }

                // 直接使用策略模式生成表单文件
                formCodeGenerator.writeFormFileWithStrategy(entity, outputDir.toString(), packageName)
                generatedFormClasses.add(qualifiedName)

                logger.info("生成表单（策略模式）: ${entity.simpleName.asString()}Form")

            } catch (e: Exception) {
                logger.error("生成表单失败: ${entity.simpleName.asString()}, 错误: ${e.message}")
            }
        }

        logger.warn("表单类生成完成，共生成 ${generatedFormClasses.size} 个")

        return super.process(resolver)
    }

    override fun processEntities(entities: List<EntityMetadata>) {
        // 表单生成已经在 process 阶段完成，这里不需要再处理
        logger.info("processEntities: 表单生成已在 process 阶段完成")
    }
}
