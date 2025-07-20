package com.addzero.kmp.component.tree_command

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UnfoldLess
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.search_bar.AddSearchBar
import com.addzero.kmp.component.tree.AddTree
import com.addzero.kmp.component.tree.rememberTreeViewModel
import com.addzero.kmp.component.tree.selection.CompleteSelectionResult

/**
 * 🚀 完全重构的支持命令的树组件 - 基于 TreeViewModel 架构
 *
 * 🎯 设计理念：
 * - 完全基于 TreeViewModel，移除 TreeNodeInfo 依赖
 * - 头部和尾部内容在外部声明，不使用插槽
 * - 使用 AddSearchBar 组件和 TreeSearch 算法
 * - 清晰的职责分离：命令处理 vs 树渲染
 *
 * @param items 树形结构数据列表
 * @param getId 获取节点ID的函数
 * @param getLabel 获取节点标签的函数
 * @param getChildren 获取子节点的函数
 * @param modifier 修饰符
 * @param getNodeType 获取节点类型的函数
 * @param getIcon 获取节点图标的函数
 * @param initiallyExpandedIds 初始展开的节点ID列表
 * @param commands 启用的树命令列表
 * @param onNodeClick 节点点击回调
 * @param onNodeContextMenu 节点右键菜单回调
 * @param onCommandInvoke 命令执行回调
 * @param onSelectionChange 选择变化回调(多选模式)
 * @param onCompleteSelectionChange 完整选择变化回调(包含推导的父节点)
 * @param onItemsChanged 过滤后项目变化回调
 * @param autoEnableMultiSelect 自动开启多选模式
 * @param multiSelectClickToToggle 多选模式下点击节点直接切换选中状态
 */
@Composable
fun <T> AddTreeWithCommand(
    items: List<T>,
    getId: (T) -> Any,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>,
    modifier: Modifier = Modifier,
    getNodeType: (T) -> String = { "" },
    getIcon: @Composable (node: T) -> ImageVector? = { null },
    initiallyExpandedIds: Set<Any> = emptySet(),
    commands: Set<TreeCommand> = setOf(TreeCommand.SEARCH),
    onNodeClick: (T) -> Unit = {},
    onNodeContextMenu: (T) -> Unit = {},
    onCommandInvoke: (TreeCommand, Any?) -> Unit = { _, _ -> },
    onSelectionChange: (List<T>) -> Unit = {},
    onCompleteSelectionChange: (CompleteSelectionResult) -> Unit = {},
    onItemsChanged: (List<T>) -> Unit = {},
    autoEnableMultiSelect: Boolean = false,
    multiSelectClickToToggle: Boolean = false
) {

    // 🎯 创建和配置 TreeViewModel
    val viewModel = rememberTreeViewModel<T>()

    // 🔧 配置 ViewModel
    LaunchedEffect(items, getId, getLabel, getChildren, autoEnableMultiSelect, multiSelectClickToToggle) {
        viewModel.configure(
            getId = getId,
            getLabel = getLabel,
            getChildren = getChildren,
            getNodeType = getNodeType,
            getIcon = getIcon
        )

        // 🎯 配置多选行为
        viewModel.configureMultiSelect(
            autoEnable = autoEnableMultiSelect,
            clickToToggle = multiSelectClickToToggle
        )

        viewModel.onNodeClick = onNodeClick
        viewModel.onNodeContextMenu = onNodeContextMenu
        viewModel.onSelectionChange = onSelectionChange
        viewModel.onCompleteSelectionChange = onCompleteSelectionChange

        viewModel.setItems(items, initiallyExpandedIds)
    }

    // 🎮 命令处理函数
    val handleCommand = { command: TreeCommand ->
        when (command) {
            TreeCommand.SEARCH -> {
                viewModel.toggleSearchBar()
            }

            TreeCommand.MULTI_SELECT -> {
                viewModel.updateMultiSelectMode(!viewModel.multiSelectMode)
            }

            TreeCommand.EXPAND_ALL -> {
                viewModel.expandAll()
                onCommandInvoke(command, viewModel.expandedIds)
            }

            TreeCommand.COLLAPSE_ALL -> {
                viewModel.collapseAll()
                onCommandInvoke(command, null)
            }

            else -> onCommandInvoke(command, null)
        }
    }

    // 🎯 通知过滤结果变化
    LaunchedEffect(viewModel.filteredItems) {
        onItemsChanged(viewModel.filteredItems)
    }

    Column(modifier = modifier) {
        // 🛠️ 工具栏（外部声明）
        if (commands.isNotEmpty()) {
            CommandToolbar(
                commands = commands,
                multiSelectMode = viewModel.multiSelectMode,
                onCommandClick = { handleCommand(it) }
            )
        }

        // 🔍 搜索栏（外部声明）
        AnimatedVisibility(
            visible = viewModel.showSearchBar,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            AddSearchBar(
                keyword = viewModel.searchQuery,
                onKeyWordChanged = { viewModel.updateSearchQuery(it) },
                onSearch = {
                    // 🎯 搜索时自动展开包含匹配项的父节点
                    viewModel.performSearch()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                placeholder = "搜索树节点..."
            )
        }

        // 🎮 展开/收起控制（外部声明）
        if (TreeCommand.EXPAND_ALL in commands || TreeCommand.COLLAPSE_ALL in commands) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (TreeCommand.EXPAND_ALL in commands) {
                    TextButton(
                        onClick = { handleCommand(TreeCommand.EXPAND_ALL) }
                    ) {
                        Icon(Icons.Default.UnfoldMore, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("展开全部")
                    }
                }

                if (TreeCommand.COLLAPSE_ALL in commands) {
                    TextButton(
                        onClick = { handleCommand(TreeCommand.COLLAPSE_ALL) }
                    ) {
                        Icon(Icons.Default.UnfoldLess, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("收起全部")
                    }
                }
            }
        }

        // 🌳 树组件（完全基于 TreeViewModel）
        AddTree(
            viewModel = viewModel,
            modifier = Modifier.weight(1f)
        )

        // 📊 底部状态栏（外部声明）
        AnimatedVisibility(
            visible = viewModel.multiSelectMode,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            SelectedItemsBar(
                onClearSelection = {
                    viewModel.updateMultiSelectMode(false)
                    onSelectionChange(emptyList())
                }
            )
        }
    }
}

/**
 * 搜索栏
 */
@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        placeholder = { Text("搜索节点...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "清除")
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        ),
        shape = MaterialTheme.shapes.small
    )
}


/**
 * 底部选择工具栏
 */
@Composable
private fun SelectedItemsBar(
    onClearSelection: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "多选模式",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            TextButton(onClick = onClearSelection) {
                Text("退出多选")
            }
        }
    }
}

/**
 * 递归过滤树节点
 */
private fun <T> filterTreeItems(
    items: List<T>,
    query: String,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>
): List<T> {
    val lowerQuery = query.trim().lowercase()
    if (lowerQuery.isBlank()) return items

    return items.filter { item ->
        // 节点标签匹配
        val matches = getLabel(item).lowercase().contains(lowerQuery)

        // 或者子节点中有匹配的
        val childrenMatch = filterTreeItems(getChildren(item), query, getLabel, getChildren).isNotEmpty()

        matches || childrenMatch
    }
}

/**
 * 获取树中所有节点的ID
 */
private fun <T> getAllIds(
    items: List<T>,
    getId: (T) -> Any,
    getChildren: (T) -> List<T>
): Set<Any> {
    val result = mutableSetOf<Any>()

    fun collectIds(nodes: List<T>) {
        nodes.forEach { node ->
            result.add(getId(node))
            collectIds(getChildren(node))
        }
    }

    collectIds(items)
    return result
}



