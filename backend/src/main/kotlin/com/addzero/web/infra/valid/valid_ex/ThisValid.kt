package com.addzero.web.infra.valid.valid_ex

import jakarta.validation.Constraint

/**
 * 标记在类上,把this上下文带到校验逻辑里
 * @author zjarlin
 * @see Annotation
 *
 * @since 2024/01/04
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Retention(
    AnnotationRetention.RUNTIME
)
@Constraint(validatedBy = [ThisValidator::class])
annotation class ThisValid