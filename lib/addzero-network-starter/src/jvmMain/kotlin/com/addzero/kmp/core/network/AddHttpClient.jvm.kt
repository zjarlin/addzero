package com.addzero.kmp.core.network


import io.ktor.client.*
import io.ktor.client.engine.cio.*

actual val apiClient: HttpClient
    get() = HttpClient(CIO, configClient())
