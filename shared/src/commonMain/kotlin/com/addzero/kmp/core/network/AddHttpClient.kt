package com.addzero.kmp.core.network

import com.addzero.kmp.settings.SettingContext4Compose
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.http.Url

object AddHttpClient {
    private var mytoken: String? = null


    fun setToken(token: String?) {
        this.mytoken = token
    }


    fun getToken(): String? {
        return mytoken
    }


    val httpclient = apiClient.config {
        configToken(getToken())
        configBaseUrl(SettingContext4Compose.BASE_URL)
    }
    val ktorfit = Ktorfit.Builder().httpClient(httpclient).build()

}