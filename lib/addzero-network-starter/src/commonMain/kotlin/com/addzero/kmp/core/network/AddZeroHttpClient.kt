package com.addzero.kmp.core.network

import com.addzero.kmp.core.network.json.json
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.api.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlin.time.Duration.Companion.minutes

// 创建一个通用的 HTTP 客户端工具类
expect val apiClient: HttpClient


internal fun configClient(): HttpClientConfig<*>.() -> Unit = {

    //超时配置 - 针对AI接口的长时间响应
    configTimeout()
    //响应拦截器
    install(createClientPlugin("HttpResponseInterceptor") {
        onResponse { response ->
            val bool = response.status.value != HttpStatusCode.OK.value
            if (bool) {
                val orNull = runCatching {
                    // 在协程作用域内执行挂起操作
                    response.bodyAsText()
                }.getOrNull()
                println("异常body: $orNull")
                GlobalEventDispatcher.handler(response)
            }
        }
    })
    configHeaders()
    //日志插件
    configLog()
    //json解析插件
    configJson()
}


fun HttpClientConfig<*>.configBaseUrl(baseUrl: String) {
    defaultRequest {
//            url("https://api.apiopen.top")
        url(baseUrl)
    }
}

private fun HttpClientConfig<*>.configHeaders() {
    defaultRequest {
        // 添加基础请求头
        headers {
            append(HttpHeaders.Accept, "application/json")
            append(HttpHeaders.ContentType, "application/json")
        }
    }
}


fun HttpClientConfig<*>.configToken(token: String?) {
    defaultRequest {
        // 添加基础请求头
        headers {
            append(HttpHeaders.Accept, "application/json")
            append(HttpHeaders.ContentType, "application/json")
        }
        // 添加token
        headers {
            if (token != null) {
                append(HttpHeaders.Authorization, token)
            }
        }
    }
}


private fun HttpClientConfig<*>.configLog() {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}

private fun HttpClientConfig<*>.configJson() {
    install(ContentNegotiation) {
        json(json)
    }
}

/**
 * 配置超时时间 - 针对AI接口的长时间响应
 */
private fun HttpClientConfig<*>.configTimeout() {
    install(HttpTimeout) {
        // 请求超时时间 - 5分钟，适合AI接口的长时间处理
        requestTimeoutMillis = 5.minutes.inWholeMilliseconds
        // 连接超时时间 - 30秒
        connectTimeoutMillis = 30_000
        // Socket超时时间 - 5分钟
        socketTimeoutMillis = 5.minutes.inWholeMilliseconds
    }


}

