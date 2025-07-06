package com.addzero.kmp.screens.json

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.viewmodel.JsonDesignerViewModel

/**
 * Excel上传区域组件
 */
@Composable
fun ExcelUploadArea(
    viewModel: JsonDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF7FAFC)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 标题
            Text(
                text = "📊 Excel模板管理",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 上传区域
                ExcelUploadSection(
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f)
                )
                
                // 常用模板区域
                CommonTemplatesSection(
                    viewModel = viewModel,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Excel上传区域
 */
@Composable
private fun ExcelUploadSection(
    viewModel: JsonDesignerViewModel,
    modifier: Modifier = Modifier
) {
    var showUploadDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题和上传按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📤 上传Excel",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Button(
                onClick = { showUploadDialog = true },
                modifier = Modifier.height(32.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Icon(
                    Icons.Default.Upload,
                    contentDescription = "上传",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("上传", fontSize = 12.sp)
            }
        }
        
        // 上传的文件列表
        if (viewModel.uploadedExcelFiles.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.uploadedExcelFiles) { template ->
                    ExcelFileCard(
                        template = template,
                        onSaveAsCommon = { viewModel.saveAsCommonTemplate(template) },
                        onDelete = { viewModel.deleteTemplate(template) },
                        isCommon = false
                    )
                }
            }
        } else {
            // 空状态
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clickable { showUploadDialog = true },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, 
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CloudUpload,
                        contentDescription = "上传",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "点击上传Excel文件",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
    
    // 上传对话框
    if (showUploadDialog) {
        ExcelUploadDialog(
            onDismiss = { showUploadDialog = false },
            onUpload = { fileName ->
                viewModel.uploadExcelFile(fileName) { 
                    // TODO: 实际上传逻辑
                    println("上传文件: $fileName")
                }
                showUploadDialog = false
            }
        )
    }
}

/**
 * 常用模板区域
 */
@Composable
private fun CommonTemplatesSection(
    viewModel: JsonDesignerViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 标题
        Text(
            text = "⭐ 常用模板",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        // 常用模板列表
        if (viewModel.commonTemplates.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.commonTemplates) { template ->
                    ExcelFileCard(
                        template = template,
                        onSaveAsCommon = { },
                        onDelete = { viewModel.deleteTemplate(template) },
                        isCommon = true
                    )
                }
            }
        } else {
            // 空状态
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, 
                    MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.BookmarkBorder,
                        contentDescription = "无模板",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "暂无常用模板",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Excel文件卡片
 */
@Composable
private fun ExcelFileCard(
    template: JsonDesignerViewModel.ExcelTemplate,
    onSaveAsCommon: () -> Unit,
    onDelete: () -> Unit,
    isCommon: Boolean
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCommon) Color(0xFFFFF7ED) else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCommon) Color(0xFFFB923C) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 文件信息
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.TableChart,
                        contentDescription = "Excel",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(16.dp)
                    )
                    
                    if (isCommon) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "常用",
                            tint = Color(0xFFFB923C),
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
                
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 11.sp
                )
                
                Text(
                    text = template.fileName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 10.sp
                )
            }
            
            // 操作按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (!isCommon) {
                    IconButton(
                        onClick = onSaveAsCommon,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            Icons.Default.BookmarkAdd,
                            contentDescription = "保存为常用",
                            modifier = Modifier.size(12.dp),
                            tint = Color(0xFFFB923C)
                        )
                    }
                }
                
                IconButton(
                    onClick = onDelete,
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

/**
 * Excel上传对话框
 */
@Composable
private fun ExcelUploadDialog(
    onDismiss: () -> Unit,
    onUpload: (String) -> Unit
) {
    var fileName by remember { mutableStateOf("") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("上传Excel文件")
        },
        text = {
            Column {
                Text("请输入Excel文件名（模拟上传）：")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    placeholder = { Text("例如：用户数据.xlsx") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (fileName.isNotBlank()) {
                        onUpload(fileName)
                    }
                },
                enabled = fileName.isNotBlank()
            ) {
                Text("上传")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
