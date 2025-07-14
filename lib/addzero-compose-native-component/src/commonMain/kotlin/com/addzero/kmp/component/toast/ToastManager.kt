package com.addzero.kmp.component.toast

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow


/**
 * 全局Toast消息管理器
 * 用于集中处理应用中的提示消息，特别是API错误
 */
object ToastManager {
    // 私有的消息流
    private val _messageFlow = MutableSharedFlow<ToastMessage>(extraBufferCapacity = 10)

    // 公开的只读消息流
    val messageFlow = _messageFlow.asSharedFlow()

    /**
     * 显示成功消息
     */
    suspend fun success(message: String) {
        _messageFlow.emit(ToastMessage(message, MessageType.SUCCESS))
    }

    /**
     * 显示错误消息
     */
    suspend fun error(message: String) {
        _messageFlow.emit(ToastMessage(message, MessageType.ERROR))
    }

    /**
     * 显示警告消息
     */
    suspend fun warning(message: String) {
        _messageFlow.emit(ToastMessage(message, MessageType.WARNING))
    }

    /**
     * 显示信息消息
     */
    suspend fun info(message: String) {
        _messageFlow.emit(ToastMessage(message, MessageType.INFO))
    }

}

/**
 * Toast消息数据类
 */
data class ToastMessage(
    val content: String,
    val type: MessageType
)

/**
 * Toast监听器组件，用于在UI层显示Toast消息
 */
@Composable
fun ToastListener() {
    var currentMessage by remember { mutableStateOf<String?>(null) }
    var messageType by remember { mutableStateOf(MessageType.INFO) }

    // 监听消息流
    LaunchedEffect(Unit) {
        ToastManager.messageFlow.collect { toastMessage ->
            currentMessage = toastMessage.content
            messageType = toastMessage.type
        }
    }

    // 显示Toast
    AddMessageToast(
        message = currentMessage,
        type = messageType,
        onDismiss = { currentMessage = null }
    )
}
