package site.addzero.util

import org.koin.core.component.KoinComponent

object KoinInjector : KoinComponent {
    /**
     * 注入单个实例
     */
    inline fun <reified T : Any> inject(): T {
        val koin = getKoin()
        return koin.get()
    }

    /**
     * 注入实例列表
     */
    inline fun <reified T : Any> injectList(): List<T> {
        val koin = getKoin()
        return koin.getAll()
    }

    /**
     * 根据条件筛选实例
     */
    inline fun <reified T : Any> getSupportStrategty(predicate: (T) -> Boolean): T? {
        return injectList<T>().firstOrNull(predicate)
    }
}

