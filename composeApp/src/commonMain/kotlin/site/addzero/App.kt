package site.addzero

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.mp.KoinPlatformTools
import site.addzero.admin.components.AdminSidebar
import site.addzero.admin.components.AdminSidebarFooter
import site.addzero.admin.components.TopBar
import site.addzero.component.sidebar.SidebarLayout
import site.addzero.component.sidebar.SidebarProvider
import site.addzero.di.NavgationViewModel
import site.addzero.generated.RouteKeys
import site.addzero.generated.RouteTable
import site.addzero.ioc.generated.IocContainer
import site.addzero.themes.ShadcnTheme
import site.addzero.ui.auth.LoginScreen
import site.addzero.ui.infra.model.favorite.FavoriteTabsViewModel
import site.addzero.ui.infra.model.navigation.RecentTabsManagerViewModel
import site.addzero.ui.infra.theme.ThemeViewModel
import site.addzero.viewmodel.ChatViewModel
import site.addzero.viewmodel.LoginViewModel
import site.addzero.viewmodel.SysRouteViewModel


@Composable
fun App() {
    initKoin()
    IocContainer.iocAllStart()

    // 原来的自定义侧边栏实现（已注释）
//    CustomSidebarLayout()

    // TODO: 在这里实现新的 ModalNavigationDrawer
    ModalNavigationDrawerLayout()
}

/**
 * 原来自定义侧边栏的实现逻辑
 * 保留作为参考和备份
 */
@Composable
private fun CustomSidebarLayout() {
    var currentRoute by remember { mutableStateOf("dashboard") }
    val controller = rememberNavController()
    var isSidebarOpen by remember { mutableStateOf(true) }

    ShadcnTheme(isDarkTheme = false) {
        SidebarProvider(defaultOpen = isSidebarOpen) {
            SidebarLayout(
                modifier = Modifier.fillMaxSize(),
                sidebarHeader = {
                    // 这里可以添加自定义的侧边栏头部
                },
                sidebarContent = {
                    AdminSidebar(
                        selectedRoute = currentRoute,
                        navController = controller,
                        onNavigate = { route ->
                            currentRoute = route
                        }
                    )
                },
                sidebarFooter = {
                    AdminSidebarFooter(
                        selectedRoute = currentRoute,
                        onNavigate = { route ->
                            currentRoute = route
                        },
                        onLogout = {
                            // 处理退出登录逻辑
//                            val loginViewModel = koinViewModel<LoginViewModel>()
//                            loginViewModel.logout()
                            currentRoute = "login"
                        }
                    )
                }
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // 顶部导航栏
                    TopBar(
                        currentRoute = currentRoute,
                        onMenuClick = {
                            // 切换侧边栏状态
                            isSidebarOpen = !isSidebarOpen
                        },
                        onNotificationClick = { /* 处理通知点击 */ },
                        onProfileClick = {
                            currentRoute = "profile"
                        }
                    )

                    NavHost(
                        navController = controller,
                        startDestination = RouteKeys.HOME_SCREEN,
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ) {
                        // 动态生成导航目标
                        RouteTable.allRoutes.forEach { (route, content) ->
                            this.composable(route) {
                                content()
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 新的 ModalNavigationDrawer 实现
 * 这里你可以测试官方的 ModalNavigationDrawer 效果
 */
@Composable
private fun ModalNavigationDrawerLayout() {
    // TODO: 在这里实现 ModalNavigationDrawer
    // 临时使用简单的文本，等待你的实现
    ShadcnTheme(isDarkTheme = true) {
        // 临时占位内容
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "准备实现 ModalNavigationDrawer",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "请在这里添加你的 ModalNavigationDrawer 实现",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
private fun MainLayoutWithLogin() {
    val loginViewModel = koinViewModel<LoginViewModel>()
    if (loginViewModel.currentToken == null) {
        LoginScreen()
    } else {
        context(
            NavgationViewModel,
            koinViewModel<RecentTabsManagerViewModel>(),
            koinViewModel<ThemeViewModel>(),
            koinViewModel<ChatViewModel>(),
            //路由菜单视图
            koinViewModel<SysRouteViewModel>(),
            koinViewModel<FavoriteTabsViewModel>()
        ) {
            // 使用 NavHost 导航系统
            NavgationViewModel.Initialize(
                controller = NavgationViewModel.getNavController()
            )
        }
    }

}

//@Composable
private fun initKoin() {
    val orNull = KoinPlatformTools.defaultContext().getOrNull()
    if (orNull == null) {
        startKoin {
            printLogger()
            modules(
                defaultModule,
            )
        }
    }
}
