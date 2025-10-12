package site.addzero

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
import site.addzero.youtrack.layout.YouTrackLayout
import site.addzero.youtrack.layout.YouTrackLayoutSlots
import site.addzero.di.NavgationViewModel
import site.addzero.generated.RouteKeys
import site.addzero.generated.RouteTable
import site.addzero.ioc.generated.IocContainer
import site.addzero.themes.ShadcnTheme
import site.addzero.themes.YouTrackColors
import site.addzero.themes.YouTrackLightColors
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
    var isSidebarOpen by remember { mutableStateOf(true) }
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
        YouTrackLayout(
            isSidebarOpen = isSidebarOpen,
            onSidebarToggle = { isSidebarOpen = !isSidebarOpen },
            slots = object : YouTrackLayoutSlots {
                @Composable
                override fun Sidebar(
                    onNavigate: (String) -> Unit,
                    selectedRoute: String,
                    searchQuery: String,
                    onSearchQueryChange: (String) -> Unit
                ) {
                    // 侧边栏内容
                    YouTrackSidebar(
                        navigationMetadataProvider = navigationMetadataProvider,
                        selectedRoute = selectedRoute,
                        onNavigate = onNavigate,
                        searchQuery = searchQuery,
                        onSearchQueryChange = onSearchQueryChange,
                        onLogout = {
                            // 处理退出登录逻辑
                            controller.navigate("login")
                        },
                        userInfo = userInfo,
                        appTitle = "AddZero",
                        appSubtitle = "智能管理平台",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                @Composable
                override fun TopBar(
                    currentRoute: String,
                    currentTitle: String,
                    onMenuClick: () -> Unit,
                    onThemeToggle: () -> Unit
                ) {
                    YouTrackTopBar(
                        currentTitle = currentTitle,
                        onMenuClick = onMenuClick,
                        onNotificationClick = { /* 处理通知点击 */ },
                        onProfileClick = {
                            controller.navigate("profile")
                        },
                        onSearchClick = { /* 处理搜索点击 */ },
                        onThemeToggle = {
                            isDarkTheme = !isDarkTheme
                        }
                    )
                }

                @Composable
                override fun SidebarHeader(
                    searchQuery: String,
                    onSearchQueryChange: (String) -> Unit
                ) {
                    YouTrackSidebarHeader(
                        searchQuery = searchQuery,
                        onSearchQueryChange = onSearchQueryChange,
                        appTitle = "AddZero",
                        appSubtitle = "智能管理平台"
                    )
                }

                @Composable
                override fun SidebarContent(
                    onNavigate: (String) -> Unit,
                    selectedRoute: String,
                    searchQuery: String
                ) {
                    // 构建树形菜单数据
                    val menuTree = remember(searchQuery) {
                        MenuTreeBuilder.buildFromNavigationMetadata(
                            navigationItems = navigationMetadataProvider.getAllNavigationItems(),
                            searchQuery = searchQuery
                        )
                    }

                    YouTrackMenuTree(
                        nodes = menuTree,
                        selectedRoute = selectedRoute,
                        onNavigate = onNavigate,
                        searchQuery = searchQuery,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                @Composable
                override fun SidebarFooter(
                    selectedRoute: String,
                    onNavigate: (String) -> Unit
                ) {
                    YouTrackSidebarFooter(
                        selectedRoute = selectedRoute,
                        onNavigate = onNavigate,
                        onLogout = {
                            // 处理退出登录逻辑
                            controller.navigate("login")
                        },
                        userInfo = userInfo
                    )
                }

                @Composable
                override fun MainContent(
                    paddingValues: PaddingValues
                ) {
                    NavHost(
                        navController = controller,
                        startDestination = RouteKeys.HOME_SCREEN,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
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
