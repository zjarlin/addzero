package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.entity.VisionRequest
import com.addzero.kmp.entity.sys.ai.AiPrompt
import com.addzero.kmp.entity.sys.ai.SavePromptRequest
import com.addzero.kmp.entity.sys.ai.ChatWithPromptRequest
import com.addzero.kmp.entity.sys.ai.PromptStatistics
import com.addzero.kmp.entity.sys.ai.SearchPromptRequest

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys.ai.AiController
 * 基础路径: /ai
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface AiApi {

/**
 * getDeepSeekBalance
 * HTTP方法: GET
 * 路径: /aigetDeepSeekBalance
 * 返回类型: kotlin.String?
 */
    @GET("/aigetDeepSeekBalance")    suspend fun getDeepSeekBalance(): kotlin.String?

/**
 * chatVision
 * HTTP方法: POST
 * 路径: /ai/chatVision
 * 参数:
 *   - visionRequest: com.addzero.kmp.entity.VisionRequest (RequestBody)
 * 返回类型: kotlin.String?
 */
    @POST("/ai/chatVision")    suspend fun chatVision(
        @Body visionRequest: com.addzero.kmp.entity.VisionRequest
    ): kotlin.String?

/**
 * genVideo
 * HTTP方法: POST
 * 路径: /ai/genVideo
 * 参数:
 *   - visionRequest: com.addzero.kmp.entity.VisionRequest (RequestBody)
 * 返回类型: kotlin.String?
 */
    @POST("/ai/genVideo")    suspend fun genVideo(
        @Body visionRequest: com.addzero.kmp.entity.VisionRequest
    ): kotlin.String?

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

/**
 * getPrompts
 * HTTP方法: GET
 * 路径: /ai/prompts
 * 参数:
 *   - category: kotlin.String? (Query)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.entity.sys.ai.AiPrompt>
 */
    @GET("/ai/prompts")    suspend fun getPrompts(
        @Query("category") category: kotlin.String?
    ): kotlin.collections.List<com.addzero.kmp.entity.sys.ai.AiPrompt>

/**
 * getTools
 * HTTP方法: GET
 * 路径: /ai/tools
 * 返回类型: kotlin.collections.List<kotlin.String>
 */
    @GET("/ai/tools")    suspend fun getTools(): kotlin.collections.List<kotlin.String>

/**
 * getPromptById
 * HTTP方法: GET
 * 路径: /ai/prompts/{id}
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: com.addzero.kmp.entity.sys.ai.AiPrompt?
 */
    @GET("/ai/prompts/{id}")    suspend fun getPromptById(
        @Query("id") id: kotlin.String
    ): com.addzero.kmp.entity.sys.ai.AiPrompt?

/**
 * savePrompt
 * HTTP方法: POST
 * 路径: /ai/prompts
 * 参数:
 *   - request: com.addzero.kmp.entity.sys.ai.SavePromptRequest (RequestBody)
 * 返回类型: com.addzero.kmp.entity.sys.ai.AiPrompt
 */
    @POST("/ai/prompts")    suspend fun savePrompt(
        @Body request: com.addzero.kmp.entity.sys.ai.SavePromptRequest
    ): com.addzero.kmp.entity.sys.ai.AiPrompt

/**
 * updatePrompt
 * HTTP方法: POST
 * 路径: /ai/prompts/{id}
 * 参数:
 *   - id: kotlin.String (Query)
 *   - request: com.addzero.kmp.entity.sys.ai.SavePromptRequest (RequestBody)
 * 返回类型: com.addzero.kmp.entity.sys.ai.AiPrompt?
 */
    @POST("/ai/prompts/{id}")    suspend fun updatePrompt(
        @Query("id") id: kotlin.String,
        @Body request: com.addzero.kmp.entity.sys.ai.SavePromptRequest
    ): com.addzero.kmp.entity.sys.ai.AiPrompt?

/**
 * deletePrompt
 * HTTP方法: POST
 * 路径: /ai/prompts/{id}/delete
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: kotlin.collections.Map<kotlin.String, kotlin.String>
 */
    @POST("/ai/prompts/{id}/delete")    suspend fun deletePrompt(
        @Query("id") id: kotlin.String
    ): kotlin.collections.Map<kotlin.String, kotlin.String>

/**
 * getCategories
 * HTTP方法: GET
 * 路径: /ai/prompts/categories
 * 返回类型: kotlin.collections.List<kotlin.String>
 */
    @GET("/ai/prompts/categories")    suspend fun getCategories(): kotlin.collections.List<kotlin.String>

/**
 * getTags
 * HTTP方法: GET
 * 路径: /ai/prompts/tags
 * 返回类型: kotlin.collections.List<kotlin.String>
 */
    @GET("/ai/prompts/tags")    suspend fun getTags(): kotlin.collections.List<kotlin.String>

/**
 * searchPrompts
 * HTTP方法: GET
 * 路径: /ai/prompts/search
 * 参数:
 *   - keyword: kotlin.String (Query)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.entity.sys.ai.AiPrompt>
 */
    @GET("/ai/prompts/search")    suspend fun searchPrompts(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<com.addzero.kmp.entity.sys.ai.AiPrompt>

/**
 * chatWithPrompt
 * HTTP方法: POST
 * 路径: /ai/prompts/{id}/chat
 * 参数:
 *   - id: kotlin.String (Query)
 *   - variables: kotlin.collections.Map<kotlin.String, kotlin.String> (RequestBody)
 * 返回类型: kotlin.String
 */
    @POST("/ai/prompts/{id}/chat")    suspend fun chatWithPrompt(
        @Query("id") id: kotlin.String,
        @Body variables: kotlin.collections.Map<kotlin.String, kotlin.String>
    ): kotlin.String

/**
 * chatWithPromptV2
 * HTTP方法: POST
 * 路径: /ai/prompts/chat
 * 参数:
 *   - request: com.addzero.kmp.entity.sys.ai.ChatWithPromptRequest (RequestBody)
 * 返回类型: kotlin.String
 */
    @POST("/ai/prompts/chat")    suspend fun chatWithPromptV2(
        @Body request: com.addzero.kmp.entity.sys.ai.ChatWithPromptRequest
    ): kotlin.String

/**
 * getPromptStatistics
 * HTTP方法: GET
 * 路径: /ai/prompts/statistics
 * 返回类型: com.addzero.kmp.entity.sys.ai.PromptStatistics
 */
    @GET("/ai/prompts/statistics")    suspend fun getPromptStatistics(): com.addzero.kmp.entity.sys.ai.PromptStatistics

/**
 * searchPromptsV2
 * HTTP方法: POST
 * 路径: /ai/prompts/search
 * 参数:
 *   - request: com.addzero.kmp.entity.sys.ai.SearchPromptRequest (RequestBody)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.entity.sys.ai.AiPrompt>
 */
    @POST("/ai/prompts/search")    suspend fun searchPromptsV2(
        @Body request: com.addzero.kmp.entity.sys.ai.SearchPromptRequest
    ): kotlin.collections.List<com.addzero.kmp.entity.sys.ai.AiPrompt>

}