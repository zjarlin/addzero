package com.addzero.kmp.network

import com.addzero.kmp.network.AddHttpClient.configClient
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO

actual val apiClient: HttpClient = HttpClient(CIO) {
    configClient()
}
