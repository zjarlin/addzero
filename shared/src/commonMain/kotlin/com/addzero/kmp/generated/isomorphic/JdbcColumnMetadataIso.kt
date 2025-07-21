@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.addzero.kmp.generated.isomorphic

import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Contextual

@Serializable
data class JdbcColumnMetadataIso(
    val tableName: String? = null,
    val columnName: String? = null,
    val jdbcType: Long? = null,
    val columnType: String? = null,
    val columnLength: Long? = null,
    val nullableBoolean: Boolean? = null,
    val nullableFlag: String? = null,
    val remarks: String? = null,
    val defaultValue: String? = null,
    val primaryKeyFlag: String? = null,
    val tableId: Long? = null,
    val id: Long? = null,
    val updateBy: SysUserIso? = null,
    val createBy: SysUserIso? = null,
    @Contextual val createTime: kotlinx.datetime.LocalDateTime = kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    @Contextual val updateTime: kotlinx.datetime.LocalDateTime? = null
)