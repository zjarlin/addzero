package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.isomorphic.DeviceIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.product.DeviceController
 * 基础路径: /device
 */
interface DeviceApi {

/**
 * page
 * HTTP方法: POST
 * 路径: /device/page
 * 参数:
 *   - commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO (RequestBody)
 * 返回类型: site.addzero.entity.PageResult<site.addzero.generated.isomorphic.DeviceIso>
 */
    @POST("/device/page")    suspend fun page(
        @Body commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO
    ): site.addzero.entity.PageResult<site.addzero.generated.isomorphic.DeviceIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /device/save
 * 参数:
 *   - input: site.addzero.generated.isomorphic.DeviceIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.DeviceIso
 */
    @POST("/device/save")    suspend fun save(
        @Body input: site.addzero.generated.isomorphic.DeviceIso
    ): site.addzero.generated.isomorphic.DeviceIso

/**
 * upsert
 * HTTP方法: POST
 * 路径: /device/upsert
 * 参数:
 *   - input: site.addzero.generated.isomorphic.DeviceIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.DeviceIso
 */
    @POST("/device/upsert")    suspend fun upsert(
        @Body input: site.addzero.generated.isomorphic.DeviceIso
    ): site.addzero.generated.isomorphic.DeviceIso

/**
 * edit
 * HTTP方法: PUT
 * 路径: /device/update
 * 参数:
 *   - e: site.addzero.generated.isomorphic.DeviceIso (RequestBody)
 * 返回类型: kotlin.Int
 */
    @PUT("/device/update")    suspend fun edit(
        @Body e: site.addzero.generated.isomorphic.DeviceIso
    ): kotlin.Int

/**
 * deleteByIds
 * HTTP方法: DELETE
 * 路径: /device/delete
 * 参数:
 *   - ids: kotlin.String (RequestParam)
 * 返回类型: kotlin.Int
 */
    @DELETE("/device/delete")    suspend fun deleteByIds(
        @Query("ids") ids: kotlin.String
    ): kotlin.Int

/**
 * delete
 * HTTP方法: GET
 * 路径: /device/delete
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: kotlin.Boolean
 */
    @GET("/device/delete")    suspend fun delete(
        @Query("id") id: kotlin.String
    ): kotlin.Boolean

/**
 * saveBatch
 * HTTP方法: POST
 * 路径: /device/saveBatch
 * 参数:
 *   - input: kotlin.collections.List<site.addzero.generated.isomorphic.DeviceIso> (RequestBody)
 * 返回类型: kotlin.Int
 */
    @POST("/device/saveBatch")    suspend fun saveBatch(
        @Body input: kotlin.collections.List<site.addzero.generated.isomorphic.DeviceIso>
    ): kotlin.Int

/**
 * findById
 * HTTP方法: GET
 * 路径: /device/findById
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: site.addzero.generated.isomorphic.DeviceIso
 */
    @GET("/device/findById")    suspend fun findById(
        @Query("id") id: kotlin.String
    ): site.addzero.generated.isomorphic.DeviceIso

/**
 * loadTableConfig
 * HTTP方法: GET
 * 路径: /device/loadTableConfig
 * 返回类型: kotlin.Unit
 */
    @GET("/device/loadTableConfig")    suspend fun loadTableConfig(): kotlin.Unit

}