package com.addzero.kmp.di

import androidx.navigation.NavHostController
import com.addzero.kmp.generated.RouteKeys

object NavgationService {
    private lateinit var navController: NavHostController

    fun initialize(controller: NavHostController) {
        navController = controller
    }

    // 业务导航方法
    fun goHome() {
        navController.navigate(RouteKeys.HOME_SCREEN)
    }

    fun navigate(key: String) {
        navController.navigate(key)
    }

    fun goBack() {
        navController.popBackStack()
    }

    fun getNavController(): NavHostController {
        return navController
    }
}
