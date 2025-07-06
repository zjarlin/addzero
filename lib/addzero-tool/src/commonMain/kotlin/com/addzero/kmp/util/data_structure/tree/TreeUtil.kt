package com.addzero.kmp.util.data_structure.tree//package com.addzero.kmp.util.data_structure.tree
//
//import com.addzero.kmp.util.data_structure.predicate.treeClient
//import kotlin.reflect.KProperty1
//
///**
// * 树状结构工具类
// */
//object TreeUtil {
//    /**
//     * 获取面包屑路径
//     * @param list 所有节点列表
//     * @param targetId 目标节点ID
//     * @param getId 获取节点ID的函数
//     * @param getParentId 获取父节点ID的函数
//     * @param getLabel 获取节点显示标签的函数
//     */
//    fun <T> getBreadcrumb(
//        list: List<T>,
//        targetId: Any,
//        getId: (T) -> Any,
//        getParentId: (T) -> Any?,
//        getLabel: (T) -> String
//    ): List<String> {
//        return findNodePath(list, targetId, getId, getParentId).map { getLabel(it) }
//    }
//
//
//
//
//    /**
//     * 获取面包屑路径
//     * @param list 所有节点列表
//     * @param targetId 目标节点ID
//     * @param getId 获取节点ID的函数
//     * @param getParentId 获取父节点ID的函数
//     * @param getLabel 获取节点显示标签的函数
//     */
//    fun <T : Any> getBreadcrumbList(
//        list: List<T>,
//        targetId: Any,
//        getId: KProperty1<T, Any>,
//        getParentId: KProperty1<T, Any?>,
//        getChildren: KProperty1<T, List<T>>,
//        setChildren: T.(List<T>) -> Unit
//    ): List<T> {
////        TreeUtil.traverseTree {  }
//        val treeClient = treeClient<T>(
//            dataList = list,
//            getId = getId,
//            getParentId = getParentId,
//            getChildren = getChildren,
//            setChildren = setChildren
//        )
//        val search = treeClient.search<T> {
//            getId eq targetId
//        }
//        val flattenTree = flattenTree(search, getChildren)
////        TreePredicateBuilder
//
//        return flattenTree
//    }
//
//
//
//    /**
//     * 构建树形结构
//     * @param list 扁平列表
//     * @param getId 获取节点ID的函数
//     * @param getParentId 获取父节点ID的函数
//     * @param isRoot 判断节点是否为根节点的函数，默认为parentId为null的节点
//     * @param setChildren 设置子节点列表的函数
//     * @param debug 是否输出调试信息
//     */
//    fun <T> buildTree(
//        list: List<T>,
//        getId: (T) -> Any,
//        getParentId: (T) -> Any?,
//        isRoot: (T) -> Boolean = { getParentId(it) == null },
//        setChildren: T.(List<T>) -> Unit,
//        debug: Boolean = false
//    ): List<T> {
//        if (list.isEmpty()) {
//            if (debug) println("TreeUtil.buildTree: 输入列表为空")
//            return emptyList()
//        }
//
//        val result = mutableListOf<T>()
//        val nodeMap = mutableMapOf<Any, T>()
//
//        // 第一遍遍历：构建节点映射
//        list.forEach { node ->
//            val nodeId = getId(node)
//            if (debug) println("TreeUtil.buildTree: 处理节点 ID=${nodeId}, parentId=${getParentId(node)}")
//            nodeMap[nodeId] = node
//        }
//
//        if (debug) println("TreeUtil.buildTree: 节点映射构建完成，共 ${nodeMap.size} 个节点")
//
//        // 识别根节点
//        val rootNodes = list.filter { isRoot(it) }
//        if (debug) println("TreeUtil.buildTree: 识别出 ${rootNodes.size} 个根节点")
//
//        if (rootNodes.isEmpty()) {
//            if (debug) {
//                println("TreeUtil.buildTree: 警告 - 未找到根节点！")
//                // 检查第一个节点的父ID是否存在
//                if (list.isNotEmpty()) {
//                    val firstNode = list.first()
//                    val parentId = getParentId(firstNode)
//                    println("TreeUtil.buildTree: 第一个节点信息 - ID=${getId(firstNode)}, parentId=$parentId")
//                    if (parentId != null) {
//                        println("TreeUtil.buildTree: 父节点${if (nodeMap.containsKey(parentId)) "存在" else "不存在"}于映射中")
//                    }
//                }
//                // 尝试查找可能的循环引用
//                val potentialCycles = list.filter { node ->
//                    val parentId = getParentId(node)
//                    parentId != null && getId(node) == parentId
//                }
//                if (potentialCycles.isNotEmpty()) {
//                    println("TreeUtil.buildTree: 警告 - 发现可能的循环引用，共 ${potentialCycles.size} 个")
//                }
//            }
//
//            // 如果没有根节点，选择第一个节点作为根节点
//            if (list.isNotEmpty()) {
//                result.add(list.first())
//                if (debug) println("TreeUtil.buildTree: 没有根节点，选择第一个节点作为根节点")
//            }
//        } else {
//            result.addAll(rootNodes)
//        }
//
//        // 构建父子关系
//        val childrenMap = mutableMapOf<Any, MutableList<T>>()
//
//        // 预先分组所有子节点
//        list.forEach { node ->
//            val parentId = getParentId(node)
//            if (parentId != null && !isRoot(node)) {
//                if (!childrenMap.containsKey(parentId)) {
//                    childrenMap[parentId] = mutableListOf()
//                }
//                childrenMap[parentId]?.add(node)
//            }
//        }
//
//        if (debug) println("TreeUtil.buildTree: 子节点分组完成，共 ${childrenMap.size} 个父节点有子节点")
//
//        // 分配子节点
//        nodeMap.forEach { (nodeId, node) ->
//            val children = childrenMap[nodeId] ?: mutableListOf()
//            if (children.isNotEmpty()) {
//                if (debug) println("TreeUtil.buildTree: 节点 ID=$nodeId 有 ${children.size} 个子节点")
//                node.setChildren(children)
//            } else {
//                node.setChildren(emptyList())
//            }
//        }
//
//        if (debug) println("TreeUtil.buildTree: 树构建完成，根节点数量: ${result.size}")
//        return result
//    }
//
//    /**
//     * 获取节点现有的子节点
//     * 辅助方法，用于buildTree
//     */
//    private fun <T> T.getExistingChildren(
//        getId: (T) -> Any,
//        getParentId: (T) -> Any?,
//        allNodes: List<T>
//    ): List<T> {
//        val nodeId = getId(this)
//        return allNodes.filter { getParentId(it) == nodeId }
//    }
//
//    /**
//     * 扁平化树结构
//     * @param nodes 树节点列表
//     * @param getChildren 获取子节点的函数
//     */
//    fun <T> flattenTree(
//        nodes: List<T>, getChildren: (T) -> List<T>
//    ): List<T> = nodes.flatMap { node ->
//        listOf(node) + flattenTree(getChildren(node), getChildren)
//    }
//
//    /**
//     * 查找节点及其所有子节点
//     * @param nodes 树节点列表
//     * @param targetId 目标节点ID
//     * @param getId 获取节点ID的函数
//     * @param getChildren 获取子节点的函数
//     */
//    fun <T> findNodeAndChildren(
//        nodes: List<T>, targetId: Any, getId: (T) -> Any, getChildren: (T) -> List<T>
//    ): List<T> = nodes.flatMap { node ->
//        when {
//            getId(node) == targetId -> listOf(node) + flattenTree(getChildren(node), getChildren)
//            else -> findNodeAndChildren(getChildren(node), targetId, getId, getChildren)
//        }
//    }.takeIf { it.isNotEmpty() } ?: emptyList()
//
//    /**
//     * 查找节点路径（从根到目标节点）
//     * @param list 所有节点列表
//     * @param targetId 目标节点ID
//     * @param getId 获取节点ID的函数
//     * @param getParentId 获取父节点ID的函数
//     */
//    fun <T> findNodePath(
//        list: List<T>, targetId: Any?, getId: (T) -> Any, getParentId: (T) -> Any?
//    ): List<T> {
//        val nodeMap = list.associateBy { getId(it) }
//        val result = mutableListOf<T>()
//
//        var currentId = targetId
//        while (currentId != null) {
//            val node = nodeMap[currentId] ?: break
//            result.add(0, node) // 插入到列表开头
//            currentId = getParentId(node)
//        }
//
//        return result
//    }
//
//    /**
//     * 计算节点层级
//     * @param list 所有节点列表
//     * @param nodeId 目标节点ID
//     * @param getId 获取节点ID的函数
//     * @param getParentId 获取父节点ID的函数
//     */
//    fun <T> calculateNodeLevel(
//        list: List<T>, nodeId: Any, getId: (T) -> Any, getParentId: (T) -> Any?
//    ): Int = findNodePath(list, nodeId, getId, getParentId).size - 1
//
//    /**
//     * 计算树的最大深度
//     * @param nodes 树节点列表
//     * @param getChildren 获取子节点的函数
//     */
//    fun <T> getTreeDepth(
//        nodes: List<T>, getChildren: (T) -> List<T>
//    ): Int = nodes.maxOfOrNull {
//        1 + getTreeDepth(getChildren(it), getChildren)
//    } ?: 0
//
//    /**
//     * 对树进行排序
//     * @param nodes 树节点列表
//     * @param comparator 节点比较器
//     * @param getChildren 获取子节点的函数
//     * @param setChildren 设置子节点的函数
//     */
//    fun <T> sortTree(
//        nodes: List<T>,
//        getChildren: (T) -> MutableList<T>,
//        setChildren: T.(List<T>) -> Unit,
//        comparator: Comparator<T>,
//    ): List<T> = nodes.sortedWith(comparator).map { node ->
//        val sortedChildren = sortTree(
//            getChildren(node), getChildren, setChildren, comparator
//        )
//        node.setChildren(sortedChildren.toMutableList())
//        node
//    }
//
//    /**
//     * 过滤树节点
//     * @param nodes 树节点列表
//     * @param predicate 过滤条件
//     * @param getChildren 获取子节点的函数
//     * @param setChildren 设置子节点的函数
//     */
//    fun <T> filterTree(
//        nodes: List<T>, predicate: (T) -> Boolean, getChildren: (T) -> MutableList<T>, setChildren: (T, MutableList<T>) -> Unit
//    ): List<T> = nodes.filter(predicate).map { node ->
//        val filteredChildren = filterTree(getChildren(node), predicate, getChildren, setChildren)
//        setChildren(node, filteredChildren.toMutableList())
//        node
//    }
//
//    /**
//     * 遍历树（深度优先）
//     * @param nodes 树节点列表
//     * @param getChildren 获取子节点的函数
//     * @param action 对每个节点执行的操作
//     */
//    fun <T> traverseTree(
//        nodes: List<T>, getChildren: (T) -> List<T>, action: (T) -> Unit
//    ) {
//        nodes.forEach { node ->
//            action(node)
//            traverseTree(getChildren(node), getChildren, action)
//        }
//    }
//}
