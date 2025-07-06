package com.addzero.web.infra.valid.valid_ex


import com.addzero.common.util.ThreadLocalUtil
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class ThisValidator : ConstraintValidator<ThisValid?, Any?> {
    override fun isValid(p0: Any?, p1: ConstraintValidatorContext?): Boolean {
        ThreadLocalUtil.set(p0)
        return true
    }
}