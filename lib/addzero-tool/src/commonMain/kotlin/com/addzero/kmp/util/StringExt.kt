package com.addzero.kmp.util

/**
 * 字符串首字母小写
 */
fun String.lowerFirst(): String {
    if (isEmpty()) return this
    return first().lowercase() + substring(1)
}





