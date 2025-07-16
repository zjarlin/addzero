@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual

@Serializable
data class SysWeatherIso(
    @Contextual val date: kotlinx.datetime.LocalDate = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val highTemp: Long? = null,
    val lowTemp: Long? = null,
    val amCondition: String? = null,
    val pmCondition: String? = null,
    val wind: String? = null,
    val aqi: Long? = null,
    val areaId: String? = null,
    val areaType: String? = null,
    val week: String? = null,
    val id: Long? = null,
    val updateBy: SysUserIso? = null,
    val createBy: SysUserIso? = null,
    @Contextual val createTime: kotlinx.datetime.LocalDateTime = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    @Contextual val updateTime: kotlinx.datetime.LocalDateTime? = null
)