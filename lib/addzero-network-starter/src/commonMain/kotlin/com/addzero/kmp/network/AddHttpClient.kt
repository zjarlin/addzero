package com.addzero.kmp.network// 在 commonMain 中定义共享代码
//import com.lt.lazy_people_http.config.LazyPeopleHttpConfig
import com.addzero.kmp.network.json.json
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


object AddHttpClient {


    //    val hfconfig = LazyPeopleHttpConfig(apiClient)
//    val ktorfit = Ktorfit.Builder().httpClient(apiClient).build()

    // Mock的token获取方式
    private var mytoken: String? = null

    // 设置mock token
    fun setToken(token: String?) {

//        Validator.Pattern
        mytoken = token
    }

    // 获取当前token
    fun getCurrentToken(): String? = mytoken


    fun configClient(): HttpClientConfig<*>.() -> Unit = {
        //请求头配置
        configHeaders()

        //超时配置 - 针对AI接口的长时间响应
        configTimeout()

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
        //基础url配置
//        configUrl()
        //日志插件
        configLog()
        //json解析插件
        configJson()
        //响应
//        configBaseRes()
    }


//    fun HttpClientConfig<*>.configUrl() {
//        defaultRequest {
////            url("https://api.apiopen.top")
//            url(BASE_URL)
//        }
//    }

    fun HttpClientConfig<*>.configHeaders() {
        defaultRequest {
            // 添加基础请求头
            headers {
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.ContentType, "application/json")
            }

            // 添加token
            getCurrentToken()?.let { token ->
                headers {
//                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.Authorization, token)
                }
            }
        }
    }

//    fun HttpClientConfig<*>.configBaseRes() {
//        install(BaseResponsePlugin) {
//            keysForStatus = listOf("code")
//            successCode = "200"
//            keysForData = listOf("data")
//        }
//    }

    fun HttpClientConfig<*>.configLog() {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    fun HttpClientConfig<*>.configJson() {
        install(ContentNegotiation) {
            json(json)
        }
    }

    /**
     * 配置超时时间 - 针对AI接口的长时间响应
     */
    fun HttpClientConfig<*>.configTimeout() {
        install(HttpTimeout) {
            // 请求超时时间 - 5分钟，适合AI接口的长时间处理
            requestTimeoutMillis = 5.minutes.inWholeMilliseconds
            // 连接超时时间 - 30秒
            connectTimeoutMillis = 30_000
            // Socket超时时间 - 5分钟
            socketTimeoutMillis = 5.minutes.inWholeMilliseconds
        }
    }


}

