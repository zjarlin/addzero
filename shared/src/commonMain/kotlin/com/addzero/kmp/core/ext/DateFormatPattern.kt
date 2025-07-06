package com.addzero.kmp.core.ext

/**
 * 日期格式模式枚举
 */
enum class DateFormatPattern(val pattern: String) {
    // Date formats
    DATE_SHORT("yyyy-MM-dd"),
    DATE_DOT("yyyy.MM.dd"),
    DATE_XIE("yyyy/MM/dd"),

    DATE_MEDIUM("yyyy年MM月dd日"),

    // Time formats
    TIME_SHORT("HH:mm"),
    TIME_MEDIUM("HH:mm:ss"),

    // DateTime formats
    DATETIME_SHORT("yyyy-MM-dd HH:mm"),
    DATETIME_MEDIUM("yyyy-MM-dd HH:mm:ss"),
    DATETIME_LONG("yyyy年MM月dd日 HH时mm分ss秒"),
}