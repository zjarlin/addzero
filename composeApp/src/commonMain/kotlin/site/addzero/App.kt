package site.addzero

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.mp.KoinPlatformTools
import site.addzero.adapter.RouteKeysNavigationAdapter
import site.addzero.youtrack.components.UserInfo
import site.addzero.youtrack.components.*
import site.addzero.component.sidebar.*
import site.addzero.component.Button
import site.addzero.component.ButtonSize
import site.addzero.component.ButtonVariant
import site.addzero.di.NavgationViewModel
import site.addzero.generated.RouteKeys
import site.addzero.generated.RouteTable
import site.addzero.ioc.generated.IocContainer
import site.addzero.themes.ShadcnTheme
import site.addzero.themes.YouTrackColors
import site.addzero.themes.YouTrackLightColors
import site.addzero.themes.colors
import site.addzero.ui.auth.LoginScreen
import site.addzero.ui.infra.model.favorite.FavoriteTabsViewModel
import site.addzero.ui.infra.model.navigation.RecentTabsManagerViewModel
import site.addzero.ui.infra.theme.ThemeViewModel
import site.addzero.viewmodel.ChatViewModel
import site.addzero.viewmodel.LoginViewModel
import site.addzero.viewmodel.SysRouteViewModel

/**
 * 根据图标名称获取对应的图标
 */
@Composable
private fun getIconForRoute(iconName: String): @Composable (() -> Unit)? {
    return when (iconName) {
        "Home" -> {{ Icon(Icons.Default.Home, contentDescription = null) }}
        "Group" -> {{ Icon(Icons.Default.Group, contentDescription = null) }}
        "Business" -> {{ Icon(Icons.Default.Business, contentDescription = null) }}
        "Category" -> {{ Icon(Icons.Default.Category, contentDescription = null) }}
        "Dashboard" -> {{ Icon(Icons.Default.Dashboard, contentDescription = null) }}
        "Settings" -> {{ Icon(Icons.Default.Settings, contentDescription = null) }}
        "Person" -> {{ Icon(Icons.Default.Person, contentDescription = null) }}
        "Notifications" -> {{ Icon(Icons.Default.Notifications, contentDescription = null) }}
        "Favorite" -> {{ Icon(Icons.Default.Favorite, contentDescription = null) }}
        "Search" -> {{ Icon(Icons.Default.Search, contentDescription = null) }}
        "Menu" -> {{ Icon(Icons.Default.Menu, contentDescription = null) }}
        "Help" -> {{ Icon(Icons.Default.Info, contentDescription = null) }}
        "Info" -> {{ Icon(Icons.Default.Info, contentDescription = null) }}
        "Edit" -> {{ Icon(Icons.Default.Edit, contentDescription = null) }}
        "Delete" -> {{ Icon(Icons.Default.Delete, contentDescription = null) }}
        "Add" -> {{ Icon(Icons.Default.Add, contentDescription = null) }}
        "Sharp" -> {{ Icon(Icons.Default.Star, contentDescription = null) }}
        else -> null
    }
}

@Composable
fun App() {
    initKoin()
    IocContainer.iocAllStart()

    // 使用自定义侧边栏实现
    CustomSidebarLayout()

    // ModalNavigationDrawer 实现（已注释）
//    ModalNavigationDrawerLayout()
}

/**
 * 使用独立模块的插槽化 YouTrack 侧边栏实现
 */
@Composable
private fun CustomSidebarLayout() {
    val controller = rememberNavController()
    var isDarkTheme by remember { mutableStateOf(true) }

    // 导航元数据适配器
    val navigationMetadataProvider = remember { RouteKeysNavigationAdapter() }

    // 获取当前用户信息（这里可以根据实际情况从 ViewModel 获取）
    val loginViewModel = koinViewModel<LoginViewModel>()
    val userInfo = remember(loginViewModel.currentToken) {
        UserInfo(
            name = loginViewModel.currentToken?.nickname ?: loginViewModel.currentToken?.username ?: "用户",
            email = loginViewModel.currentToken?.email ?: "user@example.com"
        )
    }

    ShadcnTheme(
        isDarkTheme = isDarkTheme,
        shadcnDarkColors = YouTrackColors,
        shadcnLightColors = YouTrackLightColors
    ) {
        SidebarProvider(defaultOpen = true) {
            SidebarLayout(
                sidebarHeader = {
                    SidebarHeader {
                        // 应用标题区域
                        Column {
                            Text(
                                "AddZero",
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colors.sidebarForeground,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "智能管理平台",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colors.sidebarForeground.copy(alpha = 0.7f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 搜索框
                        YouTrackSidebarHeader(
                            searchQuery = "",
                            onSearchQueryChange = { },
                            appTitle = "AddZero",
                            appSubtitle = "智能管理平台"
                        )
                    }
                },
                sidebarContent = {
                    SidebarContent {
                        // 构建树形菜单数据
                        val menuTree = remember("") {
                            MenuTreeBuilder.buildFromNavigationMetadata(
                                navigationItems = navigationMetadataProvider.getAllNavigationItems(),
                                searchQuery = ""
                            )
                        }

                        YouTrackMenuTree(
                            nodes = menuTree,
                            selectedRoute = RouteKeys.HOME_SCREEN,
                            onNavigate = { route ->
                                controller.navigate(route)
                            },
                            searchQuery = "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                },
                sidebarFooter = {
                    SidebarFooter {
                        YouTrackSidebarFooter(
                            selectedRoute = RouteKeys.HOME_SCREEN,
                            onNavigate = { route ->
                                controller.navigate(route)
                            },
                            onLogout = {
                                controller.navigate("login")
                            },
                            userInfo = userInfo
                        )
                    }
                },
                content = {
                    // 主要内容区域包含顶部导航栏和页面内容
                    Column(modifier = Modifier.fillMaxSize()) {
                        // 顶部导航栏
                        YouTrackTopBar(
                            currentTitle = "AddZero Admin",
                            onMenuClick = {
                                // shadcn sidebar 会自动处理菜单切换
                            },
                            onNotificationClick = { /* 处理通知点击 */ },
                            onProfileClick = {
                                controller.navigate("profile")
                            },
                            onSearchClick = { /* 处理搜索点击 */ },
                            onThemeToggle = {
                                isDarkTheme = !isDarkTheme
                            }
                        )

                        // 页面内容
                        NavHost(
                            navController = controller,
                            startDestination = RouteKeys.HOME_SCREEN,
                            modifier = Modifier.fillMaxSize()
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
            )
        }
    }
}

/**
 * 新的 ModalNavigationDrawer 实现
 * 参考官方 DetailedDrawerExample，使用官方组件
 */


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
