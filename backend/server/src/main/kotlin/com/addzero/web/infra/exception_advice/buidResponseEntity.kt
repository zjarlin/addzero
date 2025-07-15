package com.addzero.web.infra.exception_advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

inline fun <reified T> T.buidResponseEntity(): ResponseEntity<T?> {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(this)
}

inline fun <reified E : Throwable> E?.buildMessage(): String {
    return buildString {
        var currentCause: Throwable? = this@buildMessage
        while (currentCause != null) {
            append(currentCause.message ?: currentCause.toString())
            currentCause = currentCause.cause
            if (currentCause != null) {
                append(System.lineSeparator())
                append("Caused by: ")
            }
        }
    }
}
