package com.addzero.kmp.component.form

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.api.ApiProvider.sysDeptApi
import com.addzero.kmp.component.tree_command.AddTreeWithCommand
import com.addzero.kmp.component.tree_command.TreeCommand
import com.addzero.kmp.isomorphic.SysDeptIso
import kotlinx.coroutines.launch

/**
 * 🏢 单选部门选择组件
 *
 * 基于 AddDeptSelector 派生的单选版本，选择一个部门后立即关闭并确认
 *
 * @param selectedDept 当前选中的部门
 * @param onValueChange 选择变化回调，返回选中的部门（可为null）
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param enabled 是否启用
 * @param maxHeight 最大高度
 * @param allowClear 是否允许清除选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSingleDeptSelector(
    selectedDept: SysDeptIso? = null,
    onValueChange: (SysDeptIso?) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "请选择部门",
    enabled: Boolean = true,
    maxHeight: androidx.compose.ui.unit.Dp = 400.dp,
    allowClear: Boolean = true
) {
    // 🔧 依赖注入
    val deptApi = sysDeptApi
    val scope = rememberCoroutineScope()

    // 🎯 状态管理
    var isExpanded by remember { mutableStateOf(false) }
    var deptTree by remember { mutableStateOf<List<SysDeptIso>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

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
                value = selectedDept?.name ?: "",
                onValueChange = { },
                readOnly = true,
                enabled = enabled,
                placeholder = { Text(placeholder) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = "部门",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 清除按钮
                        if (selectedDept != null && enabled && allowClear) {
                            IconButton(
                                onClick = {
                                    onValueChange(null)
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
                        // 🌳 单选部门树选择器
                        SingleDeptTreeSelector(
                            deptTree = deptTree,
                            selectedDept = selectedDept,
                            onDeptSelected = { dept ->
                                onValueChange(dept)
                                isExpanded = false // 选择后立即关闭
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

/**
 * 🌳 单选部门树选择器
 *
 * 专门用于单选模式的部门树组件
 *
 * @param deptTree 部门树数据
 * @param selectedDept 当前选中的部门
 * @param onDeptSelected 部门选择回调
 */
@Composable
private fun SingleDeptTreeSelector(
    deptTree: List<SysDeptIso>,
    selectedDept: SysDeptIso?,
    onDeptSelected: (SysDeptIso) -> Unit
) {
    // 🔧 获取部门图标
    val getDeptIcon: @Composable (SysDeptIso) -> androidx.compose.ui.graphics.vector.ImageVector? = { dept ->
        when {
            dept.children.isNotEmpty() -> Icons.Default.Business // 有子部门的用企业图标
            else -> Icons.Default.Group // 叶子部门用团队图标
        }
    }

    // 🎯 获取初始展开的节点ID
    val initiallyExpandedIds = remember(deptTree) {
        buildSet {
            fun collectExpandedIds(depts: List<SysDeptIso>) {
                depts.forEach { dept ->
                    if (dept.children.isNotEmpty()) {
                        dept.id?.let { add(it) }
                        collectExpandedIds(dept.children)
                    }
                }
            }
            collectExpandedIds(deptTree)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // 🛠️ 操作提示
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "选择部门",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "点击部门名称即可选择",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Divider(modifier = Modifier.padding(bottom = 8.dp))

        // 🌳 部门树组件 - 单选模式
        AddTreeWithCommand(
            items = deptTree,
            getId = { it.id ?: 0L },
            getLabel = { it.name },
            getChildren = { it.children },
            getNodeType = { "department" },
            getIcon = getDeptIcon,
            initiallyExpandedIds = initiallyExpandedIds,
            commands = setOf(
                TreeCommand.SEARCH,
                TreeCommand.EXPAND_ALL,
                TreeCommand.COLLAPSE_ALL
                // 注意：不包含 MULTI_SELECT，保持单选模式
            ),
            // 单选模式配置
            autoEnableMultiSelect = false,  // 不自动开启多选
            multiSelectClickToToggle = false, // 不使用多选点击切换
            onNodeClick = { dept ->
                // 点击节点直接选择（只对叶子节点有效）
                if (dept.children.isEmpty()) {
                    onDeptSelected(dept)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 300.dp)
        )
    }
}
