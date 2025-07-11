package com.addzero.kmp.ui.infra.responsive

// getMenuIcon 函数需要在这里重新定义或导入
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.compose.icons.IconMap
import com.addzero.kmp.entity.sys.menu.EnumSysMenuType
import com.addzero.kmp.entity.sys.menu.SysMenuVO
import com.addzero.kmp.kt_util.isNotBlank
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
import com.addzero.kmp.ui.infra.theme.AppThemeType
import com.addzero.kmp.ui.infra.theme.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * 🚀 顶部导航栏组件（移动端专用）
 *
 * 在移动端显示水平滚动的菜单项
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    modifier: Modifier = Modifier,
    config: ResponsiveConfig
) {
    val themeViewModel = koinViewModel<ThemeViewModel>()

    val currentTheme = themeViewModel.currentTheme
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        // 主导航栏
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(config.topbarHeight),
            color = when (currentTheme) {
                AppThemeType.GRADIENT_RAINBOW,
                AppThemeType.GRADIENT_SUNSET,
                AppThemeType.GRADIENT_OCEAN,
                AppThemeType.GRADIENT_FOREST,
                AppThemeType.GRADIENT_AURORA,
                AppThemeType.GRADIENT_NEON -> Color.Transparent

                else -> MaterialTheme.colorScheme.surface
            },
            tonalElevation = if (currentTheme.isGradient()) 0.dp else 2.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 展开/收起按钮
                AddIconButton(
                    text = if (isExpanded) "收起菜单" else "展开菜单",
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // 当前路由显示
                CurrentRouteIndicator()

                Spacer(modifier = Modifier.weight(1f))

                // 快速导航菜单（水平滚动）
                if (!isExpanded) {
                    QuickNavigationMenu()
                }
            }
        }

        // 展开的菜单（垂直布局）
        AnimatedVisibility(
            visible = isExpanded,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut()
        ) {
            ExpandedNavigationMenu(
                onMenuItemClick = { isExpanded = false }
            )
        }
    }
}

/**
 * 🎯 当前路由指示器
 */
@Composable
private fun CurrentRouteIndicator() {
    val currentRoute = MenuViewModel.currentRoute
    val currentMenu = MenuViewModel.getRouteByKey(currentRoute)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        val icon = currentMenu?.let { getMenuIcon(it) }
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        Text(
            text = currentMenu?.title ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 🚀 快速导航菜单（水平滚动）
 */
@Composable
private fun QuickNavigationMenu() {
    val topLevelMenus = MenuViewModel.menuItems.take(5) // 只显示前5个顶级菜单

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(topLevelMenus) { menu ->
            QuickNavigationItem(menu = menu)
        }
    }
}

/**
 * 🎯 快速导航项
 */
@Composable
private fun QuickNavigationItem(menu: SysMenuVO) {
    val isActive = MenuViewModel.currentRoute == menu.path
    val icon = getMenuIcon(menu)

    if (icon != null) {
        AddIconButton(
            text = menu.title,
            imageVector = icon,
            onClick = {
                if (menu.enumSysMenuType == EnumSysMenuType.SCREEN && menu.children.isEmpty()) {
                    MenuViewModel.updateRoute(menu.path)
                }
            },
            tint = if (isActive) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.size(36.dp)
        )
    }
}

/**
 * 📋 展开的导航菜单
 */
@Composable
private fun ExpandedNavigationMenu(
    onMenuItemClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            MenuViewModel.menuItems.forEach { menu ->
                ExpandedMenuItem(
                    menu = menu,
                    onItemClick = onMenuItemClick
                )
            }
        }
    }
}

/**
 * 📋 展开菜单项
 */
@Composable
private fun ExpandedMenuItem(
    menu: SysMenuVO,
    onItemClick: () -> Unit,
    level: Int = 0
) {
    val isActive = MenuViewModel.currentRoute == menu.path
    val icon = getMenuIcon(menu)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = (level * 16).dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        TextButton(
            onClick = {
                if (menu.enumSysMenuType == EnumSysMenuType.SCREEN && menu.children.isEmpty()) {
                    MenuViewModel.updateRoute(menu.path)
                    onItemClick()
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = menu.title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // 渲染子菜单
    menu.children.forEach { childMenu ->
        ExpandedMenuItem(
            menu = childMenu,
            onItemClick = onItemClick,
            level = level + 1
        )
    }
}

/**
 * 🎨 获取菜单图标
 */
@Composable
private fun getMenuIcon(vO: SysMenuVO): ImageVector? {
    // 如果有自定义图标，优先使用
    if (vO.icon.isNotBlank()) {
        return IconMap[vO.icon].vector
    }

    // 根据菜单标题推测图标
    return when {
        vO.title.contains("首页") || vO.title.contains("主页") -> Icons.Default.Home
        vO.title.contains("用户") || vO.title.contains("人员") -> Icons.Default.Person
        vO.title.contains("角色") -> Icons.Default.AdminPanelSettings
        vO.title.contains("权限") -> Icons.Default.Security
        vO.title.contains("菜单") -> Icons.Default.Menu
        vO.title.contains("系统") -> Icons.Default.Settings
        vO.title.contains("日志") -> Icons.Default.History
        vO.title.contains("监控") -> Icons.Default.Visibility
        vO.title.contains("配置") -> Icons.Default.Tune
        vO.title.contains("文件") -> Icons.Default.Folder
        vO.title.contains("数据") -> Icons.Default.Storage
        vO.title.contains("报表") -> Icons.Default.Assessment
        vO.title.contains("统计") -> Icons.Default.Analytics
        vO.title.contains("消息") -> Icons.Default.Message
        vO.title.contains("通知") -> Icons.Default.Notifications
        vO.title.contains("帮助") -> Icons.Default.Help
        vO.title.contains("关于") -> Icons.Default.Info
        else -> Icons.Default.Circle // 默认图标
    }
}
