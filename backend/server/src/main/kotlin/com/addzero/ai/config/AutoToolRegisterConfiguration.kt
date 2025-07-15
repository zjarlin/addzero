package com.addzero.ai.config

import cn.hutool.extra.spring.SpringUtil
import org.springframework.ai.tool.annotation.Tool
import org.springframework.ai.tool.method.MethodToolCallbackProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Service

fun getFunctionObjects(): Array<Any?> {
    // 获取所有标记了@Service注解的Bean
    val applicationContext = SpringUtil.getApplicationContext()
    val serviceMap = applicationContext.getBeansWithAnnotation(Service::class.java)
    val toolObjects: MutableList<Any?> = ArrayList<Any?>()


    // 遍历所有Service Bean
    for (serviceBean in serviceMap.values) {
        var clazz: Class<*> = serviceBean.javaClass

        // 处理代理类
        if (clazz.getName().contains("$$")) {
            clazz = clazz.getSuperclass()
        }

        // 检查类中是否有@Tool注解的方法
        var hasToolMethod = false
        for (method in clazz.declaredMethods) {
            if (AnnotationUtils.findAnnotation<Tool?>(method, Tool::class.java) != null) {
                hasToolMethod = true
                break
            }
        }

        // 如果存在至少一个@Tool方法，则添加到工具对象列表
        if (hasToolMethod) {
            toolObjects.add(serviceBean)
        }
    }
    return toolObjects.toTypedArray()
}


/**
 * 自动扫描并注册带有@Tool注解的组件
 */
@Configuration
class AutoToolRegisterConfiguration() {
//    private lateinit var toolObjects: MutableList<Any?>

    @Bean
    @Primary
    fun methodToolCallbackProvider(): MethodToolCallbackProvider {
        // 获取所有带有@Tool注解的对象
        val functionObjects = getFunctionObjects()

        return MethodToolCallbackProvider.builder()
            .toolObjects(*functionObjects)
            .build()
    }
}


