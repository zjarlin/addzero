package com.addzero.kmp.component.tree.selection

import androidx.compose.runtime.*

/**
 * 🎯 树选择管理器
 *
 * 统一管理树形结构的选择状态，支持多种选择策略
 */
class TreeSelectionManager<T>(
    private val strategy: TreeSelectionStrategy = CascadingSelectionStrategy()
) {

    // 节点层次结构
    private val hierarchy = TreeNodeHierarchy<T>()

    // 当前选择状态
    private val _selections = mutableStateMapOf<Any, TreeNodeSelection>()
    val selections: Map<Any, TreeNodeSelection> = _selections

    // 选中的叶子节点
    private val _selectedLeafNodes = mutableStateOf<Set<Any>>(emptySet())
    val selectedLeafNodes: State<Set<Any>> = _selectedLeafNodes

    // 🎯 完整的选中节点（包含推导的父节点）
    private val _completeSelectedNodes = mutableStateOf<Set<Any>>(emptySet())
    val completeSelectedNodes: State<Set<Any>> = _completeSelectedNodes

    // 🎯 间接选中的父节点
    private val _indirectSelectedNodes = mutableStateOf<Set<Any>>(emptySet())
    val indirectSelectedNodes: State<Set<Any>> = _indirectSelectedNodes

    // 选择变化回调
    private var onSelectionChanged: ((List<T>) -> Unit)? = null
    private var onCompleteSelectionChanged: ((CompleteSelectionResult) -> Unit)? = null

    /**
     * 🔧 初始化树结构
     */
    fun initialize(
        items: List<T>,
        getId: (T) -> Any,
        getChildren: (T) -> List<T>,
        onSelectionChanged: ((List<T>) -> Unit)? = null,
        onCompleteSelectionChanged: ((CompleteSelectionResult) -> Unit)? = null
    ) {
        this.onSelectionChanged = onSelectionChanged
        this.onCompleteSelectionChanged = onCompleteSelectionChanged

        // 构建层次结构
        hierarchy.buildHierarchy(items, getId, getChildren)

        // 初始化选择状态
        initializeSelections(items, getId, getChildren)
    }

    /**
     * 🔧 初始化选择状态
     */
    private fun initializeSelections(
        items: List<T>,
        getId: (T) -> Any,
        getChildren: (T) -> List<T>
    ) {
        _selections.clear()

        fun initializeNode(node: T, parentId: Any?) {
            val nodeId = getId(node)
            val children = getChildren(node)
            val childrenIds = children.map { getId(it) }.toSet()

            _selections[nodeId] = TreeNodeSelection(
                nodeId = nodeId,
                state = SelectionState.UNSELECTED,
                isLeaf = children.isEmpty(),
                parentId = parentId,
                childrenIds = childrenIds
            )

            // 递归初始化子节点
            children.forEach { child ->
                initializeNode(child, nodeId)
            }
        }

        items.forEach { item ->
            initializeNode(item, null)
        }

        _selectedLeafNodes.value = emptySet()
    }

    /**
     * 🖱️ 处理节点点击
     */
    fun handleNodeClick(nodeId: Any) {
        val event = SelectionEvent.NodeClicked(nodeId)
        processSelectionEvent(event)
    }

    /**
     * 🔄 切换节点选择状态
     */
    fun toggleNodeSelection(nodeId: Any, newState: SelectionState) {
        val event = SelectionEvent.NodeToggled(nodeId, newState)
        processSelectionEvent(event)
    }

    /**
     * 🧹 清除所有选择
     */
    fun clearAllSelections() {
        val event = SelectionEvent.ClearAll
        processSelectionEvent(event)
    }

    /**
     * ✅ 全选
     */
    fun selectAll() {
        val rootIds = hierarchy.getRootNodes()
        val event = SelectionEvent.SelectAll(rootIds)
        processSelectionEvent(event)
    }

    /**
     * 🔄 处理选择事件
     */
    private fun processSelectionEvent(event: SelectionEvent) {
        val result = strategy.handleSelection(event, _selections, hierarchy)

        // 更新选择状态
        result.updatedNodes.forEach { (nodeId, state) ->
            _selections[nodeId] = _selections[nodeId]?.copy(state = state)
                ?: createDefaultSelection(nodeId, state)
        }

        // 更新选中的叶子节点
        _selectedLeafNodes.value = result.selectedLeafNodes

        // 🎯 计算完整的选择结果（包含推导的父节点）
        updateCompleteSelection()

        // 触发回调
        notifySelectionChanged()
        notifyCompleteSelectionChanged()
    }

    /**
     * 🔧 创建默认选择状态
     */
    private fun createDefaultSelection(nodeId: Any, state: SelectionState): TreeNodeSelection {
        return TreeNodeSelection(
            nodeId = nodeId,
            state = state,
            isLeaf = hierarchy.isLeaf(nodeId),
            parentId = hierarchy.getParent(nodeId),
            childrenIds = hierarchy.getChildren(nodeId)
        )
    }

    /**
     * 🎯 更新完整的选择结果
     */
    private fun updateCompleteSelection() {
        val directSelected = _selectedLeafNodes.value
        val indirectSelected = mutableSetOf<Any>()

        // 为每个直接选中的节点推导父节点
        directSelected.forEach { nodeId ->
            val ancestors = hierarchy.getAncestors(nodeId)
            indirectSelected.addAll(ancestors)
        }

        val completeSelected = directSelected + indirectSelected

        // 更新状态
        _indirectSelectedNodes.value = indirectSelected
        _completeSelectedNodes.value = completeSelected

//        println("🎯 完整选择结果:")
//        println("   直接选中: $directSelected")
//        println("   间接选中: $indirectSelected")
//        println("   完整选中: $completeSelected")
    }

    /**
     * 📢 通知选择变化
     */
    private fun notifySelectionChanged() {
        onSelectionChanged?.let { callback ->
            val selectedNodes = _selectedLeafNodes.value.mapNotNull { nodeId ->
                hierarchy.getNodeData(nodeId)
            }
            callback(selectedNodes)
        }
    }

    /**
     * 📢 通知完整选择变化
     */
    private fun notifyCompleteSelectionChanged() {
        onCompleteSelectionChanged?.let { callback ->
            val directSelected = _selectedLeafNodes.value
            val indirectSelected = _indirectSelectedNodes.value
            val completeSelected = _completeSelectedNodes.value

            val selectedNodeData = completeSelected.mapNotNull { nodeId ->
                hierarchy.getNodeData(nodeId)
            }

            val result = CompleteSelectionResult(
                directSelectedNodes = directSelected,
                indirectSelectedNodes = indirectSelected,
                completeSelectedNodes = completeSelected,
                selectedNodeData = selectedNodeData
            )

            callback(result)
        }
    }

    /**
     * 🔍 获取节点选择状态
     */
    fun getNodeState(nodeId: Any): SelectionState {
        return _selections[nodeId]?.state ?: SelectionState.UNSELECTED
    }

    /**
     * 🔍 判断节点是否选中
     */
    fun isNodeSelected(nodeId: Any): Boolean {
        return getNodeState(nodeId) == SelectionState.SELECTED
    }

    /**
     * 🔍 判断节点是否半选
     */
    fun isNodeIndeterminate(nodeId: Any): Boolean {
        return getNodeState(nodeId) == SelectionState.INDETERMINATE
    }

    /**
     * 🔍 获取选中的节点数据
     */
    fun getSelectedNodes(): List<T> {
        return _selectedLeafNodes.value.mapNotNull { nodeId ->
            hierarchy.getNodeData(nodeId)
        }
    }

    /**
     * 🔍 获取选中的节点ID（仅叶子节点）
     */
    fun getSelectedNodeIds(): Set<Any> {
        return _selectedLeafNodes.value
    }

    /**
     * 🎯 获取完整的选中节点ID（包含推导的父节点）
     */
    fun getCompleteSelectedNodeIds(): Set<Any> {
        return _completeSelectedNodes.value
    }

    /**
     * 🎯 获取间接选中的父节点ID
     */
    fun getIndirectSelectedNodeIds(): Set<Any> {
        return _indirectSelectedNodes.value
    }

    /**
     * 🎯 获取完整的选择结果
     */
    fun getCompleteSelectionResult(): CompleteSelectionResult {
        val directSelected = _selectedLeafNodes.value
        val indirectSelected = _indirectSelectedNodes.value
        val completeSelected = _completeSelectedNodes.value

        val selectedNodeData = completeSelected.mapNotNull { nodeId ->
            hierarchy.getNodeData(nodeId)
        }

        return CompleteSelectionResult(
            directSelectedNodes = directSelected,
            indirectSelectedNodes = indirectSelected,
            completeSelectedNodes = completeSelected,
            selectedNodeData = selectedNodeData
        )
    }
}

/**
 * 🎯 记住树选择管理器的 Composable 函数
 */
@Composable
fun <T> rememberTreeSelectionManager(
    strategy: TreeSelectionStrategy = CascadingSelectionStrategy()
): TreeSelectionManager<T> {
    return remember { TreeSelectionManager<T>(strategy) }
}
