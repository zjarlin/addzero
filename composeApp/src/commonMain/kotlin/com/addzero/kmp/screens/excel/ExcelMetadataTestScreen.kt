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
import com.addzero.kmp.annotation.Route

import com.addzero.kmp.viewmodel.ExcelTemplateDesignerViewModel
import kotlinx.datetime.Clock.System.now
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock

/**
 * Excel元数据提取测试界面
 * 验证所有新功能
 */
@Composable
@Route("测试", "元数据提取测试")
fun ExcelMetadataTestScreen() {
    val viewModel = koinViewModel<ExcelTemplateDesignerViewModel>()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // 顶部工具栏
        MetadataTestTopBar(viewModel)

        // 主要内容 - 三栏布局
        Row(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            // 左侧设计区域 (35%)
            TestDesignArea(
                viewModel = viewModel,
                modifier = Modifier.weight(0.35f)
            )

            // 中间JSON预览 (35%)
            TestJsonPreview(
                viewModel = viewModel,
                modifier = Modifier.weight(0.35f)
            )

            // 右侧元数据提取 (30%)
            TestMetadataPanel(
                viewModel = viewModel,
                modifier = Modifier.weight(0.3f)
            )
        }
    }
}

/**
 * 测试顶部栏
 */
@Composable
private fun MetadataTestTopBar(viewModel: ExcelTemplateDesignerViewModel) {
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
                text = "🔍 元数据提取测试",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.addOneDimensionField("项目名称", "某某工程") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("添加一维", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.addTwoDimensionField("工作内容", "基础开挖") },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("添加二维", color = Color.White, fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        viewModel.addAvailableExcelFile("施工日记${now()}.xlsx", "2.5MB")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.2f)
                    )
                ) {
                    Text("添加Excel", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

/**
 * 测试设计区域
 */
@Composable
private fun TestDesignArea(
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
                text = "🎨 设计区域 (35%)",
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
 * 测试字段卡片 - 验证类型高亮
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
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                OutlinedTextField(
                    value = field.key,
                    onValueChange = onKeyChange,
                    label = { Text("字段名", fontSize = 10.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true
                )

                OutlinedTextField(
                    value = field.value,
                    onValueChange = onValueChange,
                    label = { Text("值", fontSize = 10.sp) },
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodySmall,
                    singleLine = true
                )

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            // 类型选择 - 测试高亮效果
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text("类型:", fontSize = 10.sp, color = Color(0xFF6B7280))

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
                                fontSize = 9.sp,
                                color = if (field.type == type) Color.White else Color(0xFF374151)
                            )
                        },
                        modifier = Modifier.height(20.dp),
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
 * 测试JSON预览
 */
@Composable
private fun TestJsonPreview(
    viewModel: ExcelTemplateDesignerViewModel,
    modifier: Modifier = Modifier
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📄 JSON预览 (35%)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                Button(
                    onClick = {
                        viewModel.copyJsonToClipboard()
                        showCopySuccess = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981)
                    )
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("复制", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                        text = "✅ JSON已复制",
                        modifier = Modifier.padding(8.dp),
                        color = Color.White,
                        fontSize = 11.sp
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
                        .padding(8.dp)
                ) {
                    Text(
                        text = viewModel.jsonPreview,
                        color = Color(0xFF34D399),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            lineHeight = 12.sp
                        ),
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

/**
 * 测试元数据面板
 */
@Composable
private fun TestMetadataPanel(
    viewModel: ExcelTemplateDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFC)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            Text(
                text = "🔍 元数据提取 (30%)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1E40AF)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Excel文件列表
            Text(
                text = "📁 Excel文件 (${viewModel.availableExcelFiles.size})",
                style = MaterialTheme.typography.titleSmall,
                color = Color(0xFF374151)
            )

            if (viewModel.availableExcelFiles.isNotEmpty()) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    viewModel.availableExcelFiles.forEach { excelFile ->
                        TestExcelFileCard(
                            excelFile = excelFile,
                            isInCart = viewModel.isInExtractionCart(excelFile),
                            onAddToCart = { viewModel.addToExtractionCart(excelFile) },
                            onRemove = { viewModel.removeAvailableExcelFile(excelFile) }
                        )
                    }
                }
            } else {
                Text(
                    text = "点击顶部'添加Excel'按钮",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9CA3AF)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 购物车
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "🛒 购物车 (${viewModel.metadataExtractionCart.size})",
//                    style = MaterialTheme.typography.titleSmall,
//                    color = Color(0xFF374151)
//                )
//
//                if (viewModel.metadataExtractionCart.isNotEmpty()) {
//                    Row {
//                        IconButton(
//                            onClick = { viewModel.clearExtractionCart() },
//                            modifier = Modifier.size(20.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.Clear,
//                                contentDescription = "清空",
//                                tint = Color(0xFFEF4444),
//                                modifier = Modifier.size(12.dp)
//                            )
//                        }
//
//                        IconButton(
//                            onClick = { viewModel.startMetadataExtraction() },
//                            modifier = Modifier.size(20.dp)
//                        ) {
//                            Icon(
//                                Icons.Default.PlayArrow,
//                                contentDescription = "开始",
//                                tint = Color(0xFF10B981),
//                                modifier = Modifier.size(12.dp)
//                            )
//                        }
//                    }
//                }
//            }

            // 购物车内容
//            if (viewModel.metadataExtractionCart.isNotEmpty()) {
//                val cartScrollState = rememberScrollState()
//                Column(
//                    modifier = Modifier
//                        .weight(0.5f)
//                        .verticalScroll(cartScrollState),
//                    verticalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    viewModel.metadataExtractionCart.forEach { item ->
//                        TestCartItem(
//                            item = item,
//                            onRemove = { viewModel.removeFromExtractionCart(item) }
//                        )
//                    }
//                }
//            } else {
//                Text(
//                    text = "购物车为空",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color(0xFF9CA3AF)
//                )
//            }
        }
    }
}

/**
 * 测试Excel文件卡片
 */
@Composable
private fun TestExcelFileCard(
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
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    Icons.Default.TableChart,
                    contentDescription = "Excel",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(12.dp)
                )

                Column {
                    Text(
                        text = excelFile.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        fontSize = 9.sp,
                        maxLines = 1
                    )
                    Text(
                        text = excelFile.fileSize,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280),
                        fontSize = 8.sp
                    )
                }

                if (isInCart) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "已添加",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(10.dp)
                    )
                }
            }

            Row {
                if (!isInCart) {
                    IconButton(
                        onClick = onAddToCart,
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(
                            Icons.Default.AddShoppingCart,
                            contentDescription = "添加",
                            modifier = Modifier.size(10.dp),
                            tint = Color(0xFF3B82F6)
                        )
                    }
                }

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        modifier = Modifier.size(10.dp),
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}

/**
 * 测试购物车项
 */
@Composable
private fun TestCartItem(
    item: ExcelTemplateDesignerViewModel.MetadataExtractionItem,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (item.status) {
                ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> Color(0xFFFEF3C7)
                ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> Color(0xFFDCFCE7)
                ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> Color(0xFFD1FAE5)
                ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> Color(0xFFFEE2E2)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    when (item.status) {
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> Icons.Default.Schedule
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> Icons.Default.Sync
                        ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> Icons.Default.CheckCircle
                        ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> Icons.Default.Error
                    },
                    contentDescription = item.status.name,
                    tint = when (item.status) {
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> Color(0xFFD97706)
                        ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> Color(0xFF3B82F6)
                        ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> Color(0xFF10B981)
                        ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> Color(0xFFEF4444)
                    },
                    modifier = Modifier.size(10.dp)
                )

                Column {
                    Text(
                        text = item.excelTemplate.name,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        fontSize = 8.sp,
                        maxLines = 1
                    )
                    Text(
                        text = when (item.status) {
                            ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING -> "待处理"
                            ExcelTemplateDesignerViewModel.ExtractionStatus.PROCESSING -> "处理中"
                            ExcelTemplateDesignerViewModel.ExtractionStatus.COMPLETED -> "已完成"
                            ExcelTemplateDesignerViewModel.ExtractionStatus.FAILED -> "失败"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280),
                        fontSize = 7.sp
                    )
                }
            }

            if (item.status == ExcelTemplateDesignerViewModel.ExtractionStatus.PENDING) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(16.dp)
                ) {
                    Icon(
                        Icons.Default.RemoveShoppingCart,
                        contentDescription = "移除",
                        modifier = Modifier.size(10.dp),
                        tint = Color(0xFFEF4444)
                    )
                }
            }
        }
    }
}
