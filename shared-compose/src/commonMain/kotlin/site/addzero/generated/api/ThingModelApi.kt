package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.isomorphic.ThingModelIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.product.ThingModelController
 * 基础路径: /thingModel
 */
interface ThingModelApi {

/**
 * page
 * HTTP方法: POST
 * 路径: /thingModel/page
 * 参数:
 *   - commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO (RequestBody)
 * 返回类型: site.addzero.entity.PageResult<site.addzero.generated.isomorphic.ThingModelIso>
 */
    @POST("/thingModel/page")    suspend fun page(
        @Body commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO
    ): site.addzero.entity.PageResult<site.addzero.generated.isomorphic.ThingModelIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /thingModel/save
 * 参数:
 *   - input: site.addzero.generated.isomorphic.ThingModelIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.ThingModelIso
 */
    @POST("/thingModel/save")    suspend fun save(
        @Body input: site.addzero.generated.isomorphic.ThingModelIso
    ): site.addzero.generated.isomorphic.ThingModelIso

/**
 * upsert
 * HTTP方法: POST
 * 路径: /thingModel/upsert
 * 参数:
 *   - input: site.addzero.generated.isomorphic.ThingModelIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.ThingModelIso
 */
    @POST("/thingModel/upsert")    suspend fun upsert(
        @Body input: site.addzero.generated.isomorphic.ThingModelIso
    ): site.addzero.generated.isomorphic.ThingModelIso

/**
 * edit
 * HTTP方法: PUT
 * 路径: /thingModel/update
 * 参数:
 *   - e: site.addzero.generated.isomorphic.ThingModelIso (RequestBody)
 * 返回类型: kotlin.Int
 */
    @PUT("/thingModel/update")    suspend fun edit(
        @Body e: site.addzero.generated.isomorphic.ThingModelIso
    ): kotlin.Int

/**
 * deleteByIds
 * HTTP方法: DELETE
 * 路径: /thingModel/delete
 * 参数:
 *   - ids: kotlin.String (RequestParam)
 * 返回类型: kotlin.Int
 */
    @DELETE("/thingModel/delete")    suspend fun deleteByIds(
        @Query("ids") ids: kotlin.String
    ): kotlin.Int

/**
 * delete
 * HTTP方法: GET
 * 路径: /thingModel/delete
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: kotlin.Boolean
 */
    @GET("/thingModel/delete")    suspend fun delete(
        @Query("id") id: kotlin.String
    ): kotlin.Boolean

/**
 * saveBatch
 * HTTP方法: POST
 * 路径: /thingModel/saveBatch
 * 参数:
 *   - input: kotlin.collections.List<site.addzero.generated.isomorphic.ThingModelIso> (RequestBody)
 * 返回类型: kotlin.Int
 */
    @POST("/thingModel/saveBatch")    suspend fun saveBatch(
        @Body input: kotlin.collections.List<site.addzero.generated.isomorphic.ThingModelIso>
    ): kotlin.Int

/**
 * findById
 * HTTP方法: GET
 * 路径: /thingModel/findById
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: site.addzero.generated.isomorphic.ThingModelIso
 */
    @GET("/thingModel/findById")    suspend fun findById(
        @Query("id") id: kotlin.String
    ): site.addzero.generated.isomorphic.ThingModelIso

/**
 * loadTableConfig
 * HTTP方法: GET
 * 路径: /thingModel/loadTableConfig
 * 返回类型: kotlin.Unit
 */
    @GET("/thingModel/loadTableConfig")    suspend fun loadTableConfig(): kotlin.Unit

/**
 * tree
 * HTTP方法: GET
 * 路径: /thingModel/tree
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<site.addzero.generated.isomorphic.ThingModelIso>
 */
    @GET("/thingModel/tree")    suspend fun tree(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<site.addzero.generated.isomorphic.ThingModelIso>

}