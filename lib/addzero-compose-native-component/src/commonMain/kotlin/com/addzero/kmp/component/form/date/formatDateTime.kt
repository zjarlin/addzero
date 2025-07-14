package com.addzero.kmp.component.form.date

import com.addzero.kmp.core.ext.DateFormatPattern
import kotlinx.datetime.*
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlin.time.ExperimentalTime

@OptIn(FormatStringsInDatetimeFormats::class)
fun formatDateTime(
    millis: Long,
    pattern: DateFormatPattern,
    timeZone: TimeZone
): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val localDateTime = instant.toLocalDateTime(timeZone)


    val format = try {
        LocalDateTime.Format {
            byUnicodePattern(pattern.pattern)
        }.format(localDateTime)
    } catch (e: Exception) {
        return ""
    }
    return format

}

@ExperimentalTime
@OptIn(FormatStringsInDatetimeFormats::class)
fun parseDateTimeStringToMillis(
    value: String?,
    pattern: DateFormatPattern,
    timeZone: TimeZone
): Long? {
    if (value.isNullOrBlank()) return null
    return try {
        val localDateTime =
            LocalDateTime.parse(value, LocalDateTime.Format { byUnicodePattern(pattern.pattern) })
        localDateTime.toInstant(timeZone).toEpochMilliseconds()
    } catch (e: Exception) {
        null
    }
}
