package com.addzero.ai.example

import com.addzero.ai.config.AiCtx
import org.springframework.stereotype.Component

/**
 * MCP工具使用示例
 * 展示如何在代码中使用自动注册的MCP工具
 */
@Component
class McpUsageExample {

    /**
     * 示例：使用AI聊天功能调用MCP工具
     */
    fun exampleChatWithTools() {
        // 获取配置了MCP工具的ChatClient
        val chatClient = AiCtx.defaultChatClient("ollamaChatModel")
        
        // 示例1：询问时间（会自动调用getCurrentTime工具）
        val timeResponse = chatClient.prompt()
            .user("请告诉我现在的时间")
            .call()
            .content()
        
        println("时间查询结果: $timeResponse")
        
        // 示例2：询问天气（会自动调用getCurrentWeather工具）
        val weatherResponse = chatClient.prompt()
            .user("请告诉我北京的天气情况")
            .call()
            .content()
        
        println("天气查询结果: $weatherResponse")
        
        // 示例3：数学计算（会自动调用calculate工具）
        val calcResponse = chatClient.prompt()
            .user("请帮我计算 25 * 4 的结果")
            .call()
            .content()
        
        println("计算结果: $calcResponse")
        
        // 示例4：系统信息（会自动调用getSystemInfo工具）
        val systemResponse = chatClient.prompt()
            .user("请告诉我系统的基本信息")
            .call()
            .content()
        
        println("系统信息: $systemResponse")
    }

    /**
     * 示例：复杂对话，可能涉及多个工具调用
     */
    fun exampleComplexConversation() {
        val chatClient = AiCtx.defaultChatClient("ollamaChatModel")
        
        val response = chatClient.prompt()
            .user("""
                请帮我做以下事情：
                1. 告诉我现在的时间
                2. 查询上海的天气
                3. 计算 100 除以 4 的结果
                4. 生成一个1到100之间的随机数
                请分别完成这些任务并给出结果。
            """.trimIndent())
            .call()
            .content()
        
        println("复杂对话结果: $response")
    }

    /**
     * 示例：错误处理
     */
    fun exampleErrorHandling() {
        val chatClient = AiCtx.defaultChatClient("ollamaChatModel")
        
        try {
            val response = chatClient.prompt()
                .user("请计算 10 除以 0")
                .call()
                .content()
            
            println("计算结果: $response")
        } catch (e: Exception) {
            println("发生错误: ${e.message}")
        }
    }
}
