package com.addzero.ai.config

import com.addzero.ai.config.AiCtx.defaultChatClient
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.deepseek.DeepSeekChatModel
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.ollama.OllamaEmbeddingModel
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

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
