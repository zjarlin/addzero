package com.addzero.kmp.component.file_picker

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.AddInputDescribe
import com.addzero.kmp.component.drawer.AddDrawer
import com.addzero.kmp.component.drawer.DrawerDirection
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PlatformFiles

/**
 * 文件上传状态
 */
enum class FileUploadStatus {
    IDLE,       // 空闲状态
    UPLOADING,  // 上传中
    SUCCESS,    // 上传成功
    FAILED      // 上传失败
}

/**
 * 需要引入依赖 implementation("io.github.vinceglb:filekit-compose:+")
 * 文件上传抽屉组件
 * 集成文件选择器功能，支持下载模板和填写说明
 *
 * @param visible 是否显示抽屉
 * @param title 抽屉标题
 * @param onClose 关闭回调
 * @param onUpload 上传回调，返回上传是否成功
 * @param onDownloadTemplate 下载模板回调
 * @param uploadStatus 上传状态
 * @param description 上传说明文本
 * @param showDescription 是否显示说明
 * @param acceptedFileTypes 接受的文件类型列表，例如 ["xlsx", "csv"]
 * @param maxFileSize 最大文件大小（MB），默认10MB
 * @param direction 抽屉方向
 * @param width 抽屉宽度
 */
@Composable
fun AddFileUploadDrawer(
    visible: Boolean,
    title: String = "文件上传",
    onClose: () -> Unit,
    onUpload: (PlatformFiles?) -> Unit,
    onDownloadTemplate: () -> Unit = {},
    uploadStatus: FileUploadStatus = FileUploadStatus.IDLE,
    description: String = "请上传符合要求的文件，支持批量上传",
    showDescription: Boolean = true,
    acceptedFileTypes: List<String> = listOf("xlsx", "csv"),
    maxFileSize: Int = 10, // 默认10MB
    direction: DrawerDirection = DrawerDirection.RIGHT,
    width: Int = 400
) {
    // 已选择的文件列表
    var selectedFiles by remember { mutableStateOf<PlatformFiles?>(null) }

    // 上传按钮是否启用
    val uploadEnabled =
        selectedFiles != null && selectedFiles?.isNotEmpty() == true && uploadStatus != FileUploadStatus.UPLOADING

    // 文件选择器
    val filePicker = rememberFilePickerLauncher(mode = PickerMode.Multiple()) { files ->
        selectedFiles = files
    }

    // 文件类型显示文本
    val fileTypesText = acceptedFileTypes.joinToString(", ")

    // 状态消息
    val statusMessage = when (uploadStatus) {
        FileUploadStatus.UPLOADING -> "上传中..."
        FileUploadStatus.SUCCESS -> "上传成功！"
        FileUploadStatus.FAILED -> "上传失败，请重试"
        else -> ""
    }

    // 状态颜色
    val statusColor = when (uploadStatus) {
        FileUploadStatus.SUCCESS -> MaterialTheme.colorScheme.primary
        FileUploadStatus.FAILED -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    AddDrawer(
        visible = visible,
        title = title,
        onClose = {
            selectedFiles = null
            onClose()
        },
        onSubmit = {
            if (uploadEnabled) {
                onUpload(selectedFiles)
            }
        },
        confirmEnabled = uploadEnabled,
        confirmText = "上传",
        cancelText = "取消",
        direction = direction,
        width = width,
        showButtons = true
    ) {
        // 使用可滚动容器包裹内容，避免无限高度约束问题
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 说明区域
            AddInputDescribe(showFlag = showDescription, describe = description)

            // 模板下载区域
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "下载模板填写后上传",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Button(
                        onClick = onDownloadTemplate,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "下载模板",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("下载模板")
                    }
                }
            }

            // 文件上传区域
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { filePicker.launch() },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "上传文件",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "点击选择文件上传",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "支持的文件类型: $fileTypesText，最大 ${maxFileSize}MB",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // 已选择文件列表
            if (!selectedFiles.isNullOrEmpty()) {
                Text(
                    text = "已选择 ${selectedFiles?.size ?: 0} 个文件",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedFiles?.forEach { file ->
                        file.getSize()?.let {
                            FileItem(
                                fileName = file.name,
                                fileSize = formatFileSize(it),
                                onDelete = {
                                    selectedFiles = selectedFiles?.filter { it != file }
                                }
                            )
                        }
                    }
                }
            }

            // 状态消息
            if (statusMessage.isNotEmpty()) {
                Text(
                    text = statusMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = statusColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
            }
        }
    }
}

/**
 * 文件项组件
 */
@Composable
private fun FileItem(
    fileName: String,
    fileSize: String,
    onDelete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilePresent,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = fileName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = fileSize,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "删除",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


