@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable
import com.addzero.kmp.generated.enums.EnumShellPlatforms
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual

@Serializable
data class BizDotfilesIso(
    val osType: List<String> = emptyList(),
    val osStructure: EnumShellPlatforms? = null,
    val defType: String = "",
    val name: String = "",
    val value: String = "",
    val describtion: String? = null,
    val status: String = "",
    val fileUrl: String? = null,
    val location: String? = null,
    val id: Long? = null,
    val updateBy: SysUserIso? = null,
    val createBy: SysUserIso? = null,
    @Contextual val createTime: kotlinx.datetime.LocalDateTime = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    @Contextual val updateTime: kotlinx.datetime.LocalDateTime? = null
)