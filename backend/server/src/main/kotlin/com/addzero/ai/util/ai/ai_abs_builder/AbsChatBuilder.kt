package com.addzero.ai.util.ai.ai_abs_builder

import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.addzero.ai.config.AiCtx
import com.alibaba.fastjson2.toJSONString
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec
import org.springframework.ai.chat.client.ChatClient.PromptUserSpec
import org.springframework.ai.chat.memory.ChatMemory
import org.springframework.ai.chat.model.ChatResponse
import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.context.annotation.Description
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Flux
import java.util.function.Consumer

fun PromptUserSpec.useMedia(file: Array<MultipartFile>?) {
    val media = AiUtil.convertMultipartFilesToMedias(file)
    if (ArrayUtil.isNotEmpty(media)) {
        this.media(*media)
    }
}

fun PromptUserSpec.useStructuredOutput(
    question: String,
    promptTemplate: String,
    fomatJson: String,
    jsonComment: String,
) {
    if (StrUtil.isBlank(fomatJson)) {
        return
    }
    val (newPrompt, quesCtx) = AiCtx.structuredOutputContext(question, promptTemplate, fomatJson, jsonComment)
    this.text(newPrompt).params(quesCtx)
}


class AbsChatBuilder<V : VectorStore>(
    modelName: String,
    promptUserSpecConsumer: Consumer<PromptUserSpec>,
) {
    private val spec: ChatClientRequestSpec

    init {
        val build = AiCtx.defaultChatClient(modelName)
        val spec = build.prompt().user(promptUserSpecConsumer)
        this.spec = spec
    }

    fun buildToStream(): Flux<ServerSentEvent<String>> {
//        val httpServletResponse: HttpServletResponse = SprCtxUtil.httpServletResponse!!
//        httpServletResponse.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE

        val message = spec.stream().chatResponse().map { chatResponse: ChatResponse ->
            val toJson = chatResponse.toJSONString()
            ServerSentEvent.builder(toJson).event("message").build()
        }
        return message
    }


    fun PromptUserSpec.buildToStr(): String? {
        val call = spec.call()
        return call.content()
    }

    fun <T> buildToEntity(clazz: Class<T>): T? {
        return spec.call().entity(clazz)
    }

    fun usePromptTemplate(condition: Boolean, promptTemplate: String): AbsChatBuilder<V> {
        if (condition) {
            spec.system(promptTemplate)
        }
        return this
    }

    fun useChatHistory(condition: Boolean, sessionId: String): AbsChatBuilder<V> {
        if (condition) {
            val chatMemory = SpringUtil.getBean(ChatMemory::class.java)
            //会话记忆
//            spec.advisors( //会话记录注册
//                PromptChatMemoryAdvisor(chatMemory),  //回答带上会话记录
//                MessageChatMemoryAdvisor(chatMemory, sessionId, 10)
//            )
        }
        return this
    }

    fun useVectorStore(condition: Boolean, vectorStore: V): AbsChatBuilder<V> {
        if (condition) {
            val promptWithContext = """
                    下面是上下文信息
                    ---------------------
                    {question_answer_context}
                    ---------------------
                    给定的上下文和提供的历史信息，而不是事先的知识，回复用户的意见。如果答案不在上下文中，告诉用户你不能回答这个问题。

                    """.trimIndent()
            val TOP_K = 5
            val SIMILARITY_THRESHOLD = 0.85
            val request =
                SearchRequest.builder().query(promptWithContext).topK(TOP_K).similarityThreshold(SIMILARITY_THRESHOLD)
                    .build()

//            val questionAnswerAdvisor = QuestionAnswerAdvisor(vectorStore, request)
//            spec.advisors(questionAnswerAdvisor)
        }
        return this
    }

    companion object {
        private val FUNCTION_BEAN_NAMES: Array<String> = SpringUtil.getApplicationContext().getBeanNamesForAnnotation(
            Description::class.java
        ).filter { beanName ->
            val beanType = SpringUtil.getApplicationContext().getType(beanName)
            Function::class.java.isAssignableFrom(beanType)
        }.toTypedArray()

        /**
         * 构建 AbsChatBuilder 实例
         *
         * @param modelName             模型名称
         * @param vectorStore           向量存储
         * @param question              问题
         * @param sessionId             会话ID
         * @param enableVectorStore      是否启用向量存储
         * @param enableFunctionCalling 是否启用函数调用
         * @param promptTemplate        提示模板
         * @param file                  文件输入
         * @param formatJson            JSON 格式输出
         * @param formatJsonComment     JSON 格式注释
         * @return AbsChatBuilder 实例
         */
        fun <V : VectorStore> of(
            modelName: String,
            vectorStore: V,
            question: String,
            sessionId: String,
            enableVectorStore: Boolean?,
            promptTemplate: String,
            file: Array<MultipartFile>?,
            formatJson: String?,
            formatJsonComment: String?,
        ): AbsChatBuilder<VectorStore> {
            // 构建 AbsChatBuilder 实例
            val builder = AbsChatBuilder<VectorStore>(modelName) {
                it.text(question) // 设置问题
                // 处理结构化输出
                if (!formatJson.isNullOrEmpty() && !formatJsonComment.isNullOrEmpty()) {
                    it.useStructuredOutput(question, promptTemplate, formatJson, formatJsonComment)
                }
                // 处理文件输入
                it.useMedia(file)
            }
            val useFunctionCalling = builder.useChatHistory(StrUtil.isNotBlank(sessionId), sessionId)
                .usePromptTemplate(StrUtil.isNotBlank(promptTemplate), promptTemplate)
                .useVectorStore(enableVectorStore!!, vectorStore)
//            .useFunctionCalling(enableFunctionCalling!!)
            return useFunctionCalling
        }

    }
}
