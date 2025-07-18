package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable

/**
 * 表单字段配置
 */
@Serializable
data class StateFormField(
    val field: String,
    val label: String,
    val type: EnumFieldRenderType,
    val required: Boolean = false,
    val placeholder: String? = null,
    val helpText: String? = null,
    val defaultValue: String? = null,
    val options: List<OptionItem>? = null,
    val readOnly: Boolean = false,
    val hidden: Boolean = false
)


