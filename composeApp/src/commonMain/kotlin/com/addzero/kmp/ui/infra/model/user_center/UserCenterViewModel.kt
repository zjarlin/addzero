package com.addzero.kmp.ui.infra.model.user_center

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.core.network.AddHttpClient
import com.addzero.kmp.assist.api
import com.addzero.kmp.generated.api.ApiProvider.sysUserCenterApi
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class UserCenterViewModel(
    private val loginViewModel: LoginViewModel
) : ViewModel() {

//    val userInfo by derivedStateOf {
//        loginViewModel.currentLoginUser
//    }

    // 加载状态
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    // 密码重置状态
    var isResettingPassword by mutableStateOf(false)

    var newPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var passwordErrorMsg by mutableStateOf<String?>(null)


    // 初始化时加载用户数据
    init {
//        val loginViewModel = LoginUtil.viewModel
        loadUserData()
    }

    // 从服务加载用户数据
    fun loadUserData() {
        api(isLoading, { isLoading = it }) {
            val jsonResponse = sysUserCenterApi.getCurrentUser()
            loginViewModel.currentToken = jsonResponse
        }
    }


    fun confirmPasswordReset(): Boolean {
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            passwordErrorMsg = "请填写密码"
            return false
        }
        if (newPassword != confirmPassword) {
            passwordErrorMsg = "两次输入的密码不一致"
            return false
        }

        api(isLoading, { isLoading = it }) {

            val success = sysUserCenterApi.updatePassword(newPassword)


            if (success) {
                loadUserData()
                isResettingPassword = false
                newPassword = ""
                confirmPassword = ""
                passwordErrorMsg = null
            }
        }

        return true
    }

    // 用户登出
    fun logout() {
        api {
            loginViewModel.logout()
            val logout = sysUserCenterApi.logout()

            AddHttpClient.setToken(null)

        }

    }

    fun cancelPasswordReset() {
        isResettingPassword = false
    }

    fun startPasswordReset() {
        isResettingPassword = true


    }
}
