package com.addzero.kmp.core.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO

actual val apiClient: io.ktor.client.HttpClient
   get() = HttpClient(CIO, configClient())
