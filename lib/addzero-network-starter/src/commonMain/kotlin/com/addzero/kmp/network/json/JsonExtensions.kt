package com.addzero.kmp.network.json

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule


val globalSerializersModule = SerializersModule {
    contextual(Any::class, AnySerializer)
}

val json = Json {
//    encodeDefaults = false
    //显示null
//    explicitNulls = true
    ignoreUnknownKeys = true
    isLenient = true
//    prettyPrint = true
    useAlternativeNames = false
    // 允许将值强制转换为目标类型
    coerceInputValues = true
    //注册Any序列化器
    serializersModule = globalSerializersModule
}


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

