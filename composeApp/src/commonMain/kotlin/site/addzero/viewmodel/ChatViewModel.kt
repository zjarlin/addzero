package site.addzero.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import site.addzero.BuildKonfig
import site.addzero.assist.api
import site.addzero.core.ext.nowLong
import site.addzero.core.network.apiClientWithSse
import site.addzero.generated.api.ApiProvider.chatApi

/**
 * 🤖 聊天视图模型
 *
 * 管理聊天界面的状态和业务逻辑，包含消息发送、重试、错误处理等功能
 */
@KoinViewModel
class ChatViewModel : ViewModel() {
    var showChatBot by mutableStateOf(false)

    // 聊天消息列表，包含消息内容、是否为用户消息、消息ID、是否可重试
    var chatMessages = mutableStateListOf<ChatMessage>()
        private set

    var chatInput by mutableStateOf("")

    // AI思考状态
    var isAiThinking by mutableStateOf(false)
        private set

    // 重试状态 - 记录正在重试的消息ID
    var retryingMessageId by mutableStateOf<String?>(null)
        private set


    /**
     * 发送消息
     * @param input 消息内容，如果为null则使用chatInput
     */
    fun sendMessage(input: String? = null) {
        val msg = input ?: chatInput
        if (msg.isNullOrEmpty()) return

        // 生成消息ID
        val messageId = generateMessageId()

        // 添加用户消息
        chatMessages.add(
            ChatMessage(
                id = messageId,
                content = msg,
                isUser = true,
                canRetry = false,
                isError = false
            )
        )
        chatInput = ""

        // 发送AI请求
        sendAiRequest(msg, messageId)
    }

    /**
     * 重试消息
     * @param messageId 要重试的消息ID
     */
    fun retryMessage(messageId: String) {
        val messageIndex = chatMessages.indexOfFirst { it.id == messageId }
        if (messageIndex == -1) return

        val message = chatMessages[messageIndex]
        if (!message.canRetry) return

        // 找到对应的用户消息
        val userMessageIndex = messageIndex - 1
        if (userMessageIndex < 0) return

        val userMessage = chatMessages[userMessageIndex]
        if (!userMessage.isUser) return

        // 移除错误的AI回复
        chatMessages.removeAt(messageIndex)

        // 设置重试状态
        retryingMessageId = messageId

        // 重新发送请求
        sendAiRequest(userMessage.content, messageId)
    }

    /**
     * 发送AI请求的核心方法
     */
    private fun sendAiRequest(message: String, messageId: String) {
        // 开始AI思考状态
        isAiThinking = true
        val aiMessageId = messageId + "_ai"

        // 添加初始的AI回复消息（空内容）
        chatMessages.add(
            ChatMessage(
                id = aiMessageId,
                content = "",
                isUser = false,
                canRetry = false,
                isError = false
            )
        )

        sselogic(message, aiMessageId)
//        flowlogic(message, aiMessageId)
//       extracted
    }

    private fun flowlogic(message: String, aiMessageId: String) {

        api {
            try {
                val askFlow = chatApi.askFlow(message)
                askFlow.collect {
                    val currentMessages = chatMessages.toMutableList()
                    val aiMessageIndex = currentMessages.indexOfFirst { it.id == aiMessageId }

                    if (aiMessageIndex != -1) {
                        val existingMessage = currentMessages[aiMessageIndex]
                        currentMessages[aiMessageIndex] = existingMessage.copy(
                            content = existingMessage.content + it
                        )
                        chatMessages.clear()
                        chatMessages.addAll(currentMessages)
                    }

                }
                // 完成后更新AI消息状态
                val finalMessages = chatMessages.toMutableList()
                val aiMessageIndex = finalMessages.indexOfFirst { it.id == aiMessageId }
                if (aiMessageIndex != -1) {
                    val finalMessage = finalMessages[aiMessageIndex]
                    finalMessages[aiMessageIndex] = finalMessage.copy(canRetry = true)
                    chatMessages.clear()
                    chatMessages.addAll(finalMessages)
                }
            } catch (e: Exception) {
                // 处理错误情况
                val errorMessages = chatMessages.toMutableList()
                val aiMessageIndex = errorMessages.indexOfFirst { it.id == aiMessageId }
                if (aiMessageIndex != -1) {
                    val errorMessage = errorMessages[aiMessageIndex]
                    errorMessages[aiMessageIndex] = errorMessage.copy(
                        content = "抱歉，回复出错了: ${e.message}",
                        isError = true,
                        canRetry = true
                    )
                    chatMessages.clear()
                    chatMessages.addAll(errorMessages)
                }

            } finally {
                isAiThinking = false
                retryingMessageId = null
            }

        }
    }

    private fun sselogic(message: String, aiMessageId: String) {
        viewModelScope.launch {
            // 使用SSE方式接收流式响应
            try {
                apiClientWithSse.sse(
                    //                    host = BASE_HOST,
                    //                    port = BASE_PORT,
                    //                    path = "/chat/ask",
                    request = {
                        this.method = HttpMethod.parse("GET")
                        url {
                            //                            takeFrom(ktorfit.baseUrl + "/chat/ask")
                            takeFrom(BuildKonfig.BASE_URL + "/chat/ask")
                            parameter("text", message)
                        }
                    }
                ) {
                    incoming.collect { event ->
                        val currentMessages = chatMessages.toMutableList()
                        val aiMessageIndex = currentMessages.indexOfFirst { it.id == aiMessageId }

                        if (aiMessageIndex != -1) {
                            val existingMessage = currentMessages[aiMessageIndex]
                            currentMessages[aiMessageIndex] = existingMessage.copy(
                                content = existingMessage.content + event.data
                            )
                            chatMessages.clear()
                            chatMessages.addAll(currentMessages)
                        }

                    }
                }

                // 完成后更新AI消息状态
                val finalMessages = chatMessages.toMutableList()
                val aiMessageIndex = finalMessages.indexOfFirst { it.id == aiMessageId }
                if (aiMessageIndex != -1) {
                    val finalMessage = finalMessages[aiMessageIndex]
                    finalMessages[aiMessageIndex] = finalMessage.copy(canRetry = true)
                    chatMessages.clear()
                    chatMessages.addAll(finalMessages)
                }
            } catch (e: Exception) {
                // 处理错误情况
                val errorMessages = chatMessages.toMutableList()
                val aiMessageIndex = errorMessages.indexOfFirst { it.id == aiMessageId }
                if (aiMessageIndex != -1) {
                    val errorMessage = errorMessages[aiMessageIndex]
                    errorMessages[aiMessageIndex] = errorMessage.copy(
                        content = "抱歉，回复出错了: ${e.message}",
                        isError = true,
                        canRetry = true
                    )
                    chatMessages.clear()
                    chatMessages.addAll(errorMessages)
                }
            } finally {
                isAiThinking = false
                retryingMessageId = null
            }
        }
    }

    /**
     * 开始新聊天 - 清空聊天记录
     */
    fun startNewChat() {
        chatMessages.clear()
        chatInput = ""
        isAiThinking = false
        retryingMessageId = null
    }

    /**
     * 生成唯一的消息ID
     */
    private fun generateMessageId(): String {
        return "msg_${nowLong()}_${(0..999).random()}"
    }

    /**
     * 检查是否有错误消息可以重试
     */
    fun hasRetryableMessages(): Boolean {
        return chatMessages.any { it.canRetry && it.isError }
    }

    /**
     * 重试所有失败的消息
     */
    fun retryAllFailedMessages() {
        val failedMessages = chatMessages.filter { it.canRetry && it.isError }
        failedMessages.forEach { message ->
            retryMessage(message.id)
        }
    }


}

/**
 * 💬 聊天消息数据类
 *
 * @property id 消息唯一标识符
 * @property content 消息内容
 * @property isUser 是否为用户消息
 * @property canRetry 是否可以重试（仅对AI错误消息有效）
 * @property isError 是否为错误消息
 * @property timestamp 消息时间戳
 */
data class ChatMessage(
    val id: String,
    val content: String,
    val isUser: Boolean,
    val canRetry: Boolean = false,
    val isError: Boolean = false,
    val timestamp: Long = nowLong()
)
