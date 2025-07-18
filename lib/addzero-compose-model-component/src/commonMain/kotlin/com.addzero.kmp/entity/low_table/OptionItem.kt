package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable

/**
 * 下拉/多选选项
 */
@Serializable
data class OptionItem(
    val value: String,
    val label: String
)
