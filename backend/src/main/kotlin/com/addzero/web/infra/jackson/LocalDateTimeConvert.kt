//package com.addzero.web.infra.jackson
//
//import com.fasterxml.jackson.core.JsonGenerator
//import com.fasterxml.jackson.core.JsonParser
//import com.fasterxml.jackson.databind.DeserializationContext
//import com.fasterxml.jackson.databind.JsonDeserializer
//import com.fasterxml.jackson.databind.JsonSerializer
//import com.fasterxml.jackson.databind.SerializerProvider
//import org.springframework.boot.jackson.JsonComponent
//import java.io.IOException
//import java.time.*
//import java.time.format.DateTimeFormatter
//import java.time.temporal.Temporal
//
//@JsonComponent
//class TemporalConvert {
//
//    open class Serializer<T : Temporal>(
////        private val pattern: String
//    ) : JsonSerializer<T?>() {
//        @Throws(IOException::class)
//        override fun serialize(
//            columnValue: T?,
//            jsonGenerator: JsonGenerator,
//            serializerProvider: SerializerProvider
//        ) {
//            if (columnValue != null) {
//                val formatter = DateTimeFormatter.ofPattern(pattern)
//                val formattedValue = when (columnValue) {
//                    is LocalDateTime -> columnValue.format(formatter)
//                    is LocalDate -> columnValue.format(formatter)
//                    is LocalTime -> columnValue.format(formatter)
//                    else -> throw IllegalArgumentException("Unsupported temporal jdbcType: ${columnValue::class.java}")
//                }
//                jsonGenerator.writeString(formattedValue)
//            }
//        }
//    }
//
//    open class Deserializer<T : Temporal>(
//        private val pattern: String,
//        private val jdbcType: Class<T>
//    ) : JsonDeserializer<T?>() {
//        @Throws(IOException::class)
//        override fun deserialize(
//            jsonParser: JsonParser,
//            deserializationContext: DeserializationContext
//        ): T? {
//            val text = jsonParser.text
//            val formatter = DateTimeFormatter.ofPattern(pattern)
//
//            return when (jdbcType) {
//                LocalDateTime::class.java -> LocalDateTime.parse(text, formatter) as? T
//                LocalDate::class.java -> LocalDate.parse(text, formatter) as? T
//                LocalTime::class.java -> LocalTime.parse(text, formatter) as? T
//                else -> throw IllegalArgumentException("Unsupported temporal jdbcType: $jdbcType")
//            }
//        }
//    }
//}
//
//@JsonComponent
//class LocalDateSerializer : TemporalConvert.Serializer<LocalDate>("yyyy-MM-dd")
//
//@JsonComponent
//class LocalDateTimeSerializer : TemporalConvert.Serializer<LocalDateTime>("yyyy-MM-dd HH:mm:ss")
//
//@JsonComponent
//class LocalTimeSerializer : TemporalConvert.Serializer<LocalTime>("HH:mm:ss")
//
//@JsonComponent
//class LocalDateDeserializer : TemporalConvert.Deserializer<LocalDate>("yyyy-MM-dd", LocalDate::class.java)
//
//@JsonComponent
//class LocalDateTimeDeserializer : TemporalConvert.Deserializer<LocalDateTime>("yyyy-MM-dd HH:mm:ss", LocalDateTime::class.java)
//
//@JsonComponent
//class LocalTimeDeserializer : TemporalConvert.Deserializer<LocalTime>("HH:mm:ss", LocalTime::class.java)
