package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.generated.isomorphic.SysFavoriteTabIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.controller.SysFavoriteTabController
 * 基础路径: /sysFavoriteTab
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface SysFavoriteTabApi {

/**
 * topFavoriteRoutes
 * HTTP方法: GET
 * 路径: /sysFavoriteTab/topFavoriteRoutes
 * 参数:
 *   - top: kotlin.Int (Query)
 * 返回类型: kotlin.collections.List<kotlin.String>
 */
    @GET("/sysFavoriteTab/topFavoriteRoutes")    suspend fun topFavoriteRoutes(
        @Query("top") top: kotlin.Int
    ): kotlin.collections.List<kotlin.String>

/**
 * add
 * HTTP方法: POST
 * 路径: /sysFavoriteTab/add
 * 参数:
 *   - sysFavoriteTab: com.addzero.kmp.generated.isomorphic.SysFavoriteTabIso (RequestBody)
 * 返回类型: kotlin.Boolean
 */
    @POST("/sysFavoriteTab/add")    suspend fun add(
        @Body sysFavoriteTab: com.addzero.kmp.generated.isomorphic.SysFavoriteTabIso
    ): kotlin.Boolean

}