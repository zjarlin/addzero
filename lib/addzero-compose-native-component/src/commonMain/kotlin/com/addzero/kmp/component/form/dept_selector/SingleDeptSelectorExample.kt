package com.addzero.kmp.component.form.dept_selector

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.form.selector.AddDeptSelector
import com.addzero.kmp.component.form.selector.AddSingleDeptSelector
import com.addzero.kmp.generated.isomorphic.SysDeptIso

/**
 * 🏢 单选部门选择器使用示例
 *
 * 展示 AddSingleDeptSelector 与 AddDeptSelector 的对比使用
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Route
fun SingleDeptSelectorExample() {
    // 🎯 状态管理
    var selectedSingleDept by remember { mutableStateOf<SysDeptIso?>(null) }
    var selectedMultiDepts by remember { mutableStateOf<List<SysDeptIso>>(emptyList()) }
    var selectedParentDept by remember { mutableStateOf<SysDeptIso?>(null) }
    var selectedManagerDept by remember { mutableStateOf<SysDeptIso?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 📋 标题
        Text(
            text = "🏢 部门选择器对比示例",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Divider()

        // 🎯 单选部门选择器示例
        ExampleSection(
            title = "1. 单选部门选择器 (AddSingleDeptSelector)",
            description = "选择一个部门后立即关闭，适合选择上级部门、负责部门等场景"
        ) {
            AddSingleDeptSelector(
                value = selectedSingleDept,
                onValueChange = {selectedSingleDept = it },
                placeholder = "请选择所属部门"
            )

            if (selectedSingleDept != null) {
                SelectionResult(
                    title = "选择结果:",
                    dept = selectedSingleDept!!
                )
            }
        }

        // 🎯 多选部门选择器对比
        ExampleSection(
            title = "2. 多选部门选择器 (AddDeptSelector)",
            description = "支持选择多个部门，需要点击确认按钮，适合选择管理部门、参与部门等场景"
        ) {
            AddDeptSelector(
                value = selectedMultiDepts,
                onValueChange = { selectedMultiDepts = it },
                placeholder = "请选择管理部门"
            )

            if (selectedMultiDepts.isNotEmpty()) {
                MultiSelectionResult(
                    title = "选择结果:",
                    depts = selectedMultiDepts
                )
            }
        }

        // 🎯 上级部门选择示例
        ExampleSection(
            title = "3. 上级部门选择",
            description = "典型的单选场景，选择组织架构中的上级部门"
        ) {
            AddSingleDeptSelector(
                value = selectedParentDept,
                onValueChange = { selectedParentDept = it },
                placeholder = "请选择上级部门",
                allowClear = true
            )

            if (selectedParentDept != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "上级部门: ${selectedParentDept!!.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        if (selectedParentDept!!.id != null) {
                            Text(
                                text = "部门ID: ${selectedParentDept!!.id}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }

        // 🎯 负责人部门选择示例
        ExampleSection(
            title = "4. 负责人部门选择",
            description = "不允许清除的单选场景，必须选择一个部门"
        ) {
            AddSingleDeptSelector(
                value = selectedManagerDept,
                onValueChange = { selectedManagerDept = it },
                placeholder = "请选择负责人所在部门",
                allowClear = false // 不允许清除
            )

            if (selectedManagerDept != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(
                        text = "负责人部门: ${selectedManagerDept!!.name}",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }

        // 🎯 对比说明
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💡 使用场景对比",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                val comparisons = listOf(
                    "🎯 单选选择器" to listOf(
                        "选择后立即关闭，无需确认",
                        "适合上级部门、负责部门等单一选择",
                        "界面简洁，操作快速",
                        "支持清除选择（可配置）"
                    ),
                    "📋 多选选择器" to listOf(
                        "支持选择多个部门",
                        "需要点击确认按钮",
                        "适合管理部门、参与部门等多选场景",
                        "内嵌标签显示选择结果"
                    )
                )

                comparisons.forEach { (title, features) ->
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    features.forEach { feature ->
                        Text(
                            text = "  • $feature",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        // 🎯 操作按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    selectedSingleDept = null
                    selectedMultiDepts = emptyList()
                    selectedParentDept = null
                    selectedManagerDept = null
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("清除所有选择")
            }

            Button(
                onClick = {
                    // 模拟预设选择
                    selectedSingleDept = SysDeptIso(id = 1L, name = "技术部")
                    selectedMultiDepts = listOf(
                        SysDeptIso(id = 1L, name = "技术部"),
                        SysDeptIso(id = 2L, name = "市场部")
                    )
                    selectedParentDept = SysDeptIso(id = 0L, name = "总公司")
                    selectedManagerDept = SysDeptIso(id = 1L, name = "技术部")
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("预设选择")
            }
        }
    }
}

/**
 * 📋 示例区域组件
 */
@Composable
private fun ExampleSection(
    title: String,
    description: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            content()
        }
    }
}

/**
 * 📋 单选结果显示
 */
@Composable
private fun SelectionResult(
    title: String,
    dept: SysDeptIso
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = dept.name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            if (dept.id != null) {
                Text(
                    text = "ID: ${dept.id}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

/**
 * 📋 多选结果显示
 */
@Composable
private fun MultiSelectionResult(
    title: String,
    depts: List<SysDeptIso>
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            depts.forEachIndexed { index, dept ->
                Text(
                    text = "${index + 1}. ${dept.name}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f))

            Text(
                text = "总计: ${depts.size} 个部门",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
