import com.addzero.kmp.util.lowerFirst
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger

/**
 * API 提供者代码生成器 (shared模块使用
 *
 * 负责根据服务元数据生成 ApiProvider 类
 */
class ApiProviderCodeGenerator(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) {

    companion object {
        private const val PACKAGE_NAME = "com.addzero.kmp.generated.api"
        private const val PROVIDER_NAME = "ApiProvider"
    }

    /**
     * 生成 API 提供者类
     *
     * @param serviceMetadataList 服务元数据列表
     */
    fun generateApiProvider(serviceMetadataList: List<ServiceMetadataExtractor.ServiceMetadata>) {
        if (serviceMetadataList.isEmpty()) {
            logger.info("没有服务接口，跳过 ApiProvider 生成")
            return
        }

        logger.info("开始生成 ApiProvider 类...")

        // 收集所有源文件的依赖
        val dependencies = serviceMetadataList.map { it.containingFile }.toSet()

        // 生成服务属性
        val serviceProperties = generateServiceProperties(serviceMetadataList)

        // 构建完整的类内容
        val classContent = """
            package $PACKAGE_NAME
            import com.addzero.kmp.core.network.AddHttpClient.ktorfit
           import com.addzero.kmp.generated.api.* 
            
            /**
             * 服务实例提供者
             * 
             * 提供所有 Ktorfit 服务接口的实例
             * 由 KSP 自动生成，不要手动修改
             */
            object $PROVIDER_NAME {
                $serviceProperties
            }
        """.trimIndent()

        // 创建文件
        try {
            codeGenerator.createNewFile(
                dependencies = Dependencies(true, *dependencies.toTypedArray()),
                packageName = PACKAGE_NAME,
                fileName = PROVIDER_NAME
            ).use { stream ->
                stream.write(classContent.toByteArray())
            }

            logger.info("成功生成 ApiProvider 类，包含 ${serviceMetadataList.size} 个服务接口")
        } catch (e: Exception) {
            logger.warn("生成 ApiProvider 类失败: ${e.message}")
            throw e
        }
    }

    /**
     * 生成服务属性
     *
     * @param serviceMetadataList 服务元数据列表
     * @return 服务属性字符串
     */
    private fun generateServiceProperties(serviceMetadataList: List<ServiceMetadataExtractor.ServiceMetadata>): String {
        return serviceMetadataList.joinToString("\n    ") { service ->
            val propertyName = service.simpleName.lowerFirst()
            val createMethodName = "create${service.simpleName}"

            """/**
     * ${service.simpleName} 服务实例
     */
    val $propertyName: ${service.qualifiedName} = ktorfit.$createMethodName()"""
        }
    }
}
