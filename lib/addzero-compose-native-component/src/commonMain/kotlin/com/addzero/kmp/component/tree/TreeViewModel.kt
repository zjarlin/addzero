package com.addzero.kmp.component.tree

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.addzero.kmp.component.tree.selection.CascadingSelectionStrategy
import com.addzero.kmp.component.tree.selection.CompleteSelectionResult
import com.addzero.kmp.component.tree.selection.SelectionState
import com.addzero.kmp.component.tree.selection.TreeSelectionManager
import com.addzero.kmp.util.data_structure.tree.TreeSearch

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

    // 🎯 多选配置
    var autoEnableMultiSelect by mutableStateOf(false)
    var multiSelectClickToToggle by mutableStateOf(false)

    // 🎯 选择管理器 - 使用设计模式管理复杂的选择逻辑
    private val selectionManager = TreeSelectionManager<T>(CascadingSelectionStrategy())

    // 📋 选中的项目 - 通过选择管理器获取
    val selectedItems: State<Set<Any>> = selectionManager.selectedLeafNodes

    // 🎯 完整的选中项目（包含推导的父节点）
    val completeSelectedItems: State<Set<Any>> = selectionManager.completeSelectedNodes

    // 🎯 间接选中的父节点
    val indirectSelectedItems: State<Set<Any>> = selectionManager.indirectSelectedNodes

    // 🔍 搜索状态
    var searchQuery by mutableStateOf("")

    var showSearchBar by mutableStateOf(false)

    // 🚀 性能优化：缓存机制
    private val iconCache = mutableMapOf<Any, ImageVector?>()
    private val childrenCache = mutableMapOf<Any, List<T>>()
    private val labelCache = mutableMapOf<Any, String>()

    // 📋 过滤后的数据 - 使用 TreeSearch 实现正确的树搜索
    val filteredItems by derivedStateOf {
        if (searchQuery.isBlank()) {
            items
        } else {
            // 🚀 使用 TreeSearch 的正确算法
            val mutableItems = items.toMutableList()
            TreeSearch.preserveParentNode(
                trees = mutableItems,
                getChildrenFun = { getChildren(it) },
                getKeyFun = { getLabel(it) },
                key = searchQuery
            )
            mutableItems
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

    /**
     * 🎯 配置多选行为
     */
    fun configureMultiSelect(
        autoEnable: Boolean = false,
        clickToToggle: Boolean = false
    ) {
        autoEnableMultiSelect = autoEnable
        multiSelectClickToToggle = clickToToggle

        // 如果设置了自动开启多选，立即开启
        if (autoEnable) {
            multiSelectMode = true
        }
    }

    // 🎭 事件回调
    var onNodeClick: (T) -> Unit = {}
    var onNodeContextMenu: (T) -> Unit = {}
    var onSelectionChange: (List<T>) -> Unit = {}
    var onCompleteSelectionChange: (CompleteSelectionResult) -> Unit = {}

    /**
     * 🚀 初始化树数据
     */
    fun setItems(
        newItems: List<T>,
        initiallyExpandedIds: Set<Any> = emptySet()
    ) {
        items = newItems
        expandedIds = initiallyExpandedIds

        // 🚀 清空所有缓存，因为数据已更新
        clearAllCaches()

        // 🎯 初始化选择管理器
        if (isConfigured) {
            selectionManager.initialize(
                items = newItems,
                getId = getId,
                getChildren = getChildren,
                onSelectionChanged = { selectedNodes ->
                    onSelectionChange(selectedNodes)
                },
                onCompleteSelectionChanged = { completeResult ->
                    // 🎯 处理完整选择结果（包含推导的父节点）
//                    println("🎯 TreeViewModel 完整选择结果:")
//                    println("   直接选中: ${completeResult.directSelectedNodes}")
//                    println("   间接选中: ${completeResult.indirectSelectedNodes}")
//                    println("   完整选中: ${completeResult.completeSelectedNodes}")

                    // 可以在这里添加额外的处理逻辑
                    onCompleteSelectionChange(completeResult)
                }
            )
        }
    }

    /**
     * 🚀 性能优化的图标获取方法 - 使用缓存避免重复计算
     */
    @Composable
    fun getIconCached(node: T): ImageVector? {
        val nodeId = getId(node)

        // 先检查缓存
        iconCache[nodeId]?.let { return it }

        // 缓存未命中，计算图标
        val icon = getIcon(node) ?: run {
            // 如果没有配置图标，使用 NodeType 进行推测
            val label = getLabel(node)
            val children = getChildren(node)
            com.addzero.kmp.component.tree.NodeType.guessIcon(label, children.isNotEmpty())
        }

        // 存入缓存
        iconCache[nodeId] = icon
        return icon
    }

    /**
     * 🚀 性能优化的标签获取方法 - 使用缓存
     */
    fun getLabelCached(node: T): String {
        val nodeId = getId(node)
        return labelCache.getOrPut(nodeId) { getLabel(node) }
    }

    /**
     * 🚀 性能优化的子节点获取方法 - 使用缓存
     */
    fun getChildrenCached(node: T): List<T> {
        val nodeId = getId(node)
        return childrenCache.getOrPut(nodeId) { getChildren(node) }
    }

    /**
     * 🚀 清空所有缓存
     */
    private fun clearAllCaches() {
        iconCache.clear()
        childrenCache.clear()
        labelCache.clear()
    }

    /**
     * 📊 性能监控：获取缓存统计信息
     */
    fun getCacheStats(): String {
        return "TreeViewModel 缓存统计: " +
                "图标缓存=${iconCache.size}, " +
                "标签缓存=${labelCache.size}, " +
                "子节点缓存=${childrenCache.size}"
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

        // 🎯 多选模式下的特殊处理
        if (multiSelectMode && multiSelectClickToToggle) {
            // 多选模式下点击节点直接切换选中状态
            toggleItemSelection(nodeId)
            return
        }

        // 🎯 原来的单选行为：
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
            selectionManager.clearAllSelections()
        }
    }

    fun toggleItemSelection(nodeId: Any) {
        if (!multiSelectMode) return

        // 🎯 使用选择管理器处理复杂的选择逻辑
        selectionManager.handleNodeClick(nodeId)
    }

    /**
     * 🎯 高级选择操作 - 使用选择管理器
     */
    fun getNodeSelectionState(nodeId: Any): SelectionState {
        return selectionManager.getNodeState(nodeId)
    }

    fun isNodeIndeterminate(nodeId: Any): Boolean {
        return selectionManager.isNodeIndeterminate(nodeId)
    }

    fun clearAllSelections() {
        selectionManager.clearAllSelections()
    }

    fun selectAllNodes() {
        selectionManager.selectAll()
    }

    fun isItemSelected(nodeId: Any): Boolean {
        return selectionManager.isNodeSelected(nodeId)
    }

    /**
     * 🎯 获取完整的选择结果（包含推导的父节点）
     */
    fun getCompleteSelectionResult(): CompleteSelectionResult {
        return selectionManager.getCompleteSelectionResult()
    }

    /**
     * 🎯 获取完整的选中节点ID（包含推导的父节点）
     */
    fun getCompleteSelectedNodeIds(): Set<Any> {
        return selectionManager.getCompleteSelectedNodeIds()
    }

    /**
     * 🎯 获取间接选中的父节点ID
     */
    fun getIndirectSelectedNodeIds(): Set<Any> {
        return selectionManager.getIndirectSelectedNodeIds()
    }

    private fun notifySelectionChange() {
        // 使用选择管理器获取选中的节点
        val selectedNodes = selectionManager.getSelectedNodes()
        onSelectionChange(selectedNodes)
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


