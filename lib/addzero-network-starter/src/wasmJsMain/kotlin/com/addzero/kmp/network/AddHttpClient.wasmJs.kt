package com.addzero.kmp.network

import com.addzero.kmp.network.AddHttpClient.configClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.js.*

actual val apiClient: HttpClient = HttpClient(Js) {
    configClient()
}
