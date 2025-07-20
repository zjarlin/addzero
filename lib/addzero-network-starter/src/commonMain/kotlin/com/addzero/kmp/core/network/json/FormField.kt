@file:OptIn(ExperimentalSerializationApi::class)

package com.addzero.kmp.core.network.json

import com.addzero.kmp.core.network.json.FormField.Companion.toFormField
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// 密封类表示字段的三种状态
@Serializable(with = FormFieldSerializer::class)
sealed class FormField<out T> {  // 注意添加 `out` 协变
    object Undefined : FormField<Nothing>()  // 保持为 object
    data class Null<T>(val dummy: Boolean = true) : FormField<T>()
    data class Value<T>(val data: T) : FormField<T>()

    companion object {

        fun <T> T?.undefined(): FormField<T> = Undefined  // 提供工厂方法
        fun <T> T?.toFormField(): FormField<T> = when (this) {
            null -> Null()
            else -> Value(this)
        }
    }
}

// 扩展方法
val <T> FormField<T>.value: T?
    get() = when (this) {
        is FormField.Value -> this.data
        is FormField.Null -> null
        FormField.Undefined -> null
    }

fun <T> FormField<T>.valueOr(default: T): T = when (this) {
    is FormField.Value -> this.data
    else -> default
}

fun <T> FormField<T>.valueOrNull(): T? = when (this) {
    is FormField.Value -> this.data
    else -> null
}

// 自定义序列化器（控制 JSON 输出）
class FormFieldSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<FormField<T>> {
    override val descriptor = dataSerializer.descriptor
    override fun serialize(encoder: Encoder, value: FormField<T>) {
        when (value) {
            is FormField.Value -> encoder.encodeSerializableValue(dataSerializer, value.data)
            is FormField.Null -> encoder.encodeNull()
            FormField.Undefined -> {}  // 不序列化
        }
    }

    override fun deserialize(decoder: Decoder): FormField<T> {
        val decodeNullableSerializableValue = decoder.decodeNullableSerializableValue(dataSerializer)
        return decodeNullableSerializableValue.toFormField()
    }
}
