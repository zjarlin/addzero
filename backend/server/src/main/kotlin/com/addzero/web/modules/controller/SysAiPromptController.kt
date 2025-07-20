package com.addzero.web.modules.controller

import com.addzero.model.entity.SysAiPrompt
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sysAiPrompt")
class SysAiPromptController(
    private val toolCallbackProvider: ToolCallbackProvider,

    ) {

    /**
     * 🔧 获取所有MCP工具描述
     *
     * @return 工具描述列表
     */
    @GetMapping("/getPrompts")
    fun getPrompts(): List<SysAiPrompt> {
        val tools = toolCallbackProvider.toolCallbacks
        val map = tools.map {
            val toolDefinition = it.toolDefinition
            val name = toolDefinition.name().trim()
            val description = toolDefinition.description().trim()
            SysAiPrompt {
                title = name
                content = description
                category = description
                tags = description
                isBuiltIn = true
            }
        }
        return map
    }

}