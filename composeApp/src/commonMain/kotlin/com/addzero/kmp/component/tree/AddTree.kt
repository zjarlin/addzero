package com.addzero.kmp.component.tree

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.addzero.kmp.anno.Good
import com.addzero.kmp.component.tree.NodeType.Companion.guessIcon

/**
 * 树形组件 - 核心渲染逻辑
 * @param items 树形结构数据列表
 * @param getId 获取节点ID的函数
 * @param getLabel 获取节点标签的函数
 * @param getChildren 获取子节点的函数
 * @param getNodeType 获取节点类型的函数
 * @param getIcon 获取节点图标的函数
 * @param initiallyExpandedIds 初始展开的节点ID列表
 * @param onCurrentNodeClick 节点点击回调
 * @param onNodeContextMenu 节点右键菜单回调
 * @param nodeRender 节点渲染函数
 * @param topSlot 顶部插槽（通常用于搜索）
 * @param multiSelectRender 多选模式插槽
 * @param expandAllSlot 展开全部插槽
 * @param collapseAllSlot 收起全部插槽
 * @param bottomSlot 底部插槽
 * @param contextMenuContent 右键菜单内容插槽
 */
@Composable
@Good
fun <T> AddTree(
    items: List<T>,
    modifier: Modifier = Modifier,
    getId: (T) -> Any,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>,
    getNodeType: (T) -> String = { "" },
    getIcon: @Composable (node: T) -> ImageVector? = {
        guessIcon(getChildren, it, getLabel)
    },
    initiallyExpandedIds: Set<Any> = emptySet(),
    onCurrentNodeClick: (T) -> Unit = {},
    onNodeContextMenu: (T) -> Unit = {},
    nodeRender: @Composable (TreeNodeInfo<T>) -> Unit = {
        DefaultNodeRender(it)
    },
    // 插槽组件
    topSlot: @Composable () -> Unit = {},
    multiSelectRender: @Composable (TreeNodeInfo<T>, Boolean, (Boolean) -> Unit) -> Unit = { _, _, _ -> },
    expandAllSlot: @Composable (Set<Any>, (Set<Any>) -> Unit) -> Unit = { _, _ -> },
    collapseAllSlot: @Composable (Set<Any>, () -> Unit) -> Unit = { _, _ -> },
    bottomSlot: @Composable () -> Unit = {},
    contextMenuContent: @Composable (T) -> Unit = {},
    // 多选模式
    multiSelectMode: Boolean = false,
    onSelectionChange: (List<T>) -> Unit = { _ -> },
) {
    // 全局树状态
    val treeState = remember {
        TreeState(
            selectedNodeId = null,
            expandedIds = initiallyExpandedIds.toMutableSet()
        )
    }

    // 状态委托
    var selectedNodeId: Any? by remember { mutableStateOf(treeState.selectedNodeId) }
    var expandedIds by remember { mutableStateOf(treeState.expandedIds) }
    var selectedItems by remember { mutableStateOf(setOf<Any>()) }

    // 用于多选模式
    LaunchedEffect(selectedItems, multiSelectMode) {
        if (multiSelectMode) {
            val selectedNodes = items.flatMap { getAllNodes(it, getChildren) }
                .filter { getId(it) in selectedItems }
            onSelectionChange(selectedNodes)
        }
    }

    // 展开全部函数
    val expandAll = {
        expandedIds = getAllIds(items, getId, getChildren).toMutableSet()
    }

    // 折叠全部函数
    val collapseAll = {
        expandedIds = mutableSetOf()
    }

    Column(modifier = modifier) {
        // 顶部插槽 (搜索等)
        topSlot()

        // 展开/折叠全部控制
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.align(Alignment.CenterStart)) {
                expandAllSlot(expandedIds) { expandedIds = it }
            }
            Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                collapseAllSlot(expandedIds) { collapseAll() }
            }
        }

        // 树形结构
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column {
                // 递归渲染顶层节点
                items.forEach { node ->
                    TreeNode(
                        node = node,
                        getId = getId,
                        getLabel = getLabel,
                        getChildren = getChildren,
                        getNodeType = getNodeType,
                        getIcon = getIcon,
                        level = 0,
                        selectedNodeId = selectedNodeId,
                        expandedIds = expandedIds,
                        onNodeClick = { clickedNode ->
                            val nodeId = getId(clickedNode)
                            if (multiSelectMode) {
                                // 多选模式：切换选择状态
                                selectedItems = if (selectedItems.contains(nodeId)) {
                                    selectedItems - nodeId
                                } else {
                                    selectedItems + nodeId
                                }
                            } else {
                                // 单选模式：设置选中节点
                                selectedNodeId = nodeId
                                onCurrentNodeClick(clickedNode)
                            }

                            // 如果有子节点，切换展开状态
                            if (getChildren(clickedNode).isNotEmpty()) {
                                expandedIds = if (expandedIds.contains(nodeId)) {
                                    expandedIds - nodeId
                                } else {
                                    expandedIds + nodeId
                                }
                            }
                        },
                        onNodeContextMenu = onNodeContextMenu,
                        nodeRender = nodeRender,
                        contextMenuContent = contextMenuContent,
                        multiSelectMode = multiSelectMode,
                        multiSelectRender = multiSelectRender,
                        selectedItems = selectedItems,
                        onSelectionChange = { id, selected ->
                            selectedItems = if (selected) {
                                selectedItems + id
                            } else {
                                selectedItems - id
                            }
                        }
                    )
                }
            }
        }

        // 底部插槽
        bottomSlot()
    }
}

/**
 * 保存树状态的内部类
 */
private class TreeState<T>(
    var selectedNodeId: T?,
    var expandedIds: Set<Any>
)

/**
 * 获取树中所有节点的ID
 */
private fun <T> getAllIds(
    items: List<T>,
    getId: (T) -> Any,
    getChildren: (T) -> List<T>
): Set<Any> {
    val result = mutableSetOf<Any>()

    fun collectIds(nodes: List<T>) {
        nodes.forEach { node ->
            result.add(getId(node))
            collectIds(getChildren(node))
        }
    }

    collectIds(items)
    return result
}

/**
 * 获取树中所有节点
 */
private fun <T> getAllNodes(
    root: T,
    getChildren: (T) -> List<T>
): List<T> {
    val result = mutableListOf<T>()

    fun collect(node: T) {
        result.add(node)
        getChildren(node).forEach { child ->
            collect(child)
        }
    }

    collect(root)
    return result
}

/**
 * 单个树节点及其子节点的递归渲染
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun <T> TreeNode(
    node: T,
    getId: (T) -> Any,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>,
    getNodeType: (T) -> String,
    getIcon: @Composable ((T) -> ImageVector?),
    level: Int,
    selectedNodeId: Any?,
    expandedIds: Set<Any>,
    onNodeClick: (T) -> Unit,
    onNodeContextMenu: (T) -> Unit,
    nodeRender: @Composable ((TreeNodeInfo<T>) -> Unit),
    contextMenuContent: @Composable (T) -> Unit,
    multiSelectMode: Boolean,
    multiSelectRender: @Composable (TreeNodeInfo<T>, Boolean, (Boolean) -> Unit) -> Unit,
    selectedItems: Set<Any>,
    onSelectionChange: (Any, Boolean) -> Unit
) {
    val nodeId = getId(node)
    val children = getChildren(node)
    val hasChildren = children.isNotEmpty()
    val isExpanded = expandedIds.contains(nodeId)
    val isSelected = if (multiSelectMode) {
        nodeId in selectedItems
    } else {
        nodeId == selectedNodeId
    }
    val nodeType = getNodeType(node)
    val icon = getIcon(node)

    // 构建节点信息
    val nodeInfo = TreeNodeInfo(
        node = node,
        id = nodeId,
        label = getLabel(node),
        hasChildren = hasChildren,
        isExpanded = isExpanded,
        isSelected = isSelected,
        level = level,
        nodeType = nodeType,
        icon = icon,
        onNodeClick = onNodeClick
    )

    // 显示右键菜单状态
    var showContextMenu by remember { mutableStateOf(false) }

    Column {
        // 显示右键菜单
        if (showContextMenu) {
            contextMenuContent(node)
            showContextMenu = false
        }

        // 直接渲染节点，使用节点自身的交互功能
        if (multiSelectMode) {
            // 多选模式渲染
            multiSelectRender(
                nodeInfo,
                nodeId in selectedItems,
                { selected -> onSelectionChange(nodeId, selected) }
            )
        } else {
            // 标准节点渲染
            nodeRender(nodeInfo)
        }

        // 如果展开且有子节点，渲染子节点
        if (isExpanded && hasChildren) {
            children.forEach { childNode ->
                TreeNode(
                    node = childNode,
                    getId = getId,
                    getLabel = getLabel,
                    getChildren = getChildren,
                    getNodeType = getNodeType,
                    getIcon = getIcon,
                    level = level + 1,
                    selectedNodeId = selectedNodeId,
                    expandedIds = expandedIds,
                    onNodeClick = onNodeClick,
                    onNodeContextMenu = onNodeContextMenu,
                    nodeRender = nodeRender,
                    contextMenuContent = contextMenuContent,
                    multiSelectMode = multiSelectMode,
                    multiSelectRender = multiSelectRender,
                    selectedItems = selectedItems,
                    onSelectionChange = onSelectionChange
                )
            }
        }
    }
}

