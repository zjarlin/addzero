package com.addzero.kmp.core.network

import com.addzero.kmp.core.network.AddHttpClient.configClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual val apiClient: HttpClient
    get() = HttpClient(Darwin) {
        configClient()
    }
