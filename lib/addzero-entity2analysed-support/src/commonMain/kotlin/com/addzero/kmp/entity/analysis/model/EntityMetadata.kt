package com.addzero.kmp.entity.analysis.model

/**
 * Jimmer 实体元数据
 *
 * 包含生成同构体和表单所需的所有信息
 */
data class EntityMetadata(
    val className: String,
    val packageName: String,
    val qualifiedName: String,
    val properties: List<PropertyMetadata>,
    val imports: Set<String>,

    // 实体描述信息（从注解中提取）
    val description: String = "",

    // 生成相关信息
    val isoClassName: String = "${className}Iso",
    val formClassName: String = "${className}Form"
)

/**
 * 属性元数据
 *
 * 包含生成代码所需的完整属性信息
 */
data class PropertyMetadata(
    val name: String,
    val typeName: String,
    val qualifiedTypeName: String?,
    val isNullable: Boolean,
    val annotations: List<String>,

    // 类型分析结果
    val isJimmerEntity: Boolean,
    val isEnum: Boolean,
    val isCollection: Boolean,
    val isBasicType: Boolean,

    // 集合泛型信息
    val genericType: String?,
    val genericQualifiedType: String?,

    // 生成相关
    val isoTypeName: String,
    val defaultValue: String,
    val label: String,
    val isRequired: Boolean,

    // @LabelProp 相关
    val labelPropField: String? = null
)

/**
 * 枚举元数据
 */
data class EnumMetadata(
    val className: String,
    val packageName: String,
    val qualifiedName: String,
    val values: List<String>,
    val firstValue: String?
)

/**
 * 类型分析结果
 */
data class TypeAnalysisResult(
    val isJimmerEntity: Boolean,
    val isEnum: Boolean,
    val isCollection: Boolean,
    val isBasicType: Boolean,
    val isDateTimeType: Boolean,
    val genericType: String? = null,
    val genericQualifiedType: String? = null
)
