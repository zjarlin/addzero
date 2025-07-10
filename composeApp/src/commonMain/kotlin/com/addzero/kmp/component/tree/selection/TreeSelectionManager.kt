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
    
    // 选择变化回调
    private var onSelectionChanged: ((List<T>) -> Unit)? = null
    
    /**
     * 🔧 初始化树结构
     */
    fun initialize(
        items: List<T>,
        getId: (T) -> Any,
        getChildren: (T) -> List<T>,
        onSelectionChanged: ((List<T>) -> Unit)? = null
    ) {
        this.onSelectionChanged = onSelectionChanged
        
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
        
        // 触发回调
        notifySelectionChanged()
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
     * 🔍 获取选中的节点ID
     */
    fun getSelectedNodeIds(): Set<Any> {
        return _selectedLeafNodes.value
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
