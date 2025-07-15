package com.addzero.ai.agent.dbdesign

import com.addzero.ai.config.ChatModels
import com.addzero.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.kmp.entity.ai.FormDTO
import org.springframework.ai.tool.annotation.Tool
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController(value = "/agent")
class DbDesignController {


    @GetMapping("dbdesign")
    @Tool(description = "设计数据库")
    fun dbdesign(@RequestParam modelName: String = ChatModels.OLLAMA, @RequestParam ques: String): FormDTO? {
        val template = """
           你是一个DBA专家,请根据用户的内容设计数据库
           以下是我给定的内容{question}
        """.trimIndent()
        val ask = AiUtil.ask(ques, template, modelName, FormDTO::class.java)
        return ask
    }
}
