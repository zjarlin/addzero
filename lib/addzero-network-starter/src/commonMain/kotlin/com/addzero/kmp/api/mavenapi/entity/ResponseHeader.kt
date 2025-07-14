package com.addzero.kmp.generated.api.mavenapi.entity

import kotlinx.serialization.Serializable

@Serializable

data class ResponseHeader(
    val QTime: Int,
    val params: Params,
    val status: Int
)
