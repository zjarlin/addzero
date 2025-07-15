package com.addzero.web.infra.monitor

/**
 * @author zjarlin
 * @date 2025/04/14
 * @constructor 创建[Monitor]
 * @param [value]
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MustBeDocumented
annotation class Monitor(
    /**
     * 日志描述信息
     */
    val value: String = ""
)
