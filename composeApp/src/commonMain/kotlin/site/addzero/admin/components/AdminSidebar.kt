package site.addzero.admin.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import site.addzero.component.sidebar.Sidebar
import site.addzero.component.sidebar.SidebarContent
import site.addzero.component.sidebar.SidebarFooter
import site.addzero.component.sidebar.SidebarGroup
import site.addzero.component.sidebar.SidebarGroupContent
import site.addzero.component.sidebar.SidebarGroupLabel
import site.addzero.component.sidebar.SidebarHeader
import site.addzero.component.sidebar.SidebarMenu
import site.addzero.component.sidebar.SidebarMenuButton
import site.addzero.generated.RouteKeys

@Composable
fun AdminSidebar(
    selectedRoute: String ,
    navController: NavController,
    onNavigate: (String) -> Unit = {}
) {
    // 根据路由分组
    val groupedRoutes = RouteKeys.allMeta.groupBy { it.value }

    Sidebar(
        modifier = Modifier.fillMaxSize()
    ) {
        SidebarHeader {
            SidebarMenuButton(
                text = "后台管理系统",
                onClick = { onNavigate("dashboard") },
                isActive = selectedRoute == "dashboard"
            )
        }

        SidebarContent {

            // 动态渲染所有路由分组
            groupedRoutes.forEach { (groupName, routes) ->
                if (groupName.isNotEmpty()) {
                    SidebarGroup {
                        SidebarGroupLabel(text = groupName)
                        SidebarGroupContent {
                            SidebarMenu {
                                routes.forEach { route ->
                                    SidebarMenuButton(
                                        text = route.title,
                                        onClick = {
                                            // 使用 NavController 进行真正的导航
                                            navController.navigate(route.routePath)
                                            onNavigate(route.routePath)
                                        },
                                        isActive = selectedRoute == route.routePath
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        SidebarFooter {
            // 可以在这里添加其他底部功能
            SidebarMenuButton(
                text = "设置",
                onClick = { onNavigate("settings") },
                isActive = selectedRoute == "settings"
            )
        }
    }
}
