//package com.addzero.web.modules.ai.util.ollama
//
//import com.addzero.web.modules.ai.config.DefaultCtx
//import com.addzero.web.modules.ai.util.ai.ctx.AiCtx
//import org.springframework.ai.chat.messages.SystemMessage
//import org.springframework.ai.chat.messages.UserMessage
//import org.springframework.ai.chat.prompt.Prompt
//import org.springframework.ai.ollama.OllamaChatModel
//import org.springframework.ai.ollama.api.OllamaOptions
//import org.springframework.stereotype.Component
//
///**
// * 为了AiUtil适配ollama 不能运行时切换模型，所以这里在封装一个
// * @author zjarlin
// * @date 2024/10/04
// * @constructor 创建[OllamaUtil]
// * @param [ollamaChatModel]
// */
//@Component
//class OllamaUtil(private val ollamaChatModel: OllamaChatModel) {
//
//    fun ask(
//        question: String,
//        promptTemplate: String = "",
//        chatModelStr: String = DefaultCtx.defaultChatModelName,
//        formatJson: String = "",
//        formatJsonComment: String = "",
//    ): String? {
//
//
//        val (newPromptTemplate, quesCtx) = AiCtx.structuredOutputContext(
//            question, promptTemplate, formatJson, formatJsonComment
//        )
//        val systemMessage = SystemMessage(newPromptTemplate)
//        val userMessage = UserMessage(question)
//        val metadata = userMessage.metadata
//        metadata.putAll(quesCtx)
//        //看下这里的合并效果
//        val listOf = listOf(systemMessage, userMessage)
//        val response = ollamaChatModel.call(
//            Prompt(
//                listOf, OllamaOptions.builder().withModel(chatModelStr)
//                    .withTemperature(0.4).build()
//            )
//        )
//        return response.result.output.content
//    }
//
//
//}