package site.addzero.ai.agent.dbdesign

import cn.hutool.extra.spring.SpringUtil
import site.addzero.ai.config.ChatModels
import site.addzero.ai.util.ai.ai_abs_builder.AiUtil
import site.addzero.entity.ai.FormDTO
import org.springframework.ai.tool.annotation.Tool
import org.springframework.stereotype.Repository
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
