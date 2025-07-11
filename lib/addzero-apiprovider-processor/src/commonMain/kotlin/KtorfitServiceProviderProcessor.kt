
import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Ktorfit 服务提供者处理器
 *
 * 功能：处理带有 HTTP 方法注解的服务接口，生成 ApiProvider 类
 * 采用两阶段处理：process阶段收集元数据，finish阶段生成代码
 */
class KtorfitServiceProviderProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {

    // 服务元数据抽取器
    private val metadataExtractor = ServiceMetadataExtractor(logger)

    // API 提供者代码生成器
    private val apiProviderGenerator = ApiProviderCodeGenerator(codeGenerator, logger)

    // 收集到的服务元数据
    private var serviceMetadataList: List<ServiceMetadataExtractor.ServiceMetadata> = emptyList()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // process 阶段只收集元数据，不生成代码
        if (serviceMetadataList.isEmpty()) {
            serviceMetadataList = metadataExtractor.extractServiceMetadata(resolver)
        }
        return emptyList()
    }

    override fun finish() {
        // finish 阶段生成代码
        if (serviceMetadataList.isNotEmpty()) {
            apiProviderGenerator.generateApiProvider(serviceMetadataList)
        }
    }
}

/**
 * Ktorfit 服务提供者处理器提供者
 *
 * 功能：提供 KtorfitServiceProviderProcessor 实例
 */
class KtorfitServiceProviderProcessorProvider : SymbolProcessorProvider {

    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return KtorfitServiceProviderProcessor(
            codeGenerator = environment.codeGenerator,
            logger = environment.logger,
            options = environment.options
        )
    }
}
