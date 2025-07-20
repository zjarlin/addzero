package com.addzero.kmp.processor.type

import com.google.devtools.ksp.symbol.KSType

/**
 * 类型映射策略接口
 * 负责将后端类型映射为前端兼容的类型
 */
interface TypeMappingStrategy {
    /**
     * 检查是否支持处理该类型
     */
    fun supports(type: KSType): Boolean

    /**
     * 获取策略优先级，数值越高优先级越高
     */
    fun getPriority(): Int

    /**
     * 执行类型映射
     */
    fun mapType(type: KSType): String
}

/**
 * Jimmer 实体类型映射策略
 * 将 Jimmer 实体映射为同构体类型
 */
class JimmerEntityMappingStrategy : TypeMappingStrategy {
    override fun supports(type: KSType): Boolean {
        return isJimmerEntity(type)
    }

    override fun getPriority(): Int = 100

    override fun mapType(type: KSType): String {
        val simpleName = type.declaration.simpleName.asString()
        return "com.addzero.kmp.generated.isomorphic.${simpleName}Iso"
    }

    private fun isJimmerEntity(type: KSType): Boolean {
        return try {
            val declaration = type.declaration
            declaration.annotations.any {
                it.shortName.asString() == "Entity" &&
                        it.annotationType.resolve().declaration.qualifiedName?.asString()?.contains("jimmer") == true
            }
        } catch (e: Exception) {
            false
        }
    }
}

/**
 * Spring 类型映射策略
 * 将 Spring 特有类型映射为跨平台兼容类型
 */
class SpringTypeMappingStrategy : TypeMappingStrategy {
    override fun supports(type: KSType): Boolean {
        val qualifiedName = type.declaration.qualifiedName?.asString() ?: ""
        return qualifiedName.startsWith("org.springframework.")
    }

    override fun getPriority(): Int = 90

    override fun mapType(type: KSType): String {
        val qualifiedName = type.declaration.qualifiedName?.asString() ?: ""
        val simpleName = type.declaration.simpleName.asString()

        return when {
            // MultipartFile 映射为 Ktor 的文件上传类型
            qualifiedName == "org.springframework.web.multipart.MultipartFile" ||
                    simpleName == "MultipartFile" -> {
                "io.ktor.client.request.forms.MultiPartFormDataContent"
            }

            // ResponseEntity 提取泛型参数
            qualifiedName == "org.springframework.http.ResponseEntity" ||
                    simpleName == "ResponseEntity" -> {
                extractGenericType(type) ?: "kotlin.Any"
            }

            // Page 映射为 List
            qualifiedName == "org.springframework.data.domain.Page" ||
                    simpleName == "Page" -> {
                val genericType = extractGenericType(type) ?: "kotlin.Any"
                "kotlin.collections.List<$genericType>"
            }

            // Pageable 在客户端不需要
            qualifiedName == "org.springframework.data.domain.Pageable" ||
                    simpleName == "Pageable" -> {
                "kotlin.Unit" // 或者可以映射为自定义的分页参数类型
            }

            // 其他 Spring 类型映射为 Any
            else -> "kotlin.Any"
        }
    }

    private fun extractGenericType(type: KSType): String? {
        return try {
            val firstArg = type.arguments.firstOrNull()
            firstArg?.type?.resolve()?.let { argType ->
                // 递归处理泛型参数的类型映射
                TypeMappingManager.mapType(argType)
            }
        } catch (e: Exception) {
            null
        }
    }
}

/**
 * 默认类型映射策略
 * 保持原类型不变
 */
class DefaultTypeMappingStrategy : TypeMappingStrategy {
    override fun supports(type: KSType): Boolean = true

    override fun getPriority(): Int = 0

    override fun mapType(type: KSType): String {
        val qualifiedName = type.declaration.qualifiedName?.asString()
        val simpleName = type.declaration.simpleName.asString()

        // 处理可空性
        val nullableSuffix = if (type.nullability == com.google.devtools.ksp.symbol.Nullability.NULLABLE) "?" else ""

        // 处理泛型参数
        val genericArgs = if (type.arguments.isNotEmpty()) {
            type.arguments.joinToString(", ") { arg ->
                arg.type?.resolve()?.let { TypeMappingManager.mapType(it) } ?: "*"
            }
        } else null

        val baseType = qualifiedName ?: simpleName

        return when {
            genericArgs != null -> "$baseType<$genericArgs>$nullableSuffix"
            else -> "$baseType$nullableSuffix"
        }
    }
}
