package com.addzero.kmp.entity.low_table

/**
 * 表格动作按钮
 */
data class StateActionButton<T>(
    val text: String,
    val icon: String,
    val onClick: (item: T) -> Unit,
    val visible: (item: T) -> Boolean = { true },
    val enabled: (item: T) -> Boolean = { true },
    val color: EnumButtonColor = EnumButtonColor.DEFAULT,
    val confirmMessage: String? = null
)
