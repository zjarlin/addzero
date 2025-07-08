package com.addzero.kmp.ui.infra

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.addzero.kmp.screens.ai.AiChatScreen
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel.currentRoute
import com.addzero.kmp.ui.infra.model.menu.SideMenu
import com.addzero.kmp.ui.infra.model.navigation.RecentTabsManager
import com.addzero.kmp.ui.infra.navigation.NavigationObserver
import com.addzero.kmp.viewmodel.ChatViewModel

import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainLayout() {
    val navController = rememberNavController()
    val vm = koinViewModel<RecentTabsManager>()
    val chatViewModel = koinViewModel<ChatViewModel>()
    val showChatBot = chatViewModel.showChatBot

    // 添加导航观察器，监听路由变化
    NavigationObserver(
        recentViewModel = vm,
        navController = navController,
        getRouteTitle = { route ->
            // 获取路由标题的逻辑
            MenuViewModel.getRouteByKey(route)
        }
    )

    // 为搜索框创建状态
    val isSearchOpen = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // 使用LaunchedEffect自动请求焦点，这样可以确保键盘事件能被捕获
    LaunchedEffect(Unit) {
        delay(100) // 短暂延迟确保组件已渲染
        focusRequester.requestFocus()
    }

    // 使用Box作为顶层容器，捕获全局键盘事件
    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester)
            .focusTarget() // 使组件可获取焦点
            .onKeyEvent { keyEvent -> // 使用onKeyEvent而不是onPreviewKeyEvent
                when {
                    // Cmd+K: 打开搜索框
                    (keyEvent.key == Key.K && keyEvent.isMetaPressed && keyEvent.type == KeyEventType.KeyDown) -> {
                        isSearchOpen.value = true
                        true
                    }
                    // Cmd+W: 关闭当前标签页
                    (keyEvent.key == Key.W && keyEvent.isMetaPressed && keyEvent.type == KeyEventType.KeyDown) -> {
                        val currentIndex = vm.currentTabIndex
                        if (currentIndex >= 0 && currentIndex < vm.tabs.size) { // 添加边界检查
                            vm.closeTab(currentIndex, navController)
                            true
                        } else {
                            false
                        }
                    }
                    // Cmd+Shift+T: 恢复最近关闭的标签页
                    (keyEvent.key == Key.T && keyEvent.isMetaPressed &&
                            keyEvent.isShiftPressed && keyEvent.type == KeyEventType.KeyDown) -> {
                        vm.reopenLastClosedTab(navController)
                        true
                    }
                    // Cmd+Shift+A: 切换AI聊天界面
                    (keyEvent.key == Key.A && keyEvent.isMetaPressed &&
                            keyEvent.isShiftPressed && keyEvent.type == KeyEventType.KeyDown) -> {
                        chatViewModel.showChatBot = !chatViewModel.showChatBot
                        true
                    }

                    else -> false
                }
            }
    ) {
        // 主内容区（始终可见）
        Scaffold(
            topBar = {
                SysTopBar(
                    navController = navController,
                    isSearchOpen = isSearchOpen,
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                        // 左侧菜单
                        SideMenu()

                        // 主内容区 - 根据聊天界面状态动态调整权重
                        Column(
                            modifier = Modifier.weight(
                                if (showChatBot) 1f else 1f // 当聊天界面显示时，主内容区占据剩余空间
                            )
                        ) {
                            // 错误消息提示
                            // 添加全局Toast监听器
//                            ToastListener()

                            // 面包屑导航 - 传递NavController
                            Breadcrumb(
                                currentRouteRefPath = currentRoute,
                                navController = navController,
                            )

                            // 最近访问标签页 - 不再需要在此处监听快捷键
                            AddRecentTabs(
                                navController = navController,
                                listenShortcuts = false,
                                recentViewModel = vm // 关闭标签页组件内的快捷键监听，改为全局监听
                            )
                            // 主要内容
                            MainContent(
                                navController = navController
                            )
                        }

                        // AI聊天界面 - 使用AnimatedVisibility实现滑入滑出效果
                        AnimatedVisibility(
                            visible = showChatBot,
                            enter = slideInHorizontally(
                                initialOffsetX = { it }, // 从右侧滑入
                                animationSpec = tween(300)
                            ),
                            exit = slideOutHorizontally(
                                targetOffsetX = { it }, // 向右侧滑出
                                animationSpec = tween(300)
                            )
                        ) {
                            AiChatScreen()
                        }
                    }
                }
            }
        }
    }
}
