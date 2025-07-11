package com.addzero.kmp.component.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.addzero.kmp.api.ApiProvider.sysDeptApi
import com.addzero.kmp.component.form.dept_selector.DeptTreeSelector
import com.addzero.kmp.isomorphic.SysDeptIso
import kotlinx.coroutines.launch

/**
 * 🏢 部门选择组件
 *
 * 基于 AddTreeWithCommand 封装的部门多选组件
 *
 * @param selectedDepts 当前选中的部门列表
 * @param onValueChange 选择变化回调，返回选中的部门列表
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param enabled 是否启用
 * @param showConfirmButton 是否显示确认按钮
 * @param maxHeight 最大高度
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeptSelector(
    selectedDepts: List<SysDeptIso> = emptyList(),
    onValueChange: (List<SysDeptIso>) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "请选择部门",
    enabled: Boolean = true,
    showConfirmButton: Boolean = true,
    maxHeight: Dp = 400.dp
) {
    // 🔧 依赖注入
    val deptApi = sysDeptApi
    val scope = rememberCoroutineScope()

    // 🎯 状态管理
    var isExpanded by remember { mutableStateOf(false) }
    var deptTree by remember { mutableStateOf<List<SysDeptIso>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var tempSelectedDepts by remember { mutableStateOf(selectedDepts) }

    // 🔄 加载部门树数据
    LaunchedEffect(isExpanded) {
        if (isExpanded && deptTree.isEmpty()) {
            isLoading = true
            error = null
            try {
                deptTree = deptApi.tree("")
            } catch (e: Exception) {
                error = "加载部门数据失败: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    // 🎯 同步外部选中状态
    LaunchedEffect(selectedDepts) {
        tempSelectedDepts = selectedDepts
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
                value = "", // 始终为空，内容由 leadingIcon 显示
                onValueChange = { },
                readOnly = true,
                enabled = enabled,
                placeholder = if (selectedDepts.isEmpty()) {
                    { Text(placeholder) }
                } else null, // 有选择时不显示占位符
                leadingIcon = if (selectedDepts.isNotEmpty()) {
                    {
                        // 在输入框内显示选择的标签
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f) // 占用大部分宽度，为尾部图标留空间
                                .padding(start = 4.dp)
                        ) {
                            AddSelectedChips(
                                selectedItems = selectedDepts,
                                onRemoveItem = { deptToRemove ->
                                    val newSelection = selectedDepts.filter { it.id != deptToRemove.id }
                                    tempSelectedDepts = newSelection
                                    onValueChange(newSelection)
                                },
                                getLabel = { it.name },
                                getId = { it.id ?: 0L },
                                enabled = enabled,
                                maxItems = 3, // 在输入框内最多显示3个
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
                        if (selectedDepts.isNotEmpty() && enabled) {
                            IconButton(
                                onClick = {
                                    tempSelectedDepts = emptyList()
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
                                            deptTree = deptApi.tree("")
                                        } catch (e: Exception) {
                                            error = "加载部门数据失败: ${e.message}"
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

                    deptTree.isNotEmpty() -> {
                        // 🌳 部门树选择器
                        DeptTreeSelector(
                            deptTree = deptTree,
                            selectedDepts = tempSelectedDepts,
                            onSelectionChange = { newSelection ->
                                tempSelectedDepts = newSelection
                                if (!showConfirmButton) {
                                    // 如果不显示确认按钮，立即回调
                                    onValueChange(newSelection)
                                }
                            },
                            onConfirm = if (showConfirmButton) {
                                {
                                    onValueChange(tempSelectedDepts)
                                    isExpanded = false
                                }
                            } else null,
                            onCancel = {
                                tempSelectedDepts = selectedDepts
                                isExpanded = false
                            }
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
                                text = "暂无部门数据",
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


