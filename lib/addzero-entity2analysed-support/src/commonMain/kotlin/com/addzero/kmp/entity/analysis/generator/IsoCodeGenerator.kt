package com.addzero.kmp.entity.analysis.generator

import com.addzero.kmp.entity.analysis.model.EntityMetadata
import com.addzero.kmp.entity.analysis.model.PropertyMetadata
import com.google.devtools.ksp.processing.KSPLogger

/**
 * 同构体代码生成器
 *
 * 负责生成同构体类的代码
 */
class IsoCodeGenerator(
    private val logger: KSPLogger
) {

    /**
     * 生成同构体代码
     */
    fun generateIsoCode(
        metadata: EntityMetadata,
        packageName: String = "com.addzero.kmp.isomorphic"
    ): String {
        // 生成属性代码
        val props = metadata.properties.joinToString(",\n") { prop ->
            generatePropertyCode(prop)
        }

        // 生成优化的导入语句
        val optimizedImports = generateOptimizedImports(metadata)

        // 生成同构体代码
        return """
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
     * 2. 将 Jimmer 实体导包替换为同构体导包
     */
    private fun generateOptimizedImports(metadata: EntityMetadata): String {
        val optimizedImports = mutableSetOf<String>()

        metadata.properties.forEach { prop ->
            when {
                // 日期时间类型：使用 kotlinx.datetime
                prop.typeName in setOf("LocalDateTime", "LocalDate", "Instant") -> {
                    optimizedImports.add("import kotlinx.datetime.${prop.typeName}")
                    // 添加序列化支持
                    optimizedImports.add("import kotlinx.serialization.Contextual")
                    optimizedImports.add("import kotlin.time.ExperimentalTime")


                }

                // Jimmer 实体类型：不导入原实体，因为我们使用同构体
                prop.isJimmerEntity && !prop.isCollection -> {
                    // 不添加原实体导入，同构体在同一包下
                }

                // 集合中的 Jimmer 实体：不导入原实体
                prop.isCollection && prop.genericType != null -> {
                    // 如果泛型不是 Jimmer 实体，可能需要导入
                    if (!prop.isJimmerEntity) {
                        prop.genericQualifiedType?.let { qualifiedType ->
                            if (!isKotlinBuiltinType(qualifiedType)) {
                                optimizedImports.add("import $qualifiedType")
                            }
                        }
                    }
                }

                // 枚举类型：导入原枚举
                prop.isEnum -> {
                    prop.qualifiedTypeName?.let { qualifiedType ->
                        optimizedImports.add("import $qualifiedType")
                    }
                }

                // 其他非基础类型
                !prop.isBasicType && prop.qualifiedTypeName != null -> {
                    val qualifiedType = prop.qualifiedTypeName!!
                    if (!isKotlinBuiltinType(qualifiedType) && !isJavaTimeType(qualifiedType)) {
                        optimizedImports.add("import $qualifiedType")
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
                qualifiedType in setOf("String", "Int", "Long", "Double", "Float", "Boolean")
    }

    /**
     * 判断是否为 Java 时间类型（需要替换为 kotlinx.datetime）
     */
    private fun isJavaTimeType(qualifiedType: String): Boolean {
        return qualifiedType.startsWith("java.time.")
    }

    /**
     * 写入同构体文件
     */
    fun writeIsoFile(
        metadata: EntityMetadata,
        outputDir: String,
        packageName: String = "com.addzero.kmp.isomorphic"
    ) {
        val code = generateIsoCode(metadata, packageName)

        // 写入文件
        val file = java.io.File("$outputDir/${metadata.isoClassName}.kt")
        file.parentFile?.mkdirs()
        file.writeText(code)

        logger.info("Generated isomorphic data class: ${metadata.isoClassName} at ${file.absolutePath}")
    }
}
