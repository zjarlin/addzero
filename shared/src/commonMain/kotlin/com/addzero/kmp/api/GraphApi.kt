package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.entity.graph.entity.GraphPO
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.ai.graphrag.controller.GraphController
 * 基础路径: /graph
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface GraphApi {

/**
 * createGraph
 * HTTP方法: GET
 * 路径: /graphextractQues
 * 参数:
 *   - ques: kotlin.String (RequestParam)
 *   - modelName: kotlin.String (RequestParam)
 * 返回类型: com.addzero.kmp.entity.graph.entity.GraphPO
 */
    @GET("/graphextractQues")    suspend fun createGraph(
        @Query("ques") ques: kotlin.String,
        @Query("modelName") modelName: kotlin.String
    ): com.addzero.kmp.entity.graph.entity.GraphPO

/**
 * createGraph
 * HTTP方法: POST
 * 路径: /graphextractGraphFile
 * 参数:
 *   - modelName: kotlin.String (RequestParam)
 *   - file: io.ktor.client.request.forms.MultiPartFormDataContent (RequestParam)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.entity.graph.entity.GraphPO>
 */
    @POST("/graphextractGraphFile")    suspend fun createGraph(
        @Query("modelName") modelName: kotlin.String,
        @Query("file") file: io.ktor.client.request.forms.MultiPartFormDataContent
    ): kotlin.collections.List<com.addzero.kmp.entity.graph.entity.GraphPO>

}