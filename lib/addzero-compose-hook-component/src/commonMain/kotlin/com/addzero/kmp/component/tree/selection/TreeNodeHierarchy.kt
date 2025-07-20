package com.addzero.kmp.component.tree.selection

/**
 * 🌳 树节点层次结构管理器
 *
 * 负责管理树形结构的父子关系，提供高效的层次查询
 */
class TreeNodeHierarchy<T> {

    // 父子关系映射
    private val parentToChildren = mutableMapOf<Any, MutableSet<Any>>()
    private val childToParent = mutableMapOf<Any, Any>()
    private val nodeToData = mutableMapOf<Any, T>()
    private val leafNodes = mutableSetOf<Any>()

    /**
     * 🔧 构建层次结构
     */
    fun buildHierarchy(
        items: List<T>,
        getId: (T) -> Any,
        getChildren: (T) -> List<T>
    ) {
        // 清空现有数据
        clear()

        // 递归构建层次结构
        items.forEach { item ->
            buildNodeHierarchy(item, null, getId, getChildren)
        }
    }

    /**
     * 🌿 递归构建单个节点的层次结构
     */
    private fun buildNodeHierarchy(
        node: T,
        parentId: Any?,
        getId: (T) -> Any,
        getChildren: (T) -> List<T>
    ) {
        val nodeId = getId(node)
        nodeToData[nodeId] = node

        // 设置父子关系
        if (parentId != null) {
            childToParent[nodeId] = parentId
            parentToChildren.getOrPut(parentId) { mutableSetOf() }.add(nodeId)
        }

        // 处理子节点
        val children = getChildren(node)
        if (children.isEmpty()) {
            // 叶子节点
            leafNodes.add(nodeId)
        } else {
            // 递归处理子节点
            children.forEach { child ->
                buildNodeHierarchy(child, nodeId, getId, getChildren)
            }
        }
    }

    /**
     * 🔍 获取节点的父节点ID
     */
    fun getParent(nodeId: Any): Any? = childToParent[nodeId]

    /**
     * 🔍 获取节点的子节点ID集合
     */
    fun getChildren(nodeId: Any): Set<Any> = parentToChildren[nodeId] ?: emptySet()

    /**
     * 🔍 判断是否为叶子节点
     */
    fun isLeaf(nodeId: Any): Boolean = leafNodes.contains(nodeId)

    /**
     * 🔍 获取节点数据
     */
    fun getNodeData(nodeId: Any): T? = nodeToData[nodeId]

    /**
     * 🔍 获取所有根节点ID
     */
    fun getRootNodes(): Set<Any> {
        return nodeToData.keys.filter { childToParent[it] == null }.toSet()
    }

    /**
     * 🔍 获取所有叶子节点ID
     */
    fun getAllLeafNodes(): Set<Any> = leafNodes.toSet()

    /**
     * 🔍 获取节点的所有祖先节点ID（从父节点到根节点）
     */
    fun getAncestors(nodeId: Any): List<Any> {
        val ancestors = mutableListOf<Any>()
        var currentParent = getParent(nodeId)

        while (currentParent != null) {
            ancestors.add(currentParent)
            currentParent = getParent(currentParent)
        }

        return ancestors
    }

    /**
     * 🔍 获取节点的所有后代节点ID（包括所有子孙节点）
     */
    fun getDescendants(nodeId: Any): Set<Any> {
        val descendants = mutableSetOf<Any>()
        collectDescendants(nodeId, descendants)
        return descendants
    }

    /**
     * 🌿 递归收集后代节点
     */
    private fun collectDescendants(nodeId: Any, descendants: MutableSet<Any>) {
        val children = getChildren(nodeId)
        children.forEach { childId ->
            descendants.add(childId)
            collectDescendants(childId, descendants)
        }
    }

    /**
     * 🧹 清空层次结构
     */
    fun clear() {
        parentToChildren.clear()
        childToParent.clear()
        nodeToData.clear()
        leafNodes.clear()
    }
}
