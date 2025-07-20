package com.addzero.kmp.component.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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

    // 🎨 设置消息类型对应的图标、颜色和渐变
    val (icon, primaryColor, secondaryColor) = when (type) {
        MessageType.SUCCESS -> Triple(
            Icons.Default.CheckCircle,
            Color(0xFF4CAF50), // 绿色
            Color(0xFF81C784)  // 浅绿色
        )

        MessageType.ERROR -> Triple(
            Icons.Default.Error,
            Color(0xFFF44336), // 红色
            Color(0xFFE57373)  // 浅红色
        )

        MessageType.WARNING -> Triple(
            Icons.Default.Warning,
            Color(0xFFFF9800), // 橙色
            Color(0xFFFFB74D)  // 浅橙色
        )

        MessageType.INFO -> Triple(
            Icons.Default.Info,
            Color(0xFF2196F3), // 蓝色
            Color(0xFF64B5F6)  // 浅蓝色
        )
    }

    // 🎯 移除动画，提升性能

    AnimatedVisibility(
        visible = visible && message != null,
        enter = fadeIn(tween(150)), // 🎯 简化动画，提升性能
        exit = fadeOut(tween(150))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp), // 🎨 添加水平边距，确保不会贴边
            contentAlignment = Alignment.TopCenter
        ) {
            message?.let {
                BeautifulMessageCard(
                    message = it,
                    icon = icon,
                    primaryColor = primaryColor,
                    secondaryColor = secondaryColor
                )
            }
        }
    }
}

/**
 * 🎨 美化的消息卡片（高性能版本）
 */
@Composable
private fun BeautifulMessageCard(
    message: String,
    icon: ImageVector,
    primaryColor: Color,
    secondaryColor: Color
) {
    // 🌈 创建静态渐变背景，提升性能
    val gradientBrush = Brush.linearGradient(
        colors = listOf(
            primaryColor.copy(alpha = 0.9f),
            secondaryColor.copy(alpha = 0.7f),
            primaryColor.copy(alpha = 0.8f)
        )
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(top = 16.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = primaryColor.copy(alpha = 0.3f),
                spotColor = primaryColor.copy(alpha = 0.5f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(gradientBrush)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 🎨 简洁的图标容器（高性能）
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Color.White.copy(alpha = 0.2f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = "消息图标",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // 🎨 美化的文字
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            // 🌟 添加装饰性光晕效果
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color.White.copy(alpha = 0.6f))
                    .align(Alignment.TopEnd)
            )
        }
    }
}
