package com.addzero.kmp.screens.excel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.processor.annotation.Route
import com.addzero.kmp.viewmodel.ExcelTemplateDesignerViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Excel模板设计器 - 简单易用版本
 * 专门为Excel模板填充设计数据结构
 */
@Composable
@Route("工具", "Excel模板设计器")
fun ExcelTemplateDesignerScreen() {
    val viewModel = koinViewModel<ExcelTemplateDesignerViewModel>()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部说明和工具栏
        TopInstructionBar(viewModel)

        // 主要内容区域
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 左侧字段设计区域 (缩窄)
            FieldDesignArea(
                viewModel = viewModel,
                modifier = Modifier.weight(0.35f)
            )

            // 中间JSON预览区域
            JsonPreviewArea(
                viewModel = viewModel,
                modifier = Modifier.weight(0.35f)
            )

            // 右侧元数据提取面板
            MetadataExtractionPanel(
                viewModel = viewModel,
                modifier = Modifier.weight(0.3f)
            )
        }

        // 底部Excel模板管理
        ExcelTemplateManagement(
            viewModel = viewModel,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * 顶部说明和工具栏
 */
@Composable
private fun TopInstructionBar(viewModel: ExcelTemplateDesignerViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF3B82F6)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Excel模板数据设计器",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Button(
                    onClick = { viewModel.clearAll() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "重置", tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("重置", color = Color.White)
                }
            }

            Text(
                text = "💡 设计说明：定义一维区域(vo)和二维区域(dtos)的字段，自动生成Excel模板填充所需的JSON格式",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

/**
 * 字段设计区域
 */
@Composable
private fun FieldDesignArea(
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
            // 一维区域设计
            OneDimensionFieldsSection(viewModel)

            HorizontalDivider()

            // 二维区域设计
            TwoDimensionFieldsSection(viewModel)

            // 错误信息显示
            viewModel.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "⚠️ $error",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

/**
 * 一维区域字段设计
 */
@Composable
private fun OneDimensionFieldsSection(viewModel: ExcelTemplateDesignerViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题和添加按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔹 一维区域 (vo: Map<String, Any>)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF059669)
            )

            OutlinedButton(
                onClick = { viewModel.addOneDimensionField() },
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("添加字段", fontSize = 12.sp)
            }
        }

        Text(
            text = "💡 用于填充单个值的字段，如标题、日期等",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6B7280)
        )

        // 字段列表
        viewModel.oneDimensionFields.forEach { field ->
            FieldEditCard(
                field = field,
                onUpdate = { key, value, type ->
                    viewModel.updateOneDimensionField(field, key, value, type)
                },
                onDelete = { viewModel.deleteOneDimensionField(field) }
            )
        }
    }
}

/**
 * 二维区域字段设计
 */
@Composable
private fun TwoDimensionFieldsSection(viewModel: ExcelTemplateDesignerViewModel) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题和添加按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "🔸 二维区域 (dtos: List<Map<String, Any>>)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF7C3AED)
            )

            OutlinedButton(
                onClick = { viewModel.addTwoDimensionField() },
                modifier = Modifier.height(32.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加", modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("添加字段", fontSize = 12.sp)
            }
        }

        Text(
            text = "💡 用于填充列表数据的字段，如表格行数据",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF6B7280)
        )

        // 字段列表
        viewModel.twoDimensionFields.forEach { field ->
            FieldEditCard(
                field = field,
                onUpdate = { key, value, type ->
                    viewModel.updateTwoDimensionField(field, key, value, type)
                },
                onDelete = { viewModel.deleteTwoDimensionField(field) }
            )
        }
    }
}

/**
 * 字段编辑卡片
 */
@Composable
private fun FieldEditCard(
    field: ExcelTemplateDesignerViewModel.FieldItem,
    onUpdate: (String?, String?, ExcelTemplateDesignerViewModel.FieldType?) -> Unit,
    onDelete: () -> Unit
) {
    // 使用本地状态来确保输入框可编辑
    var keyValue by remember(field.id) { mutableStateOf(field.key) }
    var valueValue by remember(field.id) { mutableStateOf(field.value) }

    // 当字段更新时同步本地状态
    LaunchedEffect(field.key) {
        keyValue = field.key
    }
    LaunchedEffect(field.value) {
        valueValue = field.value
    }

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 字段名输入
                OutlinedTextField(
                    value = keyValue,
                    onValueChange = {
                        keyValue = it
                        onUpdate(it, null, null)
                    },
                    label = { Text("字段名", fontSize = 12.sp) },
                    modifier = Modifier.weight(0.4f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 字段值输入
                OutlinedTextField(
                    value = valueValue,
                    onValueChange = {
                        valueValue = it
                        onUpdate(null, it, null)
                    },
                    label = { Text("示例值", fontSize = 12.sp) },
                    modifier = Modifier.weight(0.4f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 删除按钮
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
                Text(
                    text = "类型:",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF6B7280)
                )

                ExcelTemplateDesignerViewModel.FieldType.values().forEach { type ->
                    FilterChip(
                        selected = field.type == type,
                        onClick = { onUpdate(null, null, type) },
                        label = {
                            Text(
                                text = when (type) {
                                    ExcelTemplateDesignerViewModel.FieldType.STRING -> "文本"
                                    ExcelTemplateDesignerViewModel.FieldType.NUMBER -> "数字"
                                },
                                fontSize = 10.sp,
                                color = if (field.type == type) Color.White else Color(0xFF374151)
                            )
                        },
                        modifier = Modifier.height(24.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = Color.White,
                            containerColor = Color(0xFFF3F4F6),
                            labelColor = Color(0xFF374151)
                        )
                    )
                }
            }
        }
    }
}

/**
 * JSON预览区域
 */
@Composable
private fun JsonPreviewArea(
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
            // 标题和工具按钮
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
                    // 复制按钮
                    IconButton(
                        onClick = {
                            val jsonContent = viewModel.copyJsonToClipboard()
                            // TODO: 实际复制到剪贴板
                            showCopySuccess = true
                        }
                    ) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "复制JSON",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    // 保存为模板按钮
                    IconButton(
                        onClick = { showSaveDialog = true }
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = "保存为模板",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 数据说明
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF374151)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "📋 生成的数据格式:",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color.White
                    )
                    Text(
                        text = "• vo: Map<String, Any> - 一维区域数据",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9CA3AF),
                        fontSize = 10.sp
                    )
                    Text(
                        text = "• dtos: List<Map<String, Any>> - 二维区域数据",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9CA3AF),
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                            lineHeight = 16.sp
                        ),
                        fontSize = 11.sp
                    )
                }
            }
        }
    }

    // 复制成功提示
    if (showCopySuccess) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            showCopySuccess = false
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF10B981)
            )
        ) {
            Text(
                text = "✅ JSON已复制到剪贴板",
                modifier = Modifier.padding(12.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    // 保存模板对话框
    if (showSaveDialog) {
        SaveJsonTemplateDialog(
            onSave = { templateName ->
                viewModel.saveAsJsonTemplate(templateName)
                showSaveDialog = false
            },
            onDismiss = { showSaveDialog = false }
        )
    }
}

/**
 * Excel模板管理区域
 */
@Composable
private fun ExcelTemplateManagement(
    viewModel: ExcelTemplateDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Excel模板管理",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Button(
                    onClick = {
                        // 模拟上传
                        viewModel.uploadExcelTemplate("示例模板.xlsx") { fileName ->
                            println("上传文件: $fileName")
                        }
                    }
                ) {
                    Icon(Icons.Default.Upload, contentDescription = "上传", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("上传模板")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 上传的Excel模板
                TemplateSection(
                    title = "📤 Excel模板",
                    templates = viewModel.excelTemplates,
                    onSaveAsCommon = { viewModel.saveAsCommonTemplate(it) },
                    onDelete = { viewModel.deleteTemplate(it) },
                    isCommon = false,
                    modifier = Modifier.weight(1f)
                )

                // 常用Excel模板
                TemplateSection(
                    title = "⭐ 常用Excel",
                    templates = viewModel.commonTemplates,
                    onSaveAsCommon = { },
                    onDelete = { viewModel.deleteTemplate(it) },
                    isCommon = true,
                    modifier = Modifier.weight(1f)
                )

                // JSON模板
                JsonTemplateSection(
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * 模板区域组件
 */
@Composable
private fun TemplateSection(
    title: String,
    templates: List<ExcelTemplateDesignerViewModel.ExcelTemplate>,
    onSaveAsCommon: (ExcelTemplateDesignerViewModel.ExcelTemplate) -> Unit,
    onDelete: (ExcelTemplateDesignerViewModel.ExcelTemplate) -> Unit,
    isCommon: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (templates.isNotEmpty()) {
            templates.forEach { template ->
                TemplateCard(
                    template = template,
                    onSaveAsCommon = { onSaveAsCommon(template) },
                    onDelete = { onDelete(template) },
                    isCommon = isCommon
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color(0xFFE5E7EB)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCommon) "暂无常用模板" else "暂无上传模板",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
        }
    }
}

/**
 * 模板卡片
 */
@Composable
private fun TemplateCard(
    template: ExcelTemplateDesignerViewModel.ExcelTemplate,
    onSaveAsCommon: () -> Unit,
    onDelete: () -> Unit,
    isCommon: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCommon) Color(0xFFFEF3C7) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCommon) Color(0xFFD97706) else Color(0xFFE5E7EB)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.TableChart,
                    contentDescription = "Excel",
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(16.dp)
                )

                Column {
                    Text(
                        text = template.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        fontSize = 12.sp
                    )
                    Text(
                        text = template.fileName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280),
                        fontSize = 10.sp
                    )
                }

                if (isCommon) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "常用",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (!isCommon) {
                    IconButton(
                        onClick = onSaveAsCommon,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.BookmarkAdd,
                            contentDescription = "保存为常用",
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFD97706)
                        )
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}

/**
 * 保存JSON模板对话框
 */
@Composable
private fun SaveJsonTemplateDialog(
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var templateName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("💾 保存JSON模板")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("为当前的JSON设计保存一个模板，方便后续复用：")

                OutlinedTextField(
                    value = templateName,
                    onValueChange = { templateName = it },
                    label = { Text("模板名称") },
                    placeholder = { Text("例如：施工日记元数据JSON模板") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "💡 保存后可以与Excel模板绑定使用",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (templateName.isNotBlank()) {
                        onSave(templateName.trim())
                    }
                },
                enabled = templateName.isNotBlank()
            ) {
                Text("保存")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

/**
 * JSON模板管理区域
 */
@Composable
private fun JsonTemplateSection(
    viewModel: ExcelTemplateDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "📋 JSON模板",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        if (viewModel.jsonTemplates.isNotEmpty()) {
            viewModel.jsonTemplates.forEach { template ->
                JsonTemplateCard(
                    template = template,
                    isSelected = viewModel.selectedJsonTemplate == template,
                    onLoad = { viewModel.loadJsonTemplate(template) },
                    onDelete = { viewModel.deleteJsonTemplate(template) }
                )
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Color(0xFFE5E7EB)
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无JSON模板",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
            }
        }
    }
}

/**
 * JSON模板卡片
 */
@Composable
private fun JsonTemplateCard(
    template: ExcelTemplateDesignerViewModel.JsonTemplate,
    isSelected: Boolean,
    onLoad: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFDCFCE7) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) Color(0xFF16A34A) else Color(0xFFE5E7EB)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.DataObject,
                    contentDescription = "JSON",
                    tint = Color(0xFF7C3AED),
                    modifier = Modifier.size(16.dp)
                )

                Column {
                    Text(
                        text = template.name,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "一维:${template.oneDimensionFields.size} 二维:${template.twoDimensionFields.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280),
                        fontSize = 10.sp
                    )
                }

                if (isSelected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "已选择",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(
                    onClick = onLoad,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "加载模板",
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFF16A34A)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }


    /**
     * 可选择的Excel文件卡片
     */
    @Composable
    fun AvailableExcelFileCard(
        excelFile: ExcelTemplateDesignerViewModel.ExcelTemplate,
        isInCart: Boolean,
        onAddToCart: () -> Unit,
        onRemove: () -> Unit
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isInCart) Color(0xFFDCFCE7) else Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isInCart) Color(0xFF10B981) else Color(0xFFE5E7EB)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.TableChart,
                        contentDescription = "Excel",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(14.dp)
                    )

                    Column {
                        Text(
                            text = excelFile.name,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            fontSize = 11.sp,
                            maxLines = 1
                        )
                        Text(
                            text = excelFile.fileSize,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280),
                            fontSize = 9.sp
                        )
                    }

                    if (isInCart) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "已添加",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if (!isInCart) {
                        IconButton(
                            onClick = onAddToCart,
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                Icons.Default.AddShoppingCart,
                                contentDescription = "添加到购物车",
                                modifier = Modifier.size(12.dp),
                                tint = Color(0xFF3B82F6)
                            )
                        }
                    }

                    IconButton(
                        onClick = onRemove,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "删除",
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFEF4444)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 可选择的Excel文件卡片
 */
@Composable
fun AvailableExcelFileCard(
    excelFile: ExcelTemplateDesignerViewModel.ExcelTemplate,
    isInCart: Boolean,
    onAddToCart: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isInCart) Color(0xFFDCFCE7) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isInCart) Color(0xFF10B981) else Color(0xFFE5E7EB)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.TableChart,
                    contentDescription = "Excel",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(14.dp)
                )

                Column {
                    Text(
                        text = excelFile.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                    Text(
                        text = excelFile.fileSize,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280),
                        fontSize = 9.sp
                    )
                }

                if (isInCart) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "已添加",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (!isInCart) {
                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Default.AddShoppingCart,
                            contentDescription = "添加到购物车",
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFF3B82F6)
                        )
                    }
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(20.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(12.dp),
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}
