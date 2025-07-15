package com.addzero.common.util.data_structure.spo.entity


/**
 * @author zjarlin
 * @since 2024/3/29 12:46
 */
data class Spo(
    var subject: String,
    var predicate: String,
    var `object`: String?,
    var context: String?,
)