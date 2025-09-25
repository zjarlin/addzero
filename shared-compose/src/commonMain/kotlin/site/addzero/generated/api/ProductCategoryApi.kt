package site.addzero.generated.api

import de.jensklingenberg.ktorfit.http.*
import site.addzero.entity.PageResult
import site.addzero.entity.low_table.CommonTableDaTaInputDTO
import site.addzero.generated.isomorphic.ProductCategoryIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: site.addzero.web.modules.controller.product.ProductCategoryController
 * 基础路径: /productCategory
 */
interface ProductCategoryApi {

/**
 * page
 * HTTP方法: POST
 * 路径: /productCategory/page
 * 参数:
 *   - commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO (RequestBody)
 * 返回类型: site.addzero.entity.PageResult<site.addzero.generated.isomorphic.ProductCategoryIso>
 */
    @POST("/productCategory/page")    suspend fun page(
        @Body commonTableDaTaInputDTO: site.addzero.entity.low_table.CommonTableDaTaInputDTO
    ): site.addzero.entity.PageResult<site.addzero.generated.isomorphic.ProductCategoryIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /productCategory/save
 * 参数:
 *   - input: site.addzero.generated.isomorphic.ProductCategoryIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.ProductCategoryIso
 */
    @POST("/productCategory/save")    suspend fun save(
        @Body input: site.addzero.generated.isomorphic.ProductCategoryIso
    ): site.addzero.generated.isomorphic.ProductCategoryIso

/**
 * upsert
 * HTTP方法: POST
 * 路径: /productCategory/upsert
 * 参数:
 *   - input: site.addzero.generated.isomorphic.ProductCategoryIso (RequestBody)
 * 返回类型: site.addzero.generated.isomorphic.ProductCategoryIso
 */
    @POST("/productCategory/upsert")    suspend fun upsert(
        @Body input: site.addzero.generated.isomorphic.ProductCategoryIso
    ): site.addzero.generated.isomorphic.ProductCategoryIso

/**
 * edit
 * HTTP方法: PUT
 * 路径: /productCategory/update
 * 参数:
 *   - e: site.addzero.generated.isomorphic.ProductCategoryIso (RequestBody)
 * 返回类型: kotlin.Int
 */
    @PUT("/productCategory/update")    suspend fun edit(
        @Body e: site.addzero.generated.isomorphic.ProductCategoryIso
    ): kotlin.Int

/**
 * deleteByIds
 * HTTP方法: DELETE
 * 路径: /productCategory/delete
 * 参数:
 *   - ids: kotlin.String (RequestParam)
 * 返回类型: kotlin.Int
 */
    @DELETE("/productCategory/delete")    suspend fun deleteByIds(
        @Query("ids") ids: kotlin.String
    ): kotlin.Int

/**
 * delete
 * HTTP方法: GET
 * 路径: /productCategory/delete
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: kotlin.Boolean
 */
    @GET("/productCategory/delete")    suspend fun delete(
        @Query("id") id: kotlin.String
    ): kotlin.Boolean

/**
 * saveBatch
 * HTTP方法: POST
 * 路径: /productCategory/saveBatch
 * 参数:
 *   - input: kotlin.collections.List<site.addzero.generated.isomorphic.ProductCategoryIso> (RequestBody)
 * 返回类型: kotlin.Int
 */
    @POST("/productCategory/saveBatch")    suspend fun saveBatch(
        @Body input: kotlin.collections.List<site.addzero.generated.isomorphic.ProductCategoryIso>
    ): kotlin.Int

/**
 * findById
 * HTTP方法: GET
 * 路径: /productCategory/findById
 * 参数:
 *   - id: kotlin.String (Query)
 * 返回类型: site.addzero.generated.isomorphic.ProductCategoryIso
 */
    @GET("/productCategory/findById")    suspend fun findById(
        @Query("id") id: kotlin.String
    ): site.addzero.generated.isomorphic.ProductCategoryIso

/**
 * loadTableConfig
 * HTTP方法: GET
 * 路径: /productCategory/loadTableConfig
 * 返回类型: kotlin.Unit
 */
    @GET("/productCategory/loadTableConfig")    suspend fun loadTableConfig(): kotlin.Unit

}