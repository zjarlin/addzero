package com.addzero.kmp.util.data_structure.tree


/**
 * @author addzero
 * @since 2022/10/11 11:05 PM
 */
object TreeSearch {
    /**
     * 获取所有叶子节点
     *
     * @param currentNode 当前节点
     * @param allNodes 所有节点列表
     * @param getIdFun 获取节点ID的函数
     * @param getPidFun 获取父节点ID的函数
     * @return 所有叶子节点列表
     */
    fun <T> getAllLeafNodes(
        currentNode: T,
        allNodes: List<T>,
        getIdFun: (T) -> String,
        getPidFun: (T) -> String?
    ): List<T> {
        val leafNodes = mutableListOf<T>()
        leafNodes.add(currentNode)

        // 递归查找所有子节点
        allNodes.forEach { childNode ->
            if (getPidFun(childNode) == getIdFun(currentNode)) {
                leafNodes.addAll(
                    getAllLeafNodes(
                        childNode,
                        allNodes,
                        getIdFun,
                        getPidFun
                    )
                )
            }
        }
        return leafNodes
    }

    /**
     * 保留包含指定关键字的父节点
     *
     * @param trees 树形结构列表
     * @param getChildrenFun 获取子节点列表的函数
     * @param getKeyFun 获取关键字内容的函数
     * @param key 搜索关键字
     */
    fun <T> preserveParentNode(
        trees: MutableList<T>,
        getChildrenFun: (T) -> List<T>?,
        getKeyFun: (T) -> String,
        key: String
    ) {
        if (key.isBlank()) return

        trees.removeAll { node ->
            !sonIsContainsStr(node, getChildrenFun, getKeyFun, key)
        }
    }

    /**
     * 保留满足条件的父节点
     *
     * @param trees 树形结构列表
     * @param getChildrenFun 获取子节点列表的函数
     * @param predicate 节点判断条件
     */
    fun <T> preserveParentNode(
        trees: MutableList<T>,
        getChildrenFun: (T) -> List<T>?,
        predicate: (T) -> Boolean
    ) {
        trees.removeAll { node ->
            !sonIsContainsStr(node, getChildrenFun, predicate)
        }
    }

    /**
     * 判断节点或其子节点是否包含关键字
     *
     * @param node 当前节点
     * @param getChildrenFun 获取子节点列表的函数
     * @param getKeyFun 获取关键字内容的函数
     * @param key 搜索关键字
     * @return 是否包含关键字
     */
    fun <T> sonIsContainsStr(
        node: T,
        getChildrenFun: (T) -> List<T>?,
        getKeyFun: (T) -> String,
        key: String
    ): Boolean {
        val predicate = { e: T -> getKeyFun(e).contains(key, ignoreCase = true) }
        return sonIsContainsStr(node, getChildrenFun, predicate)
    }

    /**
     * 判断节点或其子节点是否满足条件
     *
     * @param node 当前节点
     * @param getChildrenFun 获取子节点列表的函数
     * @param predicate 节点判断条件
     * @return 是否满足条件
     */
    fun <T> sonIsContainsStr(
        node: T,
        getChildrenFun: (T) -> List<T>?,
        predicate: (T) -> Boolean
    ): Boolean {
        if (node == null) return false

        val contains = predicate(node)
        val children = getChildrenFun(node)?.toMutableList() ?: return contains

        // 从后向前遍历子节点
        for (i in children.indices.reversed()) {
            val childNode = children[i]
            if (sonIsContainsStr(childNode, getChildrenFun, predicate)) {
                return true
            } else {
                children.removeAt(i)
            }
        }

        return contains
    }

    /**
     * 保留包含指定关键字的父子节点
     *
     * @param trees 树形结构列表
     * @param getChildrenFun 获取子节点列表的函数
     * @param getKeyFun 获取关键字内容的函数
     * @param key 搜索关键字
     */
    fun <T> preserveParentAndChildNode(
        trees: MutableList<T>,
        getChildrenFun: (T) -> List<T>?,
        getKeyFun: (T) -> String,
        key: String
    ) {
        if (key.isBlank()) return

        trees.removeAll { node ->
            !sonAndFatherIsContains(node, getChildrenFun, getKeyFun, key)
        }
    }

    /**
     * 保留满足条件的父子节点
     *
     * @param trees 树形结构列表
     * @param getChildrenFun 获取子节点列表的函数
     * @param predicate 节点判断条件
     */
    fun <T> preserveParentAndChildNode(
        trees: MutableList<T>,
        getChildrenFun: (T) -> List<T>?,
        predicate: (T) -> Boolean
    ) {
        trees.removeAll { node ->
            !sonAndFatherIsContains(node, getChildrenFun, predicate)
        }
    }

    /**
     * 判断节点或其子节点是否包含关键字（保留父子节点）
     *
     * @param node 当前节点
     * @param getChildrenFun 获取子节点列表的函数
     * @param getKeyFun 获取关键字内容的函数
     * @param key 搜索关键字
     * @return 是否包含关键字
     */
    fun <T> sonAndFatherIsContains(
        node: T,
        getChildrenFun: (T) -> List<T>?,
        getKeyFun: (T) -> String,
        key: String
    ): Boolean {
        val predicate = { e: T -> getKeyFun(e).contains(key, ignoreCase = true) }
        return sonAndFatherIsContains(node, getChildrenFun, predicate)
    }

    /**
     * 判断节点或其子节点是否满足条件（保留父子节点）
     *
     * @param node 当前节点
     * @param getChildrenFun 获取子节点列表的函数
     * @param predicate 节点判断条件
     * @return 是否满足条件
     */
    fun <T> sonAndFatherIsContains(
        node: T,
        getChildrenFun: (T) -> List<T>?,
        predicate: (T) -> Boolean
    ): Boolean {
        if (node == null) return false

        var curFlag = predicate(node)
        val children = getChildrenFun(node)?.toMutableList() ?: return curFlag

        // 从后向前遍历子节点
        for (i in children.indices.reversed()) {
            val childNode = children[i]
            val childFlag = sonAndFatherIsContains(childNode, getChildrenFun, predicate)
            if (childFlag || (children.all { !predicate(it) } && curFlag)) {
                curFlag = true
            } else {
                children.removeAt(i)
            }
        }

        return curFlag
    }

    /**
     * 树节点比较器（根节点在前）
     */
    private class NodeComparator<T>(
        private val getPidFun: (T) -> String?,
        private val isRoot: (T) -> Boolean = { t -> getPidFun(t) == null }
    ) : Comparator<T> {
        override fun compare(node1: T, node2: T): Int {
            return when {
                isRoot(node1) && getPidFun(node2) != null -> -1
                getPidFun(node1) != null && isRoot(node2) -> 1
                else -> 0
            }
        }
    }
}
