package com.addzero.kmp.core.network


import io.ktor.client.*
import io.ktor.client.engine.js.*

actual val apiClient: HttpClient = HttpClient(Js) {
    configClient()
}
