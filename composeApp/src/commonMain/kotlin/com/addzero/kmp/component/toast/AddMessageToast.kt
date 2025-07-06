package com.addzero.kmp.component.toast

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * 消息提示组件
 *
 * @param message 消息内容，为null时不显示
 * @param type 消息类型，影响显示的图标和颜色
 * @param duration 显示持续时间(毫秒)，默认3000ms
 * @param onDismiss 消息关闭后的回调
 */
@Composable
fun AddMessageToast(
    message: String?,
    type: MessageType = MessageType.INFO,
    duration: Long = 3000,
    onDismiss: () -> Unit = {}
) {
    // 控制组件可见性
    var visible by remember { mutableStateOf(false) }

    // 当消息不为空时显示提示
    LaunchedEffect(message) {
        if (message != null) {
            visible = true
            // 延迟后自动关闭
            delay(duration)
            visible = false
            onDismiss()
        }
    }

    // 设置消息类型对应的图标和颜色
    val (icon, backgroundColor) = when (type) {
        MessageType.SUCCESS -> Icons.Default.Check to MaterialTheme.colorScheme.primaryContainer
        MessageType.ERROR -> Icons.Default.Error to MaterialTheme.colorScheme.errorContainer
        MessageType.WARNING -> Icons.Default.Warning to MaterialTheme.colorScheme.tertiaryContainer
        MessageType.INFO -> Icons.Default.Check to MaterialTheme.colorScheme.surfaceVariant
    }

    AnimatedVisibility(
        visible = visible && message != null,
        enter = fadeIn(tween(200)) + slideInVertically(tween(200)) { -it },
        exit = fadeOut(tween(200)) + slideOutVertically(tween(200)) { -it }
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            message?.let {
                MessageCard(
                    message = it,
                    icon = icon,
                    backgroundColor = backgroundColor
                )
            }
        }
    }
}

/**
 * 消息卡片
 */
@Composable
private fun MessageCard(
    message: String,
    icon: ImageVector,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(top = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = "消息图标",
                    tint = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
