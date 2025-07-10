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
import com.addzero.kmp.ui.infra.responsive.ResponsiveMainLayout
import com.addzero.kmp.viewmodel.ChatViewModel

import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainLayout() {
    // 🚀 使用新的响应式主布局
    ResponsiveMainLayout()
}
