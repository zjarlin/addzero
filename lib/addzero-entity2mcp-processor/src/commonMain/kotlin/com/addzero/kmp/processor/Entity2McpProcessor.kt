package com.addzero.kmp.processor

import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.entity.analysis.model.EntityMetadata
import com.addzero.kmp.entity.analysis.processor.BaseJimmerProcessor
import com.addzero.kmp.util.genCode
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger

/**
 * 实体转MCP服务处理器
 *
 * 专门负责生成AiCrudService实现类，被backend模块依赖
 * 基于BaseJimmerProcessor，使用统一的实体分析逻辑
 *
 * 生成目录：backend/build/generated/ksp/main/kotlin/com/addzero/ai/mcp/generated/
 */
class Entity2McpProcessor(
    codeGenerator: CodeGenerator,
    logger: KSPLogger,
    options: Map<String, String>
) : BaseJimmerProcessor(codeGenerator, logger, options) {

    // 跟踪已生成的MCP服务，避免重复生成
    private val generatedMcpServices = mutableSetOf<String>()


    // MCP服务代码生成器
    private val mcpCodeGenerator = McpServiceCodeGenerator(codeGenerator, logger)


    override fun processEntities(entities: List<EntityMetadata>) {
        logger.warn("开始生成MCP服务类...")

        // 从Settings中获取配置
        val packageName = "com.addzero.kmp.generated.mcp"

        // 为每个实体生成MCP服务
        entities.forEach { entity ->
            try {
                // 检查是否已生成
                if (entity.qualifiedName in generatedMcpServices) {
                    logger.warn("MCP服务已存在，跳过生成: ${entity.className}McpService")
                    return@forEach
                }

                // 生成MCP服务
                mcpCodeGenerator.generateMcpService(
                    entity = entity,
                    packageName = packageName
                )

                // 记录已生成的服务
                generatedMcpServices.add(entity.qualifiedName)

                logger.warn("成功生成MCP服务: ${entity.className}McpService")
            } catch (e: Exception) {
                logger.error("生成MCP服务失败: ${entity.className}, 错误: ${e.message}")
                e.printStackTrace()
            }
        }

        logger.warn("MCP服务生成完成，共生成 ${generatedMcpServices.size} 个服务")
        generatedMcpServices.clear()
    }
}

/**
 * MCP服务代码生成器
 * 使用模板字符串生成代码
 */
class McpServiceCodeGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {


    /**
     * 生成MCP服务类
     */
    fun generateMcpService(
        entity: EntityMetadata,
        packageName: String
    ) {

        // 构建服务类名
        val serviceClassName = "${entity.className}McpService"

        // 构建实体类名和同构体类名
        val entityFullName = "${entity.packageName}.${entity.className}"
//        isomorphicPackageName
        val settings = SettingContext.settings
        val isomorphicPackageName = settings.isomorphicPackageName
        val isomorphicClassSuffix = settings.isomorphicClassSuffix
        val isoFullName = "$isomorphicPackageName.${entity.className}$isomorphicClassSuffix"

        // 获取实体描述（现在直接从EntityMetadata中获取）
        val entityDescription = entity.description.ifBlank { entity.className.lowercase() }

        // 生成文件
        val fileName = "$serviceClassName.kt"
        val fileContent = generateServiceFileContent(
            packageName = packageName,
            serviceClassName = serviceClassName,
            entityName = entity.className,
            entityFullName = entityFullName,
            isoFullName = isoFullName,
            entityDescription = entityDescription
        )
        val mcpdir = "${settings.serverSourceDir}/${
            packageName.replace(
                ".",
                "/"
            )
        }/$fileName"

        genCode(mcpdir, fileContent, false)
    }


    /**
     * 生成服务文件内容
     * 使用模板字符串
     */
    private fun generateServiceFileContent(
        packageName: String,
        serviceClassName: String,
        entityName: String,
        entityFullName: String,
        isoFullName: String,
        entityDescription: String
    ): String {
        return """
            package $packageName

            import com.addzero.common.consts.sql
            import com.addzero.web.infra.jackson.toJson
            import org.babyfish.jimmer.ImmutableObjects
            import org.springframework.ai.tool.annotation.Tool
            import org.springframework.ai.tool.annotation.ToolParam
            import org.springframework.stereotype.Service
            import $entityFullName
            import $isoFullName

            /**
             * $entityName MCP服务
             *
             * 提供${entityDescription}相关的CRUD操作和AI工具
             * 自动生成的代码，请勿手动修改
             */
            @Service
            class $serviceClassName  {

                @Tool(description = "保存${entityDescription}数据到数据库")
                fun save$entityName(@ToolParam(description = "${entityDescription}数据对象") entity: ${entityName}Iso): String {
                       val toJson = entity.toJson()
        val fromString = ImmutableObjects.fromString(${entityName}::class.java, toJson)
        val save = sql.save(fromString)
        return "保存成功"
 
                }
            }
        """.trimIndent()
    }
}
