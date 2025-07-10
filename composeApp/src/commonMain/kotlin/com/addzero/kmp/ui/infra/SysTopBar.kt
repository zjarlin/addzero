package com.addzero.kmp.ui.infra

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.addzero.kmp.component.button.AddFloatingActionButton
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.theme.QuickThemeToggle
import com.addzero.kmp.component.upload_manager.GlobalUploadManager
import com.addzero.kmp.component.upload_manager.UploadManagerUI
import com.addzero.kmp.settings.SettingContext4Compose.APP_NAME
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

    // 上传管理器对话框状态
    var showUploadManager by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            // 🎨 美化的标题组件
            BeautifulTitle(appName = APP_NAME, currentTheme = currentTheme)
        },
        navigationIcon = {


            Row(
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
            ) {
                // 导航栏横纵切换按钮（点击时切换侧边栏展开状态）
                MenuLayoutToggleButton(
                    isExpanded = MenuViewModel.isExpand,
                    onToggle = { MenuViewModel.isExpand = !MenuViewModel.isExpand }
                )

                // 间距
                Spacer(modifier = Modifier.width(8.dp))

                // 快速主题切换按钮 - 用于测试渐变效果
                QuickThemeToggle()

            }

        },
        actions = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {


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

                // 上传管理器按钮
                val uploadManager = GlobalUploadManager.instance
                val activeTasksCount = uploadManager.activeTasks.size

                if (activeTasksCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text(
                                    text = activeTasksCount.toString(),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    ) {
                        AddIconButton(
                            imageVector = Icons.Default.CloudUpload,
                            text = "上传管理器",
                            onClick = { showUploadManager = true }
                        )
                    }
                } else {
                    AddIconButton(
                        imageVector = Icons.Default.CloudUpload,
                        text = "上传管理器",
                        onClick = { showUploadManager = true }
                    )
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

    // 上传管理器对话框
    if (showUploadManager) {
        Dialog(
            onDismissRequest = { showUploadManager = false }
        ) {
            Card(
                modifier = Modifier
                    .width(800.dp)
                    .height(600.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                UploadManagerUI(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

