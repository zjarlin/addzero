@file:OptIn(ExperimentalTime::class)

package com.addzero.kmp.component.file_picker

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.filekit.ext.toMultipartFile
import com.addzero.kmp.component.upload_manager.GlobalUploadManager
import com.addzero.kmp.component.upload_manager.UploadManagerUI
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.ktor.client.request.forms.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.math.absoluteValue
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// 全局开关：是否使用模拟上传功能（不连接后端）
var USE_MOCK_FILE_UPLOAD = true

/**
 * 模拟文件上传响应类
 */
@Serializable
@Deprecated("用真实的")
data class MockFileUploadResponse(
    val fileUrl: String,
    val progress: Float
)

@Serializable
data class PickedFile(
    val name: String,
    val path: String?,
    val size: Long?,
    @Transient
    val multiPartFormDataContent: MultiPartFormDataContent? = null,
) {
}

/**
 * 模拟文件上传API，返回一个redisKey
 */
suspend fun mockUploadFile(content: MultiPartFormDataContent): String {
    // 模拟网络延迟
    delay(500)
    // 返回模拟的redisKey
    return "file_upload_" + Clock.System.now()
}

/**
 * 模拟查询文件上传进度API
 */
suspend fun mockQueryProgress(redisKey: String): MockFileUploadResponse {
    // 根据redisKey的hash值生成一个递增的进度
    val hashCode = redisKey.hashCode().absoluteValue
    val currentTimeMillis = Clock.System.now().toEpochMilliseconds()
    val progressBase = (currentTimeMillis % 10000) / 10000f // 0-1之间的基础进度

    // 计算模拟进度，确保同一个redisKey的进度是递增的
    val progress = minOf(1f, progressBase + (hashCode % 100) / 100f)

    // 模拟网络延迟
    delay(300)

    // 当进度接近完成时，返回文件URL
    val fileUrl = if (progress >= 0.95f) {
        "https://files.example.com/$redisKey/${redisKey.substring(redisKey.lastIndexOf('_') + 1)}.pdf"
    } else {
        ""
    }

    return MockFileUploadResponse(fileUrl, progress)
}

@Composable
fun FilePickerDemo(): Unit {
    var useMock by remember { mutableStateOf(USE_MOCK_FILE_UPLOAD) }

    var selectedFiles by remember { mutableStateOf(emptyList<PickedFile>()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 模拟开关
        Card {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("使用模拟上传：")
                Switch(
                    checked = useMock,
                    onCheckedChange = {
                        useMock = it
                        USE_MOCK_FILE_UPLOAD = it
                    }
                )
            }
        }

        // 文件选择器
        AddMultiFilePicker(
            onFilesSelected = { files ->
                selectedFiles = files
            },
            showUploadManager = true
        )

        // 显示选中的文件信息
        if (selectedFiles.isNotEmpty()) {
            Card {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "最近选择的文件:",
                        style = MaterialTheme.typography.titleMedium
                    )
                    selectedFiles.forEach { file ->
                        Text(
                            text = "• ${file.name} (${formatFileSize(file.size)})",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddMultiFilePicker(
    modifier: Modifier = Modifier,
    onFilesSelected: (List<PickedFile>) -> Unit = {},
    showUploadManager: Boolean = false
) {
    // 选中的文件列表（仅用于显示）
    var selectedFiles by remember { mutableStateOf(emptyList<PickedFile>()) }
    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberFilePickerLauncher(mode = PickerMode.Multiple()) { files ->
        files?.let { platformFiles ->
            // 转换为PickedFile并添加到全局上传管理器
            val pickedFiles = platformFiles.map { file ->
                PickedFile(
                    name = file.name,
                    path = file.path,
                    size = file.getSize(),
                    multiPartFormDataContent = null
                )
            }

            // 更新选中文件列表
            selectedFiles = pickedFiles
            onFilesSelected(pickedFiles)

            // 提交到全局上传管理器
            platformFiles.forEach { file ->
                coroutineScope.launch {
                    try {
                        val content = file.toMultipartFile()
                        GlobalUploadManager.instance.addTask(
                            fileName = file.name,
                            fileSize = file.getSize(),
                            content = content
                        )
                    } catch (e: Exception) {
                        // 处理文件转换错误
                        println("文件转换失败: ${e.message}")
                    }
                }
            }
        }
    }

    Column(modifier = modifier) {
        // 文件选择区域
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clickable { launcher.launch() },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            border = BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = "选择文件",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "点击选择文件上传",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (selectedFiles.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "已选择 ${selectedFiles.size} 个文件",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        // 上传管理器
        if (showUploadManager) {
            Spacer(modifier = Modifier.height(16.dp))
            UploadManagerUI(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * 行内带图标的文本展示
 */
@Composable
private fun RowWithIcon(icon: @Composable () -> Unit, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}

/**
 * 格式化文件大小为可读字符串
 */
fun formatFileSize(size: Long?): String = when {
    size == null -> "未知大小"
    size < 1024 -> "$size B"
    size < 1024 * 1024 -> "${size / 1024} KB"
    size < 1024 * 1024 * 1024 -> "${size / (1024 * 1024)} MB"
    else -> "${size / (1024 * 1024 * 1024)} GB"
}

