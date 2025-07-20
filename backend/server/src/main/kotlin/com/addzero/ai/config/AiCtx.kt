package com.addzero.ai.config

//import org.springframework.ai.ollama.OllamaChatModel
//import org.springframework.ai.ollama.api.OllamaOptions
import cn.hutool.core.util.ReflectUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.ai.util.ai.ai_abs_builder.AiUtil.Companion.buildStructureOutPutPrompt
import com.addzero.kmp.constant.Promts
import com.addzero.kmp.kt_util.JlStrUtil.cleanBlank
import com.addzero.kmp.kt_util.addPrefixIfNot
import com.addzero.kmp.kt_util.isBlank
import com.addzero.kmp.kt_util.isNotBlank
import com.alibaba.fastjson2.toJSONString
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.tool.ToolCallbackProvider

object AiCtx {

    /**
     * 结构化输出上下文
     * @param [question] 问题
     * @param [promptTemplate] 提示模板
     * @param [clazz] 提供类以自动生成 formatJson 和 jsonComment（可选）
     * @return [StructureOutPutPrompt]
     */
    fun structuredOutputContext(
        question: String,
        promptTemplate: String,
        clazz: Class<*>,
    ): StructureOutPutPrompt {

        val finalFormatJson = ReflectUtil.newInstance(clazz).toJSONString()
        val finalJsonComment = buildStructureOutPutPrompt(clazz)
        // 生成结构化输出的内容
        return structuredOutputContext(
            question.cleanBlank(), promptTemplate.cleanBlank(), finalFormatJson, finalJsonComment
        )
    }

    fun structuredOutputContext(
        question: String,
        promptTemplate: String?,
        formatJson: String?,
        jsonComment: String?,
    ): StructureOutPutPrompt {

        val que = "{question}"
        if (promptTemplate.isBlank() && formatJson.isBlank()) {
            val promptTemplate1 = promptTemplate.cleanBlank()
            val promptTemplate2 = StrUtil.addPrefixIfNot(promptTemplate1, que)
            return StructureOutPutPrompt(promptTemplate2, mapOf("question" to question))
        }
        if (promptTemplate.isBlank() && formatJson.isNotBlank()) {
            val promptTemplate1 = promptTemplate.cleanBlank()
            val promptTemplate2 = StrUtil.addPrefixIfNot(promptTemplate1, que)
            return StructureOutPutPrompt(promptTemplate2, mapOf("question" to question))
        }

        val formatPrompt: String = """
       ${Promts.JSON_PATTERN_PROMPT}
           {formatJson}}
            ------------------
           以下是json数据的注释：
           {jsonComment}
        """.trimIndent()
        val newPromptTemplate = promptTemplate + formatPrompt
        val quesCtx = mapOf(
            "question" to question, "formatJson" to formatJson, "jsonComment" to jsonComment
        )
        return StructureOutPutPrompt(newPromptTemplate.addPrefixIfNot(que), quesCtx)
    }

    data class StructureOutPutPrompt(val newPrompt: String, val quesCtx: Map<String, String?>)


    /**
     *
     *  #  * * 0 = "dashScopeAiVLChatModel"
     *   #  * * 1 = "dashScopeAiChatModel"
     *   #  * * 2 = "moonshotChatModel"
     *   #  * * 3 = "ollamaChatModel"        should be a list in  spring yml ?
     *   #  *         ------qwen2.5:1.5b
     *   #  *         ------qwen2.5-coder:1.5b
     *
     *   #  * * 4 = "openAiChatModel"
     *   #  * * 5 = "zhiPuAiChatModel"
     *
     *
     *
     *If the user-defined model name cannot be found, go to the ollama model to find it
     *  @param [modelName]
     * @return [ChatClient?]
     */
    fun defaultChatClient(modelName: String): ChatClient {
        val defaultChatModel = defaultChatModel(modelName)

        return defaultChatClient(defaultChatModel)
    }

    fun defaultChatClient(defaultChatModel: ChatModel): ChatClient {

        var buildOpt = defaultChatModel.defaultOptions
        val modelName = buildOpt.model


        //        if (defaultChatModel is OllamaChatModel) {
        //            val ollamaOptions = buildOpt as OllamaOptions
        //            ollamaOptions.model = modelName
        //            buildOpt = ollamaOptions as ChatOptions
        //        }
        //        defaultChatModel is dashsc

        //加入mcp服务 - 使用自动注册的工具


        val chatClientBuilder = ChatClient.builder(defaultChatModel)
            .defaultAdvisors(
                SimpleLoggerAdvisor({
                    val userText = it.prompt.contents
                    """
                            "request: " + $userText
                            "Custom model: " + $modelName
                            """.trimIndent()
                }, {
                    "Custom response: " + it.result
                }, 1)
            )

        // 只有当工具提供者不为空时才添加工具

        //默认注册tool注解
        val beansOfType = SpringUtil.getBean(ToolCallbackProvider::class.java)

        val toolCallbacks = beansOfType.toolCallbacks
        if (toolCallbacks.isNotEmpty()) {
            chatClientBuilder.defaultToolCallbacks(*toolCallbacks)
        }
        chatClientBuilder.defaultOptions(buildOpt)
        val chatClient = chatClientBuilder.build()
        return chatClient
    }

    private fun defaultChatModel(modelName: String): ChatModel {
//        val bean = SpringUtil.getBean<OllamaChatModel>(OllamaChatModel::class.java)
//        return bean


        val bean = SpringUtil.getBeansOfType<ChatModel>(ChatModel::class.java)
        val model = bean[modelName]
//        ?: SpringUtil.getBean<OllamaChatModel>(OllamaChatModel::class.java)
        return model!!
    }


}
