package com.addzero.exception

enum class ErrorCode(val code: Int, val message: String) {
    /**
     * 枚举值没有定义
     */
    ENUM_VALUE_IS_NOT_DEFINE(405, "value is not defined"),

    /**
     * 未知的 MIME 类型
     */
    UNKNOWN_MIME_TYPE(406, "unknown mime type"),
}