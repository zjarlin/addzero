package site.addzero.youtrack.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * YouTrack 风格的顶部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouTrackTopBar(
    currentTitle: String = "AddZero Admin",
    onMenuClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit,
    onThemeToggle: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = currentTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "菜单",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            // 主题切换按钮
            ThemeToggleButton(onClick = onThemeToggle)

            // 搜索按钮
            IconButton(onClick = onSearchClick) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "搜索",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 通知按钮
            IconButton(onClick = onNotificationClick) {
                Box {
                    Icon(
                        Icons.Default.NotificationsNone,
                        contentDescription = "通知",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // 通知数量徽章
                    Badge(
                        modifier = Modifier
                            .offset(x = (-4).dp, y = (-4).dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Text(
                            "3",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }

            // 用户头像按钮
            IconButton(onClick = onProfileClick) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = "用户中心",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = modifier
            .shadow(
                elevation = 2.dp,
                spotColor = MaterialTheme.colorScheme.primary
            )
    )
}

/**
 * 主题切换按钮组件
 */
@Composable
private fun ThemeToggleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 获取当前主题的背景色来判断是否为暗色主题
    // 简单判断：如果背景色的红色分量很小，则认为是暗色主题
    val currentBgColor = MaterialTheme.colorScheme.background
    val isActuallyDarkTheme = currentBgColor.red < 0.3f

    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (isActuallyDarkTheme) {
                Icons.Default.LightMode
            } else {
                Icons.Default.DarkMode
            },
            contentDescription = if (isActuallyDarkTheme) "切换到浅色主题" else "切换到深色主题",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}