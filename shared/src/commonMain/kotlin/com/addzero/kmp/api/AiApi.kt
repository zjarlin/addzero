package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys.ai.AiController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface AiApi {

/**
 * ask
 * HTTP方法: GET
 * 路径: /ai/ask
 * 参数:
 *   - promt: kotlin.String (Query)
 * 返回类型: kotlin.String
 */
    @GET("/ai/ask")    suspend fun ask(
        @Query("promt") promt: String
    ): String

/**
 * getDeepSeekBalance
 * HTTP方法: GET
 * 路径: getDeepSeekBalance
 * 返回类型: kotlin.String
 */
    @GET("getDeepSeekBalance")    suspend fun getDeepSeekBalance(): String

/**
 * 视觉
 * HTTP方法: POST
 * 路径: /ai/chatVision
 * 参数:
 *   - 视觉Request: com.addzero.kmp.视觉Request (RequestBody)
 * 返回类型: kotlin.String
 */
    @POST("/ai/chatVision")    suspend fun 视觉(
        @Body 视觉Request: 视觉Request
    ): String

/**
 * genVideo
 * HTTP方法: POST
 * 路径: /ai/genVideo
 * 参数:
 *   - 视觉Request: com.addzero.kmp.视觉Request (RequestBody)
 * 返回类型: kotlin.String
 */
    @POST("/ai/genVideo")    suspend fun genVideo(
        @Body 视觉Request: 视觉Request
    ): String

/**
 * getAiVideoProgres
 * HTTP方法: GET
 * 路径: /ai/getAiVideoProgres
 * 参数:
 *   - taskkId: kotlin.String (Query)
 * 返回类型: kotlin.Unit
 */
    @GET("/ai/getAiVideoProgres")    suspend fun getAiVideoProgres(
        @Query("taskkId") taskkId: String
    ): Unit

}