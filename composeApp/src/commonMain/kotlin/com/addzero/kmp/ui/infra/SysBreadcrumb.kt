package com.addzero.kmp.ui.infra

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.addzero.kmp.generated.RouteTable
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel

/**
 * 面包屑组件
 * @param currentRouteRefPath 当前路由路径
 * @param navController 导航控制器（可选）
 */
@Composable
fun Breadcrumb(
    currentRouteRefPath: String,
    navController: NavController? = null
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 获取导航状态
            val backStackEntryState = navController?.currentBackStackEntryAsState()
            val currentRoute = backStackEntryState?.value?.destination?.route ?: currentRouteRefPath

            // 生成面包屑项
            val breadcrumbs = getBreadcrumbs(currentRoute)

            // 渲染面包屑
            breadcrumbs.forEachIndexed { index, breadcrumbItem ->
                // 面包屑文本
                renderBreadcrumbs(breadcrumbItem, navController)

                // 如果不是最后一项，添加箭头分隔符
                if (index < breadcrumbs.size - 1) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun renderBreadcrumbs(breadcrumbItem: BreadcrumbItem, navController: NavController?) {
    Text(
        text = breadcrumbItem.title,
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = if (breadcrumbItem.isActive) FontWeight.Bold else FontWeight.Normal,
        color = if (breadcrumbItem.isActive)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.clickable(enabled = !breadcrumbItem.isActive && navController != null) {
            // 如果不是当前活动项且有导航控制器，则导航到对应路由
            if (!breadcrumbItem.isActive && navController != null) {
                if (RouteTable.allRoutes.containsKey(breadcrumbItem.route)) {
                    navController.navigate(breadcrumbItem.route) {
                        launchSingleTop = true
                        restoreState = true
                    }
                    MenuViewModel.updateRoute(breadcrumbItem.route)
                }
            }
        }
    )
}

// 面包屑项数据类
private data class BreadcrumbItem(
    val title: String,
    val route: String,
    val isActive: Boolean = false
)

// 根据当前路由生成面包屑项列表
private fun getBreadcrumbs(currentRoute: String): List<BreadcrumbItem> {
    val cacleBreadcrumb = MenuViewModel.cacleBreadcrumb
    return cacleBreadcrumb.map {
        BreadcrumbItem(
            title = it.title,
            route = it.path,
            isActive = it.path == currentRoute
        )
    }


}
