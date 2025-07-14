package com.addzero.web.modules.sys.login

import com.addzero.kmp.entity.CheckSignInput

fun identifyInputType(input: String): CheckSignInput {
    val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    val phoneRegex = Regex("^1[3-9]\\d{9}$")
    return when {
        emailRegex.matches(input) -> CheckSignInput.EMAIL
        phoneRegex.matches(input) -> CheckSignInput.PHONE
        else -> CheckSignInput.USERNAME
    }
}
