package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.product.MqttSseController
 * 基础路径: /mqtt
 */
interface MqttSseApi {

/**
 * consumer
 * HTTP方法: GET
 * 路径: /mqtt/consumer
 * 参数:
 *   - brokerHost: kotlin.String (RequestParam)
 *   - brokerPort: kotlin.Int (RequestParam)
 *   - topic: kotlin.String (RequestParam)
 * 返回类型: kotlin.Any
 */
    @GET("/mqtt/consumer")    suspend fun consumer(
        @Query("brokerHost") brokerHost: kotlin.String,
        @Query("brokerPort") brokerPort: kotlin.Int,
        @Query("topic") topic: kotlin.String
    ): kotlin.Any

}