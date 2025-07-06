
import com.addzero.kmp.util.lowerFirst
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate

private const val PKG = "com.addzero.kmp.api"


private const val providerName = """ApiProvider"""

class KtorfitServiceProviderProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // 找出所有可能的HTTP方法注解
        val httpMethodAnnotations = listOf(
            "de.jensklingenberg.ktorfit.http.GET",
            "de.jensklingenberg.ktorfit.http.POST",
            "de.jensklingenberg.ktorfit.http.PUT",
            "de.jensklingenberg.ktorfit.http.DELETE",
            "de.jensklingenberg.ktorfit.http.PATCH",
            "de.jensklingenberg.ktorfit.http.HEAD",
            "de.jensklingenberg.ktorfit.http.OPTIONS"
        )

        // 收集所有具有HTTP方法注解的函数
        val annotatedFunctions = mutableListOf<KSFunctionDeclaration>()

        // 遍历每个HTTP方法注解，查找使用该注解的函数
        for (annotationName in httpMethodAnnotations) {
            val functions = resolver.getSymbolsWithAnnotation(annotationName)
                .filterIsInstance<KSFunctionDeclaration>()
                .filter { it.validate() }

            annotatedFunctions.addAll(functions)
        }

        // 获取包含这些函数的类（即服务接口）
        val services = annotatedFunctions
            .map { it.parentDeclaration as? KSClassDeclaration }
            .filterNotNull()
            .distinct()
            .toList()

        if (services.isEmpty()) return emptyList()

        // 收集所有源文件的依赖
        val dependencies = services.map { it.containingFile!! }.toSet()

        val text = """
            package $PKG
            import com.addzero.kmp.core.network.AddHttpClient.ktorfit
            
            /**
             * 服务实例提供
             * 由KSP自动生成
             */
            object $providerName {
                ${
            services.joinToString("\n    ") { service ->
                val qfName = service.qualifiedName!!.asString()
                val simpleNameUpper = service.simpleName.asString()
                val simpleName = simpleNameUpper.lowerFirst()

                "val $simpleName: $qfName = ktorfit.create$simpleNameUpper()"
            }
        }
            }
        """.trimIndent()

        val file = codeGenerator.createNewFile(
            dependencies = Dependencies(true, *dependencies.toTypedArray()),
            packageName = PKG,
            fileName = "$providerName"
        ).use { stream ->
            stream.write(text.toByteArray())
        }

        return emptyList()
    }
}

class KtorfitServiceProviderProcessorProvider : SymbolProcessorProvider {

    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return KtorfitServiceProviderProcessor(
            environment.codeGenerator,
            environment.logger,
            environment.options
        )
    }
}
