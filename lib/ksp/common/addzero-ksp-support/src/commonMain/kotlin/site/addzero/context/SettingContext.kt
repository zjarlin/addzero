package site.addzero.context

import site.addzero.core.ext.map2bean
import site.addzero.core.ext.bean2map
import java.util.concurrent.atomic.AtomicReference

object SettingContext {
    private val settingsRef = AtomicReference<Settings?>()

    val settings: Settings
        get() = settingsRef.get() ?: Settings()

    fun initialize(op: Map<String, String>) {
        val toMap = settings.bean2map()
        val map = toMap + op

        val mapToBean = map.map2bean<Settings>()
        settingsRef.compareAndSet(null, mapToBean)
    }
}
