package site.addzero.entity.analysis.processor

import site.addzero.context.SettingContext
import site.addzero.entity.analysis.analyzer.JimmerEntityAnalyzer
import site.addzero.entity.analysis.model.EntityMetadata
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

/**
 * Jimmer 处理器基类
 *
 * 提供统一的 Jimmer 实体元数据收集流程：
 * 1. 查找 Jimmer 实体
 * 2. 收集实体元数据
 * 3. 子类实现具体的处理逻辑
 *
 * 注意：此基类只负责元数据收集，不涉及具体的代码生成逻辑
 */
abstract class BaseJimmerProcessor(
    protected val codeGenerator: CodeGenerator,
    protected val logger: KSPLogger,
    protected val options: Map<String, String>
) : SymbolProcessor {

    // Jimmer 实体分析器
    protected val entityAnalyzer = JimmerEntityAnalyzer(logger)

    // 存储收集到的实体元数据
    protected val collectedEntities = mutableListOf<EntityMetadata>()

    // 存储原始的实体声明（用于需要访问 KSPropertyDeclaration 的处理器）
    protected val entityDeclarations = mutableMapOf<String, KSClassDeclaration>()

    override fun process(resolver: Resolver): List<KSAnnotated> {
        SettingContext.initialize(options)

        // 检查是否应该执行此处理器
        if (!shouldProcess()) {
            logger.warn("${this::class.simpleName} 跳过执行")
            return emptyList()
        }

        logger.warn("🚀 ${this::class.simpleName} 开始执行！")
        logger.warn("BaseJimmer元数据处理器初始化配置: ${SettingContext.settings}")

        // 查找所有 Jimmer 实体
        val entitySymbols = resolver
            .getSymbolsWithAnnotation("org.babyfish.jimmer.sql.Entity")
            .filterIsInstance<KSClassDeclaration>()

        logger.warn("找到 ${entitySymbols.count()} 个 Jimmer 实体")

        if (!entitySymbols.iterator().hasNext()) {
            logger.warn("没有找到任何 Jimmer 实体，退出处理")
            return emptyList()
        }

        // 第一阶段：收集实体元数据
        entitySymbols.forEach { entity ->
            logger.warn("收集实体元数据: ${entity.simpleName.asString()}")
            try {
                val metadata = entityAnalyzer.analyzeEntity(entity)
                collectedEntities.add(metadata)
                // 保存原始实体声明
                entityDeclarations[metadata.qualifiedName] = entity
            } catch (e: Exception) {
                logger.error("收集实体元数据失败: ${entity.simpleName.asString()}, 错误: ${e.message}")
            }
        }

        logger.warn("元数据收集完成，共收集 ${collectedEntities.size} 个实体")
        return entitySymbols.filterNot { it.validate() }.toList()
    }

    override fun finish() {
        if (collectedEntities.isEmpty()) {
            logger.warn("没有收集到实体元数据，跳过处理")
            return
        }

        logger.warn("开始处理收集到的实体元数据...")

        try {
            // 调用子类的处理逻辑
            processEntities(collectedEntities)

            logger.warn("实体元数据处理完成")
        } catch (e: Exception) {
            logger.error("实体元数据处理失败: ${e.message}")
            throw e
        }
    }

    /**
     * 检查是否应该执行此处理器
     * 子类可以重写此方法来实现条件执行
     */
    protected open fun shouldProcess(): Boolean = true

    /**
     * 处理实体元数据
     * 子类必须实现此方法来定义具体的处理逻辑（如代码生成等）
     */
    protected abstract fun processEntities(entities: List<EntityMetadata>)
}
