package com.addzero.kmp.core.network

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

actual val apiClient: HttpClient
    get() = HttpClient(Darwin) {
        configClient()
    }
