package generator

import com.addzero.kmp.entity.analysis.model.EntityMetadata
import com.addzero.kmp.entity.analysis.model.PropertyMetadata
import com.google.devtools.ksp.processing.KSPLogger

/**
 * 同构体代码生成器
 * 负责将 Jimmer 实体元数据转换为同构体 Kotlin 代码
 */
class IsoCodeGenerator(private val logger: KSPLogger) {

    /**
     * 生成同构体代码
     */
    fun generateIsoCode(metadata: EntityMetadata, packageName: String): String {
        // 生成属性代码
        val props = metadata.properties.joinToString(",\n") { prop ->
            generatePropertyCode(prop)
        }

        // 生成优化的导入语句
        val optimizedImports = generateOptimizedImports(metadata)

        // 生成同构体代码
        return """
            |@file:OptIn(kotlin.time.ExperimentalTime::class)
            |
            |package $packageName
            |
            |import kotlinx.serialization.Serializable
            |$optimizedImports
            |
            |@Serializable
            |data class ${metadata.isoClassName}(
            |$props
            |)
        """.trimMargin()
    }

    /**
     * 生成属性代码
     */
    private fun generatePropertyCode(prop: PropertyMetadata): String {
        val nullableSuffix = if (prop.isNullable) "?" else ""
        val contextualAnnotation = if (prop.typeName in setOf("LocalDateTime", "LocalDate", "Instant")) {
            "@Contextual "
        } else ""
        return "    ${contextualAnnotation}val ${prop.name}: ${prop.isoTypeName}$nullableSuffix = ${prop.defaultValue}"
    }

    /**
     * 生成优化的导入语句
     * 1. 修复 java.time.LocalDateTime -> kotlinx.datetime.LocalDateTime
     * 2. 严格过滤掉所有 Jimmer 实体的导入
     */
    private fun generateOptimizedImports(metadata: EntityMetadata): String {
        val optimizedImports = mutableSetOf<String>()

        metadata.properties.forEach { prop ->
            when {
                // 日期时间类型：使用 kotlinx.datetime
                prop.typeName in setOf("LocalDateTime", "LocalDate", "Instant") -> {
                    optimizedImports.add("import kotlinx.datetime.${prop.typeName}")
                    optimizedImports.add("import kotlinx.serialization.Contextual")
                    optimizedImports.add("import kotlinx.datetime.toLocalDateTime")
                    optimizedImports.add("import kotlin.time.ExperimentalTime")
//                    optimizedImports.add("import kotlinx.datetime.Clock")
                    optimizedImports.add("import kotlinx.datetime.TimeZone")
                }

                // Jimmer 实体类型：绝对不导入原实体，同构体在同一包下
                prop.isJimmerEntity -> {
                    logger.info("跳过 Jimmer 实体导入: ${prop.qualifiedTypeName}")
                }

                // 集合类型：检查泛型是否为 Jimmer 实体
                prop.isCollection -> {
                    val genericQualified = prop.genericQualifiedType
                    if (genericQualified != null && !isJimmerEntityByPackage(genericQualified)) {
                        // 只有非 Jimmer 实体的泛型才导入
                        if (!isKotlinBuiltinType(genericQualified) && !isJavaTimeType(genericQualified)) {
                            optimizedImports.add("import $genericQualified")
                        }
                    } else if (genericQualified != null) {
                        logger.info("跳过集合中的 Jimmer 实体导入: $genericQualified")
                    }
                }

                // 枚举类型：导入原枚举（枚举不需要转换为同构体）
                prop.isEnum -> {
                    prop.qualifiedTypeName?.let { qualifiedType ->
                        if (!isJimmerEntityByPackage(qualifiedType)) {
                            optimizedImports.add("import $qualifiedType")
                        }
                    }
                }

                // 其他非基础类型：严格检查是否为 Jimmer 实体
                !prop.isBasicType && prop.qualifiedTypeName != null -> {
                    val qualifiedType = prop.qualifiedTypeName!!
                    if (!isKotlinBuiltinType(qualifiedType) &&
                        !isJavaTimeType(qualifiedType) &&
                        !isJimmerEntityByPackage(qualifiedType)
                    ) {
                        optimizedImports.add("import $qualifiedType")
                    } else if (isJimmerEntityByPackage(qualifiedType)) {
                        logger.info("跳过 Jimmer 实体导入: $qualifiedType")
                    }
                }
            }
        }

        return optimizedImports.sorted().joinToString("\n")
    }

    /**
     * 判断是否为 Kotlin 内置类型
     */
    private fun isKotlinBuiltinType(qualifiedType: String): Boolean {
        return qualifiedType.startsWith("kotlin.") ||
                qualifiedType in setOf("String", "Int", "Long", "Boolean", "Double", "Float", "List", "Set", "Map")
    }

    /**
     * 判断是否为 Java 时间类型（需要替换为 kotlinx.datetime）
     */
    private fun isJavaTimeType(qualifiedType: String): Boolean {
        return qualifiedType.startsWith("java.time.")
    }

    /**
     * 通过包名判断是否为 Jimmer 实体
     * 这是一个额外的安全检查，防止遗漏的 Jimmer 实体导入
     */
    private fun isJimmerEntityByPackage(qualifiedType: String): Boolean {
        return qualifiedType.contains(".entity.") ||
                qualifiedType.contains(".modules.") ||
                qualifiedType.startsWith("com.addzero.web.modules.")
    }
}
