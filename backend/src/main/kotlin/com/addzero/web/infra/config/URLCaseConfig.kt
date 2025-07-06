package com.addzero.web.infra.config

import org.springframework.context.annotation.Configuration
import org.springframework.util.AntPathMatcher
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * rest路径不区分大小写配置
 * @author zjarlin
 * @see WebMvcConfigurer
 *
 * @since 2024/01/13
 */
@Configuration
open class URLCaseConfig : WebMvcConfigurer {
    override fun configurePathMatch(configurer: PathMatchConfigurer) {
        val matcher = AntPathMatcher()
        matcher.setCaseSensitive(false)
        configurer.setPathMatcher(matcher)
    }
}