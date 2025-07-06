package com.addzero.ai.mcp

import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class McpServer(
//    private val toolCallbackProvider: ToolCallbackProvider
) {
    @Tool(description = "获取当前时间")
    fun getCurrentTime(): String {
        val now = LocalDateTime.now()
        return now.toString()
    }


//    @Tool(description = "获取可用的工具列表")
//    fun getAListOfAvailableTools(): List<String> {
//        return toolCallbackProvider.toolCallbacks.map {
//            val toolDefinition = it.toolDefinition
//            val name = toolDefinition.name()
//            val description = toolDefinition.description()
//            description
//        }
//
//    }

}
