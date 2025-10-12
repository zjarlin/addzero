package site.addzero.component.sidebar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import site.addzero.themes.colors
import site.addzero.component.Button
import site.addzero.component.ButtonSize
import site.addzero.component.ButtonVariant
import site.addzero.themes.radius

/**
 * 主侧边栏容器
 */
@Composable
fun Sidebar(
    modifier: Modifier = Modifier,
    sidebarWidth: Dp = 256.dp,
    mobileWidthMobile: Dp = 288.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    val sidebarState = LocalSidebarState.current
    val width = if (sidebarState.isMobile) {
        mobileWidthMobile
    } else {
        sidebarWidth
    }
    Column (
        modifier = modifier
            .fillMaxHeight()
            .width(width)
            .background(
                color = MaterialTheme.colors.sidebar,
                shape = if (sidebarState.isMobile) RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp) else RoundedCornerShape(0.dp)
            )
    ) {
        content()
    }
}

/**
 * 侧边栏触发按钮（汉堡菜单）
 */
@Composable
fun SidebarTrigger(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {
        Icon(
            Icons.Default.Menu,
            contentDescription = "切换侧边栏",
            tint = MaterialTheme.colors.sidebarForeground
        )
    }
) {
    val sidebarState = LocalSidebarState.current

    // 在移动端始终显示触发按钮，在桌面端当侧边栏可关闭时显示
    Button(
        onClick = sidebarState.toggleSidebar,
        modifier = modifier,
        size = ButtonSize.Icon,
        variant = ButtonVariant.Ghost
    ) {
        content()
    }
}

/**
 * 适应侧边栏的主内容包装器
 */
@Composable
fun SidebarInset(
    modifier: Modifier = Modifier,
    sidebarContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val sidebarState = LocalSidebarState.current

    if (sidebarState.isMobile) {
        // 移动端：侧边栏以遮罩形式覆盖内容
        Box(modifier = modifier.fillMaxSize()) {
            // 主内容占据全部空间
            content()

            // 侧边栏打开时的背景遮罩
            if (sidebarState.isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { sidebarState.closeSidebar() }
                        .zIndex(1f)
                )
            }

            // 动画侧边栏遮罩
            AnimatedVisibility(
                visible = sidebarState.isOpen,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                modifier = Modifier.zIndex(2f)
            ) {
                Sidebar {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(280.dp)
                            .background(
                                color = MaterialTheme.colors.sidebar,
                                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
                            )
                    ) {
                        sidebarContent()
                    }
                }
            }
        }
    } else {
        // 桌面端：根据侧边栏存在情况调整内容区域
        Box(
            modifier = modifier
                .fillMaxSize()
                .then(if (sidebarState.isOpen) Modifier.padding(start = 16.dp) else Modifier)
        ) {
            content()
        }
    }
}

/**
 * 完整的侧边栏布局包装器
 */
@Composable
fun SidebarLayout(
    modifier: Modifier = Modifier,
    sidebarHeader: @Composable (() -> Unit)? = null,
    sidebarContent: @Composable (() -> Unit)? = null,
    sidebarFooter: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val sidebarState = LocalSidebarState.current

    if (sidebarState.isMobile) {
        // 移动端布局：遮罩覆盖
        Box(modifier = modifier.fillMaxSize()) {
            // 主内容
            content()

            // 背景遮罩
            if (sidebarState.isOpen) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { sidebarState.closeSidebar() }
                        .zIndex(1f)
                )
            }

            // 动画侧边栏
            AnimatedVisibility(
                visible = sidebarState.isOpen,
                enter = slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ),
                modifier = Modifier.zIndex(2f)
            ) {
                Sidebar {
                    sidebarHeader?.invoke()
                    Box(modifier = Modifier.weight(1f)) {
                        sidebarContent?.invoke()
                    }
                    sidebarFooter?.invoke()
                }
            }
        }
    } else {
        // 桌面端布局：并排显示，侧边栏可以隐藏
        Row(modifier = modifier.fillMaxSize()) {
            // 侧边栏 - 仅在打开时显示
            AnimatedVisibility(
                visible = sidebarState.isOpen,
            ) {
                Sidebar {
                    sidebarHeader?.invoke()
                    Box(modifier = Modifier.weight(1f)) {
                        sidebarContent?.invoke()
                    }
                    sidebarFooter?.invoke()
                }
            }

            // 主内容 - 占据剩余空间
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                content()
            }
        }
    }
}

/**
 * 侧边栏内容组件
 */
@Composable
fun SidebarContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        content()
    }
}

@Composable
fun SidebarHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column (
        modifier = modifier
            .defaultMinSize(minHeight = 64.dp)
            .fillMaxWidth()
            .padding(top = 24.dp, start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        content()
    }
}

@Composable
fun SidebarFooter(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        content()
    }
}

@Composable
fun SidebarGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        content()
    }
}

@Composable
fun SidebarGroupLabel(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.padding(horizontal = 12.dp, vertical = 4.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.sidebarForeground.copy(alpha = 0.7f)
    )
}

@Composable
fun SidebarGroupContent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun SidebarMenu(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun SidebarMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.radius.md))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        content()
    }
}

@Composable
fun SidebarMenuButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false
) {
    val sidebarState = LocalSidebarState.current
    val colors = MaterialTheme.colors
    val backgroundColor = if (isActive) colors.sidebarAccent else Color.Unspecified
    val textColor = if (isActive) colors.sidebarAccentForeground else colors.sidebarForeground

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MaterialTheme.radius.md))
            .background(backgroundColor)
            .clickable {
                onClick()
                // 移动端菜单选择后自动关闭侧边栏
                if (sidebarState.isMobile) {
                    sidebarState.closeSidebar()
                }
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
