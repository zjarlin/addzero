package com.addzero.ai.config

import cn.hutool.extra.spring.SpringUtil
import org.slf4j.LoggerFactory
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

fun getFunctionObjects(): Array<Any?> {
    val toolObjects: MutableList<Any?> = ArrayList()

    // 扫描多个注解类型
    processAnno(toolObjects, RestController::class.java)
    processAnno(toolObjects, Service::class.java)
    processAnno(toolObjects, Component::class.java)

    return toolObjects.toTypedArray()
}

private fun processAnno(toolObjects: MutableList<Any?>, annotationType: Class<out Annotation>): MutableList<Any?> {
    val applicationContext = SpringUtil.getApplicationContext()
    val beansMap = applicationContext.getBeansWithAnnotation(annotationType)

    for (bean in beansMap.values) {
        var clazz: Class<*> = bean.javaClass

        // 处理代理类
        if (clazz.name.contains("$$")) {
            clazz = clazz.superclass
        }

        // 检查类中是否有@Tool注解的方法
        var hasToolMethod = false
        for (method in clazz.declaredMethods) {
            if (AnnotationUtils.findAnnotation(method, Tool::class.java) != null) {
                hasToolMethod = true
                break
            }
        }

        // 如果存在至少一个@Tool方法，则添加到工具对象列表
        if (hasToolMethod && !toolObjects.contains(bean)) {
            toolObjects.add(bean)
        }
    }
    return toolObjects
}


/**
 * 自动扫���并注册带有@Tool注解的组件
 */
@Configuration
class AutoToolRegisterConfiguration : ApplicationListener<ContextRefreshedEvent> {
    private val logger = LoggerFactory.getLogger(AutoToolRegisterConfiguration::class.java)
    private var toolCallbackProvider: ToolCallbackProvider? = null

    @Bean
    fun toolCallbackProvider(): ToolCallbackProvider {
        return object : ToolCallbackProvider {
            override fun getToolCallbacks() = toolCallbackProvider?.getToolCallbacks() ?: emptyArray()
        }
    }

    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        logger.info("开始注册工具...")
        val functionObjects = getFunctionObjects()
        toolCallbackProvider = MethodToolCallbackProvider.builder()
            .toolObjects(*functionObjects)
            .build()
        logger.info("工具注册完成，共注册 ${functionObjects.size} 个工具")
    }
}
