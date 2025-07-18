package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.controller.FavoriteTabsController
 * 基础路径: /sysMenu
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface FavoriteTabsApi {

/**
 * getFavoriteRoutes
 * HTTP方法: GET
 * 路径: /sysMenu/getFavoriteRoutes
 * 返回类型: kotlin.collections.List<kotlin.String>
 */
    @GET("/sysMenu/getFavoriteRoutes")    suspend fun getFavoriteRoutes(): kotlin.collections.List<kotlin.String>

/**
 * addFavoriteRoute
 * HTTP方法: POST
 * 路径: /sysMenu/addFavoriteRoute
 * 参数:
 *   - request: kotlin.collections.Map<kotlin.String, kotlin.String> (RequestBody)
 * 返回类型: kotlin.Boolean
 */
    @POST("/sysMenu/addFavoriteRoute")    suspend fun addFavoriteRoute(
        @Body request: kotlin.collections.Map<kotlin.String, kotlin.String>
    ): kotlin.Boolean

/**
 * removeFavoriteRoute
 * HTTP方法: DELETE
 * 路径: /sysMenu/removeFavoriteRoute
 * 参数:
 *   - request: kotlin.collections.Map<kotlin.String, kotlin.String> (RequestBody)
 * 返回类型: kotlin.Boolean
 */
    @DELETE("/sysMenu/removeFavoriteRoute")    suspend fun removeFavoriteRoute(
        @Body request: kotlin.collections.Map<kotlin.String, kotlin.String>
    ): kotlin.Boolean

/**
 * updateFavoriteRoutesOrder
 * HTTP方法: POST
 * 路径: /sysMenu/updateFavoriteRoutesOrder
 * 参数:
 *   - request: kotlin.collections.Map<kotlin.String, kotlin.collections.List<kotlin.String>> (RequestBody)
 * 返回类型: kotlin.Boolean
 */
    @POST("/sysMenu/updateFavoriteRoutesOrder")    suspend fun updateFavoriteRoutesOrder(
        @Body request: kotlin.collections.Map<kotlin.String, kotlin.collections.List<kotlin.String>>
    ): kotlin.Boolean

}