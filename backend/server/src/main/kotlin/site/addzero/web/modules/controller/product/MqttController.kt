package site.addzero.web.modules.controller.product

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import site.addzero.utils.MQTTUtils
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

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

    // 设置SSE连接超时时间（30分钟）
    private val SSE_TIMEOUT = TimeUnit.MINUTES.toMillis(30)

    // 线程池：定时生成并推送随机数
    private val executor = Executors.newSingleThreadScheduledExecutor()

    @GetMapping("/consumer/sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun consumerSse(
        @RequestParam(defaultValue = "broker.emqx.io") brokerHost: String,
        @RequestParam(defaultValue = "1883") brokerPort: Int,
        @RequestParam topic: String
    ): SseEmitter {

        // 1. 创建 SSE 发射器（超时时间 30 分钟）
        val emitter = SseEmitter(1800000L)

        // 2. 连接关闭时清理线程资源
        emitter.onCompletion {
            println("SSE 连接关闭，停止随机数推送")
            executor.shutdown()
        }

        // 3. 定时推送随机数（每秒 1 次）
        executor.scheduleAtFixedRate({
            try {
                // 生成 0-100 的随机整数
                val randomNum = Random.nextInt(101)
                // 推送 SSE 消息（仅包含随机数数据）
                emitter.send(SseEmitter.event().data(randomNum))
                println("推送随机数：$randomNum")
            } catch (e: Exception) {
                // 推送失败（客户端断开），关闭连接
                emitter.complete()
            }
        }, 0, 1, TimeUnit.SECONDS) // 立即开始，间隔 1 秒

        return emitter
    }
}

//    @GetMapping("/consumer/sse", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
//    fun consumerSse(
//        @RequestParam(defaultValue = "broker.emqx.io") brokerHost: String,
//        @RequestParam(defaultValue = "1883") brokerPort: Int,
//        @RequestParam topic: String
//    ): SseEmitter {
//        // 创建SSE发射器
//        val emitter = SseEmitter(SSE_TIMEOUT)
//
//        // 当客户端断开连接时关闭MQTT连接
//        emitter.onCompletion {
//            // 清理MQTT相关资源
//            println("SSE连接已关闭，清理MQTT资源")
//        }
//
//        emitter.onTimeout {
//            emitter.completeWithError(RuntimeException("SSE连接超时"))
//        }
//
//        // 创建MQTT工具类
//        val mqttUtils = MQTTUtils(
//            brokerHost = brokerHost,
//            brokerPort = brokerPort,
//        )
//
//        // 创建消费者实例
//        val consumer = mqttUtils.createConsumerInstance()
//
//        // 订阅主题并通过SSE推送消息
//        MQTTUtils.subscribeToTopic(consumer, topic) { mqttMessage ->
//            try {
//                // 构建SSE消息
//                val sseEvent = SseEmitter.event()
//                    .data(
//                        mapOf(
//                            "topic" to mqttMessage.topic,
//                            "content" to String(mqttMessage.payloadAsBytes),
//                            "timestamp" to System.currentTimeMillis()
//                        )
//                    )
//                    .name("mqtt-message") // 事件名称，客户端可以根据名称过滤
//
//                // 发送消息到客户端
//                emitter.send(sseEvent)
//            } catch (e: Exception) {
//                // 发送失败，通常是客户端已断开连接
//                println("发送SSE消息失败: ${e.message}")
//                // 关闭MQTT连接
//
////                consumer.disconnect()
//                // 完成SSE连接
//                emitter.complete()
//            }
//        }
//
//        return emitter
//    }
