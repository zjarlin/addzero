@file:OptIn(ExperimentalMaterial3Api::class)

package com.addzero.kmp.screens.json

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.viewmodel.JsonDesignerViewModel

/**
 * JSON元素树组件
 */
@Composable
fun JsonElementTree(
    element: JsonDesignerViewModel.JsonElement,
    viewModel: JsonDesignerViewModel,
    modifier: Modifier = Modifier,
    level: Int = 0
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        JsonElementItem(
            element = element,
            viewModel = viewModel,
            level = level
        )

        if (element.isExpanded && element.children.isNotEmpty()) {
            element.children.forEach { child ->
                JsonElementTree(
                    element = child,
                    viewModel = viewModel,
                    level = level + 1
                )
            }
        }
    }
}

/**
 * JSON元素项组件
 */
@Composable
private fun JsonElementItem(
    element: JsonDesignerViewModel.JsonElement,
    viewModel: JsonDesignerViewModel,
    level: Int
) {
    val isSelected = viewModel.selectedElement == element
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    } else {
        Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (level * 16).dp)
            .clickable { viewModel.selectElement(element) },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // 元素头部
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧：展开/收起按钮 + 类型图标 + 键名
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 展开/收起按钮
                    if (element.type == JsonDesignerViewModel.JsonElementType.OBJECT ||
                        element.type == JsonDesignerViewModel.JsonElementType.ARRAY
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleElementExpansion(element) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                if (element.isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (element.isExpanded) "收起" else "展开",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(24.dp))
                    }

                    // 类型图标
                    Icon(
                        when (element.type) {
                            JsonDesignerViewModel.JsonElementType.OBJECT -> Icons.Default.DataObject
                            JsonDesignerViewModel.JsonElementType.ARRAY -> Icons.Default.DataArray
                            JsonDesignerViewModel.JsonElementType.STRING -> Icons.Default.TextFields
                            JsonDesignerViewModel.JsonElementType.NUMBER -> Icons.Default.Numbers
                            JsonDesignerViewModel.JsonElementType.BOOLEAN -> Icons.Default.ToggleOn
                            JsonDesignerViewModel.JsonElementType.NULL -> Icons.Default.Block
                        },
                        contentDescription = element.type.name,
                        tint = when (element.type) {
                            JsonDesignerViewModel.JsonElementType.OBJECT -> Color(0xFF3B82F6)
                            JsonDesignerViewModel.JsonElementType.ARRAY -> Color(0xFF8B5CF6)
                            JsonDesignerViewModel.JsonElementType.STRING -> Color(0xFF10B981)
                            JsonDesignerViewModel.JsonElementType.NUMBER -> Color(0xFFF59E0B)
                            JsonDesignerViewModel.JsonElementType.BOOLEAN -> Color(0xFFEF4444)
                            JsonDesignerViewModel.JsonElementType.NULL -> Color(0xFF6B7280)
                        },
                        modifier = Modifier.size(16.dp)
                    )

                    // 键名编辑
                    if (element.parent?.type != JsonDesignerViewModel.JsonElementType.ARRAY) {
                        OutlinedTextField(
                            value = element.key,
                            onValueChange = { viewModel.updateElement(element, key = it) },
                            modifier = Modifier.width(120.dp),
                            textStyle = MaterialTheme.typography.bodySmall,
                            placeholder = { Text("键名", fontSize = 12.sp) },
                            singleLine = true
                        )
                    }
                }

                // 右侧：操作按钮
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // 添加子元素按钮
                    if (element.type == JsonDesignerViewModel.JsonElementType.OBJECT ||
                        element.type == JsonDesignerViewModel.JsonElementType.ARRAY
                    ) {

                        IconButton(
                            onClick = { viewModel.addString(element) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "添加字符串",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF10B981)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.addObject(element) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.DataObject,
                                contentDescription = "添加对象",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF3B82F6)
                            )
                        }

                        IconButton(
                            onClick = { viewModel.addArray(element) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.DataArray,
                                contentDescription = "添加数组",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFF8B5CF6)
                            )
                        }
                    }

                    // 删除按钮
                    if (element.parent != null) {
                        IconButton(
                            onClick = { viewModel.deleteElement(element) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "删除",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFEF4444)
                            )
                        }
                    }
                }
            }

            // 值编辑区域
            if (element.type != JsonDesignerViewModel.JsonElementType.OBJECT &&
                element.type != JsonDesignerViewModel.JsonElementType.ARRAY
            ) {

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 类型选择
                    var expanded by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.width(100.dp)
                    ) {
                        OutlinedTextField(
                            value = element.type.name,
                            onValueChange = { },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor(),
                            textStyle = MaterialTheme.typography.bodySmall
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            JsonDesignerViewModel.JsonElementType.values().forEach { type ->
                                if (type != JsonDesignerViewModel.JsonElementType.OBJECT &&
                                    type != JsonDesignerViewModel.JsonElementType.ARRAY
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(type.name, fontSize = 12.sp) },
                                        onClick = {
                                            viewModel.updateElement(element, type = type)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 值编辑
                    OutlinedTextField(
                        value = element.value,
                        onValueChange = { viewModel.updateElement(element, value = it) },
                        modifier = Modifier.weight(1f),
                        textStyle = MaterialTheme.typography.bodySmall,
                        placeholder = {
                            Text(
                                when (element.type) {
                                    JsonDesignerViewModel.JsonElementType.STRING -> "字符串值"
                                    JsonDesignerViewModel.JsonElementType.NUMBER -> "数字值"
                                    JsonDesignerViewModel.JsonElementType.BOOLEAN -> "true/false"
                                    JsonDesignerViewModel.JsonElementType.NULL -> "null"
                                    else -> "值"
                                },
                                fontSize = 12.sp
                            )
                        },
                        singleLine = element.type != JsonDesignerViewModel.JsonElementType.STRING
                    )
                }
            }

            // 子元素计数显示
            if ((element.type == JsonDesignerViewModel.JsonElementType.OBJECT ||
                        element.type == JsonDesignerViewModel.JsonElementType.ARRAY) &&
                element.children.isNotEmpty()
            ) {

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${element.children.size} 个子元素",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
            }
        }
    }
}
