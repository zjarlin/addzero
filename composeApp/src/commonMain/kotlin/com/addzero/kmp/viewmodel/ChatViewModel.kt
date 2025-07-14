package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.generated.api.ApiProvider.chatApi
import com.addzero.kmp.ext.api
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.KoinComponent

/**
 * 主布局组件
 *
 * 整体应用布局框架，包含顶栏、侧边菜单和主内容区
 */
@KoinViewModel
class ChatViewModel : ViewModel() {
    var showChatBot by mutableStateOf(false)
    var chatMessages = mutableStateListOf<Pair<Boolean, String>>()
        private set
    var chatInput by mutableStateOf("")
        private set

    // AI思考状态
    var isAiThinking by mutableStateOf(false)
        private set

    fun sendMessage(input: String? = null) {
        val msg = input ?: chatInput
        if (msg.isBlank()) return

        // 添加用户消息
        chatMessages.add(true to msg)
        chatInput = ""

        // 开始AI思考状态
        isAiThinking = true

        api {
            try {
                val joinToString = chatApi.chat(msg)
//                val resp = aiApi.ask(msg)
//                val joinToString = resp.choices?.map { it.message?.content }?.joinToString("\n")?:"🤖响应异常!"

                chatMessages.add(false to joinToString!!)
            } catch (e: Exception) {
                // 处理超时或其他错误
                val errorMsg = when {
                    e.message?.contains("timeout", ignoreCase = true) == true ->
                        "抱歉，AI响应超时了，请稍后重试 ⏰"
                    else -> "抱歉，发生了错误：${e.message} 😔"
                }
                chatMessages.add(false to errorMsg)
            } finally {
                // 结束AI思考状态
                isAiThinking = false
            }
        }
    }

    fun updateInput(value: String) {
        chatInput = value
    }

    /**
     * 开始新聊天 - 清空聊天记录
     */
    fun startNewChat() {
        chatMessages.clear()
        chatInput = ""
    }
}
