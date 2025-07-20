package com.addzero.kmp.entity.low_table

enum class KmpType(val code: String) {
    LONG("Long"),
    INTEGER("Integer"),
    INT("Int"),
    SHORT("Short"),
    FLOAT("Float"),
    DOUBLE("Double"),
    BIGDECIMAL("BigDecimal"),
    LOCALDATE("LocalDate"),
    LOCALTIME("LocalTime"),
    LOCALDATETIME("LocalDateTime"),
    BOOLEAN("Boolean"),
    STRING("String")
}
