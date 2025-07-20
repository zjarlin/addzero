package com.addzero.ai.util

import org.springframework.http.codec.ServerSentEvent
import reactor.core.publisher.Flux
import java.time.Duration

object SSEUtil {

    fun <T> toSSEStream(interval: Duration, data: T, eventName: String = "sseEventName"):
            Flux<ServerSentEvent<T>> {
        return Flux.interval(interval).map { sequence ->
            ServerSentEvent.builder<T>().id(java.lang.String.valueOf(sequence)).event(eventName).data(data).build()
        }
    }
}
