import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate

/**
 * 服务元数据抽取器
 *
 * 负责从源代码中抽取 Ktorfit 服务接口的元数据
 */
class ServiceMetadataExtractor(
    private val logger: KSPLogger
) {

    /**
     * 服务元数据
     */
    data class ServiceMetadata(
        val qualifiedName: String,
        val simpleName: String,
        val containingFile: KSFile
    )

    /**
     * HTTP 方法注解列表
     */
    private val httpMethodAnnotations = listOf(
        "de.jensklingenberg.ktorfit.http.GET",
        "de.jensklingenberg.ktorfit.http.POST",
        "de.jensklingenberg.ktorfit.http.PUT",
        "de.jensklingenberg.ktorfit.http.DELETE",
        "de.jensklingenberg.ktorfit.http.PATCH",
        "de.jensklingenberg.ktorfit.http.HEAD",
        "de.jensklingenberg.ktorfit.http.OPTIONS"
    )

    /**
     * 从解析器中提取服务元数据
     *
     * @param resolver KSP 解析器
     * @return 服务元数据列表
     */
    fun extractServiceMetadata(resolver: Resolver): List<ServiceMetadata> {
        logger.info("开始收集 Ktorfit 服务接口元数据...")

        // 收集所有具有HTTP方法注解的函数
        val annotatedFunctions = mutableListOf<KSFunctionDeclaration>()

        // 遍历每个HTTP方法注解，查找使用该注解的函数
        for (annotationName in httpMethodAnnotations) {
            val functions = resolver.getSymbolsWithAnnotation(annotationName)
                .filterIsInstance<KSFunctionDeclaration>()
                .filter { it.validate() }

            annotatedFunctions.addAll(functions)
        }

        if (annotatedFunctions.isEmpty()) {
            logger.info("未找到任何带有 HTTP 方法注解的函数")
            return emptyList()
        }

        logger.info("找到 ${annotatedFunctions.size} 个带有 HTTP 方法注解的函数")

        // 获取包含这些函数的类（即服务接口）
        val services = annotatedFunctions
            .map { it.parentDeclaration as? KSClassDeclaration }
            .filterNotNull()
            .distinct()
            .mapNotNull { service ->
                val qualifiedName = service.qualifiedName?.asString()
                val containingFile = service.containingFile

                if (qualifiedName != null && containingFile != null) {
                    ServiceMetadata(
                        qualifiedName = qualifiedName,
                        simpleName = service.simpleName.asString(),
                        containingFile = containingFile
                    )
                } else {
                    logger.warn("服务接口 ${service.simpleName.asString()} 缺少必要信息，跳过")
                    null
                }
            }

        logger.info("成功收集到 ${services.size} 个服务接口的元数据")

        return services
    }
}
