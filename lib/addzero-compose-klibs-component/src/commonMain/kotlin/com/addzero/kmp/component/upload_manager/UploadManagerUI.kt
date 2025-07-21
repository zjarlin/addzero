@file:OptIn(ExperimentalTime::class)

package com.addzero.kmp.component.upload_manager

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.file_picker.formatFileSize
import kotlin.time.ExperimentalTime

/**
 * 上传管理器UI组件
 * 类似浏览器下载管理器的界面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadManagerUI(
    modifier: Modifier = Modifier, uploadManager: UploadManager = GlobalUploadManager.instance
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("进行中", "已完成", "失败")

    Column(modifier = modifier.fillMaxSize()) {
        // 顶部操作栏
        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "上传管理器", style = MaterialTheme.typography.headlineSmall
                )

                Row {
                    // 清除已完成按钮
                    if (uploadManager.completedTasks.isNotEmpty()) {
                        TextButton(
                            onClick = { uploadManager.clearCompletedTasks() }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("清除已完成")
                        }
                    }

                    // 清除失败按钮
                    if (uploadManager.failedTasks.isNotEmpty()) {
                        TextButton(
                            onClick = { uploadManager.clearFailedTasks() }) {
                            Icon(Icons.Default.Delete, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("清除失败")
                        }
                    }
                }
            }
        }

        // 标签页
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                val count = when (index) {
                    0 -> uploadManager.activeTasks.size
                    1 -> uploadManager.completedTasks.size
                    2 -> uploadManager.failedTasks.size
                    else -> 0
                }

                Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = {
                    Text("$title ($count)")
                })
            }
        }

        // 任务列表
        val currentTasks = when (selectedTab) {
            0 -> uploadManager.activeTasks
            1 -> uploadManager.completedTasks
            2 -> uploadManager.failedTasks
            else -> emptyList()
        }

        if (currentTasks.isEmpty()) {
            // 空状态
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = when (selectedTab) {
                            0 -> Icons.Default.CloudUpload
                            1 -> Icons.Default.CheckCircle
                            2 -> Icons.Default.Error
                            else -> Icons.Default.CloudUpload
                        },
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = when (selectedTab) {
                            0 -> "暂无进行中的上传任务"
                            1 -> "暂无已完成的上传任务"
                            2 -> "暂无失败的上传任务"
                            else -> "暂无任务"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentTasks) { task ->
                    UploadTaskItem(
                        task = task,
                        onCancel = { uploadManager.cancelTask(task.id) },
                        onRetry = { /* 需要传入content参数，这里暂时留空 */ })
                }
            }
        }
    }
}

/**
 * 单个上传任务项
 */
@Composable
private fun UploadTaskItem(
    task: UploadTask, onCancel: () -> Unit, onRetry: () -> Unit, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
            containerColor = when (task.status) {
                UploadTaskStatus.FAILED -> MaterialTheme.colorScheme.errorContainer
                UploadTaskStatus.COMPLETED -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 文件信息行
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = task.fileName,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatFileSize(task.fileSize),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 状态图标和操作按钮
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (task.status) {
                        UploadTaskStatus.PENDING -> {
                            Icon(
                                Icons.Default.Schedule,
                                contentDescription = "等待中",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        UploadTaskStatus.UPLOADING, UploadTaskStatus.QUERYING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp), strokeWidth = 2.dp
                            )
                        }

                        UploadTaskStatus.COMPLETED -> {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "完成",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        UploadTaskStatus.FAILED -> {
                            IconButton(onClick = onRetry) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "重试",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }

                    if (task.isActive) {
                        IconButton(onClick = onCancel) {
                            Icon(
                                Icons.Default.Cancel,
                                contentDescription = "取消",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // 进度条
            if (task.isActive && task.progress > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { task.progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "${(task.progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 错误信息
            if (task.errorMessage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "错误: ${task.errorMessage}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }


            // 文件链接
            if (task.fileUrl != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "文件链接: ${task.fileUrl}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
