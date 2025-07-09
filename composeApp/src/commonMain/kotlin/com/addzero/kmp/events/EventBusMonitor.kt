package com.addzero.kmp.events

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.addzero.kmp.component.toast.ToastManager
import com.addzero.kmp.core.network.AddHttpClient
import com.addzero.kmp.entity.Res
import com.addzero.kmp.viewmodel.LoginViewModel
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Forbidden
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EventBusConsumer() {
    val koinViewModel = koinViewModel<LoginViewModel>()
//    val hasPermition = koinViewModel.hasPermission(permissionCode)

    LaunchedEffect(Unit) {
        EventBus.consumer<HttpResponse> {
            if (this.status.value == 500) {
                show("系统异常")
//                show(this.bodyAsText())
            }
            val body = this.body<Res<Any>>()
            val message = body.message
            val code = body.code
            val status = HttpStatusCode(code, message)
            when (status) {
                OK -> {
                    if (message.isNotBlank()) {
                        ToastManager.info(message)
                    }
                }
                Unauthorized -> {
//                    LoginScreen()
//                    LoginUtil.viewModel= LoginViewModel()
                    koinViewModel.currentToken = null
                    AddHttpClient.setToken(null)

                    show(message)
                    // 401 未登录/Token失效，跳转登录页
//                    NavgationService.navigate(RouteKeys.LOGIN_SCREEN)
                }

                Forbidden -> {
                    // 403 无权限，可弹出提示
                    ToastManager.error("无权限访问")
                }

                BadRequest -> {
                    //业务异常警告
                    show(message)
                }
                // 处理自定义状态码
                HttpStatusCode(499, "Need More Info") -> {
                    // 比如弹窗、跳转、发事件等
                    ToastManager.info("请补充资料后再操作")
                    // 你也可以 navigate 到某个补充资料页面
                    // navigate(RouteKeys.COMPLETE_PROFILE)
                }
                // 你可以继续扩展更多状态码
                else -> { /* 其他全局处理 */
                    show(message)
                }
            }
        }
    }
}

private suspend fun show(message: String) {
    if (message.isNotBlank()) {
        ToastManager.warning(message)
    }
}

