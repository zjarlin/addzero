package com.addzero.kmp.context

import com.addzero.kmp.context.BeanUtil.mapToBean
import com.addzero.kmp.context.BeanUtil.toMap
import java.util.concurrent.atomic.AtomicReference

object SettingContext {
    private val settingsRef = AtomicReference<Settings?>()

    val settings: Settings
        get() = settingsRef.get() ?: Settings()

    fun initialize(op: Map<String, String>) {
        val toMap = settings.toMap()
        val map = toMap + op
        val mapToBean = mapToBean(map)
        settingsRef.compareAndSet(null, mapToBean)

    }
}