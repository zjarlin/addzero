package com.addzero.kmp.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.reflect.KClass

object EventBus {
    val eventFlows = mutableMapOf<KClass<*>, MutableSharedFlow<*>>()

    // 发送事件
    suspend inline fun <reified T : Any> emit(event: T) {
        val flow = getOrCreateFlow<T>()
        flow.emit(event)
    }

    // 监听事件
    inline fun <reified T : Any> events() = getOrCreateFlow<T>().asSharedFlow()

    // 内部方法：获取或创建对应类型的流
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Any> getOrCreateFlow(): MutableSharedFlow<T> {
        return eventFlows.getOrPut(T::class) {
            MutableSharedFlow<T>(extraBufferCapacity = 64)
        } as MutableSharedFlow<T>
    }

    suspend inline fun <reified T : Any> consumer(
        crossinline block: suspend T.() -> Unit
    ) {
        events<T>().collect {
            block(it)
        }
    }
}
