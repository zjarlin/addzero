package com.addzero.kmp.screens.excel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.viewmodel.ExcelTemplateDesignerViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Excel模板设计器测试界面
 * 用于测试输入框编辑功能
 */
@Composable
@Route("测试", "Excel模板测试")
fun ExcelTemplateTestScreen() {
    val viewModel = koinViewModel<ExcelTemplateDesignerViewModel>()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 简化的顶部栏
        SimpleTopBar(viewModel)

        // 主要内容
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 左侧字段编辑
            SimpleFieldEditor(
                viewModel = viewModel,
                modifier = Modifier.weight(0.6f)
            )

            // 右侧JSON预览
            SimpleJsonPreview(
                viewModel = viewModel,
                modifier = Modifier.weight(0.4f)
            )
        }
    }
}

/**
 * 简化的顶部栏
 */
@Composable
private fun SimpleTopBar(viewModel: ExcelTemplateDesignerViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📊 Excel模板测试",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.addOneDimensionField() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("添加一维", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.addTwoDimensionField() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("添加二维", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.clearAll() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("清空", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * 简化的字段编辑器
 */
@Composable
private fun SimpleFieldEditor(
    viewModel: ExcelTemplateDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "🎨 字段编辑器",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            // 一维字段
            Text(
                text = "🔹 一维字段 (${viewModel.oneDimensionFields.size}个)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF059669)
            )

            viewModel.oneDimensionFields.forEachIndexed { index, field ->
                SimpleFieldCard(
                    field = field,
                    index = index,
                    onKeyChange = { newKey ->
                        viewModel.updateOneDimensionField(field, key = newKey)
                    },
                    onValueChange = { newValue ->
                        viewModel.updateOneDimensionField(field, value = newValue)
                    },
                    onDelete = {
                        viewModel.deleteOneDimensionField(field)
                    }
                )
            }

            HorizontalDivider()

            // 二维字段
            Text(
                text = "🔸 二维字段 (${viewModel.twoDimensionFields.size}个)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF7C3AED)
            )

            viewModel.twoDimensionFields.forEachIndexed { index, field ->
                SimpleFieldCard(
                    field = field,
                    index = index,
                    onKeyChange = { newKey ->
                        viewModel.updateTwoDimensionField(field, key = newKey)
                    },
                    onValueChange = { newValue ->
                        viewModel.updateTwoDimensionField(field, value = newValue)
                    },
                    onDelete = {
                        viewModel.deleteTwoDimensionField(field)
                    }
                )
            }
        }
    }
}

/**
 * 简化的字段卡片
 */
@Composable
private fun SimpleFieldCard(
    field: ExcelTemplateDesignerViewModel.FieldItem,
    index: Int,
    onKeyChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 标题行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "字段 ${index + 1}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF6B7280)
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 输入字段
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = field.key,
                    onValueChange = onKeyChange,
                    label = { Text("字段名", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true
                )

                OutlinedTextField(
                    value = field.value,
                    onValueChange = onValueChange,
                    label = { Text("示例值", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true
                )
            }

            // 调试信息
            Text(
                text = "ID: ${field.id} | Key: '${field.key}' | Value: '${field.value}'",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9CA3AF),
                fontSize = 10.sp
            )
        }
    }
}

/**
 * 简化的JSON预览
 */
@Composable
private fun SimpleJsonPreview(
    viewModel: ExcelTemplateDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1F2937)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "📄 JSON预览",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 错误信息
            viewModel.errorMessage?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFDC2626)
                    )
                ) {
                    Text(
                        text = "⚠️ $error",
                        modifier = Modifier.padding(8.dp),
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // JSON内容
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF111827)
                )
            ) {
                val scrollState = rememberScrollState()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(12.dp)
                ) {
                    Text(
                        text = viewModel.jsonPreview,
                        color = Color(0xFF34D399),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            lineHeight = 14.sp
                        ),
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
