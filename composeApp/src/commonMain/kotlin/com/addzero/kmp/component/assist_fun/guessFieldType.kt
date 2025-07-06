package com.addzero.kmp.component.assist_fun

import com.addzero.kmp.entity.low_table.ConfigFieldTypeMapper
import com.addzero.kmp.entity.low_table.EnumFieldRenderType

/**
 * 猜测字段类型
 */
fun guessFieldType(fieldName: String, javaType: String? = null): EnumFieldRenderType {
    // 1. 如果提供了Java类型,先尝试从类型推导
    if (!javaType.isNullOrBlank()) {
        val baseType = ConfigFieldTypeMapper.convertJvmTypeToBaseType(javaType)

        // 2. 尝试从字段名称推测
        val guessedTypes = ConfigFieldTypeMapper.guessTypeByFieldName(fieldName)
        if (guessedTypes.isNotEmpty()) {
            // 检查字段名推测的类型是否与字段基础类型兼容
            val compatibleType = guessedTypes.firstOrNull {
                ConfigFieldTypeMapper.getAvailableRenderTypes(baseType).contains(it)
            }
            if (compatibleType != null) {
                return compatibleType
            }
        }

        // 3. 使用基础类型的默认渲染类型
        return ConfigFieldTypeMapper.getDefaultRenderType(baseType, fieldName)
    }

    // 如果没有Java类型,仅从字段名推测
    val guessedTypes = ConfigFieldTypeMapper.guessTypeByFieldName(fieldName)
    if (guessedTypes.isNotEmpty()) {
        return guessedTypes.first()
    }

    // 默认返回文本类型
    return EnumFieldRenderType.TEXT
}
