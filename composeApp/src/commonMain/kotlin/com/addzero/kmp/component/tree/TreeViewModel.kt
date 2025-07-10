package com.addzero.kmp.component.tree

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 🎯 树组件的 ViewModel - 管理所有响应式状态
 *
 * 核心优势：
 * - 将复杂的状态管理从UI组件中分离
 * - 提供清晰的状态API和操作方法
 * - 支持状态的独立测试
 * - 减少组件参数，提高可用性
 */
class TreeViewModel<T> {

    // 🌳 核心数据状态
    var items by mutableStateOf<List<T>>(emptyList())

    // 🎯 选择状态
    var selectedNodeId by mutableStateOf<Any?>(null)

    // 📂 展开状态
    var expandedIds by mutableStateOf<Set<Any>>(emptySet())

    // 🔄 多选状态
    var multiSelectMode by mutableStateOf(false)

    var selectedItems by mutableStateOf<Set<Any>>(emptySet())

    // 🔍 搜索状态
    var searchQuery by mutableStateOf("")

    var showSearchBar by mutableStateOf(false)

    // 📋 过滤后的数据 - 使用 derivedStateOf 实现响应式过滤
    val filteredItems by derivedStateOf {
        if (searchQuery.isBlank()) {
            items
        } else {
            filterTreeItems(
                items = items,
                query = searchQuery,
                getLabel = getLabel,
                getChildren = getChildren
            )
        }
    }

    // 🎨 配置状态（不变的配置）
    // ⚠️ 性能问题：这些函数每次访问都会重新计算，应该缓存结果
    var getId: (T) -> Any = { it.hashCode() }
    var getLabel: (T) -> String = { it.toString() }
    var getChildren: (T) -> List<T> = { emptyList() }
    var getNodeType: (T) -> String = { "" }
    var getIcon: @Composable (T) -> ImageVector? = { null }

    // 🚀 优化：添加配置验证
    private var isConfigured = false

    fun configure(
        getId: (T) -> Any,
        getLabel: (T) -> String,
        getChildren: (T) -> List<T>,
        getNodeType: (T) -> String = { "" },
        getIcon: @Composable (T) -> ImageVector? = { null }
    ) {
        this.getId = getId
        this.getLabel = getLabel
        this.getChildren = getChildren
        this.getNodeType = getNodeType
        this.getIcon = getIcon
        isConfigured = true
    }

    // 🎭 事件回调
    var onNodeClick: (T) -> Unit = {}
    var onNodeContextMenu: (T) -> Unit = {}
    var onSelectionChange: (List<T>) -> Unit = {}

    /**
     * 🚀 初始化树数据
     */
    fun setItems(
        newItems: List<T>,
        initiallyExpandedIds: Set<Any> = emptySet()
    ) {
        items = newItems
        expandedIds = initiallyExpandedIds
    }

    /**
     * 🎯 节点选择操作
     */
    fun selectNode(nodeId: Any?) {
        selectedNodeId = nodeId
    }

    fun clickNode(node: T) {
        val nodeId = getId(node)
        val hasChildren = getChildren(node).isNotEmpty()

        // 🎯 恢复原来的行为：
        // - 有子节点：选中但不触发业务回调（展开/收起由 UI 层处理）
        // - 叶子节点：选中并触发业务回调（如导航）
        selectNode(nodeId)

        if (!hasChildren) {
            // 只有叶子节点才触发业务回调
            onNodeClick(node)
        }
    }

    /**
     * 📂 展开/折叠操作
     */
    fun toggleExpanded(nodeId: Any) {
        val currentExpanded = expandedIds.toMutableSet()
        if (nodeId in currentExpanded) {
            currentExpanded.remove(nodeId)
        } else {
            currentExpanded.add(nodeId)
        }
        expandedIds = currentExpanded
    }

    fun expandAll() {
        val allIds = getAllNodeIds(items)
        expandedIds = allIds
    }

    fun collapseAll() {
        expandedIds = emptySet()
    }


    /**
     * 🔄 多选操作
     */
    fun updateMultiSelectMode(enabled: Boolean) {
        multiSelectMode = enabled
        if (!enabled) {
            selectedItems = emptySet()
        }
    }

    fun toggleItemSelection(nodeId: Any) {
        if (!multiSelectMode) return

        val currentSelected = selectedItems.toMutableSet()
        if (nodeId in currentSelected) {
            currentSelected.remove(nodeId)
        } else {
            currentSelected.add(nodeId)
        }
        selectedItems = currentSelected

        // 🚀 性能优化：使用缓存的节点映射避免重复遍历
        notifySelectionChange()
    }

    // 🎯 缓存所有节点的映射，避免重复计算
    private val allNodesCache by derivedStateOf {
        items.flatMap { getAllNodes(it) }
    }

    private fun notifySelectionChange() {
        val selectedNodes = allNodesCache.filter { getId(it) in selectedItems }
        onSelectionChange(selectedNodes)
    }

    fun isItemSelected(nodeId: Any): Boolean {
        return nodeId in selectedItems
    }

    /**
     * 🔍 搜索操作
     */
    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun performSearch() {
        // 🚀 执行搜索时的额外逻辑
        // 当前过滤逻辑在 filteredItems 中自动执行
        // 这里可以添加搜索历史、统计等功能

        // 如果搜索到结果，自动展开包含匹配项的节点
        if (searchQuery.isNotBlank()) {
            expandNodesWithMatches()
        }
    }

    private fun expandNodesWithMatches() {
        // 🎯 自动展开包含搜索结果的节点
        val matchingNodeIds = mutableSetOf<Any>()

        fun findMatchingNodes(nodes: List<T>, parentIds: List<Any> = emptyList()) {
            nodes.forEach { node ->
                val nodeId = getId(node)
                val currentPath = parentIds + nodeId

                // 检查当前节点是否匹配
                if (getLabel(node).contains(searchQuery, ignoreCase = true)) {
                    // 展开所有父节点
                    matchingNodeIds.addAll(parentIds)
                }

                // 递归检查子节点
                findMatchingNodes(getChildren(node), currentPath)
            }
        }

        findMatchingNodes(items)

        // 更新展开状态
        if (matchingNodeIds.isNotEmpty()) {
            expandedIds = expandedIds + matchingNodeIds
        }
    }

    fun toggleSearchBar() {
        showSearchBar = !showSearchBar
        if (!showSearchBar) {
            searchQuery = ""
        }
    }

    fun updateShowSearchBar(show: Boolean) {
        showSearchBar = show
        if (!show) {
            searchQuery = ""
        }
    }





    /**
     * 🛠️ 辅助方法
     */
    private fun getAllNodeIds(items: List<T>): Set<Any> {
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

    private fun getAllNodes(node: T): List<T> {
        val result = mutableListOf<T>()
        result.add(node)
        getChildren(node).forEach { child ->
            result.addAll(getAllNodes(child))
        }
        return result
    }

    fun isExpanded(nodeId: Any): Boolean {
        return nodeId in expandedIds
    }

    fun isSelected(nodeId: Any): Boolean {
        return selectedNodeId == nodeId
    }
}

/**
 * 🎯 创建和记住 TreeViewModel
 */
@Composable
fun <T> rememberTreeViewModel(): TreeViewModel<T> {
    return remember { TreeViewModel<T>() }
}

/**
 * 🔍 递归过滤树节点
 */
private fun <T> filterTreeItems(
    items: List<T>,
    query: String,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>
): List<T> {
    if (query.isBlank()) return items

    val lowerQuery = query.trim().lowercase()

    return items.mapNotNull { item ->
        val labelMatches = getLabel(item).lowercase().contains(lowerQuery)
        val children = getChildren(item)
        val filteredChildren = filterTreeItems(children, query, getLabel, getChildren)

        when {
            labelMatches -> item // 节点本身匹配，保留整个节点
            filteredChildren.isNotEmpty() -> item // 子节点有匹配，保留节点
            else -> null // 节点和子节点都不匹配，过滤掉
        }
    }
}
