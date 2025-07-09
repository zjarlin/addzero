package com.addzero.kmp.demo.upload

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.form.AddMultiFilePicker
import com.addzero.kmp.component.form.PickedFile
import com.addzero.kmp.component.form.USE_MOCK_FILE_UPLOAD
import com.addzero.kmp.component.form.formatFileSize
import com.addzero.kmp.component.upload_manager.GlobalUploadManager
import com.addzero.kmp.component.upload_manager.UploadManagerUI

/**
 * 上传管理器演示
 * 展示类似浏览器下载管理器的文件上传功能
 */
@Route("组件示例", title = "上传管理器")
@Composable
fun UploadManagerDemo() {
    var useMock by remember { mutableStateOf(USE_MOCK_FILE_UPLOAD) }
    var selectedFiles by remember { mutableStateOf(emptyList<PickedFile>()) }
    var showUploadManager by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 顶部控制面板
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "文件上传管理器演示",
                    style = MaterialTheme.typography.headlineSmall
                )
                
                Text(
                    text = "这是一个类似浏览器下载管理器的文件上传系统。选择文件后，上传任务会自动添加到全局管理器中，您可以查看所有任务的进度、状态和管理操作。",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Divider()
                
                // 设置选项
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("使用模拟上传：")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = useMock,
                            onCheckedChange = {
                                useMock = it
                                USE_MOCK_FILE_UPLOAD = it
                            }
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("显示上传管理器：")
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = showUploadManager,
                            onCheckedChange = { showUploadManager = it }
                        )
                    }
                }
            }
        }

        // 文件选择器
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.FileUpload,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "文件选择",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                
                AddMultiFilePicker(
                    onFilesSelected = { files ->
                        selectedFiles = files
                    },
                    showUploadManager = false // 这里不显示，下面单独显示
                )
                
                // 显示最近选择的文件
                if (selectedFiles.isNotEmpty()) {
                    Divider()
                    Text(
                        text = "最近选择的文件 (${selectedFiles.size} 个):",
                        style = MaterialTheme.typography.titleSmall
                    )
                    selectedFiles.take(3).forEach { file ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = file.name,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = formatFileSize(file.size),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    if (selectedFiles.size > 3) {
                        Text(
                            text = "... 还有 ${selectedFiles.size - 3} 个文件",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 上传管理器
        if (showUploadManager) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CloudUpload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "上传管理器",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    
                    // 统计信息
                    val uploadManager = GlobalUploadManager.instance
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatCard(
                            title = "进行中",
                            count = uploadManager.activeTasks.size,
                            color = MaterialTheme.colorScheme.primary
                        )
                        StatCard(
                            title = "已完成",
                            count = uploadManager.completedTasks.size,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        StatCard(
                            title = "失败",
                            count = uploadManager.failedTasks.size,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    Divider()
                    
                    // 上传管理器UI
                    UploadManagerUI(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )
                }
            }
        }
    }
}

/**
 * 统计卡片组件
 */
@Composable
private fun StatCard(
    title: String,
    count: Int,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}
