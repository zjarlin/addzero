package com.addzero.kmp.ui.infra.model.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.tree.AddTree
import com.addzero.kmp.component.tree.TreeNodeInfo
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

            // 使用AddTree组件渲染菜单树 - 减少内边距
            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp, vertical = 4.dp)) {
                AddTree(
                    items = MenuViewModel.menuItems,
                    getId = { it.path },
                    getLabel = { it.title },
                    getChildren = { it.children },
                    getIcon = {
                        getMenuIcon(it)
                    },
//                    getNodeType = { getMenuNodeType(it) },
                    initiallyExpandedIds = setOf(
                        RouteKeys.HOME_SCREEN
//                    , RoutePaths.Group.SYS_MANAGE
                    ),
                    onCurrentNodeClick = { selectedMenu ->
                        // 处理菜单项点击
                        if (selectedMenu.enumSysMenuType == EnumSysMenuType.SCREEN && selectedMenu.children.isEmpty()) {
                            // 如果是页面类型且没有子项，才进行导航
                            MenuViewModel.updateRoute(selectedMenu.path)
                        }
                        // 注意：折叠/展开状态由AddTree内部管理，这里不需要手动处理
                    },
//                    nodeRender = { nodeInfo ->
//                        // 自定义菜单项渲染
//                        customRender4SysMenu(nodeInfo)
//                    }
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
        when {
            vO.children.isNotEmpty() -> Icons.AutoMirrored.Filled.ViewList
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
            path.contains("home") -> Icons.Default.Home
            path.contains("dashboard") -> Icons.Default.Dashboard
            path.contains("user") || path.contains("account") -> Icons.Default.Person
            path.contains("setting") -> Icons.Default.Settings
            path.contains("report") -> Icons.Default.BarChart
            path.contains("data") -> Icons.Default.Storage
            path.contains("file") -> Icons.AutoMirrored.Filled.InsertDriveFile
            else -> Icons.AutoMirrored.Filled.Article
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun customRender4SysMenu(nodeInfo: TreeNodeInfo<SysMenuVO>) {
    val currentTheme = ThemeViewModel.currentTheme
    val isExpandSideMenu = isExpand
    val isExpandedNode = nodeInfo.isExpanded
    val node = nodeInfo.node
    val icon = node.icon
    val menuIcon = getMenuIcon(node)
    val vector = if (icon != "Apps" && icon.isNotBlank()) {
        IconMap[icon].vector
    } else {
        menuIcon ?: IconMap[icon].vector
    }


    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            if (!isExpandSideMenu) {
                PlainTooltip {
                    Text(nodeInfo.label)
                }
            }
        },
        state = rememberTooltipState()
    ) {
        if (isExpandSideMenu) {
            // 展开状态 - 美化菜单项设计，支持渐变主题
            val isSelected = nodeInfo.isSelected || nodeInfo.id == currentRoute
            val textColor = getMenuItemTextColor(currentTheme, isSelected)
            val iconColor = getMenuItemIconColor(currentTheme, isSelected)

            // 外层容器，处理缩进和间距
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = (nodeInfo.level * 16 + 6).dp, // 适中的缩进
                        end = 6.dp,
                        top = 2.dp,
                        bottom = 2.dp
                    )
            ) {
                MenuItemGradientBackground(
                    themeType = currentTheme,
                    isSelected = isSelected,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp) // 参考 Android Developer 的高度
                        .clickable { nodeInfo.onNodeClick(node) }
                        .padding(horizontal = 12.dp, vertical = 8.dp) // 统一内边距
                ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                // 图标
                Icon(
                    imageVector = vector,
                    contentDescription = nodeInfo.label,
                    modifier = Modifier.size(20.dp), // 稍微增大图标
                    tint = iconColor
                )

                Spacer(modifier = Modifier.width(12.dp)) // 增加间距

                // 文本 - 支持渐变效果
                GradientText(
                    text = nodeInfo.label,
                    themeType = currentTheme,
                    isSelected = isSelected,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f)
                )

                // 展开/收起箭头
                if (nodeInfo.hasChildren) {
                    val arrowIcon = if (isExpandedNode) {
                        Icons.Default.KeyboardArrowDown
                    } else {
                        Icons.AutoMirrored.Filled.KeyboardArrowRight
                    }
                    Icon(
                        imageVector = arrowIcon,
                        contentDescription = if (isExpandedNode) "折叠" else "展开",
                        modifier = Modifier.size(18.dp), // 适中的箭头尺寸
                        tint = iconColor.copy(alpha = 0.7f)
                    )
                }
                }
            }
            }
        } else {
            // 收起状态 - 美化图标设计，支持渐变主题
            val isSelected = nodeInfo.isSelected || nodeInfo.id == currentRoute
            val iconColor = getMenuItemIconColor(currentTheme, isSelected)

            // 外层容器，处理间距
            Box(
                modifier = Modifier
                    .padding(vertical = 3.dp, horizontal = 6.dp)
            ) {
                MenuItemGradientBackground(
                    themeType = currentTheme,
                    isSelected = isSelected,
                    modifier = Modifier
                        .size(44.dp, 40.dp) // 统一尺寸，与展开状态高度一致
                        .clickable { nodeInfo.onNodeClick(node) }
                ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = vector,
                        contentDescription = nodeInfo.label,
                        modifier = Modifier.size(20.dp), // 增大图标尺寸
                        tint = iconColor
                    )
                }
            }
            }
        }
    }
}

