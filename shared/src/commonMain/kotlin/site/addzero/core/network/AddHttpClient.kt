package site.addzero.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import site.addzero.BuildKonfig

object AddHttpClient {
  var mytoken: String? = null

  fun setToken(token: String?) {
    this.mytoken = token
  }

  fun getToken(): String? {
    return mytoken
  }


  val httpclient by lazy {
    apiClient.config {
      defaultRequest {
        url(BuildKonfig.BASE_URL)
      }
      configToken()

    }
  }


  private fun HttpClientConfig<*>.configToken() {
    defaultRequest {
      headers {
        mytoken?.let {
          append(HttpHeaders.Authorization, it)
        }
      }
    }
  }

  val ktorfit = Ktorfit.Builder().httpClient(httpclient).build()
}
