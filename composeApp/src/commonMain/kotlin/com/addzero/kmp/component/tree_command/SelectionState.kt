package com.addzero.kmp.component.tree_command

/**
 * 多选状态下节点的选择状态
 */
enum class SelectionState {
    NONE,       // 未选择
    SELECTED,   // 已选择
    PARTIAL     // 部分选择(子节点部分选择)
}