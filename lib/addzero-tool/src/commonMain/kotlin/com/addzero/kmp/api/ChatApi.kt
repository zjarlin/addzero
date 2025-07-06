package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.entity.ai.FormDTO

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.ai.chat.ChatController
 * 基础路径: /chat
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface ChatApi {

/**
 * jdaoisdj
 * HTTP方法: GET
 * 路径: /chatdbatest
 * 返回类型: com.addzero.kmp.entity.ai.FormDTO
 */
    @GET("/chatdbatest")    suspend fun jdaoisdj(): com.addzero.kmp.entity.ai.FormDTO

/**
 * chat
 * HTTP方法: POST
 * 路径: /chat/chatClient
 * 参数:
 *   - text: kotlin.String (Query)
 * 返回类型: kotlin.String
 */
    @POST("/chat/chatClient")    suspend fun chat(
        @Query("text") text: kotlin.String
    ): kotlin.String

}