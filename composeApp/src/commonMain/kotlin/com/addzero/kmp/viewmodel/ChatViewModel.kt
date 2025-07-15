package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.core.ext.now
import com.addzero.kmp.core.ext.nowLong
import com.addzero.kmp.generated.api.ApiProvider.chatApi
import com.addzero.kmp.generated.api.ApiProvider.aiApi
import com.addzero.kmp.entity.sys.ai.AiPrompt
import com.addzero.kmp.ext.api
import org.koin.android.annotation.KoinViewModel
import org.koin.core.component.KoinComponent

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
        private set

    // AI思考状态
    var isAiThinking by mutableStateOf(false)
        private set

    // 重试状态 - 记录正在重试的消息ID
    var retryingMessageId by mutableStateOf<String?>(null)
        private set

    // 常用提示词列表
    var commonPrompts = mutableStateListOf<AiPrompt>()
        private set

    // 提示词加载状态
    var isLoadingPrompts by mutableStateOf(false)
        private set

    /**
     * 发送消息
     * @param input 消息内容，如果为null则使用chatInput
     */
    fun sendMessage(input: String? = null) {
        val msg = input ?: chatInput
        if (msg.isBlank()) return

        // 生成消息ID
        val messageId = generateMessageId()

        // 添加用户消息
        chatMessages.add(ChatMessage(
            id = messageId,
            content = msg,
            isUser = true,
            canRetry = false,
            isError = false
        ))
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

        api {
            try {
                val response = chatApi.chat(message)

                // 添加成功的AI回复
                chatMessages.add(ChatMessage(
                    id = messageId + "_ai",
                    content = response ?: "🤖 响应为空",
                    isUser = false,
                    canRetry = false,
                    isError = false
                ))

            } catch (e: Exception) {
                // 处理错误
                val errorMsg = when {
                    e.message?.contains("timeout", ignoreCase = true) == true ->
                        "抱歉，AI响应超时了，请点击重试 ⏰"
                    e.message?.contains("network", ignoreCase = true) == true ->
                        "网络连接异常，请检查网络后重试 🌐"
                    e.message?.contains("server", ignoreCase = true) == true ->
                        "服务器暂时不可用，请稍后重试 🔧"
                    else -> "抱歉，发生了错误：${e.message} 😔\n点击重试按钮重新发送"
                }

                // 添加错误消息（可重试）
                chatMessages.add(ChatMessage(
                    id = messageId + "_ai",
                    content = errorMsg,
                    isUser = false,
                    canRetry = true,
                    isError = true
                ))

            } finally {
                // 结束AI思考状态和重试状态
                isAiThinking = false
                retryingMessageId = null
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

    /**
     * 🔄 加载常用提示词
     */
    fun loadCommonPrompts() {
        // 如果已经加载过了，就不重复加载
        if (commonPrompts.isNotEmpty()) return

        api(isLoadingPrompts, onLodingChange = { isLoadingPrompts = it }) {
            try {
                val prompts = aiApi.getPrompts(category = null)
                commonPrompts.clear()
                commonPrompts.addAll(prompts)
            } catch (e: Exception) {
                // 如果API调用失败，使用默认提示词
                loadDefaultPrompts()
            }
        }
    }

    /**
     * 🚀 初始化ViewModel（在UI中调用）
     */
    fun initialize() {
        loadCommonPrompts()
    }

    /**
     * 📝 加载默认提示词（作为备用方案）
     */
    private fun loadDefaultPrompts() {
        val defaultPrompts = listOf(
            AiPrompt(
                id = "default_1",
                title = "代码审查助手",
                content = "帮我审查这段代码的质量和最佳实践",
                category = "编程开发",
                tags = listOf("代码审查"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "default_2",
                title = "SQL优化专家",
                content = "请优化这个SQL查询的性能",
                category = "数据库",
                tags = listOf("SQL优化"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "default_3",
                title = "API设计师",
                content = "设计一个RESTful API接口",
                category = "编程开发",
                tags = listOf("API设计"),
                isBuiltIn = true
            ),
            AiPrompt(
                id = "default_4",
                title = "Bug分析专家",
                content = "分析这个bug的根本原因",
                category = "问题解决",
                tags = listOf("bug分析"),
                isBuiltIn = true
            )
        )

        commonPrompts.clear()
        commonPrompts.addAll(defaultPrompts)
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
