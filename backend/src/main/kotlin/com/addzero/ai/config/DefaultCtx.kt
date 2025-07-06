package com.addzero.ai.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@ConfigurationProperties(prefix = "defaultctx")
@Configuration
class DefaultCtx {
    companion object {
        @Value("\${defaultctx.defaultChatModel}")
        lateinit var defaultChatModelName: String
    }
}
