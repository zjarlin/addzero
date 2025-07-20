package com.addzero.web.infra.valid.entity


/**
 * @author zjarlin
 * @since 2024/1/4 13:06
 */
data class ValidVO(
    var errorNo: Int? = null,
    var successNo: Int? = null,
    var successList: List<*>? = null,
    var errorList: List<*>? = null
)