package com.addzero.kmp.generated.api.mavenapi.entity

import kotlinx.serialization.Serializable

@Serializable

data class Response(
    val docs: List<Doc>,
    val numFound: Int,
    val start: Int
)
