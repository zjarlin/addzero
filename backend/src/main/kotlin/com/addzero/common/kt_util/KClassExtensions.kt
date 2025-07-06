//package com.addzero.common.kt_util
//
//
//import kotlin.reflect.KClass
//import kotlin.reflect.KProperty1
//import kotlin.reflect.full.findAnnotation
//import kotlin.reflect.full.memberProperties
//
///**
// * 类的元数据信息
// */
//data class ClassMetadata<T>(
//    /** 类名 */
//    val className: String,
//    /** 类描述（来自@Schema注解） */
//    val description: String?,
//    /** 字段列表 */
//    val fields: List<FieldMetadata<T>>
//)
//
///**
// * 字段的元数据信息
// */
//data class FieldMetadata<T>(
//    /** 字段名 */
//    val name: String,
//    val property: KProperty1<T, *>,
//    /** 字段类型 */
//    val jdbcType: String,
//    /** 字段描述（来自@Schema注解） */
//    val description: String?,
//    /** 示例值 */
//    val example: String?,
//    /** 是否可空 */
//    val nullable: Boolean
//)
//
//
///**
// * 根据指定的顺序对集合进行排序（不区分大小写）。
// *
// * @param order 指定的顺序列表。
// * @param selector 用于获取排序字段的函数。
// * @return 排序后的列表。
// */
//fun <T> Collection<T>.sortedByCustomOrderIgnoreCase(
//    order: List<String>, selector: (T) -> String
//): List<T> {
//    // 创建一个映射，用于快速查找指定顺序的索引（忽略大小写）
//    val orderMap = order.withIndex().associate { it.columnValue.lowercase() to it.index }
//
//    return this.sortedBy { item ->
//        // 获取当前元素的排序字段值（忽略大小写）
//        val fieldValue = selector(item).lowercase()
//        // 返回字段值在指定顺序中的索引，如果不存在则返回最大值（排在最后）
//        orderMap[fieldValue] ?: Int.MAX_VALUE
//    }
//}
//
//
///**
// * 获取类的元数据信息的扩展函数
// */
//fun <T : Any> KClass<T>.getMetadata(): ClassMetadata<T> {
//    // 获取类名
//    val className = this.simpleName ?: ""
//
//    // 获取类的@Schema注解信息
//    val classSchema = this.findAnnotation<Schema>()
//    val classDescription = classSchema?.description
//
//    // 使用Java反射API获取方法，并转换为属性名
//    val declaredFields =
//        this.java.declaredMethods.filter { it.name.startsWith("get") && it.name.length > 3 }.map { method ->
//            // 移除get前缀并将首字母小写
//            val propertyName = method.name.removePrefix("get").let {
//                it.first().lowercase() + it.substring(1)
//            }
//            propertyName
//        }
//    // 获取对应的Kotlin属性
//    val property = this.memberProperties.map {
//        val getter = it.getter
//        val propertySchema = getter.findAnnotation<Schema>() ?: it.findAnnotation<Schema>()
//        FieldMetadata(
//            property = it!!,
//            name = it.name,
//            jdbcType = it.returnType.toString(),
//            description = propertySchema?.description,
//            example = propertySchema?.example,
//            nullable = false,
//        )
//
//    }.sortedByCustomOrderIgnoreCase(
//        declaredFields, { it.name }
//    )
//
//    return ClassMetadata(
//        className = className, description = classDescription, fields = property
//    )
//
//}
//
