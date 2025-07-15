package com.addzero.web.infra.jackson

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.babyfish.jimmer.jackson.ImmutableModule
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import kotlinx.datetime.LocalDateTime as KotlinxLocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

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
    }

    @Bean
    fun jackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder ->
            builder
                // 注册模块
                .modules(
                    KotlinModule.Builder().build(),
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
}