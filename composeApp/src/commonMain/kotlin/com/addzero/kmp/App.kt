package com.addzero.kmp

import androidx.compose.runtime.Composable
import com.addzero.kmp.component.toast.ToastListener
import com.addzero.kmp.events.EventBusConsumer
import com.addzero.kmp.events.emitEventBus
import com.addzero.kmp.ui.auth.LoginScreen
import com.addzero.kmp.ui.infra.MainLayout
import com.addzero.kmp.ui.infra.theme.AppThemes
import com.addzero.kmp.ui.infra.theme.FollowSystemTheme
import com.addzero.kmp.ui.infra.theme.GradientThemeWrapper
import com.addzero.kmp.ui.infra.theme.ThemeViewModel
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.addzero.kmp")
class MyModule

@Composable
fun App() {
    initKoin()
    emitEventBus()
    EventBusConsumer()
    val loginViewModel = koinViewModel<LoginViewModel>()
    val themeViewModel = koinViewModel<ThemeViewModel>()
    // 已登录时渲染主界面
    val currentTheme = themeViewModel.currentTheme
    val colorScheme = AppThemes.getColorScheme(currentTheme)

    FollowSystemTheme(colorScheme = colorScheme) {
        GradientThemeWrapper(themeType = currentTheme) {
//            AppContent(loginViewModel)
            MainLayout()
            ToastListener()
        }
    }
}

@Composable
private fun AppContent(loginViewModel: LoginViewModel) {
    if (loginViewModel.currentToken == null
//        && AddHttpClient .getCurrentToken()==null
    ) {
        // 未登录时只渲染登录页
        LoginScreen()
//            LoginUtil.cleanViewModel()
    } else {
        MainLayout()
    }
}

@Composable
private fun initKoin() {
    startKoin {
        printLogger()
        modules(
            MyModule().module
        )
    }
}

