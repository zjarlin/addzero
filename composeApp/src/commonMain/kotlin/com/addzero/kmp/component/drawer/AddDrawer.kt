package com.addzero.kmp.component.drawer

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
    // 抽屉尺寸动画
    val drawerWidth by animateDpAsState(
        targetValue = if (visible && (direction == DrawerDirection.RIGHT || direction == DrawerDirection.LEFT))
            (LocalDensity.current.density * width).dp else 0.dp,
        label = "drawerWidth"
    )

    val drawerHeight by animateDpAsState(
        targetValue = if (visible && (direction == DrawerDirection.TOP || direction == DrawerDirection.BOTTOM))
            (LocalDensity.current.density * height).dp else 0.dp,
        label = "drawerHeight"
    )

    // 只有在可见时才渲染内容，避免自动显示问题
    if (!visible) return
    // 遮罩层
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
            .clickable(onClick = onClose)
            .zIndex(10f)
    )

    // 抽屉内容
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(11f),
        contentAlignment = when (direction) {
            DrawerDirection.RIGHT -> Alignment.CenterEnd
            DrawerDirection.LEFT -> Alignment.CenterStart
            DrawerDirection.TOP -> Alignment.TopCenter
            DrawerDirection.BOTTOM -> Alignment.BottomCenter
        }
    ) {
        Surface(
            modifier = Modifier
                .let {
                    when (direction) {
                        DrawerDirection.RIGHT, DrawerDirection.LEFT ->
                            it.fillMaxHeight().width(drawerWidth)

                        DrawerDirection.TOP, DrawerDirection.BOTTOM ->
                            it.fillMaxWidth().height(drawerHeight)
                    }
                }
                .shadow(elevation = 8.dp),
            color = backgroundColor
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 抽屉标题栏
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "关闭",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                // 表单内容，使用可滚动的Box作为容器
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 1.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        content = content
                    )
                }

                // 底部按钮区域
                if (showButtons) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                            TextButton(onClick = onClose) {
                                Text(cancelText)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = onSubmit,
                                enabled = confirmEnabled
                            ) {
                                Text(confirmText)
                            }
                    }
                }
            }
        }
    }
}
