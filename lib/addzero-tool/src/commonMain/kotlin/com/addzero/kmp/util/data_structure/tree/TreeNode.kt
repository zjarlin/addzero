package com.addzero.kmp.util.data_structure.tree

/**
 * 树节点接口
 * 任何需要转为树状结构的类都应该实现此接口
 */
interface TreeNode<T> {
    /**
     * 节点ID
     */
    val id: Any

    val item: T

    /**
     * 父节点ID
     * 如果没有父节点，返回null
     */
    val parentId: Any?

    /**
     * 子节点列表
     */
    val children: MutableList<T>

    /**
     * 是否为叶子节点
     */
    val leafFlag: Boolean
        get() = children.isEmpty()

}

