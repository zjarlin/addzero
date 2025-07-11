package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.ai.mcp.mavenapi.VersionController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface VersionApi {

/**
 * getLatestVersion
 * HTTP方法: GET
 * 路径: /latest-version
 * 参数:
 *   - groupId: kotlin.String (RequestParam)
 *   - artifactId: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.Map<kotlin.String, kotlin.String>
 */
    @GET("/latest-version")    suspend fun getLatestVersion(
        @Query("groupId") groupId: kotlin.String,
        @Query("artifactId") artifactId: kotlin.String
    ): kotlin.collections.Map<kotlin.String, kotlin.String>

}