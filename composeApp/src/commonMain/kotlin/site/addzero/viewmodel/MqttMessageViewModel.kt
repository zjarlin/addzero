package site.addzero.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import site.addzero.generated.api.ApiProvider.mqttApi

@KoinViewModel
class MqttMessageViewModel: ViewModel() {
    var brokerHost by mutableStateOf("broker.emqx.io")
    var brokerPort by mutableStateOf(1883)
    var topic by mutableStateOf("")
    var message by mutableStateOf("{\n  \"key\": \"value\"\n}")
    var sendResult by mutableStateOf("")

    suspend fun sendMessage() {
        if (topic.isBlank()) {
            sendResult = "错误: Topic不能为空"
            return
        }

        try {
            withContext(Dispatchers.Default) {
                // 调用API发送MQTT消息
                val result = mqttApi.producer(
                    brokerHost = brokerHost,
                    brokerPort = brokerPort,
                    topic = topic,
                    message = message
                )
                sendResult = "成功: $result"
            }
        } catch (e: Exception) {
            sendResult = "错误: ${e.message}"
        }
    }
}
