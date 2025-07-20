@file:OptIn(ExperimentalTime::class)

package com.addzero.kmp.component.form.date

import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

/**
 * 日期格式化工具类
 * 提供日期的格式化和解析功能
 */
object DateFormatter {

    /**
     * 日期显示格式枚举
     */
    enum class DatePattern {
        /** 标准格式: yyyy-MM-dd */
        STANDARD,

        /** 斜杠格式: yyyy/MM/dd */
        SLASH,

        /** 中文格式: yyyy年MM月dd日 */
        CHINESE,

        /** 简短格式: yy-MM-dd */
        SHORT
    }

    /**
     * 将日期值格式化为指定格式的字符串
     *
     * @param value 日期值，可以是Long时间戳或字符串
     * @param pattern 日期格式模式
     * @return 格式化后的日期字符串
     */
    fun formatDateForDisplay(value: Any?, pattern: DatePattern = DatePattern.STANDARD): String {
        return when (pattern) {
            DatePattern.STANDARD -> formatDate(value)
            DatePattern.SLASH -> formatDateWithSlash(value)
            DatePattern.CHINESE -> formatDateWithChinese(value)
            DatePattern.SHORT -> {
                val standard = formatDate(value)
                if (standard.isNotEmpty() && standard.length > 5) {
                    standard.substring(2) // 去掉年份前两位
                } else {
                    standard
                }
            }
        }
    }

    /**
     * 将日期字符串解析为时间戳
     *
     * @param dateStr 日期字符串，格式为 yyyy-MM-dd
     * @return 解析后的时间戳，解析失败则返回null
     */
    fun parseDate(dateStr: String): Long? {
        return dateToTimestamp(dateStr)
    }

    /**
     * 将日期值格式化为标准日期字符串 (yyyy-MM-dd)
     *
     * @param value 日期值，可以是Long时间戳或已格式化的字符串
     * @return 格式化后的日期字符串
     */
    fun formatDate(value: Any?): String {
        return when (value) {
            is Long -> {
                try {
                    val instant = Instant.fromEpochMilliseconds(value)
                    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                    "${localDate.year}-${
                        localDate.monthNumber.toString().padStart(2, '0')
                    }-${localDate.dayOfMonth.toString().padStart(2, '0')}"
                } catch (e: Exception) {
                    ""
                }
            }

            is String -> {
                if (isValidDateString(value)) value else ""
            }

            null -> ""
            else -> ""
        }
    }

    /**
     * 将日期值格式化为斜杠日期字符串 (yyyy/MM/dd)
     *
     * @param value 日期值，可以是Long时间戳或已格式化的字符串
     * @return 格式化后的日期字符串
     */
    fun formatDateWithSlash(value: Any?): String {
        val dateStr = formatDate(value)
        return if (dateStr.isNotEmpty()) dateStr.replace("-", "/") else ""
    }

    /**
     * 将日期值格式化为中文日期字符串 (yyyy年MM月dd日)
     *
     * @param value 日期值，可以是Long时间戳或已格式化的字符串
     * @return 格式化后的日期字符串
     */
    fun formatDateWithChinese(value: Any?): String {
        val dateStr = formatDate(value)
        if (dateStr.isEmpty()) return ""

        val parts = dateStr.split("-")
        if (parts.size != 3) return ""

        return "${parts[0]}年${parts[1]}月${parts[2]}日"
    }

    /**
     * 将日期值转换为时间戳
     *
     * @param value 日期值，可以是时间戳或格式化的字符串
     * @return 时间戳，如果转换失败则返回null
     */
    fun dateToTimestamp(value: Any?): Long? {
        return when (value) {
            is Long -> value
            is String -> {
                if (!isValidDateString(value)) return null
                try {
                    val parts = value.split("-")
                    if (parts.size != 3) return null

                    val year = parts[0].toInt()
                    val month = parts[1].toInt()
                    val day = parts[2].toInt()

                    val localDate = LocalDate(year, month, day)
                    val localDateTime = LocalDateTime(localDate, LocalTime(0, 0, 0))
                    val instant = localDateTime.toInstant(TimeZone.currentSystemDefault())
                    instant.toEpochMilliseconds()
                } catch (e: Exception) {
                    null
                }
            }

            else -> null
        }
    }

    /**
     * 验证字符串是否为有效的日期字符串
     *
     * @param dateStr 待验证的日期字符串
     * @return 是否为有效日期
     */
    fun isValidDateString(dateStr: String): Boolean {
        if (!isValidDateFormat(dateStr)) return false

        try {
            val parts = dateStr.split("-")
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()

            // 检查年月日是否在有效范围内
            if (year < 1900 || year > 2100) return false
            if (month < 1 || month > 12) return false
            if (day < 1 || day > 31) return false

            // 验证特定月份的天数
            if (month == 2) {
                val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
                if (isLeapYear && day > 29) return false
                if (!isLeapYear && day > 28) return false
            } else if (month in listOf(4, 6, 9, 11) && day > 30) {
                return false
            }

            return true
        } catch (e: Exception) {
            return false
        }
    }

    /**
     * 验证字符串是否符合日期格式
     *
     * @param dateStr 待验证的日期字符串
     * @return 是否符合日期格式
     */
    fun isValidDateFormat(dateStr: String): Boolean {
        val regex = Regex("""\d{4}-\d{2}-\d{2}""")
        return regex.matches(dateStr)
    }
}
