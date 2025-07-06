package com.addzero.kmp.core.network

import io.ktor.client.statement.*

object GlobalEventDispatcher {
    lateinit var handler: ((HttpResponse) -> Unit)
}
