package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.isomorphic.SysDictItemIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.SysDictItemController
 * 基础路径: /sysDictItem
 */
interface SysDictItemApi {

/**
 * tree
 * HTTP方法: GET
 * 路径: /sysDictItem/tree
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<site.addzero.generated.isomorphic.SysDictItemIso>
 */
    @GET("/sysDictItem/tree")    suspend fun tree(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<site.addzero.generated.isomorphic.SysDictItemIso>

/**
 * page
 * HTTP方法: POST
 * 路径: /sysDictItem/page
 * 参数:
 *   - commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO (RequestBody)
 * 返回类型: site.addzero.entity.PageResult<site.addzero.generated.isomorphic.SysDictItemIso>
 */
    @POST("/sysDictItem/page")    suspend fun page(
        @Body commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO
    ): site.addzero.entity.PageResult<site.addzero.generated.isomorphic.SysDictItemIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /sysDictItem/save
 * 参数:
 *   - input: site.addzero.generated.isomorphic.SysDictItemIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.SysDictItemIso
 */
    @POST("/sysDictItem/save")    suspend fun save(
        @Body input: site.addzero.generated.isomorphic.SysDictItemIso
    ): site.addzero.generated.isomorphic.SysDictItemIso

/**
 * upsert
 * HTTP方法: POST
 * 路径: /sysDictItem/upsert
 * 参数:
 *   - input: site.addzero.generated.isomorphic.SysDictItemIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.SysDictItemIso
 */
    @POST("/sysDictItem/upsert")    suspend fun upsert(
        @Body input: site.addzero.generated.isomorphic.SysDictItemIso
    ): site.addzero.generated.isomorphic.SysDictItemIso

/**
 * edit
 * HTTP方法: PUT
 * 路径: /sysDictItem/update
 * 参数:
 *   - e: site.addzero.generated.isomorphic.SysDictItemIso (RequestBody)
 * 返回类型: kotlin.Int
 */
    @PUT("/sysDictItem/update")    suspend fun edit(
        @Body e: site.addzero.generated.isomorphic.SysDictItemIso
    ): kotlin.Int

/**
 * deleteByIds
 * HTTP方法: DELETE
 * 路径: /sysDictItem/delete
 * 参数:
 *   - ids: kotlin.String (RequestParam)
 * 返回类型: kotlin.Int
 */
    @DELETE("/sysDictItem/delete")    suspend fun deleteByIds(
        @Query("ids") ids: kotlin.String
    ): kotlin.Int

/**
 * delete
 * HTTP方法: GET
 * 路径: /sysDictItem/delete
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: kotlin.Boolean
 */
    @GET("/sysDictItem/delete")    suspend fun delete(
        @Query("id") id: kotlin.String
    ): kotlin.Boolean

/**
 * saveBatch
 * HTTP方法: POST
 * 路径: /sysDictItem/saveBatch
 * 参数:
 *   - input: kotlin.collections.List<site.addzero.generated.isomorphic.SysDictItemIso> (RequestBody)
 * 返回类型: kotlin.Int
 */
    @POST("/sysDictItem/saveBatch")    suspend fun saveBatch(
        @Body input: kotlin.collections.List<site.addzero.generated.isomorphic.SysDictItemIso>
    ): kotlin.Int

/**
 * findById
 * HTTP方法: GET
 * 路径: /sysDictItem/findById
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: site.addzero.generated.isomorphic.SysDictItemIso
 */
    @GET("/sysDictItem/findById")    suspend fun findById(
        @Query("id") id: kotlin.String
    ): site.addzero.generated.isomorphic.SysDictItemIso

/**
 * loadTableConfig
 * HTTP方法: GET
 * 路径: /sysDictItem/loadTableConfig
 * 返回类型: kotlin.Unit
 */
    @GET("/sysDictItem/loadTableConfig")    suspend fun loadTableConfig(): kotlin.Unit

}