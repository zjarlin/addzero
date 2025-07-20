package com.addzero.web.infra.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.babyfish.jimmer.jackson.ImmutableModule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import kotlin.time.ExperimentalTime
import kotlinx.datetime.LocalDateTime as KotlinxLocalDateTime

@Configuration
class JacksonConfig {

    /**
     * 自定义 LocalDateTime 格式化器
     * 支持任意位数的微秒部分（0-9位），兼容各种精度的时间戳
     */
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()

    /**
     * kotlinx.datetime.LocalDateTime 序列化器
     */
    private inner class KotlinxLocalDateTimeSerializer : JsonSerializer<KotlinxLocalDateTime>() {
        override fun serialize(value: KotlinxLocalDateTime, gen: JsonGenerator, serializers: SerializerProvider) {
            gen.writeString(dateTimeFormatter.format(value.toJavaLocalDateTime()))
        }
    }

    /**
     * kotlinx.datetime.LocalDateTime 反序列化器
     */
    private inner class KotlinxLocalDateTimeDeserializer : JsonDeserializer<KotlinxLocalDateTime>() {
        @OptIn(ExperimentalTime::class)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): KotlinxLocalDateTime {
            val text = p.text
            return if (text.isNullOrBlank()) {
                // 如果为空，返回当前时间作为默认值
                kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            } else {
                try {
                    LocalDateTime.parse(text, dateTimeFormatter).toKotlinLocalDateTime()
                } catch (e: Exception) {
                    // 解析失败时返回当前时间
                    kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                }
            }
        }

        @OptIn(ExperimentalTime::class)
        override fun getNullValue(ctxt: DeserializationContext?): KotlinxLocalDateTime {
            // 当遇到 null 值时返回当前时间作为默认值
            return kotlin.time.Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

    @Bean
    fun jackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder ->
            builder
                // 注册模块
                .modules(
                    KotlinModule.Builder()
                        .configure(KotlinFeature.NullToEmptyCollection, false)
                        .configure(KotlinFeature.NullToEmptyMap, false)
                        .configure(KotlinFeature.NullIsSameAsDefault, true) // 关键配置：null 值使用默认值
                        .configure(KotlinFeature.StrictNullChecks, false) // 关键配置：不严格检查 null
                        .build(),
                    JavaTimeModule().apply {
                        addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateTimeFormatter))
                        addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(dateTimeFormatter))
                    },
                    SimpleModule().apply {
                        addSerializer(KotlinxLocalDateTime::class.java, KotlinxLocalDateTimeSerializer())
                        addDeserializer(KotlinxLocalDateTime::class.java, KotlinxLocalDateTimeDeserializer())
                    },
                    ImmutableModule()
                )
                // 序列化配置
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToEnable(SerializationFeature.INDENT_OUTPUT)
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                // 反序列化配置
                .featuresToDisable(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
                    DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES
                )
        }
    }

    /**
     * 创建主要的 ObjectMapper Bean，确保 Spring AI 也使用这个配置
     */
//    @Bean
//    @Primary
//    fun objectMapper(): ObjectMapper {
//        return ObjectMapper().apply {
//            // 注册 Kotlin 模块
//            registerModule(
//                KotlinModule.Builder()
//                    .configure(KotlinFeature.NullToEmptyCollection, false)
//                    .configure(KotlinFeature.NullToEmptyMap, false)
//                    .configure(KotlinFeature.NullIsSameAsDefault, true) // 关键配置：null 值使用默认值
//                    .configure(KotlinFeature.StrictNullChecks, false) // 关键配置：不严格检查 null
//                    .build()
//            )
//
//            // 注册 Java Time 模块
//            registerModule(JavaTimeModule().apply {
//                addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer(dateTimeFormatter))
//                addDeserializer(LocalDateTime::class.java, LocalDateTimeDeserializer(dateTimeFormatter))
//            })
//
//            // 注册 kotlinx.datetime 模块
//            registerModule(SimpleModule().apply {
//                addSerializer(KotlinxLocalDateTime::class.java, KotlinxLocalDateTimeSerializer())
//                addDeserializer(KotlinxLocalDateTime::class.java, KotlinxLocalDateTimeDeserializer())
//            })
//
//            // 注册 Jimmer 模块
//            registerModule(ImmutableModule())
//
//            // 配置序列化特性
//            setSerializationInclusion(JsonInclude.Include.NON_NULL)
//            enable(SerializationFeature.INDENT_OUTPUT)
//            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
//
//            // 配置反序列化特性
//            disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
//            disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
//            disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
//            disable(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES) // 额外添加这个
//        }
//    }
}