@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable


@Serializable
data class CitysIso(
    val id: Int? = null,
    val areaId: String = "",
    val pinyin: String? = null,
    val py: String? = null,
    val areaName: String? = null,
    val cityName: String? = null,
    val provinceName: String? = null
)