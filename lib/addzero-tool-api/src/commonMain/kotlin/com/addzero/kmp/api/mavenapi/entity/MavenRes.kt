package com.addzero.kmp.api.mavenapi.entity

import kotlinx.serialization.Serializable

@Serializable
data class MavenRes(
    val response: Response,
    val responseHeader: ResponseHeader,
    val spellcheck: Spellcheck
)
