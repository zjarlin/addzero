//package com.addzero.web.infra.jackson
//
//import cn.hutool.core.date.DatePattern
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
//import java.time.LocalDate
//import java.time.LocalDateTime
//import java.time.LocalTime
//import java.time.format.DateTimeFormatter
//import java.util.*
//
///**
// * @description:
// * @author: luke on 2023/10/24
// * @version: 1.0
// */
//@Configuration
//open class LocalDateFormatConfig {
//    @Bean
//    @Primary
//    open fun jackson2ObjectMapperBuilderCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
//        return Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder ->
//            builder.serializerByType(
//                LocalDateTime::class.java, LocalDateTimeSerializer(
//                    DateTimeFormatter.ofPattern(
//                        DATETIME_FORMAT, Locale.CHINA
//                    )
//                )
//            ).serializerByType(
//                LocalDate::class.java, LocalDateSerializer(
//                    DateTimeFormatter.ofPattern(
//                        DATE_FORMAT, Locale.CHINA
//                    )
//                )
//            ).serializerByType(
//                LocalTime::class.java, LocalTimeSerializer(
//                    DateTimeFormatter.ofPattern(
//                        TIME_FORMAT, Locale.CHINA
//                    )
//                )
//            ).deserializerByType(
//                LocalDateTime::class.java, LocalDateTimeDeserializer(
//                    DateTimeFormatter.ofPattern(
//                        DATETIME_FORMAT, Locale.CHINA
//                    )
//                )
//            ).deserializerByType(
//                LocalDate::class.java, LocalDateDeserializer(
//                    DateTimeFormatter.ofPattern(
//                        DATE_FORMAT, Locale.CHINA
//                    )
//                )
//            ).deserializerByType(
//                LocalTime::class.java, LocalTimeDeserializer(
//                    DateTimeFormatter.ofPattern(
//                        TIME_FORMAT, Locale.CHINA
//                    )
//                )
//            )
//                // 添加对 TIMESTAMP 类型的反序列化支持
//                .deserializerByType(
//                    java.sql.Timestamp::class.java, LocalDateTimeDeserializer(
//                        DateTimeFormatter.ofPattern(
//                            DATETIME_FORMAT, Locale.CHINA
//                        )
//                    )
//                )
//        }
//    }
//
//    companion object {
//        /**
//         * Date格式化字符串
//         */
//        private const val DATE_FORMAT = DatePattern.NORM_DATE_PATTERN //yyyy-MM-dd
//
//        /**
//         * DateTime格式化字符串
//         */
//        private const val DATETIME_FORMAT = DatePattern.NORM_DATETIME_PATTERN //yyyy-MM-dd HH:mm:ss
//
//        /**
//         * Time格式化字符串
//         */
//        private const val TIME_FORMAT = "HH:mm" //HH:mm
//    }
//}
