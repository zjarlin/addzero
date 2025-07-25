package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Query

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.controller.FileController
 * 基础路径: /sys/file
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface FileApi {

    /**
     * upload
     * HTTP方法: POST
     * 路径: /sys/file/upload
     * 参数:
     *   - file: io.ktor.client.request.forms.MultiPartFormDataContent (RequestPart)
     * 返回类型: kotlin.String
     */
    @POST("/sys/file/upload")
    suspend fun upload(
        @Body file: io.ktor.client.request.forms.MultiPartFormDataContent
    ): kotlin.String

    /**
     * download
     * HTTP方法: POST
     * 路径: /sys/file/download
     * 参数:
     *   - fileId: kotlin.String (Query)
     * 返回类型: kotlin.String
     */
    @POST("/sys/file/download")
    suspend fun download(
        @Query("fileId") fileId: kotlin.String
    ): kotlin.String

    /**
     * queryProgress
     * HTTP方法: GET
     * 路径: /sys/filequeryProgress
     * 参数:
     *   - redisKey: kotlin.String (Query)
     * 返回类型: com.addzero.kmp.entity.FileUploadResponse
     */
    @GET("/sys/filequeryProgress")
    suspend fun queryProgress(
        @Query("redisKey") redisKey: kotlin.String
    ): com.addzero.kmp.entity.FileUploadResponse

}