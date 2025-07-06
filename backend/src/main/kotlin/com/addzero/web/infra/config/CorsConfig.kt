package com.addzero.web.infra.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 跨域配置
 * @author zjarlin
 * @date 2025/03/26
 * @constructor 创建[CorsConfig]
 */
@Configuration // 一定不要忽略此注解
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // 所有接口
            .allowCredentials(true) // 是否发送 Cookie
            .allowedOriginPatterns("*") // 支持域
            .allowedMethods(*arrayOf<String>("GET", "POST", "PUT", "DELETE")) // 支持方法
            .allowedHeaders("*")
            .exposedHeaders("*")
    }
}