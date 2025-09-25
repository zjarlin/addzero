package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.isomorphic.ThingModelPropertyIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.product.ThingModelPropertyController
 * 基础路径: /thingModelProperty
 */
interface ThingModelPropertyApi {

/**
 * page
 * HTTP方法: POST
 * 路径: /thingModelProperty/page
 * 参数:
 *   - commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO (RequestBody)
 * 返回类型: site.addzero.entity.PageResult<site.addzero.generated.isomorphic.ThingModelPropertyIso>
 */
    @POST("/thingModelProperty/page")    suspend fun page(
        @Body commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO
    ): site.addzero.entity.PageResult<site.addzero.generated.isomorphic.ThingModelPropertyIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /thingModelProperty/save
 * 参数:
 *   - input: site.addzero.generated.isomorphic.ThingModelPropertyIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.ThingModelPropertyIso
 */
    @POST("/thingModelProperty/save")    suspend fun save(
        @Body input: site.addzero.generated.isomorphic.ThingModelPropertyIso
    ): site.addzero.generated.isomorphic.ThingModelPropertyIso

/**
 * upsert
 * HTTP方法: POST
 * 路径: /thingModelProperty/upsert
 * 参数:
 *   - input: site.addzero.generated.isomorphic.ThingModelPropertyIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.ThingModelPropertyIso
 */
    @POST("/thingModelProperty/upsert")    suspend fun upsert(
        @Body input: site.addzero.generated.isomorphic.ThingModelPropertyIso
    ): site.addzero.generated.isomorphic.ThingModelPropertyIso

/**
 * edit
 * HTTP方法: PUT
 * 路径: /thingModelProperty/update
 * 参数:
 *   - e: site.addzero.generated.isomorphic.ThingModelPropertyIso (RequestBody)
 * 返回类型: kotlin.Int
 */
    @PUT("/thingModelProperty/update")    suspend fun edit(
        @Body e: site.addzero.generated.isomorphic.ThingModelPropertyIso
    ): kotlin.Int

/**
 * deleteByIds
 * HTTP方法: DELETE
 * 路径: /thingModelProperty/delete
 * 参数:
 *   - ids: kotlin.String (RequestParam)
 * 返回类型: kotlin.Int
 */
    @DELETE("/thingModelProperty/delete")    suspend fun deleteByIds(
        @Query("ids") ids: kotlin.String
    ): kotlin.Int

/**
 * delete
 * HTTP方法: GET
 * 路径: /thingModelProperty/delete
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: kotlin.Boolean
 */
    @GET("/thingModelProperty/delete")    suspend fun delete(
        @Query("id") id: kotlin.String
    ): kotlin.Boolean

/**
 * saveBatch
 * HTTP方法: POST
 * 路径: /thingModelProperty/saveBatch
 * 参数:
 *   - input: kotlin.collections.List<site.addzero.generated.isomorphic.ThingModelPropertyIso> (RequestBody)
 * 返回类型: kotlin.Int
 */
    @POST("/thingModelProperty/saveBatch")    suspend fun saveBatch(
        @Body input: kotlin.collections.List<site.addzero.generated.isomorphic.ThingModelPropertyIso>
    ): kotlin.Int

/**
 * findById
 * HTTP方法: GET
 * 路径: /thingModelProperty/findById
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: site.addzero.generated.isomorphic.ThingModelPropertyIso
 */
    @GET("/thingModelProperty/findById")    suspend fun findById(
        @Query("id") id: kotlin.String
    ): site.addzero.generated.isomorphic.ThingModelPropertyIso

/**
 * loadTableConfig
 * HTTP方法: GET
 * 路径: /thingModelProperty/loadTableConfig
 * 返回类型: kotlin.Unit
 */
    @GET("/thingModelProperty/loadTableConfig")    suspend fun loadTableConfig(): kotlin.Unit

}