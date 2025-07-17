package com.addzero.kmp.api.mavenapi.entity

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable

data class Spellcheck(
    val suggestions: List<@Contextual Any>
)
