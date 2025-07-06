package com.addzero.kmp.component.high_level

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.addzero.kmp.component.button.AddLoadingButton
import kotlinx.coroutines.launch

/**
 * 美观大方的表单容器组件
 * 以 Dialog 形式置顶显示
 *
 * @param visible 是否显示表单
 * @param onClose 关闭回调
 * @param onSubmit 提交回调
 * @param title 表单标题
 * @param submitText 提交按钮文本
 * @param cancelText 取消按钮文本
 * @param submitIcon 提交按钮图标
 * @param modifier 修饰符
 * @param content 表单内容
 */
@Composable
fun AddFormContainer(
    visible: Boolean,
    onClose: () -> Unit,
    onSubmit: suspend () -> Unit,
    confirmEnabled: Boolean = true,
    title: String = "表单",
    submitText: String = "提交",
    cancelText: String = "取消",
    submitIcon: ImageVector = Icons.Default.Save,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit

) {
    if (!visible) return

    Dialog(
        onDismissRequest = onClose,
        properties = DialogProperties(
            usePlatformDefaultWidth = false, // Allow custom width
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        var isLoading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        Card(
            modifier = modifier
                .fillMaxWidth(0.9f) // Use 90% of screen width
                .fillMaxHeight(0.85f), // Use 85% of screen height
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth() // Use 90% of screen width

            ) {
                // Header with Title and Close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 12.dp, top = 16.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close dialog")
                    }
                }

                // Divider
                HorizontalDivider(modifier = Modifier.padding(horizontal = 24.dp))

                // Content area
                Column(
                    modifier = Modifier
                        .weight(1f) // Occupy remaining space
                        .fillMaxWidth()
                ) {
                        content()
                }

                // Footer with buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Text(cancelText)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    AddLoadingButton(
                        enabled = confirmEnabled,
                        text = submitText,
                        onClick = {
                            if (!isLoading) {
                                isLoading = true
                                scope.launch {
                                    try {
                                        onSubmit()
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        icon = submitIcon,
                        loading = isLoading
                    )
                }
            }
        }
    }
}

// 假设 AddLoadingButton 已经存在