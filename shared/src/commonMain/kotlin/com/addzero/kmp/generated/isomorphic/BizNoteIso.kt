@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual

@Serializable
data class BizNoteIso(
    val leafFlag: Boolean = false,
    val children: List<BizNoteIso> = emptyList(),
    val parent: BizNoteIso? = null,
    val title: String = "",
    val content: String = "",
    val type: String? = null,
    val tags: List<BizTagIso> = emptyList(),
    val path: String? = null,
    val fileUrl: String? = null,
    val id: Long? = null,
    val updateBy: SysUserIso? = null,
    val createBy: SysUserIso? = null,
    @Contextual val createTime: kotlinx.datetime.LocalDateTime = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    @Contextual val updateTime: kotlinx.datetime.LocalDateTime? = null
)