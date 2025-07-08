package com.addzero.kmp.ui.infra

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.addzero.kmp.component.button.AddFloatingActionButton
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.theme.QuickThemeToggle
import com.addzero.kmp.ui.infra.model.menu.MenuLayoutToggleButton
import com.addzero.kmp.ui.infra.model.menu.MenuViewModel
import com.addzero.kmp.ui.infra.model.menu.SysUserCenterScreen
import com.addzero.kmp.ui.infra.theme.ThemeSelectionButton
import com.addzero.kmp.ui.infra.theme.ThemeToggleButton
import com.addzero.kmp.ui.infra.theme.ThemeViewModel
import com.addzero.kmp.viewmodel.ChatViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * 顶部导航栏组件
 *
 * @param navController 导航控制器
 * @param isSearchOpen 搜索框是否打开的状态
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SysTopBar(
    navController: NavController,
    isSearchOpen: MutableState<Boolean>? = null
) {
    val chatViewModel = koinViewModel<ChatViewModel>()
    val currentTheme = ThemeViewModel.currentTheme
    //是否为渐变主题
    val isGradientTheme = currentTheme.isGradient()

    TopAppBar(
        title = { Text("AddzeroKmp") },
        navigationIcon = {
            // 导航栏横纵切换按钮（点击时切换侧边栏展开状态）
            MenuLayoutToggleButton(
                isExpanded = MenuViewModel.isExpand,
                onToggle = { MenuViewModel.isExpand = !MenuViewModel.isExpand }
            )
        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // 快速主题切换按钮 - 用于测试渐变效果
                QuickThemeToggle()
                // 间距
                Spacer(modifier = Modifier.width(8.dp))


                // 主题明暗切换按钮 - 仅在非渐变主题时显示
                if (!isGradientTheme) {
                    ThemeToggleButton()
                    // 间距
                    Spacer(modifier = Modifier.width(8.dp))
                }

                ThemeSelectionButton()


                // 间距
                Spacer(modifier = Modifier.width(8.dp))


                // 全局搜索栏
                AddSysRouteSearchBar(
                    navController = navController,
                    isSearchOpen = isSearchOpen
                )
                // 间距
                Spacer(modifier = Modifier.width(8.dp))



                // 用户中心
                Box(modifier = Modifier.width(40.dp)) {
                    SysUserCenterScreen()
                }

                // 间距
                Spacer(modifier = Modifier.width(8.dp))


                // 机器人按钮
                AddFloatingActionButton(
                    imageVector = Icons.Default.SmartToy,
                    text = "AI对话",
                ) {
                    chatViewModel.showChatBot = !chatViewModel.showChatBot
                }

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}
