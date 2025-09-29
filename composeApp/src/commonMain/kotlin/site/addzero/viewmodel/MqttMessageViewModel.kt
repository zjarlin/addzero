package site.addzero.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.ktor.client.plugins.sse.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.koin.android.annotation.KoinViewModel
import site.addzero.BuildKonfig
import site.addzero.assist.api
import site.addzero.core.network.apiClientWithSse
import site.addzero.generated.api.ApiProvider.mqttApi

@KoinViewModel
class MqttMessageViewModel : ViewModel() {
    var brokerHost by mutableStateOf("broker.emqx.io")
    var brokerPort by mutableStateOf(1883)
    var topic by mutableStateOf("sensor/2/temperature")
    var message by mutableStateOf("{\n  \"key\": \"value\"\n}")
    var sendResult by mutableStateOf("")

    // 接收到的消息列表
    private val _receivedMessages = MutableStateFlow<List<String>>(emptyList())
    val receivedMessages: StateFlow<List<String>> = _receivedMessages.asStateFlow()

    // 控制订阅状态
    var isSubscribed by mutableStateOf(false)
    private var eventSourceJob: kotlinx.coroutines.Job? = null

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

    fun toggleSubscription() {
        if (isSubscribed) {
            stopSubscription()
        } else {
            startSubscription()
        }
    }

    private fun startSubscription() {
        if (topic.isBlank()) {
            sendResult = "错误: Topic不能为空"
            return
        }

        isSubscribed = true
        sendResult = "正在订阅消息..."

        api {
            apiClientWithSse.sse(
                host = BuildKonfig.BASE_HOST,
                port = BuildKonfig.BASE_PORT,
                path = "/mqtt/consumer?brokerHost=$brokerHost&brokerPort=$brokerPort&topic=$topic"
            ) {
                incoming.collect { event ->
                    // 处理SSE事件
                    val data = event.data.toString()
                    val currentList = _receivedMessages.value.toMutableList()
                    currentList.add(0, data) // 添加到列表开头
                    // 限制列表大小为100条消息
                    if (currentList.size > 100) {
                        currentList.removeAt(currentList.size - 1)
                    }
                    _receivedMessages.value = currentList
                }
            }
        }
    }

    private fun stopSubscription() {
        eventSourceJob?.cancel()
        isSubscribed = false
        sendResult = "已停止订阅"
    }

    override fun onCleared() {
        super.onCleared()
        stopSubscription()
    }
}
