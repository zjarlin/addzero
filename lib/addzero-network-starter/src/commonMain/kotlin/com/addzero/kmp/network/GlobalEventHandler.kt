package com.addzero.kmp.network

import io.ktor.client.statement.*

object GlobalEventDispatcher {
    lateinit var handler: ((HttpResponse) -> Unit)
}
