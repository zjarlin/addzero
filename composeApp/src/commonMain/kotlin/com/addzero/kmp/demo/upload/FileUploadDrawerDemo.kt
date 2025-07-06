package com.addzero.kmp.demo.upload

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.addzero.kmp.component.file_picker.AddFileUploadDrawer
import com.addzero.kmp.processor.annotation.Route

/**
 * 文件上传抽屉演示组件
 */
@Composable
@Route("组件示例", title = "文件上传抽屉")
fun FileUploadDrawerDemo() {
    var showDrawer by remember { mutableStateOf(false) }

    Button(
        onClick = { showDrawer = true }
    ) {
        Text("打开文件上传抽屉")
    }



    AddFileUploadDrawer(
        visible = showDrawer,
        title = "Excel文件上传",
        onClose = { showDrawer = false },
        onUpload = { files ->
            println("文件上传: ${files?.size ?: 0} 个文件")
            showDrawer = false
            true
        },
        onDownloadTemplate = {
            // 这里应该实现模板下载逻辑
            println("下载模板")
        },
        description = "请上传Excel文件(.xlsx)，文件大小不超过5MB，支持批量上传。\n请确保使用正确的模板格式，否则可能导致导入失败。",
        showDescription = true,
        acceptedFileTypes = listOf("xlsx"),
        maxFileSize = 5
    )

}