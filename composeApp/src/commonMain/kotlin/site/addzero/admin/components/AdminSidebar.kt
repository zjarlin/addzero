package site.addzero.admin.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import site.addzero.component.sidebar.*
import site.addzero.generated.RouteKeys

@Composable
fun AdminSidebar(
    selectedRoute: String,
    navController: NavController,
    onNavigate: (String) -> Unit = {},
    sideBarHeader: @Composable () -> Unit = {
        SidebarMenuButton(
            text = "后台管理系统",
            onClick = { onNavigate("dashboard") },
            isActive = selectedRoute == "dashboard"
        )
    },
    sideBarFooter: @Composable () -> Unit = {
        SidebarMenuButton(
            text = "设置",
            onClick = { onNavigate("settings") },
            isActive = selectedRoute == "settings"
        )
    }

) {
    val groupedRoutes = RouteKeys.allMeta.groupBy { it.value }

    Sidebar(
        modifier = Modifier.fillMaxSize()
    ) {
        SidebarHeader {
            sideBarHeader()
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
            sideBarFooter()
        }
    }
}
