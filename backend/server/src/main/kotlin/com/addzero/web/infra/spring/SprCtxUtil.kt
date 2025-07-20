package com.addzero.web.infra.spring

import cn.hutool.extra.spring.SpringUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.ApplicationContext
import org.springframework.core.ResolvableType
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * @Description: spring上下文工具类
 * @author: jeecg-boot
 */
@Component
object SprCtxUtil {
    /**
     * 获取applicationContext
     *
     * @return
     */
    /**
     * 上下文对象实例
     */
    val applicationContext: ApplicationContext
        get() = SpringUtil.getApplicationContext()

    val httpServletRequest: HttpServletRequest
        /**
         * 获取HttpServletRequest
         */
        get() = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).request

    val httpServletResponse: HttpServletResponse
        /**
         * 获取HttpServletResponse
         */
        get() = (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).response as HttpServletResponse


    val origin: String
        get() {
            val request = httpServletRequest
            return request.getHeader("Origin")
        }


    fun <T, S> getBean(clazz: Class<T>, vararg generics: Class<*>): S {
        val applicationContext = SpringUtil.getApplicationContext()
        val beanProvider =
            applicationContext.getBeanProvider<Any>(ResolvableType.forClassWithGenerics(clazz, *generics))
        val ifAvailable = beanProvider.getIfAvailable()
        return ifAvailable as S
    }


}