package com.addzero.web.infra.jackson

import cn.hutool.extra.spring.SpringUtil
import com.addzero.kmp.core.ext.parseListByKtxByKClass
import com.addzero.kmp.core.ext.parseObjectByKtx
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.datetime.LocalDate
import org.babyfish.jimmer.jackson.ImmutableModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import kotlin.reflect.KClass


//fun main() {
//    val parse = LocalDateTime.parse("2022-01-01")
//    println()}

val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
    .optionalStart()
    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
    .optionalEnd()
    .toFormatter()


val objectMapper: ObjectMapper = SpringUtil.getBean(ObjectMapper::class.java)
    .registerModule(KotlinModule.Builder().build())
    .registerModule(JavaTimeModule().apply {
        addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateTimeFormatter))
        addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(dateTimeFormatter))
    })
    .registerModule(ImmutableModule())
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


inline fun <reified T> JsonNode.getList(): List<T> =
    objectMapper.convertValue(this, object : TypeReference<List<T>>() {})

inline fun <reified T> JsonNode.getMutableList(): MutableList<T> =
    objectMapper.convertValue(this, object : TypeReference<MutableList<T>>() {})

inline fun <reified T> String.parseObject(): T = objectMapper.readValue(this, object : TypeReference<T>() {})


fun String.parseObject(): ObjectNode {
    return objectMapper.readTree(this) as ObjectNode
}

fun Any.toJson(): String =
    objectMapper.writeValueAsString(this)

fun <K, V> Map<K, V>.toJson(): String =
    objectMapper.writeValueAsString(this)

fun String.toJsonNode(): JsonNode =
    objectMapper.readTree(this)

inline fun <reified K, reified V> String.toMap(): Map<K, V> =
    objectMapper.readValue(this, object : TypeReference<Map<K, V>>() {})

inline fun <reified T> String.toList(): List<T> =
    objectMapper.readValue(this, object : TypeReference<List<T>>() {})

inline fun <reified K : Any, reified V : Any> String.toMapFromArray(
    fieldName: String,
    keyField: String,
    valueField: String
): Map<K, V> {
    val node: JsonNode = objectMapper.readTree(this)
    return node[fieldName].associate { item ->
        val key = objectMapper.convertValue(item[keyField], K::class.java)
        val value = objectMapper.convertValue(item[valueField], V::class.java)
        key to value
    }
}


inline fun <reified T> Any.convertTo(): T {
    val toJson = this.toJson()
    val parseObjectByKtx = toJson.parseObjectByKtx<T>()
    return parseObjectByKtx
}


fun <T : Any> Any.convertToList(kclass: KClass<T>): List<T> {
    val toJson = this.toJson()
    return toJson.parseListByKtxByKClass(kclass)
}

//fun <T> Any.convertToNoInline(Kclass: Class<T>): T {
//    val toJson = this.toJson()
//    val parseObjectByKtx = parseObjectByKtx(toJson, Kclass)
//    return parseObjectByKtx
//}


//fun String.parseObject(): ObjectNode {
//    val parseObject = parseObject(this)
//    return parseObject
//}
