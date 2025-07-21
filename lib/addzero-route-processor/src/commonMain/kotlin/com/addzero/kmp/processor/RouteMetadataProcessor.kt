package com.addzero.kmp.processor

import com.addzero.kmp.annotation.Route
import com.addzero.kmp.consts.GEN_PKG
import com.addzero.kmp.context.SettingContext
import com.addzero.kmp.context.toSharedSourceDir
import com.addzero.kmp.util.genCode
import com.addzero.kmp.util.toUnderLineCase
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.*
import com.google.devtools.ksp.validate

private const val ROUTE_TABLE_NAME = "RouteTable"


private const val ROUTE_KEYS_NAME = "RouteKeys"

/**
 * 路由元数据注解处理器
 */
class RouteMetadataProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    val ret = mutableSetOf<Route>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        SettingContext.initialize(options)
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
            val title =
                annotation.arguments.firstOrNull { it.name?.asString() == "title" }?.value as? String ?: simpleName

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
                simpleName = simpleName
            )
        } catch (e: Exception) {
            logger.error("Error processing Route annotation: ${e.message}", declaration)
            null
        }
    }

    private fun generateRouteTable(routeItems: Set<Route>) {
        logger.warn("解析到 ${routeItems.size} 个路由项")

        // 生成路由键对象
        val routeKeysTemplate = genRoutKeysTemplate(ROUTE_KEYS_NAME, routeItems)

        // 生成路由表
        val routeTableTemplate = """
            |package $GEN_PKG
            |
            |import androidx.compose.runtime.Composable
            |import $GEN_PKG.$ROUTE_KEYS_NAME
            |import com.addzero.kmp.annotation.Route

            |
            |/**
            | * 路由表
            | * 请勿手动修改此文件
            | */
            |object $ROUTE_TABLE_NAME {
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
            logger.warn("开始生成路由键")



            genSharedRouteKeys(routeItems)

            // 生成路由键对象
            codeGenerator.createNewFile(
                dependencies = Dependencies(true),
                packageName = GEN_PKG,
                fileName = ROUTE_KEYS_NAME
            ).use { stream ->
                stream.write(routeKeysTemplate.toByteArray())
            }


            logger.warn("开始生成路由表")
            // 生成路由表文件
            codeGenerator.createNewFile(
                dependencies = Dependencies(true),
                packageName = GEN_PKG,
                fileName = ROUTE_TABLE_NAME
            ).use { stream ->
                stream.write(routeTableTemplate.toByteArray())
            }

        } catch (e: Exception) {
            logger.warn("Error generating route files: ${e.message}")
        }
    }

    private fun genSharedRouteKeys(routeItems: Set<Route>) {
        // 生成路由键对象
        val routeKeysIsoFileName = ROUTE_KEYS_NAME + "Iso"
        val routeKeysTemplateIso = genRoutKeysTemplate(routeKeysIsoFileName, routeItems)

        genCode("${GEN_PKG.toSharedSourceDir()}/$routeKeysIsoFileName.kt", routeKeysTemplateIso)
    }

    private fun genRoutKeysTemplate(
        routeKeysFileName: String,
        routeItems: Set<Route>
    ): String {
        val routeKeysTemplate = """
                |package $GEN_PKG
                |
                |/**
                | * 路由键
                | * 请勿手动修改此文件
                | */
                |object $routeKeysFileName {
                |    ${
            routeItems.joinToString("\n    ") {
                val key = it.simpleName.toUnderLineCase().uppercase()
                "const val $key = \"${it.routePath}\""
            }
        }
                |}
                |""".trimMargin()
        return routeKeysTemplate
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
        } catch (e: FileAlreadyExistsException) {
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
            logger = environment.logger,
            options = environment.options
        )
    }
}
