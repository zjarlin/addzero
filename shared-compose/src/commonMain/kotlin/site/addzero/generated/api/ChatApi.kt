package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.ai.chat.ChatController
 * 基础路径: /chat
 */
interface ChatApi {

/**
 * gettools
 * HTTP方法: GET
 * 路径: /chatgettools
 * 返回类型: kotlin.collections.List<kotlin.Any>
 */
    @GET("/chatgettools")    suspend fun gettools(): kotlin.collections.List<kotlin.Any>

/**
 * ask
 * HTTP方法: GET
 * 路径: /chat/ask
 * 参数:
 *   - text: kotlin.String (Query)
 * 返回类型: kotlin.Any
 */
    @GET("/chat/ask")    suspend fun ask(
        @Query("text") text: kotlin.String
    ): Any

}
