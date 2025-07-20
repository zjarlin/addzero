package com.addzero.kmp.screens.ai

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import io.gitee.zjarlin.composeapp.generated.resources.Res
import io.gitee.zjarlin.composeapp.generated.resources.sleep
import org.jetbrains.compose.resources.painterResource

// Labubu风格的输入区域
@Composable
fun LabubuInputArea(
    input: String, onInputChange: (String) -> Unit, onSend: () -> Unit, enabled: Boolean
) {
    val focusRequester = remember { FocusRequester() }
    // 发送按钮的脉冲动画
    val pulseAnimation by rememberInfiniteTransition(label = "pulse").animateFloat(
        initialValue = 1f, targetValue = if (enabled) 1.05f else 1f, animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine), repeatMode = RepeatMode.Reverse
        ), label = "pulse"
    )

    Column {
        // 可爱的分割线
        Box(
            modifier = Modifier.Companion.fillMaxWidth().height(1.dp).background(
                Brush.Companion.horizontalGradient(
                    colors = listOf(
                        Color.Companion.Transparent,
                        LabubuColors.PrimaryPink.copy(alpha = 0.3f),
                        Color.Companion.Transparent
                    )
                )
            )
        )

        Row(
            modifier = Modifier.Companion.fillMaxWidth().background(Color.Companion.White).padding(16.dp),
            verticalAlignment = Alignment.Companion.Bottom
        ) {
            // 输入框 - 支持回车发送
            OutlinedTextField(
                value = input,
                onValueChange = onInputChange,
                modifier = Modifier.weight(1f).focusRequester(focusRequester).onPreviewKeyEvent { keyEvent ->
                    // 使用onPreviewKeyEvent来优先处理键盘事件
                    if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Enter) {
                        val hasShift = keyEvent.isShiftPressed
                        val hasCtrl = keyEvent.isCtrlPressed
                        val hasAlt = keyEvent.isAltPressed

                        if (hasShift || hasCtrl || hasAlt) {
                            // Shift, Ctrl, or Alt + Enter pressed, manually insert newline
                            onInputChange(input + "\n")
                            true // Consume event
                        } else {
                            // Only Enter key pressed, send message
                            if (enabled && input.isNotBlank()) {
                                onSend()
                            }
                            true // Consume event to prevent newline
                        }
                    } else {
                        false // Not an Enter key event, or not KeyDown, let it propagate
                    }
                },
                placeholder = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.ChatBubbleOutline,
                            contentDescription = null,
                            tint = LabubuColors.LightText,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "说点什么吧...", color = LabubuColors.LightText
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LabubuColors.PrimaryPink,
                    unfocusedBorderColor = LabubuColors.PrimaryPink.copy(alpha = 0.3f),
                    focusedLabelColor = LabubuColors.PrimaryPink,
                    cursorColor = LabubuColors.PrimaryPink
                ),

                maxLines = 999
            )

            Spacer(modifier = Modifier.Companion.width(12.dp))

            // 可爱的发送按钮
            FloatingActionButton(
                onClick = onSend,
                modifier = Modifier.Companion.size(56.dp).scale(pulseAnimation),
                containerColor = if (enabled) LabubuColors.PrimaryPink else Color.Transparent,
                contentColor = if (enabled) Color.White else LabubuColors.LightText,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = if (enabled) 6.dp else 2.dp
                )
            ) {
                AnimatedContent(
                    targetState = enabled, transitionSpec = {
                        scaleIn() + fadeIn() togetherWith scaleOut() + fadeOut()
                    }, label = "sendIcon"
                ) { isEnabled ->
                    if (isEnabled) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "发送",
                            modifier = Modifier.Companion.size(24.dp)
                        )
                    } else {

                        Image(
                            painter = painterResource(Res.drawable.sleep), contentDescription = "等待输入"
                        )

//                        Icon(
//                            Icons.Default.Bedtime,
//                            contentDescription = "等待输入",
//                            modifier = Modifier.size(24.dp)
//                        )
                    }
                }
            }
        }

        // 底部可爱装饰
        Row(
            modifier = Modifier.Companion.fillMaxWidth().background(Color.Companion.White)
                .padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier.Companion.size(4.dp).background(
                        LabubuColors.PrimaryPink.copy(
                            alpha = if (index == 2) 0.8f else 0.3f
                        ), CircleShape
                    )
                )
                if (index < 4) {
                    Spacer(modifier = Modifier.Companion.width(4.dp))
                }
            }
        }
    }
}