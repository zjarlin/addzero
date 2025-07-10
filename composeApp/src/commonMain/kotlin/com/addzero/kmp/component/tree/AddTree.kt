package com.addzero.kmp.component.tree

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.search_bar.AddSearchBar
import com.addzero.kmp.component.tree.NodeType

/**
 * 🚀 优化版树组件 - 使用 ViewModel 管理状态
 *
 * ⚠️ 注意：此组件仍使用旧的插槽设计，建议使用重构后的 AddTree
 *
 * 🎯 核心改进：
 * - 参数从18个减少到3个（减少83%）
 * - 使用 TreeViewModel 管理所有响应式状态
 * - 清晰的职责分离：UI渲染 vs 状态管理
 * - 更好的可测试性和可维护性
 *
 * 🔄 插槽设计问题：
 * - TopSlot/BottomSlot 应该在外部声明，不需要插槽
 * - 只有内部插槽（如 contextMenu）才是必要的
 *
 * @param viewModel 树的状态管理器
 * @param modifier UI修饰符
 * @param content 自定义内容插槽（建议重构为外部声明）
 */
@Composable
fun <T> AddTree(
    viewModel: TreeViewModel<T>,
    modifier: Modifier = Modifier,
    content: @Composable TreeScope<T>.() -> Unit = {}
) {
    // 🎯 创建树作用域
    val treeScope = remember(viewModel) { TreeScopeImpl(viewModel) }

    Column(modifier = modifier) {
        // 🎨 自定义内容插槽（应该在外部声明）
        treeScope.content()

        // 🌳 树形结构渲染 - 使用 Surface 而不是 Box
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column {
                // 使用过滤后的数据渲染
                val items = viewModel.filteredItems
                // 🚀 TODO: 对于大量数据，考虑使用 LazyColumn 和虚拟化
                items.forEach { item ->
                    TreeNodeRenderer(
                        node = item,
                        viewModel = viewModel,
                        level = 0
                    )
                }
            }
        }
    }
}

/**
 * 🎭 树作用域 - 提供插槽化扩展能力
 */
interface TreeScope<T> {
    val viewModel: TreeViewModel<T>

    @Composable
    fun TopSlot(content: @Composable () -> Unit)

    @Composable
    fun ControlsSlot(content: @Composable () -> Unit)

    @Composable
    fun BottomSlot(content: @Composable () -> Unit)

    @Composable
    fun SearchBar()

    @Composable
    fun ExpandCollapseControls()
}

/**
 * 🎭 树作用域实现
 */
private class TreeScopeImpl<T>(
    override val viewModel: TreeViewModel<T>
) : TreeScope<T> {

    @Composable
    override fun TopSlot(content: @Composable () -> Unit) {
        content()
    }

    @Composable
    override fun ControlsSlot(content: @Composable () -> Unit) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface
        ) {
            content()
        }
    }

    @Composable
    override fun BottomSlot(content: @Composable () -> Unit) {
        content()
    }

    @Composable
    override fun SearchBar() {
        if (viewModel.showSearchBar) {
            // 🚀 使用现有的 AddSearchBar 组件，功能更丰富
            AddSearchBar(
                keyword = viewModel.searchQuery,
                onKeyWordChanged = { viewModel.updateSearchQuery(it) },
                onSearch = {
                    // 🎯 执行搜索：自动展开包含匹配项的节点
                    viewModel.performSearch()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                placeholder = "搜索树节点..."
            )
        }
    }

    @Composable
    override fun ExpandCollapseControls() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = { viewModel.expandAll() }
            ) {
                Text("展开全部")
            }

            TextButton(
                onClick = { viewModel.collapseAll() }
            ) {
                Text("收起全部")
            }
        }
    }
}

/**
 * 🌳 树节点渲染器
 */
@Composable
private fun <T> TreeNodeRenderer(
    node: T,
    viewModel: TreeViewModel<T>,
    level: Int
) {
    val nodeId = viewModel.getId(node)
    val isExpanded = viewModel.isExpanded(nodeId)
    val isSelected = viewModel.isSelected(nodeId)
    val children = viewModel.getChildrenCached(node)
    val hasChildren = children.isNotEmpty()

    // 🎯 节点内容
    TreeNodeContent(
        node = node,
        viewModel = viewModel,
        level = level,
        isExpanded = isExpanded,
        isSelected = isSelected,
        hasChildren = hasChildren,
        onToggleExpanded = { viewModel.toggleExpanded(nodeId) },
        onClick = { viewModel.clickNode(node) }
    )

    // 🌿 子节点渲染
    if (hasChildren && isExpanded) {
        children.forEach { child ->
            TreeNodeRenderer(
                node = child,
                viewModel = viewModel,
                level = level + 1
            )
        }
    }
}

/**
 * 🎨 树节点内容渲染 - 恢复原来的菜单项行为
 */
@Composable
private fun <T> TreeNodeContent(
    node: T,
    viewModel: TreeViewModel<T>,
    level: Int,
    isExpanded: Boolean,
    isSelected: Boolean,
    hasChildren: Boolean,
    onToggleExpanded: () -> Unit,
    onClick: () -> Unit
) {
    val nodeId = viewModel.getId(node)
    val isItemSelected = viewModel.isItemSelected(nodeId)

    // 🎯 使用 Surface 而不是 Box，扁平化设计
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = (level * 16 + 6).dp,
                end = 6.dp,
                top = 2.dp,
                bottom = 2.dp
            )
            .clickable {
                // 🔄 原来的行为：点击整个菜单项控制展开/收起
                if (hasChildren) {
                    onToggleExpanded() // 有子节点：切换展开状态
                }
                onClick() // 总是触发点击回调
            },
        shape = RectangleShape, // 🎨 扁平化设计，不使用圆角
        tonalElevation = if (isSelected) 2.dp else 0.dp,
        color = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        } else {
            Color.Transparent
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 🔄 多选模式复选框 - 支持半选状态
            if (viewModel.multiSelectMode) {
                val selectionState = viewModel.getNodeSelectionState(nodeId)

                TriStateCheckbox(
                    state = when (selectionState) {
                        com.addzero.kmp.component.tree.selection.SelectionState.SELECTED -> ToggleableState.On
                        com.addzero.kmp.component.tree.selection.SelectionState.INDETERMINATE -> ToggleableState.Indeterminate
                        com.addzero.kmp.component.tree.selection.SelectionState.UNSELECTED -> ToggleableState.Off
                    },
                    onClick = { viewModel.toggleItemSelection(nodeId) }
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            // 🎨 节点图标 - 使用 ViewModel 的缓存图标方法
            val icon = viewModel.getIconCached(node)

            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Spacer(modifier = Modifier.width(12.dp))
            }

            // 📝 节点标签
            Text(
                text = viewModel.getLabelCached(node),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.weight(1f)
            )

            // 📂 展开/折叠箭头（只有子节点才显示）
            if (hasChildren) {
                Icon(
                    imageVector = if (isExpanded) {
                        Icons.Default.KeyboardArrowDown
                    } else {
                        Icons.AutoMirrored.Filled.KeyboardArrowRight
                    },
                    contentDescription = if (isExpanded) "折叠" else "展开",
                    modifier = Modifier.size(18.dp),
                    tint = if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    }
                )
            }
        }
    }
}

/**
 * 🎯 便捷构造函数 - 快速创建树组件
 *
 * ⚠️ 问题：函数名冲突，应该重命名避免混淆
 */
@Composable
fun <T> AddTree(
    items: List<T>,
    getId: (T) -> Any,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>,
    modifier: Modifier = Modifier,
    getNodeType: (T) -> String = { "" },
    getIcon: @Composable (T) -> ImageVector? = { node ->
        // 🚀 默认使用 NodeType 推测图标
        val label = getLabel(node)
        val children = getChildren(node)
        NodeType.guessIcon(label, children.isNotEmpty())
    },
    initiallyExpandedIds: Set<Any> = emptySet(),
    onNodeClick: (T) -> Unit = {},
    onNodeContextMenu: (T) -> Unit = {},
    onSelectionChange: (List<T>) -> Unit = {},
    content: @Composable TreeScope<T>.() -> Unit = {}
) {
    // 🎯 创建和配置 ViewModel
    val viewModel = rememberTreeViewModel<T>()

    // 🔧 优化：使用新的配置方法
    LaunchedEffect(items, getId, getLabel, getChildren) {
        viewModel.configure(
            getId = getId,
            getLabel = getLabel,
            getChildren = getChildren,
            getNodeType = getNodeType,
            getIcon = getIcon
        )
        viewModel.onNodeClick = onNodeClick
        viewModel.onNodeContextMenu = onNodeContextMenu
        viewModel.onSelectionChange = onSelectionChange

        viewModel.setItems(items, initiallyExpandedIds)
    }

    // 🚀 渲染优化版树组件
    AddTree(
        viewModel = viewModel,
        modifier = modifier,
        content = content
    )
}
