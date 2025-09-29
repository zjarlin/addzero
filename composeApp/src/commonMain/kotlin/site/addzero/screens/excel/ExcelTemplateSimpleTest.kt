package site.addzero.screens.excel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel
import site.addzero.annotation.Route
import site.addzero.viewmodel.ExcelTemplateDesignerViewModel

/**
 * Excel模板设计器简单测试
 * 验证所有新功能
 */
@Composable
@Route("测试", "Excel简单测试")
fun ExcelTemplateSimpleTest() {
    val viewModel = koinViewModel<ExcelTemplateDesignerViewModel>()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部工具栏
        TestTopBar(viewModel)

        // 主要内容
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 左侧字段编辑
            TestFieldEditor(
                viewModel = viewModel,
                modifier = Modifier.weight(0.5f)
            )

            // 右侧JSON预览和模板管理
            TestJsonArea(
                viewModel = viewModel,
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

/**
 * 测试顶部栏
 */
@Composable
private fun TestTopBar(viewModel: ExcelTemplateDesignerViewModel) {
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
                text = "📊 Excel设计器测试",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.addOneDimensionField("测试字段", "测试值") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("添加一维", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.addTwoDimensionField("温度", "25") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("添加二维", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * 测试字段编辑器
 */
@Composable
private fun TestFieldEditor(
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "🎨 字段编辑",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            // 一维字段
            Text(
                text = "🔹 一维字段 (${viewModel.oneDimensionFields.size})",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF059669)
            )

            viewModel.oneDimensionFields.forEach { field ->
                TestFieldCard(
                    field = field,
                    onKeyChange = { viewModel.updateOneDimensionField(field, key = it) },
                    onValueChange = { viewModel.updateOneDimensionField(field, value = it) },
                    onTypeChange = { viewModel.updateOneDimensionField(field, type = it) },
                    onDelete = { viewModel.deleteOneDimensionField(field) }
                )
            }

            HorizontalDivider()

            // 二维字段
            Text(
                text = "🔸 二维字段 (${viewModel.twoDimensionFields.size})",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF7C3AED)
            )

            viewModel.twoDimensionFields.forEach { field ->
                TestFieldCard(
                    field = field,
                    onKeyChange = { viewModel.updateTwoDimensionField(field, key = it) },
                    onValueChange = { viewModel.updateTwoDimensionField(field, value = it) },
                    onTypeChange = { viewModel.updateTwoDimensionField(field, type = it) },
                    onDelete = { viewModel.deleteTwoDimensionField(field) }
                )
            }
        }
    }
}

/**
 * 测试字段卡片
 */
@Composable
private fun TestFieldCard(
    field: ExcelTemplateDesignerViewModel.FieldItem,
    onKeyChange: (String) -> Unit,
    onValueChange: (String) -> Unit,
    onTypeChange: (ExcelTemplateDesignerViewModel.FieldType) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                    label = { Text("值", fontSize = 12.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            // 类型选择
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("类型:", fontSize = 12.sp)

                ExcelTemplateDesignerViewModel.FieldType.values().forEach { type ->
                    FilterChip(
                        selected = field.type == type,
                        onClick = { onTypeChange(type) },
                        label = {
                            Text(
                                text = when (type) {
                                    ExcelTemplateDesignerViewModel.FieldType.STRING -> "文本"
                                    ExcelTemplateDesignerViewModel.FieldType.NUMBER -> "数字"
                                },
                                fontSize = 10.sp
                            )
                        },
                        modifier = Modifier.height(24.dp)
                    )
                }
            }
        }
    }
}

/**
 * 测试JSON区域
 */
@Composable
private fun TestJsonArea(
    viewModel: ExcelTemplateDesignerViewModel,
    modifier: Modifier = Modifier
) {
    var showSaveDialog by remember { mutableStateOf(false) }
    var showCopySuccess by remember { mutableStateOf(false) }

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
            // 标题和按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📄 JSON预览",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.copyJsonToClipboard()
                            showCopySuccess = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        )
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("复制", fontSize = 12.sp)
                    }

                    Button(
                        onClick = { showSaveDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3B82F6)
                        )
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("保存", fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 复制成功提示
            if (showCopySuccess) {
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(2000)
                    showCopySuccess = false
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981)
                    )
                ) {
                    Text(
                        text = "✅ JSON已复制到剪贴板",
                        modifier = Modifier.padding(8.dp),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // JSON内容
            Card(
                modifier = Modifier.weight(1f),
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

            Spacer(modifier = Modifier.height(8.dp))

            // JSON模板列表
            Text(
                text = "📋 JSON模板 (${viewModel.jsonTemplates.size})",
                color = Color.White,
                style = MaterialTheme.typography.titleSmall
            )

            if (viewModel.jsonTemplates.isNotEmpty()) {
                viewModel.jsonTemplates.forEach { template ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (viewModel.selectedJsonTemplate == template)
                                Color(0xFF374151) else Color(0xFF1F2937)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = template.name,
                                color = Color.White,
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Row {
                                IconButton(
                                    onClick = { viewModel.loadJsonTemplate(template) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.PlayArrow,
                                        contentDescription = "加载",
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.deleteJsonTemplate(template) },
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
                        }
                    }
                }
            } else {
                Text(
                    text = "暂无模板",
                    color = Color(0xFF9CA3AF),
                    fontSize = 12.sp
                )
            }
        }
    }

    // 保存模板对话框
    if (showSaveDialog) {
        var templateName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("保存JSON模板") },
            text = {
                OutlinedTextField(
                    value = templateName,
                    onValueChange = { templateName = it },
                    label = { Text("模板名称") },
                    placeholder = { Text("例如：施工日记元数据JSON模板") }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (templateName.isNotBlank()) {
                            viewModel.saveAsJsonTemplate(templateName.trim())
                            showSaveDialog = false
                        }
                    },
                    enabled = templateName.isNotBlank()
                ) {
                    Text("保存")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}
