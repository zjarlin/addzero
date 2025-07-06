package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.entity.low_table.CommonTableDaTaInputDTO
import com.addzero.kmp.entity.low_table.ExportParam
import com.addzero.kmp.entity.low_table.TableSaveOrUpdateDTO

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys.common.CommonController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface CommonApi {

/**
 * getTableData
 * HTTP方法: POST
 * 路径: /common/getTable
 * 参数:
 *   - commonTableDaTaInputDTO: com.addzero.kmp.entity.low_table.CommonTableDaTaInputDTO (RequestBody)
 * 返回类型: kotlin.String
 */
    @POST("/common/getTable")    suspend fun getTableData(
        @Body commonTableDaTaInputDTO: com.addzero.kmp.entity.low_table.CommonTableDaTaInputDTO
    ): kotlin.String

/**
 * export
 * HTTP方法: POST
 * 路径: /common/exportTable
 * 参数:
 *   - exportParam: com.addzero.kmp.entity.low_table.ExportParam (RequestBody)
 * 返回类型: kotlin.Boolean
 */
    @POST("/common/exportTable")    suspend fun export(
        @Body exportParam: com.addzero.kmp.entity.low_table.ExportParam
    ): kotlin.Boolean

/**
 * edit
 * HTTP方法: POST
 * 路径: /common/edit
 * 参数:
 *   - tableSaveOrUpdateDTO: com.addzero.kmp.entity.low_table.TableSaveOrUpdateDTO (RequestBody)
 * 返回类型: kotlin.Boolean
 */
    @POST("/common/edit")    suspend fun edit(
        @Body tableSaveOrUpdateDTO: com.addzero.kmp.entity.low_table.TableSaveOrUpdateDTO
    ): kotlin.Boolean

/**
 * checkExist
 * HTTP方法: GET
 * 路径: /common/checkExist
 * 参数:
 *   - tableName: kotlin.String (Query)
 *   - column: kotlin.String (Query)
 *   - value: kotlin.String (Query)
 * 返回类型: kotlin.Boolean
 */
    @GET("/common/checkExist")    suspend fun checkExist(
        @Query("tableName") tableName: kotlin.String,
        @Query("column") column: kotlin.String,
        @Query("value") value: kotlin.String
    ): kotlin.Boolean

}