package com.addzero.kmp.component.tree

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

class TreeAssist

/**
 * 获取节点类型对应的图标
 */
@Composable
fun getNodeTypeIcon(nodeType: NodeType, isExpanded: Boolean = false): ImageVector {
    return nodeType.getIcon(isExpanded)
}

/**
 * 获取节点类型对应的图标颜色
 */
@Composable
fun getNodeTypeColor(nodeType: NodeType): Color {
    return nodeType.getColor()
}

fun <T> getDefaultNodeType(
    node: T, getLabel: (T) -> String, getId: (T) -> Any, getChildren: (T) -> List<T>
): NodeType {
    val label = getLabel(node)
    val hasChildren = getChildren(node).isNotEmpty()
    val id = getId(node).toString()

    // 特殊ID处理
    if (id == "dept_1") {
        return NodeType.COMPANY
    }

    // 使用NodeType提供的猜测功能
    return NodeType.guess(label, hasChildren)
}