package com.addzero.web.infra.valid.valid_ex.custom

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class CustomValids(vararg val value: CustomValid = [])