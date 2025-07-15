//package com.example.demo.common.config.jackson
//
//import com.fasterxml.jackson.core.JsonGenerator
//import com.fasterxml.jackson.core.JsonParser
//import com.fasterxml.jackson.databind.DeserializationContext
//import com.fasterxml.jackson.databind.JsonDeserializer
//import com.fasterxml.jackson.databind.JsonSerializer
//import com.fasterxml.jackson.databind.SerializerProvider
//import io.qifan.infrastructure.common.model.PageResult
//import org.springframework.boot.jackson.JsonComponent
//import org.springframework.data.domain.Page
//import java.io.IOException
//
//@JsonComponent
//class PageableConvert {
//
//    class Serializer : JsonSerializer<Page<*>>() {
//        @Throws(IOException::class)
//        override fun serialize(
//            page: Page<*>, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider
//        ) {
//            val pageResult: PageResult<*> =
//                PageResult<Any>().setNumber(page.number).setSize(page.size).setTotalElements(page.totalElements)
//                    .setTotalPages(page.totalPages).setContent(page.content as List<Any>)
//            jsonGenerator.writeObject(pageResult)
//        }
//    }
//
//    class Deserializer : JsonDeserializer<Page<*>>() {
//        @Throws(IOException::class)
//        override fun deserialize(
//            jsonParser: JsonParser, deserializationContext: DeserializationContext
//        ): Page<*> {
//            return jsonParser.readValueAs(Page::class.java)
//        }
//    }
//}