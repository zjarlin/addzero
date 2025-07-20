package com.addzero.kmp.exp

import com.addzero.kmp.enums.ErrorEnum

/**
 * 业务异常
 */
open class BizException(

    val description: String,

    val value: Int = 400,

    ) : RuntimeException(description) {

    constructor(errorEnum: ErrorEnum) : this(
        description = errorEnum.msg,
        value = errorEnum.code
    )
}
