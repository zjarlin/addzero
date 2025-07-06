package com.addzero.kmp.component.tree_command

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.anno.Good
import com.addzero.kmp.component.search_bar.AddSearchBar
import com.addzero.kmp.component.tree.AddTree
import com.addzero.kmp.component.tree.DefaultNodeRender
import com.addzero.kmp.component.tree.TreeNodeInfo

/**
 * 支持命令的树组件 - 包装AddTree组件
 *
 * @param items 树形结构数据列表
 * @param getId 获取节点ID的函数
 * @param getLabel 获取节点标签的函数
 * @param getChildren 获取子节点的函数
 * @param getNodeType 获取节点类型的函数
 * @param getIcon 获取节点图标的函数
 * @param initiallyExpandedIds 初始展开的节点ID列表
 * @param commands 启用的树命令列表
 * @param onNodeClick 节点点击回调
 * @param onNodeContextMenu 节点右键菜单回调
 * @param onCommandInvoke 命令执行回调
 * @param onSelectionChange 选择变化回调(多选模式)
 * @param nodeRender 节点渲染函数
 * @param contextMenuContent 右键菜单内容
 */
@Composable
@Good
fun <T> AddTreeWithCommand(
    items: List<T>,
    onItemsChanged : (List<T>) -> Unit={},
    modifier: Modifier = Modifier,
    getId: (T) -> Any,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>,
    getNodeType: (T) -> String = { "" },
    getIcon: @Composable (node: T) -> ImageVector? = { null },
    initiallyExpandedIds: Set<Any> = emptySet(),
    commands: Set<TreeCommand> =  setOf(TreeCommand.SEARCH),
    onNodeClick: (T) -> Unit = {},
    onNodeContextMenu: (T) -> Unit = {},
    onCommandInvoke: (TreeCommand, Any?) -> Unit = { _, _ -> },
    onSelectionChange: (List<T>) -> Unit = {},
    nodeRender: @Composable (TreeNodeInfo<T>) -> Unit = { DefaultNodeRender(it) },
    contextMenuContent: @Composable (T) -> Unit = {}
) {
    // 搜索状态
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }

    // 多选状态
    var multiSelectMode by remember { mutableStateOf(false) }

    // 过滤的项目
    val filteredItems = remember(items, searchQuery) {
        if (searchQuery.isBlank()) items
        else filterTreeItems(items, searchQuery, getLabel, getChildren)
    }

    // 命令处理函数
    val handleCommand = { command: TreeCommand ->
        when (command) {
            TreeCommand.SEARCH -> {
                showSearchBar = !showSearchBar
                if (!showSearchBar) searchQuery = "" // 关闭搜索时清空查询
            }

            TreeCommand.MULTI_SELECT -> {
                multiSelectMode = !multiSelectMode
            }

            else -> onCommandInvoke(command, null)
        }
    }

    Column(modifier = modifier) {
        // 工具栏
        if (commands.isNotEmpty()) {
            CommandToolbar(
                commands = commands,
                multiSelectMode = multiSelectMode,
                onCommandClick = { handleCommand(it) }
            )
        }

        // 使用AddTree的插槽
        AddTree(
            items = filteredItems,
            modifier = Modifier.weight(1f),
            getId = getId,
            getLabel = getLabel,
            getChildren = getChildren,
            getNodeType = getNodeType,
            getIcon = getIcon,
            initiallyExpandedIds = initiallyExpandedIds,
            onCurrentNodeClick = onNodeClick,
            onNodeContextMenu = onNodeContextMenu,
            nodeRender = nodeRender,
            multiSelectMode = multiSelectMode,
            onSelectionChange = onSelectionChange,
            contextMenuContent = contextMenuContent,

            // 顶部搜索插槽
            topSlot = {
                AnimatedVisibility(
                    visible = showSearchBar,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    AddSearchBar(
                        keyword = searchQuery,
                        onKeyWordChanged ={searchQuery=it} ,
                        onSearch = {onItemsChanged(filteredItems) },
                    )
                }
            },

            // 多选模式插槽
            multiSelectRender = { info, isSelected, onSelectionToggle ->
                MultiSelectNodeRender(
                    nodeInfo = info,
                    isSelected = isSelected,
                    onSelectionToggle = onSelectionToggle
                )
            },

            // 展开全部插槽
            expandAllSlot = { _, updateExpandedIds ->
                if (TreeCommand.EXPAND_ALL in commands) {
                    TextButton(
                        onClick = {
                            onCommandInvoke(TreeCommand.EXPAND_ALL, null)
                            updateExpandedIds(getAllIds(items, getId, getChildren))
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.UnfoldMore,
                            contentDescription = "展开全部",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("展开全部", style = MaterialTheme.typography.bodySmall)
                    }
                }
            },

            // 收起全部插槽
            collapseAllSlot = { _, collapseAll ->
                if (TreeCommand.COLLAPSE_ALL in commands) {
                    TextButton(
                        onClick = {
                            onCommandInvoke(TreeCommand.COLLAPSE_ALL, null)
                            collapseAll()
                        },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.UnfoldLess,
                            contentDescription = "收起全部",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("收起全部", style = MaterialTheme.typography.bodySmall)
                    }
                }
            },

            // 底部插槽
            bottomSlot = {
                AnimatedVisibility(
                    visible = multiSelectMode,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    SelectedItemsBar(
                        onClearSelection = {
                            multiSelectMode = false
                            onSelectionChange(emptyList())
                        }
                    )
                }
            }
        )
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
 * 多选模式下的节点渲染
 */
@Composable
private fun <T> MultiSelectNodeRender(
    nodeInfo: TreeNodeInfo<T>,
    isSelected: Boolean,
    onSelectionToggle: (Boolean) -> Unit
) {
    val backgroundColor = when {
        nodeInfo.isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        nodeInfo.level == 0 -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        else -> MaterialTheme.colorScheme.surface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(backgroundColor)
            .padding(start = (nodeInfo.level * 16).dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 多选框
        IconButton(
            onClick = { onSelectionToggle(!isSelected) },
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                imageVector = if (isSelected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                contentDescription = if (isSelected) "取消选择" else "选择",
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }

        // 图标
        Icon(
            imageVector = nodeInfo.icon ?: Icons.Default.Folder,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        // 节点文本
        Text(
            text = nodeInfo.label,
            style = MaterialTheme.typography.bodyMedium,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier.weight(1f)
        )

        // 显示节点类型
        if (nodeInfo.nodeType.isNotEmpty()) {
            Text(
                text = nodeInfo.nodeType,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
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
