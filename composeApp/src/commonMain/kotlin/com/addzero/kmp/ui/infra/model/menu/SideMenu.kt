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

/**
 * 侧边菜单组件
 *
 * 显示应用的主要导航菜单，支持多级菜单结构
 * 使用AddTree组件实现菜单树渲染
 */
@Composable
fun SideMenu() {
    // 侧边菜单
    Surface(
        modifier = Modifier
            .width(if (isExpand) 250.dp else 65.dp)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp
    ) {
        Column {
            // 菜单标题
            if (isExpand) {
                Text(
                    text = "导航菜单",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 使用AddTree组件渲染菜单树
            Box(modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp)) {
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
                    nodeRender = { nodeInfo ->
                        // 自定义菜单项渲染
                        customRender4SysMenu(nodeInfo)
                    }
                )
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
            // 展开状态 - 显示完整菜单项
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = vector,
                        contentDescription = if (isExpandedNode) "折叠" else "展开",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = nodeInfo.label,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                badge = {
                    if (nodeInfo.hasChildren) {
                        val arrowIcon = if (isExpandedNode) {
                            Icons.Default.KeyboardArrowDown
                        } else {
                            Icons.AutoMirrored.Filled.KeyboardArrowRight
                        }
                        Icon(
                            imageVector = arrowIcon,
                            contentDescription = if (isExpandedNode) "折叠" else "展开",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                selected = nodeInfo.isSelected || nodeInfo.id == currentRoute,
                onClick = {
                    // 直接调用节点的点击事件，让AddTree内部处理折叠逻辑
                    nodeInfo.onNodeClick(node)
                },
                modifier = Modifier.padding(
                    start = (nodeInfo.level * 16).dp,
                    top = 4.dp,
                    end = 8.dp,
                    bottom = 4.dp
                ),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    unselectedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        } else {
            // 收起状态 - 只显示图标
            val isSelected = nodeInfo.isSelected || nodeInfo.id == currentRoute
            val bgColor = if (isSelected)
                MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            else
                MaterialTheme.colorScheme.surface

            val iconColor = if (isSelected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(percent = 50))
                    .background(bgColor)
                    .clickable { nodeInfo.onNodeClick(node) }
            ) {
                Icon(
                    imageVector = vector,
                    contentDescription = nodeInfo.label,
                    modifier = Modifier.size(24.dp),
                    tint = iconColor
                )
            }
        }
    }
}

