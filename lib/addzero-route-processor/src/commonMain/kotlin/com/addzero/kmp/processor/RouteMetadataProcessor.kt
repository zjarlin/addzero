package com.addzero.kmp.processor

import com.addzero.kmp.consts.GEN_PKG
import com.addzero.kmp.annotation.Route
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate

private const val FILE_NAME = "RouteTable"


/**
 * 路由元数据注解处理器
 */
class RouteMetadataProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {
    val ret = mutableSetOf<Route>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 获取所有带有Route注解的符号
        val symbols = resolver.getSymbolsWithAnnotation(Route::class.qualifiedName!!)
            .toList()

        if (symbols.isEmpty()) return emptyList()

        // 收集所有路由项
        val routeItems = symbols.mapNotNull { symbol ->
            when (symbol) {
                is KSClassDeclaration -> processClass(symbol)
                is KSFunctionDeclaration -> processFunction(symbol)
                is KSPropertyDeclaration -> processProperty(symbol)
                else -> {
                    logger.warn("Unsupported symbol type for Route: ${symbol::class.simpleName}", symbol)
                    null
                }
            }
        }.sortedBy { it.order }

        ret.addAll(routeItems)

        // 生成代码
//        if (routeItems.isNotEmpty()) {
//            generateRouteTable(routeItems)
//        }
//        return emptyList()
        // 返回未处理的符号
        return symbols.filterNot { it.validate() }.toList()
    }

    override fun finish() {


        // 生成代码
        if (ret.isNotEmpty()) {
            generateRouteTable(ret)
        }


    }

    private fun processClass(declaration: KSClassDeclaration): Route? {
        return processSymbol(declaration) { className ->
            className
        }
    }

    private fun processFunction(declaration: KSFunctionDeclaration): Route? {
        return processSymbol(declaration) { functionName ->
            "${declaration.parentDeclaration?.qualifiedName?.asString() ?: ""}.${functionName}"
        }
    }

    private fun processProperty(declaration: KSPropertyDeclaration): Route? {
        return processSymbol(declaration) { propertyName ->
            "${declaration.parentDeclaration?.qualifiedName?.asString() ?: ""}.${propertyName}"
        }
    }

    private fun processSymbol(
        declaration: KSDeclaration,
        classNameBuilder: (String) -> String
    ): Route? {
        return try {
            val qualifiedName = declaration.qualifiedName?.asString() ?: ""
            val simpleName = declaration.simpleName.asString()
            val annotation = declaration.annotations.first {
                it.annotationType.resolve().declaration.qualifiedName?.asString() == Route::class.qualifiedName
            }



            val title = annotation.arguments.firstOrNull { it.name?.asString () == "title" }?.value as? String ?: simpleName

            val icon = annotation.arguments.firstOrNull { it.name?.asString() == "icon" }?.value as? String ?: ""
            val routePath = annotation.arguments.firstOrNull { it.name?.asString() == "routePath" }?.value as? String
                ?: qualifiedName
            val order = annotation.arguments.firstOrNull { it.name?.asString() == "order" }?.value as? Double ?: 0.0
            val group = annotation.arguments.firstOrNull { it.name?.asString() == "value" }?.value as? String ?: ""

            Route(
                value = group,
                title = title,
                routePath = routePath,
                icon = icon,
                order = order,
                qualifiedName = qualifiedName,
                simpleName=simpleName
            )
        } catch (e: Exception) {
            logger.error("Error processing Route annotation: ${e.message}", declaration)
            null
        }
    }

    private fun String.toUnderLineCase(): String {
        val sb = kotlin.text.StringBuilder()
        for ((index, char) in this.withIndex()) {
            if (index > 0 && char.isUpperCase()) {
                sb.append('_')
            }
            sb.append(char)
        }
        return sb.toString()
    }

    private fun generateRouteTable(routeItems: Set<Route>) {
        val packageName = GEN_PKG
        val fileName = FILE_NAME

        // 生成路由键对象
        val routeKeysTemplate = """
            |package $packageName
            |
            |/**
            | * 路由键
            | * 请勿手动修改此文件
            | */
            |object RouteKeys {
            |    ${
            routeItems.joinToString("\n    ") {
                val key = it.simpleName.toUnderLineCase().uppercase()
                "const val $key = \"${it.routePath}\""
            }
        }
            |}
            |""".trimMargin()

        // 生成路由表
        val routeTableTemplate = """
            |package $packageName
            |
            |import androidx.compose.runtime.Composable
            |import $packageName.RouteKeys
            |import com.addzero.kmp.processor.annotation.Route
            |
            |/**
            | * 路由表
            | * 请勿手动修改此文件
            | */
            |object $fileName {
            |    /**
            |     * 所有路由映射
            |     */
            |    val allRoutes = mapOf(
            |        ${
            routeItems.joinToString(",\n        ") {
                val key = it.simpleName.toUnderLineCase().uppercase()
                "RouteKeys.$key to  @Composable { ${it.qualifiedName}() }"
            }
        }
            |    )
            |
            |    /**
            |     * 所有路由元数据
            |     */
            |    val allMeta = listOf(
            |        ${
            routeItems.joinToString(",\n        ") {
                "Route(" +
                    "value = \"${it.value}\", " +
                    "title = \"${it.title}\", " +
                    "routePath = \"${it.routePath}\", " +
                    "icon = \"${it.icon}\", " +
                    "order = ${it.order}, " +
                    "qualifiedName = \"${it.qualifiedName}\"" +
                ")"
            }
        }
            |    )
            |
            |    /**
            |     * 根据路由键获取对应的Composable函数
            |     */
            |    operator fun get(routeKey: String): @Composable () -> Unit {
            |        return allRoutes[routeKey] ?: throw IllegalArgumentException("Route not found")
            |    }
            |}
            |""".trimMargin()

        try {
            // 生成路由键文件 - 使用安全创建方法
            createNewFileIfNeeded(
                codeGenerator = codeGenerator,
                dependencies = Dependencies(true),
                packageName = packageName,
                fileName = "RouteKeys",
                content = routeKeysTemplate
            )

            // 生成路由表文件
            codeGenerator.createNewFile(
                dependencies = Dependencies(true),
                packageName = packageName,
                fileName = fileName
            ).use { stream ->
                stream.write(routeTableTemplate.toByteArray())
            }
        } catch (e: Exception) {
            logger.warn("Error generating route files: ${e.message}")
        }
    }
    
    /**
     * 安全创建文件，如果文件已存在则跳过
     */
    private fun createNewFileIfNeeded(
        codeGenerator: CodeGenerator,
        dependencies: Dependencies,
        packageName: String,
        fileName: String,
        content: String
    ) {
        try {
            codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName = packageName,
                fileName = fileName
            ).use { stream ->
                stream.write(content.toByteArray())
            }
            logger.info("Created file: $packageName.$fileName")
        } catch (e: kotlin.io.FileAlreadyExistsException) {
            logger.info("File already exists: $packageName.$fileName, skipping generation")
        } catch (e: Exception) {
            logger.warn("Error creating file $packageName.$fileName: ${e.message}")
        }
    }
}

/**
 * 菜单元数据注解处理器提供者
 */
class RouteMetadataProcessorProvider : SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return RouteMetadataProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger
        )
    }
}