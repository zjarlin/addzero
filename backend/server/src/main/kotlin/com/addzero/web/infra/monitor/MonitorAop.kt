package com.addzero.web.infra.monitor

import com.addzero.web.infra.config.log
import com.fasterxml.jackson.databind.ObjectMapper
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.annotation.Configuration

@Aspect
@Configuration
open class MonitorAop(private val objectMapper: ObjectMapper) {

    //标记切入点，为指定包下的所有类的所有public方法
    @Around(value = "@annotation(Monitor)")
    fun around(joinPoint: ProceedingJoinPoint): Any? {
        val startTime = System.currentTimeMillis()
        val className: String = joinPoint.target.javaClass.getSimpleName()
        val methodName: String = joinPoint.signature.name
        var `object`: Any? = null
        try {
            `object` = joinPoint.proceed()
        } catch (e: Throwable) {
            //打印入参
            val args: Array<Any> = joinPoint.args
            val arg = objectMapper.writeValueAsString(args)
            //打印出参
            val res = objectMapper.writeValueAsString(`object`)

            val requestId = java.util.UUID.randomUUID().toString().replace("-", "")

            log.info("请求id:::{}", requestId)
            log.info("方法名:::{}.{}() :::入参:::{}", className, methodName, arg)
            log.info("方法名:::{}.{}() :::出参:::{}", className, methodName, res)
            throw java.lang.RuntimeException(e)
        }
        val time = System.currentTimeMillis() - startTime
        log.info("方法名:::{}.{}() 【执行时长为】:{}{}", className, methodName, time, " ms")
        return `object`
    }
}