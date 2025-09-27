package site.addzero.exp

import site.addzero.enums.ErrorEnum

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
