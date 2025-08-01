package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.controller.SysDictItemController
 * 基础路径: /sysDictItem
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface SysDictItemApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysDictItem/page
 * 返回类型: kotlin.Unit
 */
    @GET("/sysDictItem/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /sysDictItem/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysDictItem/save")    suspend fun save(): kotlin.Unit

/**
 * tree
 * HTTP方法: GET
 * 路径: /sysDictItem/tree
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.generated.isomorphic.SysDictItemIso>
 */
    @GET("/sysDictItem/tree")    suspend fun tree(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<com.addzero.kmp.generated.isomorphic.SysDictItemIso>

}