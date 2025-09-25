package site.addzero.web.modules.controller.product

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import site.addzero.utils.MQTTUtils
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/mqtt")
class MqttController {


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

}
