package com.addzero.kmp.api.mavenapi.entity

import kotlinx.serialization.Serializable

@Serializable

data class ResponseHeader(
    val QTime: Int,
    val params: Params,
    val status: Int
)
