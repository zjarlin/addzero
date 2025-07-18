package com.addzero.kmp.component.drawer

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

/**
 * 抽屉方向枚举
 */
enum class DrawerDirection {
    RIGHT, // 从右侧滑出
    TOP,   // 从顶部滑出
    LEFT,  // 从左侧滑出
    BOTTOM // 从底部滑出
}

/**
 * 通用抽屉组件
 * 可用于添加或编辑各种数据，支持多方向滑出
 *
 * @param visible 是否显示抽屉
 * @param title 抽屉标题
 * @param onClose 关闭回调
 * @param onSubmit 确认回调
 * @param confirmEnabled 确认按钮是否启用
 * @param confirmText 确认按钮文本
 * @param cancelText 取消按钮文本
 * @param direction 抽屉滑出方向
 * @param width 抽屉宽度，仅当方向为左/右时有效
 * @param height 抽屉高度，仅当方向为上/下时有效
 * @param showButtons 是否显示底部按钮区域
 * @param content 抽屉内容
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddDrawer(
    visible: Boolean,
    title: String,
    onClose: () -> Unit,
    onSubmit: () -> Unit,
    confirmEnabled: Boolean = true,
    confirmText: String = "提交",
    cancelText: String = "取消",
    direction: DrawerDirection = DrawerDirection.RIGHT,
    width: Int = 888,
    height: Int = 400,
    showButtons: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(10f)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) { onClose() },
            contentAlignment = when (direction) {
                DrawerDirection.RIGHT -> Alignment.CenterEnd
                DrawerDirection.LEFT -> Alignment.CenterStart
                DrawerDirection.TOP -> Alignment.TopCenter
                DrawerDirection.BOTTOM -> Alignment.BottomCenter
            }
        ) {
            // 使用 Surface 作为抽屉容器
            Surface(
                modifier = Modifier
                    .clickable( // 拦截点击事件，防止穿透到 Box
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {}
                    .animateEnterExit(
                        enter = when (direction) {
                            DrawerDirection.RIGHT -> slideInHorizontally(animationSpec = tween(300)) { it }
                            DrawerDirection.LEFT -> slideInHorizontally(animationSpec = tween(300)) { -it }
                            DrawerDirection.TOP -> slideInVertically(animationSpec = tween(300)) { -it }
                            DrawerDirection.BOTTOM -> slideInVertically(animationSpec = tween(300)) { it }
                        },
                        exit = when (direction) {
                            DrawerDirection.RIGHT -> slideOutHorizontally(animationSpec = tween(300)) { it }
                            DrawerDirection.LEFT -> slideOutHorizontally(animationSpec = tween(300)) { -it }
                            DrawerDirection.TOP -> slideOutVertically(animationSpec = tween(300)) { -it }
                            DrawerDirection.BOTTOM -> slideOutVertically(animationSpec = tween(300)) { it }
                        }
                    )
                    .let {
                        when (direction) {
                            DrawerDirection.RIGHT, DrawerDirection.LEFT ->
                                it
                                    .fillMaxHeight()
                                    .width(width.dp)

                            DrawerDirection.TOP, DrawerDirection.BOTTOM ->
                                it
                                    .fillMaxWidth()
                                    .height(height.dp)
                        }
                    }
                    .shadow(
                        elevation = 12.dp,
                        shape = when (direction) {
                            DrawerDirection.RIGHT -> RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
                            DrawerDirection.LEFT -> RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                            DrawerDirection.TOP -> RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                            DrawerDirection.BOTTOM -> RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        },
                        clip = false
                    )
                    .clip(
                        when (direction) {
                            DrawerDirection.RIGHT -> RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)
                            DrawerDirection.LEFT -> RoundedCornerShape(topEnd = 24.dp, bottomEnd = 24.dp)
                            DrawerDirection.TOP -> RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                            DrawerDirection.BOTTOM -> RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                        }
                    ),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 美化的标题栏
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFF1976D2), // 固定的深蓝色标题栏
                                shape = when (direction) {
                                    DrawerDirection.RIGHT -> RoundedCornerShape(topStart = 20.dp)
                                    DrawerDirection.LEFT -> RoundedCornerShape(topEnd = 20.dp)
                                    DrawerDirection.TOP -> RoundedCornerShape(
                                        bottomStart = 20.dp,
                                        bottomEnd = 20.dp
                                    )

                                    DrawerDirection.BOTTOM -> RoundedCornerShape(
                                        topStart = 20.dp,
                                        topEnd = 20.dp
                                    )
                                }
                            )
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )

                        IconButton(
                            onClick = onClose,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.1f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "关闭",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 美化的内容区域
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                    ) {
                        content()
                    }

                    // 美化的底部按钮区域
                    if (showButtons) {
                        // 分割线
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            color = Color(0xFFE0E0E0)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 20.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = onClose,
                                modifier = Modifier
                                    .height(48.dp)
                                    .widthIn(min = 100.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF1976D2)
                                ),
                                border = BorderStroke(1.dp, Color(0xFF1976D2))
                            ) {
                                Text(
                                    cancelText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Button(
                                onClick = onSubmit,
                                enabled = confirmEnabled,
                                modifier = Modifier
                                    .height(48.dp)
                                    .widthIn(min = 100.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1976D2),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    confirmText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
