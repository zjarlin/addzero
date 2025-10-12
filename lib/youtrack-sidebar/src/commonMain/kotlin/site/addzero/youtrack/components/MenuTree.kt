package site.addzero.youtrack.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 树形菜单节点数据类
 */
data class MenuTreeNode(
    val id: String,
    val title: String,
    val icon: ImageVector? = null,
    val badge: String? = null,
    val routePath: String? = null,
    val children: List<MenuTreeNode> = emptyList(),
    val isExpanded: Boolean = false,
    val level: Int = 0,
    val order: Double = 0.0
) {
    val hasChildren: Boolean get() = children.isNotEmpty()
    val isLeaf: Boolean get() = children.isEmpty()
}

/**
 * 将扁平的导航元数据转换为树形结构
 */
object MenuTreeBuilder {
    fun buildFromNavigationMetadata(
        navigationItems: List<NavigationMetadata>,
        searchQuery: String = ""
    ): List<MenuTreeNode> {
        val filteredItems = if (searchQuery.isBlank()) {
            navigationItems
        } else {
            navigationItems.filter { item ->
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.group.contains(searchQuery, ignoreCase = true) ||
                item.qualifiedName.contains(searchQuery, ignoreCase = true)
            }
        }

        // 按分组构建树形结构
        val groupedItems = filteredItems.groupBy { it.group }

        return groupedItems.mapNotNull { (groupValue, items) ->
            if (groupValue.isBlank()) {
                // 处理根级路由（没有分组的路由）
                items.map { item ->
                    MenuTreeNode(
                        id = item.id,
                        title = item.title,
                        icon = getIconForRoute(item.icon),
                        badge = getBadgeForRoute(item.routePath),
                        routePath = item.routePath,
                        level = 0,
                        order = item.order
                    )
                }
            } else {
                // 处理分组路由
                val sortedItems = items.sortedBy { it.order }
                MenuTreeNode(
                    id = groupValue,
                    title = groupValue,
                    icon = getIconForGroup(groupValue),
                    badge = "${sortedItems.size}",
                    routePath = null,
                    children = sortedItems.map { item ->
                        MenuTreeNode(
                            id = item.id,
                            title = item.title,
                            icon = getIconForRoute(item.icon),
                            badge = getBadgeForRoute(item.routePath),
                            routePath = item.routePath,
                            level = 1,
                            order = item.order
                        )
                    },
                    level = 0,
                    order = (sortedItems.minOfOrNull { it.order } as? Number)?.toDouble() ?: 0.0
                )
            }
        }.let { result ->
            val flatList = mutableListOf<MenuTreeNode>()
            result.forEach { item ->
                if (item is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    flatList.addAll(item as List<MenuTreeNode>)
                } else {
                    flatList.add(item as MenuTreeNode)
                }
            }
            flatList.sortedBy { it.order }
        }
    }

    private fun getIconForRoute(iconName: String): ImageVector? {
        return when (iconName) {
            "Home" -> Icons.Default.Home
            "Group" -> Icons.Default.Group
            "Business" -> Icons.Default.Business
            "Category" -> Icons.Default.Category
            "Dashboard" -> Icons.Default.Dashboard
            "Settings" -> Icons.Default.Settings
            "Person" -> Icons.Default.Person
            "Star" -> Icons.Default.Star
            else -> null
        }
    }

    private fun getIconForGroup(groupName: String): ImageVector? {
        return when (groupName) {
            "组件示例" -> Icons.Default.Widgets
            "系统管理" -> Icons.Default.Settings
            "测试" -> Icons.Default.BugReport
            "界面演示" -> Icons.Default.Preview
            "工具" -> Icons.Default.Build
            "物联网模块" -> Icons.Default.DevicesOther
            else -> Icons.Default.Folder
        }
    }

    private fun getBadgeForRoute(routePath: String): String? {
        return when (routePath) {
            "/dict" -> "新"
            "examples/tabs" -> "热门"
            else -> null
        }
    }
}

/**
 * 树形菜单组件
 */
@Composable
fun YouTrackMenuTree(
    nodes: List<MenuTreeNode>,
    selectedRoute: String,
    onNavigate: (String) -> Unit,
    searchQuery: String = "",
    modifier: Modifier = Modifier
) {
    var expandedNodes by remember { mutableStateOf(setOf<String>()) }

    Column(modifier = modifier.fillMaxWidth()) {
        nodes.forEach { node ->
            MenuTreeItem(
                node = node,
                isSelected = selectedRoute == node.routePath,
                isExpanded = expandedNodes.contains(node.id),
                onToggleExpand = {
                    expandedNodes = if (expandedNodes.contains(node.id)) {
                        expandedNodes - node.id
                    } else {
                        expandedNodes + node.id
                    }
                },
                onNavigate = onNavigate,
                level = node.level,
                searchQuery = searchQuery,
                selectedRoute = selectedRoute
            )
        }
    }
}

/**
 * 树形菜单项组件
 */
@Composable
private fun MenuTreeItem(
    node: MenuTreeNode,
    isSelected: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onNavigate: (String) -> Unit,
    level: Int,
    searchQuery: String,
    selectedRoute: String
) {
    var isHovered by remember { mutableStateOf(false) }

    // 项背景和动画
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isSelected -> MaterialTheme.colorScheme.primaryContainer
            isHovered -> MaterialTheme.colorScheme.surfaceVariant
            else -> Color.Transparent
        },
        animationSpec = tween(150)
    )

    // 展开/收起动画
    val expandRotation by animateFloatAsState(
        targetValue = if (isExpanded) 90f else 0f,
        animationSpec = tween(200)
    )

    // 主菜单项
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(if (node.isLeaf) 36.dp else 40.dp)
            .background(backgroundColor)
            .clickable {
                if (node.isLeaf) {
                    onNavigate(node.routePath ?: return@clickable)
                } else {
                    onToggleExpand()
                }
            }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 层级缩进
        Spacer(modifier = Modifier.width((level * 16).dp))

        // 展开/收起图标
        if (node.hasChildren) {
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = if (isExpanded) "收起" else "展开",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(16.dp)
                    .graphicsLayer { rotationZ = expandRotation }
            )
            Spacer(modifier = Modifier.width(8.dp))
        } else {
            Spacer(modifier = Modifier.width(24.dp))
        }

        // 菜单项图标
        node.icon?.let { icon ->
            Icon(
                icon,
                contentDescription = node.title,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
        } ?: Spacer(modifier = Modifier.width(30.dp))

        // 菜单项标题
        Text(
            text = node.title,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                fontSize = 14.sp
            ),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        // 徽章
        node.badge?.let { badge ->
            Badge(
                containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
            ) {
                Text(
                    badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // 选中状态指示器
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(2.dp)
                    )
            )
        }
    }

    // 子项动画展开
    AnimatedVisibility(
        visible = isExpanded && node.hasChildren,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Column {
            node.children.forEach { childNode ->
                MenuTreeItem(
                    node = childNode,
                    isSelected = selectedRoute == childNode.routePath,
                    isExpanded = false, // 子项默认不展开
                    onToggleExpand = { /* 子项不支持展开 */ },
                    onNavigate = onNavigate,
                    level = childNode.level,
                    searchQuery = searchQuery,
                    selectedRoute = selectedRoute
                )
            }
        }
    }
}