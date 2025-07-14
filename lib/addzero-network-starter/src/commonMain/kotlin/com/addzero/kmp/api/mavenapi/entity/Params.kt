package com.addzero.kmp.generated.api.mavenapi.entity

import kotlinx.serialization.Serializable

@Serializable

data class Params(
    val core: String,
    val fl: String,
    val indent: String,
    val q: String,
    val rows: String,
    val sort: String,
    val spellcheck: String,
//    val spellcheckCount: String,
    val start: String,
    val version: String,
    val wt: String
)
