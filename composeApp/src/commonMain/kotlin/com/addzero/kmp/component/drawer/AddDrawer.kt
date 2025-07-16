package com.addzero.kmp.component.drawer

import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
 * @param backgroundColor 抽屉背景色
 * @param showButtons 是否显示底部按钮区域
 * @param content 抽屉内容
 */
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
    width: Int = 360,
    height: Int = 400,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    showButtons: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    // 抽屉尺寸动画 - 添加缓动效果
    val drawerWidth by animateDpAsState(
        targetValue = if (visible && (direction == DrawerDirection.RIGHT || direction == DrawerDirection.LEFT))
            (LocalDensity.current.density * width).dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "drawerWidth"
    )

    val drawerHeight by animateDpAsState(
        targetValue = if (visible && (direction == DrawerDirection.TOP || direction == DrawerDirection.BOTTOM))
            (LocalDensity.current.density * height).dp else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = "drawerHeight"
    )

    // 只有在可见时才渲染内容，避免自动显示问题
    if (!visible) return
    // 美化的遮罩层
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.4f)) // 更深的遮罩效果
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClose() }
            .zIndex(10f)
    )

    // 抽屉内容
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(11f),
        contentAlignment = when (direction) {
            DrawerDirection.RIGHT -> Alignment.BottomEnd
            DrawerDirection.LEFT -> Alignment.BottomStart
            DrawerDirection.TOP -> Alignment.TopCenter
            DrawerDirection.BOTTOM -> Alignment.BottomCenter
        }
    ) {
        // 美化的抽屉容器
        Card(
            modifier = Modifier
                .let {
                    when (direction) {
                        DrawerDirection.RIGHT, DrawerDirection.LEFT ->
                            it.fillMaxHeight().width(drawerWidth)

                        DrawerDirection.TOP, DrawerDirection.BOTTOM ->
                            it.fillMaxWidth().height(drawerHeight)
                    }
                }
                .shadow(
                    elevation = 16.dp,
                    shape = when (direction) {
                        DrawerDirection.RIGHT -> RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                        DrawerDirection.LEFT -> RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                        DrawerDirection.TOP -> RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                        DrawerDirection.BOTTOM -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    }
                ),
            shape = when (direction) {
                DrawerDirection.RIGHT -> RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                DrawerDirection.LEFT -> RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
                DrawerDirection.TOP -> RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                DrawerDirection.BOTTOM -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            },
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFAFAFA) // 固定的现代化背景色
            ),
            border = BorderStroke(
                width = 1.dp,
                color = Color(0xFFE0E0E0)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 16.dp
            )
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
                                DrawerDirection.TOP -> RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                                DrawerDirection.BOTTOM -> RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    content = content
                )

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
