package site.addzero

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.koin.core.context.KoinContext
// import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.context.startKoin
import org.koin.ksp.generated.defaultModule
import org.koin.mp.KoinPlatformTools
// import site.addzero.admin.components.AdminSidebar
// import site.addzero.admin.components.AdminSidebarFooter
// import site.addzero.admin.components.TopBar
import site.addzero.generated.RouteKeys
import site.addzero.generated.RouteTable
import site.addzero.ioc.generated.IocContainer
import site.addzero.themes.ShadcnTheme
import site.addzero.themes.colors

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
        "Help" -> {{ Icon(Icons.AutoMirrored.Filled.Help, contentDescription = null) }}
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

    // 原来的自定义侧边栏实现（已注释）
//    CustomSidebarLayout()

    // TODO: 在这里实现新的 ModalNavigationDrawer
    ModalNavigationDrawerLayout()
}

/**
 * 原来自定义侧边栏的实现逻辑
 * 保留作为参考和备份
 *
 * NOTE: Commented out due to missing admin components
 */
/*
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
                        currentTitle = {},
                        onThemeToggle = {},
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
*/

/**
 * 新的 ModalNavigationDrawer 实现
 * 参考官方 DetailedDrawerExample，使用官方组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModalNavigationDrawerLayout() {
    var currentRoute by remember { mutableStateOf(RouteKeys.HOME_SCREEN) }
    val controller = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()

    ShadcnTheme(isDarkTheme = false) {
        ModalNavigationDrawer(
            drawerContent = {
                ModalDrawerSheet {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // 美化的侧边栏头部
                        Spacer(Modifier.height(16.dp))

                        // 应用标题区域
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colors.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Menu,
                                    contentDescription = "应用图标",
                                    tint = MaterialTheme.colors.primaryForeground,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Column {
                                Text(
                                    "AddZero Admin",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colors.foreground,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "智能管理平台",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colors.mutedForeground
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        HorizontalDivider(
                            color = MaterialTheme.colors.border,
                            thickness = 1.dp
                        )

                        // 动态渲染所有路由分组
                        val groupedRoutes = RouteKeys.allMeta.groupBy { it.value }
                        val currentRouteMeta = RouteKeys.allMeta.find { it.routePath == currentRoute }

                        groupedRoutes.forEach { (groupName, routes) ->
                            if (groupName.isNotEmpty()) {
                                // 美化的分组标题
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        groupName,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colors.mutedForeground,
                                        fontWeight = FontWeight.Medium
                                    )

                                    // 分组项目数量徽章
                                    Badge(
                                        containerColor = MaterialTheme.colors.muted.copy(alpha = 0.5f)
                                    ) {
                                        Text(
                                            routes.size.toString(),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colors.mutedForeground
                                        )
                                    }
                                }

                                // 美化的分组内容
                                routes.forEach { route ->
                                    NavigationDrawerItem(
                                        label = {
                                            Column {
                                                Text(
                                                    route.title,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    maxLines = 1
                                                )
                                                if (route.routePath == currentRoute) {
                                                    Text(
                                                        route.qualifiedName,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colors.mutedForeground,
                                                        maxLines = 1
                                                    )
                                                }
                                            }
                                        },
                                        selected = currentRoute == route.routePath,
                                        onClick = {
                                            scope.launch {
                                                drawerState.close()
                                                controller.navigate(route.routePath)
                                                currentRoute = route.routePath
                                            }
                                        },
                                        icon = route.icon.takeIf { it.isNotBlank() }?.let { getIconForRoute(it) },
                                        badge = {
                                            // 为特定路由添加徽章
                                            when (route.routePath) {
                                                "/dict" -> Badge { Text("新") }
                                                "examples/tabs" -> Badge { Text("热门") }
                                                else -> {}
                                            }
                                        },
                                        colors = NavigationDrawerItemDefaults.colors(
                                            selectedContainerColor = MaterialTheme.colors.accent.copy(alpha = 0.1f),
                                            selectedIconColor = MaterialTheme.colors.primary,
                                            selectedTextColor = MaterialTheme.colors.primary,
                                            unselectedContainerColor = Color.Transparent,
                                            unselectedIconColor = MaterialTheme.colors.mutedForeground,
                                            unselectedTextColor = MaterialTheme.colors.foreground
                                        )
                                    )
                                }

                                // 分组分隔线（最后一组不加）
                                if (groupName != groupedRoutes.keys.lastOrNull { it.isNotEmpty() }) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                                        color = MaterialTheme.colors.border.copy(alpha = 0.5f)
                                    )
                                }
                            }
                        }

                        // 用户信息底部
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
                            color = MaterialTheme.colors.border.copy(alpha = 0.5f)
                        )

                        // 美化的用户信息区域
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colors.accent.copy(alpha = 0.05f)
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                // 当前路由信息
                                currentRouteMeta?.let { route ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                "当前页面",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colors.mutedForeground
                                            )
                                            Text(
                                                route.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colors.foreground,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1
                                            )
                                        }

                                        // 状态指示器
                                        Badge(
                                            containerColor = MaterialTheme.colors.primary
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = "当前页面",
                                                modifier = Modifier.size(12.dp),
                                                tint = MaterialTheme.colors.primaryForeground
                                            )
                                        }
                                    }
                                }

                                // 快捷操作
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 设置按钮
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                drawerState.close()
                                                currentRoute = "settings"
                                            }
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                MaterialTheme.colors.muted.copy(alpha = 0.5f),
                                                RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Icon(
                                            Icons.Default.Settings,
                                            contentDescription = "设置",
                                            tint = MaterialTheme.colors.mutedForeground,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    // 用户中心按钮
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                drawerState.close()
                                                currentRoute = "profile"
                                            }
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                MaterialTheme.colors.muted.copy(alpha = 0.5f),
                                                RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Icon(
                                            Icons.Default.Person,
                                            contentDescription = "用户中心",
                                            tint = MaterialTheme.colors.mutedForeground,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    // 退出登录按钮
                                    IconButton(
                                        onClick = {
                                            scope.launch {
                                                drawerState.close()
                                                // 处理退出登录逻辑
                                                currentRoute = "login"
                                            }
                                        },
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                MaterialTheme.colors.destructive.copy(alpha = 0.1f),
                                                RoundedCornerShape(8.dp)
                                            )
                                    ) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.Logout,
                                            contentDescription = "退出登录",
                                            tint = MaterialTheme.colors.destructive,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))
                    }
                }
            },
            drawerState = drawerState
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                RouteKeys.allMeta.find { it.routePath == currentRoute }?.title
                                    ?: "后台管理系统"
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    if (drawerState.isClosed) {
                                        drawerState.open()
                                    } else {
                                        drawerState.close()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        },
                        actions = {
                            // 通知按钮
                            IconButton(onClick = { /* 处理通知点击 */ }) {
                                Text("🔔")
                            }
                            // 用户头像按钮
                            IconButton(onClick = {
                                currentRoute = "profile"
                            }) {
                                Text("👤")
                            }
                        }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = controller,
                    startDestination = RouteKeys.HOME_SCREEN,
                    modifier = Modifier.padding(innerPadding)
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


//@Composable
//private fun MainLayoutWithLogin() {
//    val loginViewModel = koinViewModel<LoginViewModel>()
//    if (loginViewModel.currentToken == null) {
//        LoginScreen()
//    } else {
//        context(
//            NavgationViewModel,
//            koinViewModel<RecentTabsManagerViewModel>(),
//            koinViewModel<ThemeViewModel>(),
//            koinViewModel<ChatViewModel>(),
//            //路由菜单视图
//            koinViewModel<SysRouteViewModel>(),
//            koinViewModel<FavoriteTabsViewModel>()
//        ) {
//            // 使用 NavHost 导航系统
//            NavgationViewModel.Initialize(
//                controller = NavgationViewModel.getNavController()
//            )
//        }
//    }
//
//}

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
