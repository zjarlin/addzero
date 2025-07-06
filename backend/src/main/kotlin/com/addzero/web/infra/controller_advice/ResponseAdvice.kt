//package com.addzero.web.infra.controller_advice
//
//import com.addzero.kmp.entity.Res
//import cn.hutool.core.collection.CollUtil
//import cn.hutool.core.util.StrUtil
//import com.addzero.kmp.entity.Res2
//import com.addzero.kmp.entity.success
//import com.alibaba.fastjson2.JSON
//import com.alibaba.fastjson2.JSONArray
//import com.alibaba.fastjson2.JSONObject
//import com.fasterxml.jackson.databind.ObjectMapper
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.core.MethodParameter
//import org.springframework.http.MediaType
//import org.springframework.http.converter.HttpMessageConverter
//import org.springframework.http.server.ServerHttpRequest
//import org.springframework.http.server.ServerHttpResponse
//import org.springframework.web.bind.annotation.RestControllerAdvice
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
//
///**
// * 包装Controller结果, 如果已是Result类型, 则直接返回, 否则进行包装.
// *
// */
//@RestControllerAdvice
//class ResponseAdvice(
//    @Value("\${controller.advice.path}") private val path: List<String?>,
//    private val objectMapper: ObjectMapper,
//) : ResponseBodyAdvice<Any?> {
//
//    companion object {
//        private val EXCLUDED_CONTROLLER_CLASSES: Set<Class<*>> = emptySet()
//        private val EXCLUDED_CONTROLLER_STRING: Set<String> = emptySet()
//    }
//
//    override fun supports(
//        returnType: MethodParameter,
//        converterType: Class<out HttpMessageConverter<*>>,
//    ): Boolean {
//        val containingClass = returnType.containingClass
//        val pkg = containingClass.packageName
//
//        if (path.isEmpty() || path.none { StrUtil.startWith(pkg, it) }) {
//            return false
//        }
//
//        // 排除标记了注解或者在排除列表中的类
//        return !returnType.hasMethodAnnotation(IgnoreResponseAdvice::class.java) && !CollUtil.contains(
//            EXCLUDED_CONTROLLER_CLASSES, containingClass
//        ) && !CollUtil.contains(EXCLUDED_CONTROLLER_STRING, containingClass.name)
//    }
//
//    override fun beforeBodyWrite(
//        body: Any?,
//        returnType: MethodParameter,
//        selectedContentType: MediaType,
//        selectedConverterType: Class<out HttpMessageConverter<*>>,
//        request: ServerHttpRequest,
//        response: ServerHttpResponse,
//    ): Any? {
//        return when (body) {
//            null -> Res2.success2("")
//            is Res2<*>, is JSONArray -> body
//            is JSONObject -> if (body.containsKey("code") && body.containsKey ("message")) body else Res.success( body)
//            is String -> try {
//                val r = JSON.parseObject(body, Res2::class.java)
//                objectMapper.writeValueAsString(r)
//            } catch (e: Exception) {
//                val success = body.success()
//                objectMapper.writeValueAsString(success)
//            }
//
//            else -> Res2.success2(body)
//        }
//    }
//}
