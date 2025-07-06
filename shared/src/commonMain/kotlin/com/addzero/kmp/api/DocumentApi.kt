package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import com.addzero.kmp.entity.ai.EmbDTO

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.ai.document.DocumentController
 * 基础路径: /document
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface DocumentApi {

/**
 * embedding
 * HTTP方法: POST
 * 路径: /documentembedding
 * 参数:
 *   - file: io.ktor.client.request.forms.MultiPartFormDataContent (RequestPart)
 * 返回类型: kotlin.collections.MutableList<kotlin.String>
 */
    @POST("/documentembedding")    suspend fun embedding(
        @Body file: io.ktor.client.request.forms.MultiPartFormDataContent
    ): MutableList<String>

/**
 * embeddingText
 * HTTP方法: POST
 * 路径: /documentembeddingText
 * 参数:
 *   - embDTO: com.addzero.kmp.EmbDTO (RequestBody)
 * 返回类型: kotlin.collections.MutableList<kotlin.String>
 */
    @POST("/documentembeddingText")    suspend fun embeddingText(
        @Body embDTO: EmbDTO
    ): MutableList<String>

/**
 * query
 * HTTP方法: GET
 * 路径: /documentquery
 * 参数:
 *   - query: kotlin.String (RequestParam)
 * 返回类型: kotlin.String
 */
    @GET("/documentquery")    suspend fun query(
        @Query("query") query: String
    ): String

}