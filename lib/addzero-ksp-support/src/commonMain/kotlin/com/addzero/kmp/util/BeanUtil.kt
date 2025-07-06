package com.addzero.kmp.util

import com.addzero.kmp.context.Settings
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object BeanUtil {
    fun Settings.toMap(): Map<String, String> =
        Settings::class.memberProperties.associate { prop ->
            prop.name to (prop.get(this)?.toString() ?: "")
        }

    inline fun <reified T : Any> mapToBean(map: Map<String, String>): T {
        val ctor = T::class.primaryConstructor!!
        val args = ctor.parameters.associateWith { param ->
            map[param.name] ?: error("Missing value for ${param.name}")
        }
        return ctor.callBy(args)
    }
}