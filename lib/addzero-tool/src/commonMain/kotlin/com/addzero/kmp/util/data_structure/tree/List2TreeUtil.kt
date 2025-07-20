package com.addzero.kmp.util.data_structure.tree

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1

/**
 * 列表转树形结构工具类
 * 支持任意实体类型转换为树形结构
 */
object List2TreeUtil {
    /**
     * 将列表转换为树形结构
     *
     * @param source 源数据列表
     * @param idFun 获取节点ID的函数
     * @param pidFun 获取父节点ID的函数
     * @param getChildFun 获取子节点列表的函数
     * @param setChildFun 设置子节点列表的函数
     * @return 树形结构的根节点列表
     */
    fun <T> list2Tree(
        source: List<T>,
        idFun: (T) -> Any?,
        pidFun: (T) -> Any?,
        getChildFun: (T) -> List<T>?,
        setChildFun: (T, List<T>) -> Unit
    ): List<T> {
        return list2Tree(
            source = source,
            isRoot = { pidFun(it) == null },
            idFun = idFun,
            pidFun = pidFun,
            getChildFun = getChildFun,
            setChildFun = setChildFun
        )
    }

    /**
     * 将列表转换为树形结构
     *
     * @param source 源数据列表
     * @param isRoot 判断是否为根节点的函数
     * @param idFun 获取节点ID的函数
     * @param pidFun 获取父节点ID的函数
     * @param getChildFun 获取子节点列表的函数
     * @param setChildFun 设置子节点列表的函数
     * @return 树形结构的根节点列表
     */
    fun <T> list2Tree(
        source: List<T>,
        isRoot: (T) -> Boolean,
        idFun: (T) -> Any?,
        pidFun: (T) -> Any?,
        getChildFun: (T) -> List<T>?,
        setChildFun: (T, List<T>) -> Unit
    ): List<T> {
        if (source.isEmpty()) {
            return emptyList()
        }

        val result = mutableListOf<T>()
        val nodeMap = mutableMapOf<Any?, T>()

        // 第一遍遍历：构建节点映射，识别根节点
        source.forEach { node ->
            val nodeId = idFun(node)
            val parentId = pidFun(node)

            // 将节点添加到映射中
            nodeMap[nodeId] = node

            // 判断是否为根节点
            if (isRoot(node) || parentId == null) {
                result.add(node)
            }
        }

        // 第二遍遍历：构建父子关系
        source.forEach { node ->
            val parentId = pidFun(node)
            val parent = parentId?.let { nodeMap[it] }

            parent?.let { p ->
                val children = getChildFun(p)?.toMutableList() ?: mutableListOf()
                children.add(node)
                setChildFun(p, children)
            }
        }

        return result
    }

    /**
     * 将树形结构转换为列表
     *
     * @param treeData 树形结构数据
     * @param getChildFun 获取子节点列表的函数
     * @param setChildFun 设置子节点列表的函数
     * @return 扁平化的列表
     */
    fun <T> tree2List(
        treeData: List<T>,
        getChildFun: (T) -> List<T>?,
        setChildFun: (T, List<T>) -> Unit
    ): List<T> {
        val result = mutableListOf<T>()

        fun traverse(node: T) {
            result.add(node)
            val children = getChildFun(node)
            children?.forEach { child ->
                traverse(child)
            }
            setChildFun(node, emptyList())
        }

        treeData.forEach { node ->
            traverse(node)
        }

        return result
    }

    /**
     * 获取节点到根的路径（面包屑）
     * 返回从目标节点到根节点的路径列表
     *
     * @param list 所有节点列表
     * @param targetId 目标节点ID
     * @param getId 获取节点ID的属性
     * @param getParentId 获取父节点ID的属性
     * @return 从目标节点到根节点的路径列表
     */
    fun <T> getBreadcrumbList(
        list: List<T>,
        targetId: Any,
        getId: KProperty1<T, Any>,
        getParentId: KProperty1<T, Any?>
    ): List<T> {
        val nodeMap = list.associateBy { getId.get(it) }
        val result = mutableListOf<T>()

        var currentId: Any? = targetId
        while (currentId != null) {
            val node = nodeMap[currentId] ?: break
            result.add(0, node) // 插入到列表开头，保持从根到目标的顺序
            currentId = getParentId.get(node)
        }

        return result
    }

    /**
     * 获取节点到根的路径（面包屑），并构建树形结构
     * 返回从根节点到目标节点的树形路径
     *
     * @param list 所有节点列表
     * @param targetId 目标节点ID
     * @param getId 获取节点ID的属性
     * @param getParentId 获取父节点ID的属性
     * @param getChildren 获取子节点列表的属性
     * @param setChildren 设置子节点列表的方法
     * @return 从根节点到目标节点的树形路径
     */
    fun <T> getBreadcrumbList(
        list: List<T>,
        targetId: Any,
        getId: KProperty1<T, Any>,
        getParentId: KProperty1<T, Any?>,
        getChildren: KMutableProperty1<T, MutableList<T>>,
        setChildren: (T, MutableList<T>) -> Unit
    ): List<T> {
        // 首先获取从目标节点到根节点的完整路径
        val pathNodes = getBreadcrumbList(list, targetId, getId, getParentId)
        if (pathNodes.isEmpty()) return emptyList()

        // 从路径中获取根节点
        val rootNode = pathNodes.first()
        val result = mutableListOf<T>()
        result.add(rootNode)

        // 清空所有节点的子节点列表，准备重新构建
        pathNodes.forEach {
            setChildren(it, mutableListOf())
        }

        // 构建从根到目标的树形路径
        for (i in 0 until pathNodes.size - 1) {
            val parent = pathNodes[i]
            val child = pathNodes[i + 1]

            // 将子节点添加到父节点的子节点列表中
            val children = getChildren.get(parent)
            children.add(child)
        }

        return result
    }
}
