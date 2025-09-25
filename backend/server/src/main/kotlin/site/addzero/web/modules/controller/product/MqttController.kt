package site.addzero.web.modules.controller.product

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import site.addzero.utils.MQTTUtils
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

@RestController
@RequestMapping("/mqtt")
class MqttController {

    // 存储活跃的订阅者，以便在需要时可以正确关闭连接
    private val emitters = ConcurrentHashMap<String, SseEmitter>()

    @GetMapping("/producer")
    fun producer(
        brokerHost: String = "broker.emqx.io",
        brokerPort: Int = 1883,
        topic: String,
        message: String
    ): String {
        val mqttUtils = MQTTUtils(
            brokerHost = brokerHost,
            brokerPort = brokerPort,
        )
        val producerInstance = mqttUtils.createProducerInstance()
        MQTTUtils.publishMessage(producerInstance, topic, message)
        return "Message published successfully"
    }

    @GetMapping("/consumer", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun consumer(
        @RequestParam(defaultValue = "broker.emqx.io") brokerHost: String,
        @RequestParam(defaultValue = "1883") brokerPort: Int,
        @RequestParam topic: String
    ): SseEmitter {
        val emitterId = "$brokerHost:$brokerPort:$topic:${System.currentTimeMillis()}"
        val emitter = SseEmitter(TimeUnit.HOURS.toMillis(1)) // 设置超时时间为1小时

        emitters[emitterId] = emitter

        emitter.onCompletion {
            emitters.remove(emitterId)
        }

        emitter.onTimeout {
            emitters.remove(emitterId)
            emitter.complete()
        }

        try {
            val mqttUtils = MQTTUtils(
                brokerHost = brokerHost,
                brokerPort = brokerPort,
            )
            val consumerInstance = mqttUtils.createConsumerInstance()

            MQTTUtils.subscribeToTopic(consumerInstance, topic) { message ->
                val msg = String(message.payloadAsBytes)
                val messageContent = """{"topic":"${message.topic}","content":"$msg"}"""

                try {
                    emitter.send(SseEmitter.event().name("message").data(messageContent))
                } catch (e: Exception) {
                    emitter.completeWithError(e)
                    emitters.remove(emitterId)
                }
            }
        } catch (e: Exception) {
            emitter.completeWithError(e)
            emitters.remove(emitterId)
        }

        return emitter
    }
}
