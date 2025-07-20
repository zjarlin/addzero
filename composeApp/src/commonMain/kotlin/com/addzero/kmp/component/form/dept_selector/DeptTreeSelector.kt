package com.addzero.kmp.component.form.dept_selector

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.tree_command.AddTreeWithCommand
import com.addzero.kmp.component.tree_command.TreeCommand
import com.addzero.kmp.generated.isomorphic.SysDeptIso

/**
 * 🌳 部门树选择器
 *
 * 基于 AddTreeWithCommand 的部门树形选择组件
 *
 * @param deptTree 部门树数据
 * @param selectedDepts 当前选中的部门
 * @param onSelectionChange 选择变化回调
 * @param onConfirm 确认回调（可选）
 * @param onCancel 取消回调
 */
@Composable
fun DeptTreeSelector(
    deptTree: List<SysDeptIso>,
    selectedDepts: List<SysDeptIso>,
    onSelectionChange: (List<SysDeptIso>) -> Unit,
    onConfirm: (() -> Unit)? = null,
    onCancel: () -> Unit
) {
    // 🎯 状态管理
    var currentSelection by remember(selectedDepts) {
        mutableStateOf(selectedDepts)
    }

    // 🔧 获取部门图标
    val getDeptIcon: @Composable (SysDeptIso) -> ImageVector? = { dept ->
        when {
            dept.children.isNotEmpty() -> Icons.Default.Business // 有子部门的用企业图标
            else -> Icons.Default.Group // 叶子部门用团队图标
        }
    }

    // 🎯 获取初始展开的节点ID
    val initiallyExpandedIds = remember(deptTree) {
        // 展开所有有子节点的部门
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
        // 🛠️ 操作提示和按钮
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

            // 操作按钮组 - 左侧：次要操作，右侧：主要操作
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 左侧：次要操作按钮组
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 取消按钮（如果需要确认模式）
                    if (onConfirm != null) {
                        TextButton(
                            onClick = onCancel,
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                "取消",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    // 清除所有选择按钮
                    if (currentSelection.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                currentSelection = emptyList()
                                onSelectionChange(emptyList())
                            },
                            modifier = Modifier.height(32.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.ClearAll,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "清除全部",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }
                }

                // 右侧：主要操作按钮
                if (onConfirm != null) {
                    Button(
                        onClick = onConfirm,
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
                        Text(
                            "确认",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        // 🌳 部门树组件
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
                TreeCommand.MULTI_SELECT,
                TreeCommand.EXPAND_ALL,
                TreeCommand.COLLAPSE_ALL
            ),
            // 自动开启多选模式
            autoEnableMultiSelect = true,
            // 多选模式下点击节点直接切换选中状态
            multiSelectClickToToggle = true,
            onSelectionChange = { selectedItems ->
                currentSelection = selectedItems
                onSelectionChange(selectedItems)
            },
            onCompleteSelectionChange = { result ->
                // 使用完整选择结果，包含推导的父节点
                val allSelectedDepts = result.selectedNodeData.filterIsInstance<SysDeptIso>()
                currentSelection = allSelectedDepts
                onSelectionChange(allSelectedDepts)
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 300.dp)
        )


    }
}

