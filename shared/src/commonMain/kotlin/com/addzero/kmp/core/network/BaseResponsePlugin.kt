package com.addzero.kmp.core.network

import com.addzero.kmp.exp.BizException
import com.addzero.kmp.exp.ExceptionCode
import com.addzero.kmp.exp.JsonException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.AttributeKey
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import io.ktor.utils.io.readUTF8Line
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class BaseResponsePlugin(private val config: BaseResponseConfig) {

    class BaseResponseConfig {
        /**表示接口成功的识别码，这里统一定义为字符串类型，会将接口返回的转化为字符串后进行比较*/
        var successCode: String = "0" //code status

        /**status字段设置的key值，为了兼容接口可能返回不同的key,所以使用数组形式*/
        var keysForStatus = listOf("status")

        /**message字段设置的key值，为了兼容接口可能返回不同的key,所以使用数组形式*/
        var keysForMessage = listOf("message")

        /**data字段设置的key值，为了兼容接口可能返回不同的key,所以使用数组形式*/
        var keysForData = listOf("data")
    }

    companion object : HttpClientPlugin<BaseResponseConfig, BaseResponsePlugin> {
        override val key: AttributeKey<BaseResponsePlugin>
            get() = AttributeKey("BaseResponsePlugin")

        override fun prepare(block: BaseResponseConfig.() -> Unit): BaseResponsePlugin =
            BaseResponsePlugin(BaseResponseConfig().apply(block))

        override fun install(plugin: BaseResponsePlugin, scope: HttpClient) {
            scope.responsePipeline.intercept(HttpResponsePipeline.Receive) {
                val responseConfig = plugin.config

                val response = it.response

                if (response !is ByteReadChannel) return@intercept

                val bodyText = response.readUTF8Line() ?: ""

//                val json = Json {
//                    useAlternativeNames = true
//                    ignoreUnknownKeys = true
//                }
                //将取出来的json解析成JsonElement
                val jsonElement = json.parseToJsonElement(bodyText)
                if (jsonElement is JsonNull || jsonElement !is JsonObject) {
                    throw JsonException(ExceptionCode.JSON_NULL.toString(), "返回的数据为空")
                }

                val jsonObject = jsonElement.jsonObject
                //获取status字段的值
                var status = ""
                for (statusKey in responseConfig.keysForStatus) {
                    val tmp = jsonObject[statusKey]
                    if (tmp != null && tmp !is JsonNull && tmp is JsonPrimitive) {
                        status = tmp.jsonPrimitive.content.trim()
                        break
                    }
                }
                //获取message字段的值
                var message = ""

                for (messageKey in responseConfig.keysForMessage) {
                    val tmp = jsonObject[messageKey]
                    if (tmp != null && tmp !is JsonNull && tmp is JsonPrimitive) {
                        message = tmp.jsonPrimitive.content.trim()
                        break
                    }
                }

                if (status != responseConfig.successCode) {
                    throw BizException(message,status.toInt())
//                    throw JsonException(status, message)
                }

                var dataJson = ""
                for (dataKey in responseConfig.keysForData) {
                    val tmp = jsonObject[dataKey]

                    if (tmp !is JsonNull) {
                        dataJson = json.encodeToString(tmp)
                        break
                    }
                }

                val byteArray = dataJson.toByteArray(Charsets.UTF_8)

                val responseContainer =
                    HttpResponseContainer(it.expectedType, ByteReadChannel(byteArray))

                proceedWith(responseContainer)
            }
        }

    }
}
