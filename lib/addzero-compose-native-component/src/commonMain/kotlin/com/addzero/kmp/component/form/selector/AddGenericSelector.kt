package com.addzero.kmp.component.form.selector

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.tree_command.AddTreeWithCommand
import com.addzero.kmp.component.tree_command.TreeCommand
import kotlinx.coroutines.launch


/**
 * 🎯 通用选择器组件
 *
 * 基于 AddTreeWithCommand 的通用选择器，支持任意类型 T 的数据选择
 *
 * @param T 数据类型
 * @param value 当前选择的项目列表
 * @param onValueChange 选择变化回调
 * @param dataProvider 数据提供者，返回树形或列表数据
 * @param getId 获取项目ID的函数
 * @param getLabel 获取项目显示标签的函数
 * @param getChildren 获取子项目的函数，默认返回空列表（用于列表数据）
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param enabled 是否启用
 * @param maxHeight 最大高度
 * @param allowClear 是否允许清除选择
 * @param multiSelect 是否多选模式
 * @param showConfirmButton 是否显示确认按钮
 * @param getIcon 获取项目图标的函数
 * @param getNodeType 获取节点类型的函数
 * @param commands 树形组件命令集合
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AddGenericMultiSelector(
    value: List<T>,
    onValueChange: (List<T>) -> Unit,
    dataProvider: suspend () -> List<T>,
    getId: (T) -> Any,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T> = { emptyList() },
    modifier: Modifier = Modifier,
    placeholder: String = "请选择",
    enabled: Boolean = true,
    maxHeight: Dp = 400.dp,
    allowClear: Boolean = true,
    multiSelect: Boolean = true,
    showConfirmButton: Boolean = true,
    getIcon: @Composable (T) -> ImageVector? = { null },
    getNodeType: (T) -> String = { "item" },
    commands: Set<TreeCommand> = setOf(
        TreeCommand.SEARCH,
        TreeCommand.MULTI_SELECT,
        TreeCommand.EXPAND_ALL,
        TreeCommand.COLLAPSE_ALL
    )
) {
    // 🔧 状态管理
    val scope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }
    var treeData by remember { mutableStateOf<List<T>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // 🔄 加载数据
    LaunchedEffect(isExpanded) {
        if (isExpanded && treeData.isEmpty()) {
            isLoading = true
            error = null
            try {
                treeData = dataProvider()
            } catch (e: Exception) {
                error = "加载数据失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Column(modifier = modifier) {
        // 📝 选择器输入框
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                if (enabled) {
                    isExpanded = it
                }
            }
        ) {
            OutlinedTextField(
                value = when {
                    value.isEmpty() -> ""
                    value.size == 1 -> getLabel(value.first())
                    else -> "${value.size} 项已选择"
                },
                onValueChange = { },
                readOnly = true,
                enabled = enabled,
                placeholder = { Text(placeholder) },
                leadingIcon = if (value.isNotEmpty() && multiSelect) {
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(start = 4.dp)
                        ) {
                            AddSelectedChips(
                                selectedItems = value,
                                onRemoveItem = { itemToRemove ->
                                    val newSelection = value.filter {
                                        getId(it) != getId(itemToRemove)
                                    }
                                    onValueChange(newSelection)
                                },
                                getLabel = getLabel,
                                getId = { getId(it).toString().toLongOrNull() ?: 0L },
                                enabled = enabled,
                                maxItems = 3,
                                contentPadding = PaddingValues(0.dp),
                                itemSpacing = 4.dp
                            )
                        }
                    }
                } else null,
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 清除按钮
                        if (value.isNotEmpty() && enabled && allowClear) {
                            IconButton(
                                onClick = {
                                    onValueChange(emptyList())
                                }
                            ) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "清除选择",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // 下拉箭头
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                    }
                },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            // 🎯 下拉菜单内容
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
                modifier = Modifier.heightIn(max = maxHeight)
            ) {
                when {
                    isLoading -> {
                        // 加载状态
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                                Text("加载中...")
                            }
                        }
                    }

                    error != null -> {
                        // 错误状态
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = error!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        isLoading = true
                                        error = null
                                        try {
                                            treeData = dataProvider()
                                        } catch (e: Exception) {
                                            error = "加载数据失败: ${e.message}"
                                        } finally {
                                            isLoading = false
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("重试")
                            }
                        }
                    }

                    treeData.isNotEmpty() -> {
                        // 🌳 通用树形/列表选择器
                        GenericTreeSelector(
                            treeData = treeData,
                            selectedItems = value,
                            onSelectionChange = { newSelection ->
                                if (showConfirmButton) {
                                    // 确认模式：暂存选择，等待确认
                                    // 这里可以添加临时状态管理
                                } else {
                                    // 实时模式：立即更新
                                    onValueChange(newSelection)
                                    if (!multiSelect) {
                                        isExpanded = false // 单选模式选择后关闭
                                    }
                                }
                            },
                            onConfirm = if (showConfirmButton) {
                                { finalSelection ->
                                    onValueChange(finalSelection)
                                    isExpanded = false
                                }
                            } else null,
                            onCancel = { isExpanded = false },
                            getId = getId,
                            getLabel = getLabel,
                            getChildren = getChildren,
                            getIcon = getIcon,
                            getNodeType = getNodeType,
                            commands = commands,
                            multiSelect = multiSelect
                        )
                    }

                    else -> {
                        // 空状态
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "暂无数据",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 🌳 通用树形选择器
 */
@Composable
private fun <T> GenericTreeSelector(
    treeData: List<T>,
    selectedItems: List<T>,
    onSelectionChange: (List<T>) -> Unit,
    onConfirm: ((List<T>) -> Unit)?,
    onCancel: () -> Unit,
    getId: (T) -> Any,
    getLabel: (T) -> String,
    getChildren: (T) -> List<T>,
    getIcon: @Composable (T) -> ImageVector?,
    getNodeType: (T) -> String,
    commands: Set<TreeCommand>,
    multiSelect: Boolean
) {
    var currentSelection by remember(selectedItems) { mutableStateOf(selectedItems) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // 🛠️ 操作栏
        if (onConfirm != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧：次要操作
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = onCancel,
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("取消", style = MaterialTheme.typography.labelMedium)
                    }

                    if (currentSelection.isNotEmpty()) {
                        TextButton(
                            onClick = { currentSelection = emptyList() },
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.ClearAll,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("清除全部", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                // 右侧：主要操作
                Button(
                    onClick = { onConfirm(currentSelection) },
                    enabled = currentSelection.isNotEmpty(),
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("确认", style = MaterialTheme.typography.labelMedium)
                }
            }

            Divider(modifier = Modifier.padding(bottom = 8.dp))
        }

        // 🌳 树形组件
        AddTreeWithCommand(
            items = treeData,
            getId = getId,
            getLabel = getLabel,
            getChildren = getChildren,
            getNodeType = getNodeType,
            getIcon = getIcon,
            commands = commands,
            autoEnableMultiSelect = multiSelect,
            multiSelectClickToToggle = multiSelect,
            onSelectionChange = { selectedItems ->
                currentSelection = selectedItems
                onSelectionChange(selectedItems)
            },
            onNodeClick = if (!multiSelect) {
                { item: T ->
                    // 单选模式：点击叶子节点直接选择
                    if (getChildren(item).isEmpty()) {
                        val newSelection = listOf(item)
                        currentSelection = newSelection
                        onSelectionChange(newSelection)
                    }
                }
            } else {
                { _: T -> /* 多选模式不处理点击 */ }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 300.dp)
        )
    }
}

