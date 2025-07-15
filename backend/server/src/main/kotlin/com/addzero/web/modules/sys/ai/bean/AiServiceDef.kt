package com.addzero.web.modules.sys.ai.bean

import cn.hutool.ai.AIServiceFactory
import cn.hutool.ai.model.deepseek.DeepSeekConfig
import cn.hutool.ai.model.deepseek.DeepSeekService
import cn.hutool.ai.model.doubao.DoubaoConfig
import cn.hutool.ai.model.doubao.DoubaoService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiServiceDef(
    private val deepSeekConfig: DeepSeekConfig,
    private val doubaoConfig: DoubaoConfig,

    ) {


    @Bean
    fun deepSeekService(): DeepSeekService {
        return AIServiceFactory.getAIService(deepSeekConfig, DeepSeekService::class.java)
    }


    @Bean
    fun doubaoService(): DoubaoService {
        return AIServiceFactory.getAIService(doubaoConfig, DoubaoService::class.java)
    }


}
