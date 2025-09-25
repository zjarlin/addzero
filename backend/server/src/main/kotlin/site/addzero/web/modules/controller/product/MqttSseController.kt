package site.addzero.web.modules.controller.product

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import site.addzero.utils.MQTTUtils
import java.util.concurrent.CopyOnWriteArrayList

@RestController
@RequestMapping("/mqtt")
class MqttSseController {

    // 用于存储所有的 SseEmitter，管理多个客户端连接
    private val emitters = CopyOnWriteArrayList<SseEmitter>()

    @GetMapping("/consumer", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun consumer(
        @RequestParam(defaultValue = "broker.emqx.io") brokerHost: String,
        @RequestParam(defaultValue = "1883") brokerPort: Int,
        @RequestParam topic: String
    ): SseEmitter {
        // 创建 SseEmitter，设置超时时间为 30 分钟
        val emitter = SseEmitter(1800000L)

        // 添加到 emitters 列表
        emitters.add(emitter)

        // 注册连接完成、超时、出错时的回调，从列表中移除该 emitter
        emitter.onCompletion { emitters.remove(emitter) }
        emitter.onTimeout {
            emitters.remove(emitter)
            emitter.complete()
        }
        emitter.onError { emitters.remove(emitter) }

        // 调用 consumerBasic 方法，订阅 MQTT 主题并设置回调
        consumerBasic(brokerHost, brokerPort, topic) { mqtt3Publish ->
            try {
                // 发送 SSE 消息，将 MQTT 消息内容推送给客户端
                val message = String(mqtt3Publish.payloadAsBytes)
                emitter.send(SseEmitter.event().name("mqttMessage").data("主题: ${mqtt3Publish.topic}, 内容: $message"))
            } catch (e: Exception) {
                // 发送失败，移除该 emitter
                emitters.remove(emitter)
                emitter.complete()
            }
        }

        return emitter
    }

    fun consumerBasic(
        brokerHost: String, brokerPort: Int, topic: String, callBackk: (Mqtt3Publish) -> Unit
    ) {
        val mqttUtils = MQTTUtils(brokerHost = brokerHost, brokerPort = brokerPort)
        val consumer = mqttUtils.createConsumerInstance()
        // 连接客户端
        consumer.connect()
        MQTTUtils.subscribeToTopic(
            consumer, topic
        ) {
            callBackk(it)
        }
    }
}
