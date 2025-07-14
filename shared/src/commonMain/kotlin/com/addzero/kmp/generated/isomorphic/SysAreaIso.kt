@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable


@Serializable
data class SysAreaIso(
    val id: Long? = null,
    val parentId: Long? = null,
    val nodeType: String? = null,
    val name: String? = null,
    val areaCode: String? = null
)