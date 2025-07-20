package com.addzero.web.modules.sys.ai.bean

import cn.hutool.ai.ModelName
import cn.hutool.ai.core.AIConfigBuilder
import cn.hutool.ai.model.deepseek.DeepSeekConfig
import cn.hutool.ai.model.doubao.DoubaoConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AiConfigDef(
    @Value("\${spring.ai.deepseek.api-key}") private val deepseekKey: String,
    @Value("\${doubao.key}") private val doubaoKey: String,

    ) {
    @Bean
    fun deepSeekConfig(): DeepSeekConfig {
        return AIConfigBuilder(ModelName.DEEPSEEK.value).setApiKey(deepseekKey).build() as DeepSeekConfig
    }


    @Bean
    fun doubaoConfig(): DoubaoConfig {
        return AIConfigBuilder(ModelName.DOUBAO.value).setApiKey(doubaoKey).build() as DoubaoConfig
    }


}
