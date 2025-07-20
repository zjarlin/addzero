package com.addzero.kmp.processor.type

import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSValueParameter

/**
 * 类型映射管理器
 * 负责协调各种类型映射策略，将后端类型映射为前端兼容类型
 */
object TypeMappingManager {

    private val strategies = listOf(
        JimmerEntityMappingStrategy(),
        SpringTypeMappingStrategy(),
        DefaultTypeMappingStrategy()
    ).sortedByDescending { it.getPriority() }

    /**
     * 映射 KSType 为前端兼容的类型字符串
     */
    fun mapType(type: KSType): String {
        return try {
            // 找到第一个支持该类型的策略
            val strategy = strategies.firstOrNull { it.supports(type) }
                ?: strategies.last() // 使用默认策略作为兜底

            strategy.mapType(type)
        } catch (e: Exception) {
            // 发生异常时使用安全的回退类型
            "kotlin.Any"
        }
    }

    /**
     * 安全地映射参数类型
     */
    fun mapParameterType(parameter: KSValueParameter): String {
        return try {
            val type = parameter.type.resolve()
            mapType(type)
        } catch (e: Exception) {
            // 参数类型解析失败时的回退处理
            val rawTypeString = parameter.type.toString()
            if (isValidTypeString(rawTypeString)) {
                rawTypeString
            } else {
                "kotlin.Any"
            }
        }
    }

    /**
     * 安全地映射返回类型
     */
    fun mapReturnType(function: KSFunctionDeclaration): String {
        return try {
            val returnType = function.returnType?.resolve()
            if (returnType != null) {
                mapType(returnType)
            } else {
                "kotlin.Unit"
            }
        } catch (e: Exception) {
            // 返回类型解析失败时的回退处理
            val rawTypeString = function.returnType?.toString()
            if (rawTypeString != null && isValidTypeString(rawTypeString)) {
                rawTypeString
            } else {
                "kotlin.Unit"
            }
        }
    }

    /**
     * 检查类型字符串是否有效
     */
    private fun isValidTypeString(typeString: String): Boolean {
        return !typeString.contains("<ERROR") &&
                !typeString.any {
                    !it.isLetterOrDigit() && it != '.' && it != '_' && it != '$' &&
                            it != '<' && it != '>' && it != '?' && it != ',' && it != ' '
                } &&
                typeString.isNotBlank()
    }

    /**
     * 添加自定义类型映射策略
     */
    fun addStrategy(strategy: TypeMappingStrategy) {
        // 这里可以实现动态添加策略的逻辑
        // 目前使用静态列表，如果需要可以改为可变列表
    }

    /**
     * 获取所有已注册的策略（用于调试）
     */
    fun getStrategies(): List<TypeMappingStrategy> {
        return strategies.toList()
    }
}
