package com.addzero.kmp.component.file_picker

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

// 暂时注释掉 FileKit 相关导入，等后续完善
// import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
// import io.github.vinceglb.filekit.core.PickerMode
// import io.github.vinceglb.filekit.core.PlatformFiles

// 临时类型定义
typealias PlatformFiles = List<String>

/**
 * 🎯 文件上传组件
 *
 * 基于 FileKit 的文件上传组件，提供：
 * - 文件选择和上传
 * - 上传进度显示
 * - 文件预览
 * - 多文件支持
 *
 * @param onFilesSelected 文件选择回调
 * @param modifier 修饰符
 * @param allowMultiple 是否允许多选
 * @param acceptedTypes 接受的文件类型
 * @param maxFileSize 最大文件大小（字节）
 * @param uploadProgress 上传进度（0.0-1.0）
 * @param isUploading 是否正在上传
 * @param enabled 是否启用
 */
@Composable
fun AddFileUploader(
    onFilesSelected: (PlatformFiles) -> Unit,
    modifier: Modifier = Modifier,
    allowMultiple: Boolean = false,
    acceptedTypes: List<String> = emptyList(),
    maxFileSize: Long = 10 * 1024 * 1024, // 10MB
    uploadProgress: Float = 0f,
    isUploading: Boolean = false,
    enabled: Boolean = true
) {
    // 暂时使用简化版本，等后续完善 FileKit 集成
    // val filePickerLauncher = rememberFilePickerLauncher(...)

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 简化的上传区域
            if (isUploading) {
                CircularProgressIndicator(
                    progress = { uploadProgress },
                    modifier = Modifier.size(48.dp),
                )
                Text(
                    text = "上传中... ${(uploadProgress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "点击选择文件",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                if (acceptedTypes.isNotEmpty()) {
                    Text(
                        text = "支持格式: ${acceptedTypes.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text(
                    text = "最大文件大小: ${formatFileSize(maxFileSize)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // 选择文件按钮
            Button(
                onClick = {
                    // 暂时模拟文件选择
                    onFilesSelected(listOf("示例文件.txt"))
                },
                enabled = enabled && !isUploading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = if (allowMultiple) "选择多个文件" else "选择文件")
            }
        }
    }
}

/**
 * 🎯 简单文件选择器
 *
 * 只提供文件选择功能，不包含上传 UI
 */
@Composable
fun AddFilePicker(
    onFileSelected: (PlatformFiles) -> Unit,
    modifier: Modifier = Modifier,
    allowMultiple: Boolean = false,
    acceptedTypes: List<String> = emptyList(),
    buttonText: String = "选择文件",
    enabled: Boolean = true
) {
    // 暂时使用简化版本
    OutlinedButton(
        onClick = {
            // 暂时模拟文件选择
            onFileSelected(listOf("示例文件.txt"))
        },
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text = buttonText)
    }
}

/**
 * 格式化文件大小
 */
fun formatFileSize(bytes: Long): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var size = bytes.toDouble()
    var unitIndex = 0

    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }

    return "${(size * 10).toInt() / 10.0} ${units[unitIndex]}"
}

/**
 * 文件上传状态
 */
data class FileUploadState(
    val isUploading: Boolean = false,
    val progress: Float = 0f,
    val error: String? = null,
    val uploadedFiles: List<UploadedFile> = emptyList()
)

/**
 * 已上传文件信息
 */
data class UploadedFile(
    val name: String,
    val size: Long,
    val url: String,
    val type: String
)
