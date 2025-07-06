package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.isomorphic.SysDictIso
import com.addzero.kmp.isomorphic.SysDictItemIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_dict.controller.SysDictController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysDictApi {

/**
 * querydict
 * HTTP方法: GET
 * 路径: /sys/dict/querydict
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.SysDictIso>
 */
    @GET("/sys/dict/querydict")    suspend fun querydict(
        @Query("keyword") keyword: String
    ): List<SysDictIso>

/**
 * saveDict
 * HTTP方法: POST
 * 路径: /sys/dict/saveDict
 * 参数:
 *   - vO: com.addzero.kmp.SysDictIso (RequestBody)
 * 返回类型: com.addzero.kmp.SysDictIso
 */
    @POST("/sys/dict/saveDict")    suspend fun saveDict(
        @Body vO: SysDictIso
    ): SysDictIso

/**
 * saveDictItem
 * HTTP方法: POST
 * 路径: /sys/dict/saveDictItem
 * 参数:
 *   - impl: com.addzero.kmp.SysDictItemIso (RequestBody)
 * 返回类型: kotlin.Unit
 */
    @POST("/sys/dict/saveDictItem")    suspend fun saveDictItem(
        @Body impl: SysDictItemIso
    ): Unit

/**
 * deleteDictItem
 * HTTP方法: GET
 * 路径: /sys/dict/deleteDictItem
 * 参数:
 *   - lng: kotlin.Long (RequestParam)
 * 返回类型: kotlin.Unit
 */
    @GET("/sys/dict/deleteDictItem")    suspend fun deleteDictItem(
        @Query("lng") lng: Long
    ): Unit

/**
 * deleteDict
 * HTTP方法: GET
 * 路径: /sys/dict/deleteDict
 * 参数:
 *   - lng: kotlin.Long (Query)
 * 返回类型: kotlin.Unit
 */
    @GET("/sys/dict/deleteDict")    suspend fun deleteDict(
        @Query("lng") lng: Long
    ): Unit

}