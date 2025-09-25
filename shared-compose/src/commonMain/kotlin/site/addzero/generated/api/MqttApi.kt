package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.product.MqttController
 * 基础路径: /mqtt
 */
interface MqttApi {

/**
 * producer
 * HTTP方法: GET
 * 路径: /mqtt/producer
 * 参数:
 *   - brokerHost: kotlin.String (Query)
 *   - brokerPort: kotlin.Int (Query)
 *   - topic: kotlin.String (Query)
 *   - message: kotlin.String (Query)
 * 返回类型: kotlin.String
 */
    @GET("/mqtt/producer")    suspend fun producer(
        @Query("brokerHost") brokerHost: kotlin.String,
        @Query("brokerPort") brokerPort: kotlin.Int,
        @Query("topic") topic: kotlin.String,
        @Query("message") message: kotlin.String
    ): kotlin.String

}