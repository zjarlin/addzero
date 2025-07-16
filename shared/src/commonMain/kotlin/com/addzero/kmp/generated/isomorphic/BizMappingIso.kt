@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable


@Serializable
data class BizMappingIso(
    val id: Long? = null,
    val fromId: Long = 0,
    val toId: Long = 0,
    val mappingType: String = ""
)