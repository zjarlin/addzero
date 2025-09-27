package site.addzero.core.network.json

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*


object AnySerializer : KSerializer<Any> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Any")

    override fun serialize(encoder: Encoder, value: Any) {
        val jsonEncoder = encoder as? JsonEncoder?: throw SerializationException("This class can be loaded only by Json")
        val element = when (value) {
            is Boolean -> JsonPrimitive(value)
            is Number -> JsonPrimitive(value)
            is String -> JsonPrimitive(value)
            is Map<*, *> -> {
                val content = value.entries.associate { (k, v) ->
                    k.toString() to (v ?: JsonNull)
                }
                JsonObject(content.mapValues { (_, v) ->
                    v as? JsonElement ?: jsonEncoder.json.encodeToJsonElement(
                        this, v!!
                    )
                })
            }

            is List<*> -> JsonArray(value.map { v ->
                v as? JsonElement ?: jsonEncoder.json.encodeToJsonElement(
                    this, v!!
                )
            })

            else -> JsonPrimitive(value.toString())
        }
        jsonEncoder.encodeJsonElement(element)
    }

    override fun deserialize(decoder: Decoder): Any {
        val jsonDecoder =
            decoder as? JsonDecoder ?: throw SerializationException("This class can be loaded only by Json")
        val element = jsonDecoder.decodeJsonElement()
        val decodeJsonElement = decodeJsonElement(element)
        return decodeJsonElement ?: ""
    }

    private fun decodeJsonElement(element: JsonElement): Any? = when (element) {
        is JsonNull -> null
        is JsonObject -> element.mapValues { decodeJsonElement(it.value) }
        is JsonArray -> element.map { decodeJsonElement(it) }

        is JsonPrimitive -> when {
            element.isString -> element.content
            element.booleanOrNull != null -> element.boolean
            element.longOrNull != null -> element.long
            element.doubleOrNull != null -> element.double
            else -> element.content
        }

    }
}
