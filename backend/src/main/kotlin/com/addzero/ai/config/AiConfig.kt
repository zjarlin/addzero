package com.addzero.ai.config

import cn.hutool.core.util.ReflectUtil
import com.addzero.ai.config.AiCtx.defaultChatClient
import com.addzero.ai.mcp.DictService
import com.addzero.kmp.generated.isomorphic.SysDictIso
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.deepseek.DeepSeekChatModel
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.ollama.OllamaEmbeddingModel
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.definition.ToolDefinition
import org.springframework.ai.tool.method.MethodToolCallback
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.util.json.schema.JsonSchemaGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener

@Configuration
class AiConfig(
    private val deepSeekChatModel: DeepSeekChatModel,
    private val ollamaEmbeddingModel: OllamaEmbeddingModel,
    private val toolCallbackProvider: ToolCallbackProvider
) {

    @Bean
    @Primary
    fun embeddingModel(): EmbeddingModel {
        return ollamaEmbeddingModel
    }

    @Bean
    fun tokenTextSplitter(): TokenTextSplitter {
        return TokenTextSplitter()
    }

//    @EventListener(ContextRefreshedEvent::class)
    @Bean
    fun chatClient(): ChatClient {
        val defaultChatClient = defaultChatClient(deepSeekChatModel)
        return defaultChatClient
    }



}
