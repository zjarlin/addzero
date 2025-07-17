package com.addzero.kmp.core.network


import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

actual val apiClient: HttpClient = HttpClient(Js) {
    configClient()
}
