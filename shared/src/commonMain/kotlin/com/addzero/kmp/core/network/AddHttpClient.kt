package com.addzero.kmp.core.network

import com.addzero.kmp.settings.SettingContext4Compose
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers

object AddHttpClient {
    private var mytoken: String? = null

    fun setToken(token: String?) {
        this.mytoken = token
    }

    fun getToken(): String? {
        return mytoken
    }

    val httpclient by lazy {
        apiClient.config {
            configBaseUrl(SettingContext4Compose.BASE_URL)
//            configToken(mytoken)
            // 动态添加token到每个请求
            defaultRequest {
                headers {
                    mytoken?.let {
                        append(io.ktor.http.HttpHeaders.Authorization, it)
                    }
                }
            }
        }
    }

    val ktorfit = Ktorfit.Builder().httpClient(httpclient).build()
}