package com.addzero.kmp.processor

import com.addzero.kmp.context.SettingContext
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import java.io.File

/**
 * Controller 转 Iso2DataProvider 处理器提供者
 */
class Controller2Iso2DataProviderProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return Controller2Iso2DataProviderProcessor(
            environment.codeGenerator,
            environment.logger,
            environment.options
        )
    }
}

/**
 * Controller 转 Iso2DataProvider 处理器
 * 扫描所有实现了 BaseTreeApi<E> 的 Controller，生成对应的 Iso2DataProvider
 */
class Controller2Iso2DataProviderProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    private val collectedControllers = mutableSetOf<String>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 初始化设置上下文
        SettingContext.initialize(options)
//        println("tttttttttttttttttt")

        // 查找所有带有 @RestController 注解的类
        val controllers = resolver.getSymbolsWithAnnotation("org.springframework.web.bind.annotation.RestController")
            .filterIsInstance<KSClassDeclaration>()
            .filter { it.validate() }
            .toList()

        if (controllers.isEmpty()) {
            logger.info("未找到任何 @RestController 注解的类")
            return emptyList()
        }

        // 过滤出实现了 BaseTreeApi<E> 的控制器
        val baseTreeApiControllers = controllers.filter { controller ->
            isBaseTreeApiImplementation(controller)
        }

        logger.info("找到 ${baseTreeApiControllers.size} 个 BaseTreeApi 实现")

        // 收集控制器信息
        val controllerInfos = mutableListOf<ControllerInfo>()
        val invalidControllers = mutableListOf<KSClassDeclaration>()

        baseTreeApiControllers.forEach { controller ->
            if (controller.validate()) {
                try {
                    val info = extractControllerInfo(controller)
                    if (info != null) {
                        controllerInfos.add(info)
                        collectedControllers.add(controller.qualifiedName?.asString() ?: "")
                    }
                } catch (e: Exception) {
                    logger.error("处理控制器 ${controller.simpleName.asString()} 时发生错误: ${e.message}")
                    invalidControllers.add(controller)
                }
            } else {
                invalidControllers.add(controller)
            }
        }

        // 生成 Iso2DataProvider 代码
        if (controllerInfos.isNotEmpty()) {
            generateIso2DataProvider(controllerInfos)
        }

        return invalidControllers
    }

    /**
     * 检查是否实现了 BaseTreeApi<E>
     */
    private fun isBaseTreeApiImplementation(controller: KSClassDeclaration): Boolean {
        return controller.superTypes.any { superType ->
            val resolved = superType.resolve()
            val declaration = resolved.declaration
            declaration.qualifiedName?.asString()?.contains("BaseTreeApi") == true
        }
    }

    /**
     * 提取控制器信息
     */
    private fun extractControllerInfo(controller: KSClassDeclaration): ControllerInfo? {
        // 查找 BaseTreeApi<E> 的泛型参数
        val baseTreeApiSuperType = controller.superTypes.firstOrNull { superType ->
            val resolved = superType.resolve()
            val declaration = resolved.declaration
            declaration.qualifiedName?.asString()?.contains("BaseTreeApi") == true
        }

        if (baseTreeApiSuperType == null) {
            logger.warn("控制器 ${controller.simpleName.asString()} 没有找到 BaseTreeApi 超类型")
            return null
        }

        val resolved = baseTreeApiSuperType.resolve()
        val typeArguments = resolved.arguments

        if (typeArguments.size < 1) {
            logger.warn("控制器 ${controller.simpleName.asString()} 的 BaseTreeApi 泛型参数不足")
            return null
        }

        // 只有一个泛型参数：实体类型 E
        val entityType = typeArguments[0].type?.resolve()

        if (entityType == null) {
            logger.warn("控制器 ${controller.simpleName.asString()} 的泛型参数无法解析")
            return null
        }

        val entityClassName = entityType.declaration.simpleName.asString()
        // 根据实体类名生成同构体全限定名：实体名 + Iso 后缀（避免重复添加 Iso）
        val isoClassName = if (entityClassName.endsWith("Iso")) entityClassName else "${entityClassName}Iso"
        val isoQualifiedName = "com.addzero.kmp.generated.isomorphic.$isoClassName"

        return ControllerInfo(
            controllerName = controller.simpleName.asString(),
            entityClassName = entityClassName,
            isoQualifiedName = isoQualifiedName
        )
    }

    /**
     * 生成 Iso2DataProvider 代码
     */
    private fun generateIso2DataProvider(controllerInfos: List<ControllerInfo>) {
        // 从 KSP 配置中获取包名，默认为 com.addzero.kmp.form_mapping
        val packageName =
            options["iso2DataProviderPackage"] ?: throw IllegalArgumentException("iso2DataProviderPackage 不能为空")

        val packagePath = packageName.replace(".", "/")

        // 生成到 shared 源码目录，而不是 build 目录
        val outputDir = File(SettingContext.settings.sharedSourceDir, packagePath)
        outputDir.mkdirs()

        // 生成导入语句
        val imports = controllerInfos.joinToString("\n") { info ->
            "import ${info.isoQualifiedName}"
        }

        // 生成映射条目
        val mappingEntries = controllerInfos.joinToString(",\n") { info ->
            val isoClassName = info.isoQualifiedName.substringAfterLast(".")
            val apiMethodName = info.entityClassName.replaceFirstChar { it.lowercase() }
            "        ${isoClassName}::class to ApiProvider.${apiMethodName}Api::tree"
        }

        val code = """
            |package $packageName
            |
            |import com.addzero.kmp.generated.api.ApiProvider
            |$imports
            |
            |
            |object Iso2DataProvider {
            |    val isoToDataProvider = mapOf(
            |$mappingEntries
            |    )
            |}
        """.trimMargin()

        val file = File(outputDir, "Iso2DataProvider.kt")
        file.writeText(code)

        logger.info("生成 Iso2DataProvider.kt，包含 ${controllerInfos.size} 个数据提供者映射")
    }

    /**
     * 控制器信息数据类
     */
    private data class ControllerInfo(
        val controllerName: String,
        val entityClassName: String,
        val isoQualifiedName: String
    )
}
