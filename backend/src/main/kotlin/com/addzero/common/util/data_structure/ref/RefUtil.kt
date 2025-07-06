package com.addzero.common.util.data_structure.ref

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ReflectUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.core.util.TypeUtil
import com.addzero.common.util.BigDecimals
import com.addzero.web.infra.jackson.parseObject
import com.addzero.web.infra.jackson.toJson
import com.fasterxml.jackson.databind.JsonNode
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.*
import kotlin.reflect.KClass

/**
 * 反射工具类
 *
 * @author zjarlin
 * @since 2022/06/29
 */
@Suppress("unused")
object RefUtil {

//       fun <T : Any> getClassMetadata(clazz: KClass<T>): List<Map<String, Any?>> {
//        return clazz.memberProperties.map { property ->
//            val apiModelProperty = property.findAnnotation<Schema>()
//            mapOf(
//                "name" to property.name,
//                "jdbcType" to property.returnType.toString(),
//                "nullable" to property.returnType.isMarkedNullable,
//                "swaggerDescription" to apiModelProperty?.description,
//                "required" to apiModelProperty?.required
//            )
//        }
//    }


    fun <T : Any> currentClass(): KClass<T> {
        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
        val type = typeArgument as Class<T>
        return type.kotlin
    }


    /**
     * 包含忽略秩序
     *
     * @param seq       seq
     * @param searchSeq 搜索seq
     * @return 返回状态true/false
     * @author zjarlin
     * @since 2022/06/18
     */
    fun containsIgnoreOrder(seq: CharSequence?, searchSeq: CharSequence?): Boolean {
        return StrUtil.contains(seq, searchSeq) || StrUtil.contains(searchSeq, seq)
    }


    /**
     * 判断下层接口返回的结果是否是new出来的对象
     *
     * @param object 下层接口返回的obj
     * @return 返回状态true/false
     * @author zjarlin
     * @since 2022/06/11
     */
    fun isNew(`object`: Any): Boolean {
        val aClass: Class<*> = `object`.javaClass
        val fields = aClass.declaredFields
        return Arrays.stream<Field>(fields).filter { field: Field -> !Modifier.isStatic(field.modifiers) }
            .map<Any?> { field: Field ->
                field.isAccessible = true
                try {
                    return@map field[`object`]
                } catch (e: IllegalAccessException) {
                    return@map null
                }
            }.allMatch { value: Any? ->
                value == null || (value is String && StrUtil.isBlank(value)) || (value is Collection<*> && CollUtil.isEmpty(
                    value
                ))
            }
    }

    fun isT(obj: Any): Boolean {
        if (Objects.isNull(obj)) {
            return false
        }
        //非原始类型
        obj.javaClass.isPrimitive
        try {
            val toJson = obj.toJson()
            obj.toJson().parseObject()
        } catch (e: Exception) {
            return false
        }
        return !isPrimitiveOrWrapper(obj.javaClass)
//        &&         !IPage::class.java.isAssignableFrom(obj.javaClass)
                && !MutableCollection::class.java.isAssignableFrom(
            obj.javaClass
        ) && !BigDecimals::class.java.isAssignableFrom(obj.javaClass) && !Enum::class.java.isAssignableFrom(
            obj.javaClass
        ) && !JsonNode::class.java.isAssignableFrom(obj.javaClass)
    }

    fun isPrimitiveOrWrapper(aClass: Class<*>): Boolean {
// Class<?> aClass = obj.getClass();
        val primitive = aClass.isPrimitive
        return primitive || Byte::class.java.isAssignableFrom(aClass) || Short::class.java.isAssignableFrom(aClass) || Int::class.java.isAssignableFrom(
            aClass
        ) || Long::class.java.isAssignableFrom(aClass) || Float::class.java.isAssignableFrom(aClass) || Double::class.java.isAssignableFrom(
            aClass
        ) || Boolean::class.java.isAssignableFrom(aClass) || Char::class.java.isAssignableFrom(aClass)
    }


    fun isCollection(obj: Any): Boolean {
        val assignableFrom = MutableCollection::class.java.isAssignableFrom(obj.javaClass)
        return assignableFrom
    }

    fun isNonNullField(obj: Any?, field: Field?): Boolean {
        val fieldValue = ReflectUtil.getFieldValue(obj, field)
        return Objects.nonNull(fieldValue)
    }

    fun isObjectField(obj: Any?, field: Field?): Boolean {
        val fieldValue = ReflectUtil.getFieldValue(obj, field)
        val `object` = isT(fieldValue)
        return `object`
    }

    fun isCollectionField(field: Field): Boolean {
        val type = field.type
        val assignableFrom = MutableCollection::class.java.isAssignableFrom(type)
        return assignableFrom
    }

}
