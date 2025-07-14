package com.addzero.kmp.component

import androidx.compose.runtime.*
import com.addzero.kmp.component.toast.ToastManager
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

// 1. 定义 CompositionLocal
val LocalCoroutineScope = staticCompositionLocalOf<CoroutineScope> {
    error("No CoroutineScope provided")
}

// 2. 全局异常处理器
val globalExceptionHandler = CoroutineExceptionHandler { ctx, throwable ->
    // 这里可以记录日志或发送到错误收集系统
    ctx
}

/**
 * 用这个包一下App
 * @param [content]
 */
@Composable
fun GlobalExceptionHandler(
    content: @Composable () -> Unit
) {
    // 错误状态管理
    var lastError by remember { mutableStateOf("") }

    // 3. 创建带有异常处理器的 CoroutineScope
    val coroutineScope = rememberCoroutineScope() + globalExceptionHandler

    // 4. 提供 CompositionLocal
    CompositionLocalProvider(
        LocalCoroutineScope provides coroutineScope
    ) {
        content()
    }

    // 5. 可选：监听全局错误并显示Toast
    LaunchedEffect(Unit) {
        coroutineScope.launch {

         ToastManager.error(lastError)
        }
    }
}


