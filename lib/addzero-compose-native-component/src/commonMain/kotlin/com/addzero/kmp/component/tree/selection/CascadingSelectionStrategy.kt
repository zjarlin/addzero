package com.addzero.kmp.component.tree.selection

/**
 * 🎯 树选择策略接口
 */
interface TreeSelectionStrategy {
    fun handleSelection(
        event: SelectionEvent,
        currentSelections: Map<Any, TreeNodeSelection>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ): SelectionResult
}

/**
 * 🎯 标准级联选择策略
 *
 * 实现标准的树形选择逻辑：
 * - 选中父节点 → 所有子节点选中
 * - 取消父节点 → 所有子节点取消
 * - 子节点全选 → 父节点选中
 * - 子节点部分选中 → 父节点半选
 */
class CascadingSelectionStrategy : TreeSelectionStrategy {

    override fun handleSelection(
        event: SelectionEvent,
        currentSelections: Map<Any, TreeNodeSelection>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ): SelectionResult {
        return when (event) {
            is SelectionEvent.NodeClicked -> handleNodeClick(event.nodeId, currentSelections, nodeHierarchy)
            is SelectionEvent.NodeToggled -> handleNodeToggle(
                event.nodeId,
                event.newState,
                currentSelections,
                nodeHierarchy
            )

            is SelectionEvent.ClearAll -> handleClearAll(currentSelections)
            is SelectionEvent.SelectAll -> handleSelectAll(event.rootIds, currentSelections, nodeHierarchy)
        }
    }

    /**
     * 🖱️ 处理节点点击
     */
    private fun handleNodeClick(
        nodeId: Any,
        currentSelections: Map<Any, TreeNodeSelection>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ): SelectionResult {
        val currentState = currentSelections[nodeId]?.state ?: SelectionState.UNSELECTED
        val newState = when (currentState) {
            SelectionState.UNSELECTED -> SelectionState.SELECTED
            SelectionState.INDETERMINATE -> SelectionState.SELECTED
            SelectionState.SELECTED -> SelectionState.UNSELECTED
        }

        return handleNodeToggle(nodeId, newState, currentSelections, nodeHierarchy)
    }

    /**
     * 🔄 处理节点状态切换
     */
    private fun handleNodeToggle(
        nodeId: Any,
        newState: SelectionState,
        currentSelections: Map<Any, TreeNodeSelection>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ): SelectionResult {
        val updatedNodes = mutableMapOf<Any, SelectionState>()
        val affectedParents = mutableSetOf<Any>()

        // 1. 更新当前节点
        updatedNodes[nodeId] = newState

        // 2. 级联更新子节点
        if (newState != SelectionState.INDETERMINATE) {
            val childrenIds = nodeHierarchy.getChildren(nodeId)
            updateChildrenRecursively(childrenIds, newState, updatedNodes, nodeHierarchy)
        }

        // 3. 向上更新父节点状态
        val parentId = nodeHierarchy.getParent(nodeId)
        if (parentId != null) {
            updateParentsRecursively(parentId, updatedNodes, currentSelections, nodeHierarchy, affectedParents)
        }

        // 4. 计算选中的叶子节点
        val selectedLeafNodes = calculateSelectedLeafNodes(updatedNodes, currentSelections, nodeHierarchy)

        return SelectionResult(updatedNodes, selectedLeafNodes, affectedParents)
    }

    /**
     * 🌿 递归更新子节点
     */
    private fun updateChildrenRecursively(
        childrenIds: Set<Any>,
        state: SelectionState,
        updatedNodes: MutableMap<Any, SelectionState>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ) {
        childrenIds.forEach { childId ->
            updatedNodes[childId] = state
            val grandChildren = nodeHierarchy.getChildren(childId)
            if (grandChildren.isNotEmpty()) {
                updateChildrenRecursively(grandChildren, state, updatedNodes, nodeHierarchy)
            }
        }
    }

    /**
     * 🌳 递归更新父节点状态
     */
    private fun updateParentsRecursively(
        parentId: Any,
        updatedNodes: MutableMap<Any, SelectionState>,
        currentSelections: Map<Any, TreeNodeSelection>,
        nodeHierarchy: TreeNodeHierarchy<*>,
        affectedParents: MutableSet<Any>
    ) {
        val childrenIds = nodeHierarchy.getChildren(parentId)
        val childrenStates = childrenIds.map { childId ->
            updatedNodes[childId] ?: currentSelections[childId]?.state ?: SelectionState.UNSELECTED
        }

        val parentState = calculateParentState(childrenStates)
        updatedNodes[parentId] = parentState
        affectedParents.add(parentId)

        // 继续向上更新
        val grandParentId = nodeHierarchy.getParent(parentId)
        if (grandParentId != null) {
            updateParentsRecursively(grandParentId, updatedNodes, currentSelections, nodeHierarchy, affectedParents)
        }
    }

    /**
     * 🧮 计算父节点状态
     */
    private fun calculateParentState(childrenStates: List<SelectionState>): SelectionState {
        val selectedCount = childrenStates.count { it == SelectionState.SELECTED }
        val indeterminateCount = childrenStates.count { it == SelectionState.INDETERMINATE }
        val totalCount = childrenStates.size

        return when {
            selectedCount == totalCount -> SelectionState.SELECTED
            selectedCount == 0 && indeterminateCount == 0 -> SelectionState.UNSELECTED
            else -> SelectionState.INDETERMINATE
        }
    }

    /**
     * 🍃 计算选中的叶子节点
     */
    private fun calculateSelectedLeafNodes(
        updatedNodes: Map<Any, SelectionState>,
        currentSelections: Map<Any, TreeNodeSelection>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ): Set<Any> {
        val selectedLeafNodes = mutableSetOf<Any>()

        // 合并当前状态和更新状态
        val allNodes = (currentSelections.keys + updatedNodes.keys).distinct()

        allNodes.forEach { nodeId ->
            val state = updatedNodes[nodeId] ?: currentSelections[nodeId]?.state ?: SelectionState.UNSELECTED
            val isLeaf = nodeHierarchy.isLeaf(nodeId)

            if (state == SelectionState.SELECTED && isLeaf) {
                selectedLeafNodes.add(nodeId)
            }
        }

        return selectedLeafNodes
    }

    /**
     * 🧹 处理清除所有选择
     */
    private fun handleClearAll(currentSelections: Map<Any, TreeNodeSelection>): SelectionResult {
        val updatedNodes = currentSelections.keys.associateWith { SelectionState.UNSELECTED }
        return SelectionResult(updatedNodes, emptySet(), emptySet())
    }

    /**
     * ✅ 处理全选
     */
    private fun handleSelectAll(
        rootIds: Set<Any>,
        currentSelections: Map<Any, TreeNodeSelection>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ): SelectionResult {
        val updatedNodes = mutableMapOf<Any, SelectionState>()
        val selectedLeafNodes = mutableSetOf<Any>()

        rootIds.forEach { rootId ->
            selectNodeAndDescendants(rootId, updatedNodes, selectedLeafNodes, nodeHierarchy)
        }

        return SelectionResult(updatedNodes, selectedLeafNodes, emptySet())
    }

    /**
     * 🌳 选择节点及其所有后代
     */
    private fun selectNodeAndDescendants(
        nodeId: Any,
        updatedNodes: MutableMap<Any, SelectionState>,
        selectedLeafNodes: MutableSet<Any>,
        nodeHierarchy: TreeNodeHierarchy<*>
    ) {
        updatedNodes[nodeId] = SelectionState.SELECTED

        if (nodeHierarchy.isLeaf(nodeId)) {
            selectedLeafNodes.add(nodeId)
        } else {
            val children = nodeHierarchy.getChildren(nodeId)
            children.forEach { childId ->
                selectNodeAndDescendants(childId, updatedNodes, selectedLeafNodes, nodeHierarchy)
            }
        }
    }
}
