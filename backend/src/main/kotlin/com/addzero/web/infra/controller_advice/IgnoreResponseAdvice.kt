package com.addzero.web.infra.controller_advice

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(
    AnnotationRetention.RUNTIME
) //排除响应处理注解
annotation class IgnoreResponseAdvice