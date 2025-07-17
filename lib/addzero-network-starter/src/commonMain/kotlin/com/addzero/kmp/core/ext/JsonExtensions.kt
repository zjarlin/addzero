package com.addzero.kmp.core.ext

import com.addzero.kmp.core.network.json.json

/**
 * 将JSON字符串解析为指定类型
 */
inline fun <reified T> String.parseObjectByKtx(): T {
    return json.decodeFromString(this)
}

/**
 * 将对象转换为JSON字符串
 */
inline fun <reified T> T.toJsonByKtx(): String {
    return json.encodeToString(this)
}

/**
 * 将对象转换为另一个对象
 */
internal inline fun <reified T> Any.convertToByKtx(): T {
    return this.toJsonByKtx().parseObjectByKtx<T>()
}

