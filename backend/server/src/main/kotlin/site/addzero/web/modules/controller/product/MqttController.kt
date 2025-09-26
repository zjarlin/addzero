package site.addzero.web.modules.controller.product

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import site.addzero.util.MQTTUtils

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
