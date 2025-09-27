package site.addzero.core.ext

import site.addzero.core.network.json.json
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
inline fun <reified T> Any.convertToByKtx(): T {
    val toJsonByKtx = this.toJsonByKtx()
    return toJsonByKtx.parseObjectByKtx<T>()
}

// 非内联版本的 Bean 转 Map
@OptIn(InternalSerializationApi::class)
fun <T : Any> T.bean2map(clazz: KClass<T>): Map<String, Any?> {
    val jsonString = json.encodeToString(clazz.serializer(), this)
    return json.parseToJsonElement(jsonString).jsonObject.bean2map()
}
// Bean 转 Map
inline fun <reified T:Any> T.bean2map(): Map<String, Any?> {
    val jsonString = json.encodeToString(this)
    return json.parseToJsonElement(jsonString).jsonObject.bean2map()
}

// 新增：使用 KClass 的 map2bean 方法（非内联）
@OptIn(InternalSerializationApi::class)
fun <T : Any> Map<String, Any?>.map2bean(clazz: KClass<T>): T {
    val jsonElement = json.encodeToString(this)
    return json.decodeFromString(clazz.serializer(), jsonElement)
}

// JsonObject 转 Map
 fun JsonObject.bean2map(): Map<String, Any?> {
    return this.mapValues { (_, value) ->
        when (value) {
            is JsonObject -> value.bean2map()
            else -> value.jsonPrimitive.contentOrNull
        }
    }
}

inline fun <reified T> Map<String, Any?>.map2bean(): T {
    val convertToByKtx = this.convertToByKtx<T>()
    return convertToByKtx
}


@OptIn(InternalSerializationApi::class)
fun <T : Any> String.parseListByKtxByKClass(kttype: KClass<T>): List<T> {
    val itemSerializer = kttype.serializer()
    val listSerializer = ListSerializer(itemSerializer)
    val decodeFromString = json.decodeFromString(listSerializer, this)
    return decodeFromString
}


