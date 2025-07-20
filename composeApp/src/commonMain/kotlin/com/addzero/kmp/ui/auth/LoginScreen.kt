package com.addzero.kmp.ui.auth

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.runtime.Composable
import coil3.compose.AsyncImage
import com.addzero.kmp.entity.SignInStatus

import com.addzero.kmp.viewmodel.LoginViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
//@Route("系统页面", "登录页", routePath = "/signFirst")
fun LoginScreen() {
//    val viewModel = koinViewModel<LoginViewModel>()
    val viewModel = koinViewModel<LoginViewModel>()


//    val viewModel = LoginUtil.viewModel
    // 直接使用LoginComponent，该组件现在内部处理登录和注册

    when (viewModel.singinStatus) {
        SignInStatus.None -> {
            LoginComponent(
                logo = Icons.Default.EmojiPeople,
                onWeChatLogin = { /* 处理微信登录 */ }, onLogin = { viewModel.signFirst() }
//        onReg = { viewModel.register() },
            )

        }

        is SignInStatus.Notregister -> {
            RegisterComponent(
                logo = Icons.Default.EmojiPeople,
//                icon
                onReg = { viewModel.register() }, onBackToLogin = {
                    viewModel.singinStatus = SignInStatus.None
                }, onSendCode = {
                    //todo 发送验证码验证邮箱
                    println("code:$it")
                })
            return
        }

        is SignInStatus.Alredyregister -> {
            val model = viewModel.userRegFormState.avatar

            SecondLoginComponent(
                onSecondLogin = viewModel::onSecondLogin,
                onFrogetPassword = { viewModel.onForgetPassword() },
                customLogo = (if (model == null) null else {
                    {
                        AsyncImage(
                            model = model,
                            contentDescription = null,
                        )
                    }
                })
            )

        }

    }

}


