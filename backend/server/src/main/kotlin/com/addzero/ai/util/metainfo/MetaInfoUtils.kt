package com.addzero.ai.util.metainfo

import cn.hutool.core.annotation.AnnotationUtil
import cn.hutool.core.util.ClassUtil
import com.addzero.ai.util.metainfo.entity.FieldInfo
import com.addzero.ai.util.metainfo.entity.toSimpleString
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType

object MetaInfoUtils {

    fun extractTableName(sql: String?): String? {
        if (sql == null || sql.isBlank()) {
            return null
        }
        val regex = "(?i)from\\s+([a-zA-Z0-9_]+)".toRegex() // 定义正则表达式
        val matchResult = regex.find(sql!!) // 查找匹配项
        return matchResult?.groups?.get(1)?.value // 返回捕获的表名
    }

    private fun findAnno(annotation: String): Boolean {
        try {
            val loadedClass = ClassUtil.loadClass<Annotation>(annotation)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    fun guessDescription(annotatedElement: AnnotatedElement): String? {
        // 定义可能的 Swagger 注解
        val swaggerAnnotations = listOf(
            // 方法注释
            "io.swagger.annotations.Api", // Swagger 2
            "io.swagger.v3.oas.annotations.Operation", // Swagger 3
            // 类注释
            "io.swagger.annotations.ApiModel", // Swagger 2
            "io.swagger.v3.oas.annotations.media.Schema", // Swagger 3
            // 字段注释
            "io.swagger.annotations.ApiModelProperty" // Swagger 2

            , "com.fasterxml.jackson.annotation.JsonPropertyDescription"
        )
        val find = swaggerAnnotations.filter { findAnno(it) }

        // 查找最后一个匹配的注解并获取描述
        val lastOrNull = find.asSequence()
            .mapNotNull { annotation ->
                val loadedClass = ClassUtil.loadClass<Annotation>(annotation)
                val swaggerAnnotation = AnnotationUtil.getAnnotation<Annotation?>(annotatedElement, loadedClass)
                swaggerAnnotation?.let {
                    // 根据不同的注解类型提取描述
                    when (annotation) {
                        "io.swagger.annotations.Api" -> it.javaClass.getMethod("description").invoke(it) as? String
                        "io.swagger.v3.oas.annotations.Operation" -> it.javaClass.getMethod("summary")
                            .invoke(it) as? String

                        "io.swagger.annotations.ApiModel" -> it.javaClass.getMethod("description").invoke(it) as? String
                        "io.swagger.v3.oas.annotations.media.Schema" -> it.javaClass.getMethod("description")
                            .invoke(it) as? String

                        "io.swagger.annotations.ApiModelProperty" -> it.javaClass.getMethod("value")
                            .invoke(it) as? String

                        "com.fasterxml.jackson.annotation.JsonPropertyDescription" -> it.javaClass.getMethod("value")
                            .invoke(it) as? String

                        else -> null
                    }
                }
            }.lastOrNull()
        return lastOrNull // 取最后一个找到的描述
    }

    fun getFieldInfos(clazz: Class<*>): List<FieldInfo> {
        return getFieldInfosRecursive(clazz)
    }

    fun getFieldInfosRecursive(clazz: Class<*>): List<FieldInfo> {
        val fieldInfoList = mutableListOf<FieldInfo>()
        // 获取当前类的所有字段，包括父类的字段
        val fields = clazz.declaredFields
        for (field in fields) {
            field.isAccessible = true
            // 获取字段上的注释
            val description = guessDescription(field)
            // 判断字段是否是嵌套对象
            val isNestedObject = isCustomObject(field.type)

            // 如果字段是嵌套对象类型，递归获取其子字段
            val children = if (isNestedObject) {
                getFieldInfosRecursive(field.type)
            } else if (isList(field)) {
                // 如果字段是一个集合类型，递归获取其泛型类型的子字段
                val genericType = (field.genericType as? ParameterizedType)?.actualTypeArguments?.get(0)
                if (genericType is Class<*> && isCustomObject(genericType)) {
                    getFieldInfosRecursive(genericType)
                } else {
                    emptyList()
                }
            } else {
                emptyList()
            }

            // 添加当前字段的信息到列表中，包括其子字段
            fieldInfoList.add(
                FieldInfo(
                    declaringClass = clazz,
                    field = field,
                    description = description,
                    fieldType = field.type,
                    isNestedObject = isNestedObject,
                    children = children // 子字段
                )
            )
        }
        return fieldInfoList
    }

    fun getSimpleFieldInfoStr(clazz: Class<*>): String {
        val joinToString = getFieldInfosRecursive(clazz).joinToString { fieldInfo ->
            fieldInfo.toSimpleString()
        }
        return joinToString

    }


    // 判断一个字段是否是自定义对象
    fun isCustomObject(clazz: Class<*>): Boolean {
        return !(clazz.isPrimitive || clazz == String::class.java || Number::class.java.isAssignableFrom(clazz) || clazz.isEnum || clazz == List::class.java)
    }

    // 判断字段是否是 List 类型
    fun isList(field: Field): Boolean {
        return List::class.java.isAssignableFrom(field.type)
    }

}
