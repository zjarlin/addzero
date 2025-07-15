package com.addzero.web.infra.jimmer.dynamicdatasource

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DS(val value: String)