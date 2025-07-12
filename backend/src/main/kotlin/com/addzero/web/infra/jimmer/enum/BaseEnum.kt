package com.addzero.web.infra.jimmer.enum

import com.addzero.web.infra.jimmer.base.SelectOption
import com.addzero.web.infra.jimmer.base.SelectOptionImpl
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

/**
 * 枚举类接口
 * @author zjarlin
 * @date 2025/04/02
 * @constructor 创建[BaseEnum]
 */
interface BaseEnum<T : Enum<T>, V> {


    /**
     * 获取枚举类的值
     *
     * @return
     */
    val value: V

    /**
     * 获取枚举类的说明
     * 1 启用
     * 2 =未启用
     *
     */
    val desc: String

//    fun CLASS_E(): Class<T> {
//        val typeArgument = TypeUtil.getTypeArgument(this.javaClass, 0)
//        val jdbcType = typeArgument as Class<T>
//        return jdbcType
//    }

    @JsonValue
    fun getValue(): SelectOptionImpl<V> {
//        dictCode拿到字典项
//        getDictItemByCode
        return SelectOptionImpl<V>(label = desc, value = value)

//        return desc
    }

    @JsonCreator
    fun fromOption(opt: SelectOption<V>?): BaseEnum<T, V>? {
        val klass = this::class
        val firstOrNull = klass.java.enumConstants.firstOrNull { it.value == opt?.value }
        return firstOrNull
    }


//    @JsonCreator
//    fun fromCode(code: String): T? { return CLASS_E().enumConstants .map { it as T }.firstOrNull { ObjUtil.equals(code, it.columnValue) }
}
