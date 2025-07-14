@file:OptIn(ExperimentalTime::class)

package com.addzero.kmp.core.ext

import kotlinx.datetime.*
import kotlinx.datetime.Clock.System.now
import kotlinx.datetime.format.byUnicodePattern
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


// 判断是否在今天
fun LocalDateTime.isToday(): Boolean {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    return year == today.year && month == today.month && dayOfMonth == today.dayOfMonth
}


/**
 * 格式化 LocalDateTime
 * @param pattern 格式模式，如 "yyyy-MM-dd HH:mm:ss"
 */
fun LocalDateTime.format(pattern: String): String {
    val format = LocalDateTime.Format {
        byUnicodePattern(pattern)
    }.format(this)
    return format

}


/**
 * 格式化 LocalDateTime
 * @param pattern 格式模式，如 "yyyy-MM-dd HH:mm:ss"
 */
fun LocalDate.format(pattern: String): String {
    val format = LocalDate.Format {
        byUnicodePattern(pattern)
    }.format(this)
    return format

}


/**
 * 格式化 LocalDateTime
 * @param pattern 格式模式，如 "yyyy-MM-dd HH:mm:ss"
 */
fun LocalTime.format(pattern: String): String {
    val format = LocalTime.Format {
        byUnicodePattern(pattern)
    }.format(this)
    return format

}

fun nowLong(): Long {
    val toEpochMilliseconds = now.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    return toEpochMilliseconds
}

@Deprecated("use nowLong instead")
fun nowInt(): Int {
    val now = now().toStdlibInstant().nanosecondsOfSecond
    return now
}


val now: LocalDateTime get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())


// 转换为友好显示 (如"2小时前")
fun LocalDateTime.toFriendlyString(): String {
    val now = now()
    val duration = now.toInstant(TimeZone.currentSystemDefault()) - this.toInstant(TimeZone.currentSystemDefault())

    return when {
        duration.inWholeDays > 365 -> "${duration.inWholeDays / 365}年前"
        duration.inWholeDays > 30 -> "${duration.inWholeDays / 30}个月前"
        duration.inWholeDays > 0 -> "${duration.inWholeDays}天前"
        duration.inWholeHours > 0 -> "${duration.inWholeHours}小时前"
        duration.inWholeMinutes > 0 -> "${duration.inWholeMinutes}分钟前"
        else -> "刚刚"
    }
}


// 判断是否是未来时间
fun LocalDateTime.isFuture(): Boolean = this > now

fun LocalDateTime.now(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
fun LocalDate.now(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
fun LocalTime.now(): LocalTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

fun String.toLocalDateTimeOrNull(): LocalDateTime? = runCatching { LocalDateTime.parse(this) }.getOrNull()
fun String.toLocalDateOrNull(): LocalDate? = runCatching { LocalDate.parse(this) }.getOrNull()
fun String.toLocalTimeOrNull(): LocalTime? = runCatching { LocalTime.parse(this) }.getOrNull()

