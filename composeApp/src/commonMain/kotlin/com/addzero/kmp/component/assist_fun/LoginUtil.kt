//package com.addzero.kmp.component.assist_fun
//
//import androidx.compose.runtime.Composable
//import com.addzero.kmp.ui.infra.model.navigation.RecentTabsManager
//import com.addzero.kmp.viewmodel.LoginViewModel
//import org.koin.compose.viewmodel.koinViewModel
//
//object LoginUtil {
//     lateinit var viewModel: LoginViewModel
//
//    @Composable
//    fun hasPermition(permissionCode: String): Boolean {
//        val hasPermition = viewModel.hasPermission(permissionCode)
//        if (!hasPermition) return true
//        return true
//    }
//
//    fun init(model: LoginViewModel) {
//        viewModel = model
//    }
//
//    @Composable
//    fun cleanViewModel() {
//        viewModel = koinViewModel<LoginViewModel>()
//        val recentTabsManager = koinViewModel<RecentTabsManager>()
//        recentTabsManager.clear()
//    }
//
//}
