@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual

@Serializable
data class SysUserIso(
    val price: String = "",
    val testInt: Int = 0,
    val id: Long? = null,
    val phone: String? = null,
    val email: String = "",
    val username: String = "",
    val password: String = "",
    val avatar: String? = null,
    val nickname: String? = null,
    val gender: String? = null,
    val depts: List<SysDeptIso> = emptyList(),
    val roles: List<SysRoleIso> = emptyList(),
    @Contextual val createTime: kotlinx.datetime.LocalDateTime = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    @Contextual val updateTime: kotlinx.datetime.LocalDateTime? = null
)