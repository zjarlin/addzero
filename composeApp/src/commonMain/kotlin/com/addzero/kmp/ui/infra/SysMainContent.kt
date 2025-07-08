package com.addzero.kmp.ui.infra

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.addzero.kmp.di.NavgationService
import com.addzero.kmp.generated.RouteKeys
import com.addzero.kmp.generated.RouteTable
import com.addzero.kmp.ui.infra.theme.MainContentGradientBackground
import com.addzero.kmp.ui.infra.theme.ThemeViewModel

/**
 * 主内容区组件
 *
 * @param navController 导航控制器
 */
@Composable
fun MainContent(navController: NavHostController) {
    val currentTheme = ThemeViewModel.currentTheme

    MainContentGradientBackground(
        themeType = currentTheme,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 0.dp,
            color = androidx.compose.ui.graphics.Color.Transparent // 透明背景显示渐变
        ) {
            // 渲染导航内容
//            navController.currentDestination
            renderNavContent(navController)

            // 标记导航图已初始化
            // 这个DisposableEffect会在组合完成后执行，此时NavHost已经完全初始化
//            DisposableEffect(Unit) {
//                onNavHostInitialized()
//                onDispose {}
//            }
        }
    }
}

@Composable
fun renderNavContent(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = RouteKeys.HOME_SCREEN,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // 动态生成导航目标
        RouteTable.allRoutes.forEach { (route, content) ->
            composable(route) {
                content()
            }
        }
    }

    NavgationService.initialize(navController)


}
