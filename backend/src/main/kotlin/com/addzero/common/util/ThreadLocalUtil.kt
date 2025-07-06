package com.addzero.common.util

class ThreadLocalUtil<T> private constructor() {
    private val threadLocal = ThreadLocal<T>()

    companion object {
        private val holder = ThreadLocal<ThreadLocalUtil<*>>()

        fun <T> set(value: T) {
            val util = getOrCreate<T>()
            util.threadLocal.set(value)
        }

        fun <T> get(): T {
            val util = getOrCreate<T>()
            return util.threadLocal.get()
        }

        fun remove() {
            val util = holder.get()
            util?.threadLocal?.remove()
        }

        @Suppress("UNCHECKED_CAST")
        private fun <T> getOrCreate(): ThreadLocalUtil<T> {
            var util = holder.get()
            if (util == null) {
                util = ThreadLocalUtil<Any>()
                holder.set(util)
            }
            return util as ThreadLocalUtil<T>
        }
    }
}
