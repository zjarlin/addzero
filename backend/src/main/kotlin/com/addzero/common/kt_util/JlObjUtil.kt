package com.addzero.common.kt_util

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.date.DateUtil
import cn.hutool.core.util.ObjUtil
import com.addzero.common.util.data_structure.ref.RefUtil

object JlObjUtil {
    // 判断是否是日期字符串
    fun isDateValue(value: Any?): Boolean {
        return when (value) {
            is String -> try {
                DateUtil.parse(value)
                true
            } catch (e: Exception) {
                false
            }

            is java.util.Date, is java.time.temporal.TemporalAccessor -> true
            else -> false
        }
    }
}

fun Any?.isNull(): Boolean {
    return ObjUtil.isNull(this)
}

fun Any?.isNotNull(): Boolean {
    return ObjUtil.isNotNull(this)
}

fun Any?.isEmpty(): Boolean {
    return ObjUtil.isEmpty(this)
}

fun Any?.isNotEmpty(): Boolean {
    return ObjUtil.isNotEmpty(this)
}


fun Any?.isNotNew(): Boolean {
    if (this == null) {
        return false
    }
    val new = RefUtil.isNew(this)
    return !new
}

fun Iterator<*>?.isEmpty(): Boolean {
    return CollUtil.isEmpty(this)
}
