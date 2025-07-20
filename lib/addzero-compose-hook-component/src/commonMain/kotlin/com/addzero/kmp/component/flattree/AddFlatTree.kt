package com.addzero.kmp.component.flattree

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.addzero.kmp.component.tree.AddTree
import com.addzero.kmp.component.tree.NodeType
import com.addzero.kmp.component.tree.rememberTreeViewModel
import com.addzero.kmp.util.data_structure.tree.List2TreeUtil

/**
 * 转换后的树节点数据结构
 * @param id 节点ID
 * @param name 节点名称
 * @param nodeType 节点类型
 * @param data 原始数据
 * @param parentId 父节点ID
 * @param children 子节点列表
 */
data class TreeNode<T>(
    val id: Any,
    val name: String,
    val nodeType: String = NodeType.DEFAULT.toString(),
    val data: T,
    val parentId: Any? = null,
    var children: List<TreeNode<T>> = emptyList()
)

/**
 * 扁平树组件 - 将任意扁平数据结构转为树结构后渲染
 * @param items 扁平结构的数据列表
 * @param getId 获取ID的函数
 * @param getParentId 获取父ID的函数
 * @param getName 获取名称的函数
 * @param getNodeType 获取节点类型的函数
 * @param modifier 修饰符
 * @param initiallyExpandedIds 初始展开的节点ID集合
 * @param onNodeClick 节点点击回调
 * @param nodeRender 自定义节点渲染函数
 */
@Composable
fun <T> AddFlatTree(
    items: List<T>,
    getId: (T) -> Any,
    getParentId: (T) -> Any?,
    isRoot: (T) -> Boolean = { getParentId(it) == null },
    getName: (T) -> String,
    getNodeType: (T) -> String = { NodeType.DEFAULT.toString() },
    modifier: Modifier = Modifier,
    initiallyExpandedIds: Set<Any> = emptySet(),
    onNodeClick: (T) -> Unit = {},
    // nodeRender 已移除，使用 AddTree 内置渲染
) {
    // 将扁平结构转换为树形结构
    // 先将原始数据转换为TreeNode
    val treeNodes = items.map { item ->
        TreeNode(
            id = getId(item),
            name = getName(item),
            nodeType = getNodeType(item),
            data = item,
            parentId = getParentId(item)
        )
    }
    val treeData =     // 使用TreeUtil构建树结构
        List2TreeUtil.list2Tree(
            source = treeNodes,
            isRoot = {
                isRoot(it.data)
            },
            idFun = { it.id },
            pidFun = { it.parentId },
            getChildFun = { it.children },
            setChildFun = { self, children -> self.children = children }
        )
//    TreeUtil.buildTree(
//        list = treeNodes,
//        getId = { it.id.toString() },
//        getParentId = { it.parentId.toString() },
//        setChildren = { children -> this.children = children }
//    )

    // 使用AddTree组件渲染树结构（使用新的 TreeViewModel API）
    val viewModel = rememberTreeViewModel<TreeNode<T>>()

    // 配置 ViewModel
    LaunchedEffect(treeData) {
        viewModel.configure(
            getId = { it.id },
            getLabel = { it.name },
            getChildren = { it.children },
            getNodeType = { it.nodeType }
        )
        viewModel.onNodeClick = { treeNode ->
            // 直接传递原始数据进行回调
            onNodeClick(treeNode.data)
        }
        viewModel.setItems(treeData, initiallyExpandedIds)
    }

    AddTree(
        viewModel = viewModel,
        modifier = modifier
    )
}

