package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.api.视觉Request

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys.ai.AiController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/api
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
        @Query("promt") promt: kotlin.String
    ): kotlin.String

/**
 * getDeepSeekBalance
 * HTTP方法: GET
 * 路径: getDeepSeekBalance
 * 返回类型: kotlin.String
 */
    @GET("getDeepSeekBalance")    suspend fun getDeepSeekBalance(): kotlin.String

/**
 * 视觉
 * HTTP方法: POST
 * 路径: /ai/chatVision
 * 参数:
 *   - 视觉Request: com.addzero.kmp.api.视觉Request (RequestBody)
 * 返回类型: kotlin.String
 */
    @POST("/ai/chatVision")    suspend fun 视觉(
        @Body 视觉Request: com.addzero.kmp.api.视觉Request
    ): kotlin.String

/**
 * genVideo
 * HTTP方法: POST
 * 路径: /ai/genVideo
 * 参数:
 *   - 视觉Request: com.addzero.kmp.api.视觉Request (RequestBody)
 * 返回类型: kotlin.String
 */
    @POST("/ai/genVideo")    suspend fun genVideo(
        @Body 视觉Request: com.addzero.kmp.api.视觉Request
    ): kotlin.String

/**
 * getAiVideoProgres
 * HTTP方法: GET
 * 路径: /ai/getAiVideoProgres
 * 参数:
 *   - taskkId: kotlin.String (Query)
 * 返回类型: kotlin.Unit
 */
    @GET("/ai/getAiVideoProgres")    suspend fun getAiVideoProgres(
        @Query("taskkId") taskkId: kotlin.String
    ): kotlin.Unit

}