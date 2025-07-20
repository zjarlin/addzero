package com.addzero.common.kt_util

import kotlin.reflect.KClass

val <T : Enum<T>> T.bitValue: Int get() = 1 shl this.ordinal


// 枚举集合 → 二进制
fun <T : Enum<T>> Collection<T>.toBitmask(): Int {
    return this.fold(0) { mask, enum -> mask or enum.bitValue }
}

object EnumBitmaskUtils {

    inline fun <reified T : Enum<T>> Int.toEnumList(): List<T> {
        return enumValues<T>().filter { (this and it.bitValue) != 0 }
    }

    // 二进制 → 枚举集合
    fun <T : Enum<T>> Int.toEnumList(clazz: KClass<T>): List<T> {
        return toEnumList(clazz.java)
    }


    // 二进制 → 枚举集合
    fun <T : Enum<T>> Int.toEnumList(clazz: Class<T>): List<T> {
        val filter = clazz.enumConstants
            .filter { (this and it.bitValue) != 0 }
        return filter
    }


}
