package com.addzero.kmp.entity.low_table

import kotlinx.serialization.Serializable

/**
 * 表单验证器
 */
@Serializable
sealed class Validator {
    @Serializable
    data class Required(val message: String = "此字段为必填项") : Validator()

    @Serializable
    data class MinLength(val length: Int, val message: String = "最小长度为 $length 个字符") : Validator()

    @Serializable
    data class MaxLength(val length: Int, val message: String = "最大长度为 $length 个字符") : Validator()

    @Serializable
    data class Min(val value: Double, val message: String = "最小值为 $value") : Validator()

    @Serializable
    data class Max(val value: Double, val message: String = "最大值为 $value") : Validator()

    @Serializable
    data class Pattern(val regex: String, val message: String) : Validator()

    @Serializable
    data class Email(val message: String = "请输入有效的邮箱地址") : Validator()

    @Serializable
    data class URL(val message: String = "请输入有效的URL") : Validator()

    /**
     * 验证值是否有效
     */
    fun validate(value: Any?): Boolean {
        return when (this) {
            is Required -> value != null && value.toString().isNotEmpty()
            is MinLength -> value == null || value.toString().length >= length
            is MaxLength -> value == null || value.toString().length <= length
            is Min -> {
                if (value == null) true
                else when (value) {
                    is Number -> value.toDouble() >= this.value
                    else -> true
                }
            }
            is Max -> {
                if (value == null) true
                else when (value) {
                    is Number -> value.toDouble() <= this.value
                    else -> true
                }
            }
            is Pattern -> value == null || value.toString().matches(Regex(regex))
            is Email -> value == null || value.toString().matches(Regex("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"))
            is URL -> value == null || value.toString().matches(Regex("^(https?|ftp)://[^\\s/$.?#].[^\\s]*\$"))
        }
    }

    /**
     * 获取错误消息
     */
    fun getErrorMessage(): String {
        return when (this) {
            is Required -> message
            is MinLength -> message
            is MaxLength -> message
            is Min -> message
            is Max -> message
            is Pattern -> message
            is Email -> message
            is URL -> message
        }
    }
}
