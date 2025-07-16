package com.addzero.kmp.ui.infra.responsive

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.addzero.kmp.screens.ai.AiChatScreen
import com.addzero.kmp.ui.infra.AddRecentTabs
import com.addzero.kmp.ui.infra.Breadcrumb
import com.addzero.kmp.ui.infra.MainContent
import com.addzero.kmp.ui.infra.SysTopBar
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
import com.addzero.kmp.ui.infra.model.menu.SideMenu
import com.addzero.kmp.ui.infra.model.navigation.RecentTabsManager
import com.addzero.kmp.ui.infra.navigation.NavigationObserver
import com.addzero.kmp.viewmodel.ChatViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

/**
 * 🚀 响应式主布局组件
 *
 * 根据屏幕尺寸自动切换侧边栏和顶部导航栏布局
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ResponsiveMainLayout(
    forceLayoutMode: LayoutMode? = null
) {
    val navController = rememberNavController()
    val vm = koinViewModel<RecentTabsManager>()
    val chatViewModel = koinViewModel<ChatViewModel>()
    val focusManager = LocalFocusManager.current

    // 获取响应式配置
    val config = rememberResponsiveConfig(forceLayoutMode)

    // 添加导航观察器
    NavigationObserver(
        recentViewModel = vm,
        navController = navController,
        getRouteTitle = { route ->
            MenuViewModel.getRouteTitleByKey(route)
        }
    )

    // 搜索框状态
    val isSearchOpen = remember { mutableStateOf(false) }

    // 全局键盘事件处理
    LaunchedEffect(Unit) {
        delay(100)
        // 在响应式布局中，焦点管理可能需要特殊处理
    }

    // 根据布局模式渲染不同的布局
    when (config.layoutMode) {
        LayoutMode.SIDEBAR -> {
            // 桌面端：侧边栏布局
            SidebarLayout(
                navController = navController,
                vm = vm,
                chatViewModel = chatViewModel,
                showChatBot = chatViewModel.showChatBot,
                isSearchOpen = isSearchOpen,
                config = config
            )
        }
        LayoutMode.TOPBAR -> {
            // 移动端：顶部导航栏布局
            TopbarLayout(
                navController = navController,
                vm = vm,
                chatViewModel = chatViewModel,
                showChatBot = chatViewModel.showChatBot,
                isSearchOpen = isSearchOpen,
                config = config
            )
        }
    }
}

/**
 * 🖥️ 侧边栏布局（桌面端）
 */
@Composable
private fun SidebarLayout(
    navController: androidx.navigation.NavHostController,
    vm: RecentTabsManager,
    chatViewModel: ChatViewModel,
    showChatBot: Boolean,
    isSearchOpen: MutableState<Boolean>,
    config: ResponsiveConfig
) {
    Scaffold(
        topBar = {
            SysTopBar(
                navController = navController,
                isSearchOpen = isSearchOpen
            )
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 侧边栏
            SideMenu()

            // 主内容区
            Column(
                modifier = Modifier.weight( 1f )
            ) {
                // 面包屑导航
                Breadcrumb(
                    currentRouteRefPath = MenuViewModel.currentRoute,
                    navController = navController
                )

                // 最近访问标签页
                AddRecentTabs(
                    navController = navController,
                    listenShortcuts = false,
                    recentViewModel = vm
                )

                // 主要内容
                MainContent(navController = navController)
            }

            // AI聊天界面
            AnimatedVisibility(
                visible = showChatBot,
                enter = slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = androidx.compose.animation.core.tween(300)
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            ) {
                AiChatScreen()
            }
        }
    }
}

/**
 * 📱 顶部导航栏布局（移动端）
 */
@Composable
private fun TopbarLayout(
    navController: androidx.navigation.NavHostController,
    vm: RecentTabsManager,
    chatViewModel: ChatViewModel,
    showChatBot: Boolean,
    isSearchOpen: MutableState<Boolean>,
    config: ResponsiveConfig
) {
    Scaffold(
        topBar = {
            Column {
                // 系统顶部栏
                SysTopBar(
                    navController = navController,
                    isSearchOpen = isSearchOpen
                )

                // 顶部导航栏
                TopNavigationBar(
                    config = config
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 面包屑导航（移动端可选）
            if (config.screenSize != ScreenSize.MOBILE) {
                Breadcrumb(
                    currentRouteRefPath = MenuViewModel.currentRoute,
                    navController = navController
                )
            }

            // 最近访问标签页
            AddRecentTabs(
                navController = navController,
                listenShortcuts = false,
                recentViewModel = vm
            )

            // 主内容区和聊天界面
            Box(modifier = Modifier.weight(1f)) {
                // 主要内容
                MainContent(navController = navController)

                // AI聊天界面（移动端使用覆盖模式）
                androidx.compose.animation.AnimatedVisibility(
                    visible = showChatBot,
                    enter = slideInVertically(
                        initialOffsetY = { it },
                        animationSpec = androidx.compose.animation.core.tween(300)
                    ) + fadeIn(),
                    exit = slideOutVertically(
                        targetOffsetY = { it },
                        animationSpec = androidx.compose.animation.core.tween(300)
                    ) + fadeOut()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        tonalElevation = 8.dp
                    ) {
                        AiChatScreen()
                    }
                }
            }
        }
    }
}
