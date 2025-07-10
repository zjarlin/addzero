package com.addzero.kmp.component.tree

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 树节点信息
 * 用于渲染单个树节点
 *
 * ⚠️ 已废弃：新的 AddTree 组件只依赖 TreeViewModel，不再使用 TreeNodeInfo
 *
 * 迁移指南：
 * - 旧方式：使用 TreeNodeInfo 传递节点信息
 * - 新方式：直接使用 TreeViewModel 管理所有状态
 *
 * @see TreeViewModel
 * @see AddTree
 */
@Deprecated(
    message = "TreeNodeInfo 已废弃，新的 AddTree 组件只依赖 TreeViewModel",
    replaceWith = ReplaceWith("TreeViewModel"),
    level = DeprecationLevel.WARNING
)
data class TreeNodeInfo<T>(
    val node: T,                      // 原始节点数据
    val id: Any,                      // 节点ID
    val label: String,                // 节点标签
    val hasChildren: Boolean,         // 是否有子节点
    var isExpanded: Boolean,          // 是否展开
    val isSelected: Boolean,          // 是否选中
    val level: Int,                   // 节点层级（从0开始）
    val nodeType: String,           // 节点类型
    val icon: ImageVector? = null,    // 节点图标
    val onNodeClick: (T) -> Unit       // 节点点击回调
)
