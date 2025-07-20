package com.addzero.kmp.ui.infra.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

/**
 * 路由管理器组件
 *
 * 监听当前路由变化并进行相应的导航
 *
 * @param navController 导航控制器
 * @param navHostInitialized 导航图是否已初始化
 * @param menuViewModel 菜单视图模型
 */
@Composable
fun RouterManager(
    navController: NavController,
    navHostInitialized: Boolean,
    currentRouteKey: String
) {
    // 监听当前路由变化 - 确保在NavHost初始化后执行
    LaunchedEffect(currentRouteKey, navHostInitialized) {
        // 只有当导航图已初始化且有当前路由时才进行导航
        if (navHostInitialized && currentRouteKey.isNotEmpty()) {
            try {
                navController.navigate(currentRouteKey) {
                    // 避免重复导航
                    launchSingleTop = true
                    // 保存状态
                    restoreState = true
                }
            } catch (e: Exception) {
                // 导航异常处理
                println("导航异常: ${e.message}")
            }
        }
    }
} 