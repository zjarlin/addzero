package com.addzero.web.infra.jimmer.scalar_provider

import com.addzero.common.kt_util.EnumBitmaskUtils.toEnumList
import com.addzero.common.kt_util.toBitmask
import org.babyfish.jimmer.sql.runtime.ScalarProvider
import kotlin.reflect.KClass

class GenericEnumScalarProvider<T : Enum<T>>(val elementClass: Class<*>) : ScalarProvider<List<T>, Int> {
    override fun toScalar(sqlValue: Int): List<T>? {
        val kotlin = elementClass.kotlin as KClass<T>
        val toEnumList = sqlValue.toEnumList(kotlin)
        return toEnumList
    }

    override fun toSql(scalarValue: List<T>): Int? {
        val toBitmask = scalarValue.toBitmask()
        return toBitmask
    }


}
