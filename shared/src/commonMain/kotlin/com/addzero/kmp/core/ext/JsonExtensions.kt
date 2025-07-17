package com.addzero.kmp.core.ext

import com.addzero.kmp.core.network.json.json
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

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


@OptIn(InternalSerializationApi::class)
fun <T : Any> String.parseListByKtxByKClass(kttype: KClass<T>): List<T> {
    val itemSerializer = kttype.serializer()
    val listSerializer = ListSerializer(itemSerializer)
    val decodeFromString = json.decodeFromString(listSerializer, this)
    return decodeFromString
}


