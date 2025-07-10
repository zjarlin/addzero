package com.addzero.kmp.ui.infra.model.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.tree.AddTree
import com.addzero.kmp.component.tree.rememberTreeViewModel
import com.addzero.kmp.compose.icons.IconMap
import com.addzero.kmp.entity.sys.menu.EnumSysMenuType
import com.addzero.kmp.entity.sys.menu.SysMenuVO
import com.addzero.kmp.generated.RouteKeys
import com.addzero.kmp.kt_util.isNotBlank
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel.currentRoute
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel.isExpand
import com.addzero.kmp.ui.infra.theme.*

/**
 * 侧边菜单组件
 *
 * 显示应用的主要导航菜单，支持多级菜单结构
 * 使用AddTree组件实现菜单树渲染
 */
@Composable
fun SideMenu() {
    val currentTheme = ThemeViewModel.currentTheme

    // 侧边菜单 - 美化设计，支持渐变主题
    SidebarGradientBackground(
        themeType = currentTheme,
        modifier = Modifier
            .width(if (isExpand) 240.dp else 56.dp)
            .fillMaxHeight()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent, // 透明背景，显示渐变
            tonalElevation = 0.dp
        ) {
        Column {
            // 菜单标题 - 减少内边距
            if (isExpand) {
                Text(
                    text = "导航菜单",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                // 添加分隔线
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                )
            }

            // 使用AddTree组件渲染菜单树 - 减少内边距，使用 Surface 而不是 Box
            Surface(
                modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                // 🎯 使用新的 TreeViewModel API
                val viewModel = rememberTreeViewModel<SysMenuVO>()

                // 配置 ViewModel
                LaunchedEffect(MenuViewModel.menuItems) {
                    viewModel.configure(
                        getId = { it.path },
                        getLabel = { it.title },
                        getChildren = { it.children },
                        getIcon = { getMenuIcon(it) }
                    )
                    viewModel.onNodeClick = { selectedMenu ->
                        // 处理菜单项点击
                        if (selectedMenu.enumSysMenuType == EnumSysMenuType.SCREEN && selectedMenu.children.isEmpty()) {
                            // 如果是页面类型且没有子项，才进行导航
                            MenuViewModel.updateRoute(selectedMenu.path)
                        }
                        // 注意：折叠/展开状态由AddTree内部管理，这里不需要手动处理
                    }
                    viewModel.setItems(
                        MenuViewModel.menuItems,
                        setOf(RouteKeys.HOME_SCREEN)
                    )
                }

                AddTree(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        }
    }
}

@Composable
private fun getMenuIcon(vO: SysMenuVO): ImageVector? {
    val path = vO.path
    return if (vO.icon.isNotBlank()) {
        val vector = IconMap[vO.icon].vector
        vector
    } else {
        // 根据路径推断图标，移除重复条件
        when {
            vO.children.isNotEmpty() -> Icons.AutoMirrored.Filled.ViewList
            path.contains("home") -> Icons.Default.Home
            path.contains("dashboard") -> Icons.Default.Dashboard
            path.contains("user") || path.contains("account") -> Icons.Default.Person
            path.contains("setting") -> Icons.Default.Settings
            path.contains("report") -> Icons.Default.BarChart
            path.contains("data") -> Icons.Default.Storage
            path.contains("file") -> Icons.AutoMirrored.Filled.InsertDriveFile
            path.contains("notification") -> Icons.Default.Notifications
            path.contains("message") -> Icons.Default.Email
            path.contains("calendar") -> Icons.Default.Event
            path.contains("task") -> Icons.AutoMirrored.Filled.Assignment
            path.contains("analytics") -> Icons.Default.Analytics
            path.contains("help") -> Icons.AutoMirrored.Filled.Help
            path.contains("about") -> Icons.Default.Info
            else -> Icons.AutoMirrored.Filled.Article
        }
    }
}

// customRender4SysMenu 已移除，使用 AddTree 内置渲染

