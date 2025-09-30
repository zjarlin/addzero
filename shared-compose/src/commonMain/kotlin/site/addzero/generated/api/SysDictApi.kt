package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.isomorphic.SysDictIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.SysDictController
 * 基础路径: /sysDict
 */
interface SysDictApi {

/**
 * querydict
 * HTTP方法: GET
 * 路径: /sysDict/querydict
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<site.addzero.generated.isomorphic.SysDictIso>
 */
    @GET("/sysDict/querydict")    suspend fun querydict(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<site.addzero.generated.isomorphic.SysDictIso>

/**
 * tree
 * HTTP方法: GET
 * 路径: /sysDict/tree
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<site.addzero.generated.isomorphic.SysDictIso>
 */
    @GET("/sysDict/tree")    suspend fun tree(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<site.addzero.generated.isomorphic.SysDictIso>

/**
 * page
 * HTTP方法: POST
 * 路径: /sysDict/page
 * 参数:
 *   - commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO (RequestBody)
 * 返回类型: site.addzero.entity.PageResult<site.addzero.generated.isomorphic.SysDictIso>
 */
    @POST("/sysDict/page")    suspend fun page(
        @Body commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO
    ): site.addzero.entity.PageResult<site.addzero.generated.isomorphic.SysDictIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /sysDict/save
 * 参数:
 *   - input: site.addzero.generated.isomorphic.SysDictIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.SysDictIso
 */
    @POST("/sysDict/save")    suspend fun save(
        @Body input: site.addzero.generated.isomorphic.SysDictIso
    ): site.addzero.generated.isomorphic.SysDictIso

/**
 * upsert
 * HTTP方法: POST
 * 路径: /sysDict/upsert
 * 参数:
 *   - input: site.addzero.generated.isomorphic.SysDictIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.SysDictIso
 */
    @POST("/sysDict/upsert")    suspend fun upsert(
        @Body input: site.addzero.generated.isomorphic.SysDictIso
    ): site.addzero.generated.isomorphic.SysDictIso

/**
 * edit
 * HTTP方法: PUT
 * 路径: /sysDict/update
 * 参数:
 *   - e: site.addzero.generated.isomorphic.SysDictIso (RequestBody)
 * 返回类型: kotlin.Int
 */
    @PUT("/sysDict/update")    suspend fun edit(
        @Body e: site.addzero.generated.isomorphic.SysDictIso
    ): kotlin.Int

/**
 * deleteByIds
 * HTTP方法: DELETE
 * 路径: /sysDict/delete
 * 参数:
 *   - ids: kotlin.String (RequestParam)
 * 返回类型: kotlin.Int
 */
    @DELETE("/sysDict/delete")    suspend fun deleteByIds(
        @Query("ids") ids: kotlin.String
    ): kotlin.Int

/**
 * delete
 * HTTP方法: GET
 * 路径: /sysDict/delete
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: kotlin.Boolean
 */
    @GET("/sysDict/delete")    suspend fun delete(
        @Query("id") id: kotlin.String
    ): kotlin.Boolean

/**
 * saveBatch
 * HTTP方法: POST
 * 路径: /sysDict/saveBatch
 * 参数:
 *   - input: kotlin.collections.List<site.addzero.generated.isomorphic.SysDictIso> (RequestBody)
 * 返回类型: kotlin.Int
 */
    @POST("/sysDict/saveBatch")    suspend fun saveBatch(
        @Body input: kotlin.collections.List<site.addzero.generated.isomorphic.SysDictIso>
    ): kotlin.Int

/**
 * findById
 * HTTP方法: GET
 * 路径: /sysDict/findById
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: site.addzero.generated.isomorphic.SysDictIso
 */
    @GET("/sysDict/findById")    suspend fun findById(
        @Query("id") id: kotlin.String
    ): site.addzero.generated.isomorphic.SysDictIso

/**
 * loadTableConfig
 * HTTP方法: GET
 * 路径: /sysDict/loadTableConfig
 * 返回类型: kotlin.Unit
 */
    @GET("/sysDict/loadTableConfig")    suspend fun loadTableConfig(): kotlin.Unit

}