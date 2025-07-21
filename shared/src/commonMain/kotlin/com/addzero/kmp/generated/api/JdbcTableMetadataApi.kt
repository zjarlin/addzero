package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.controller.JdbcTableMetadataController
 * 基础路径: /jdbcTableMetadata
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface JdbcTableMetadataApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /jdbcTableMetadata/page
 * 返回类型: kotlin.Unit
 */
    @GET("/jdbcTableMetadata/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /jdbcTableMetadata/save
 * 返回类型: kotlin.Unit
 */
    @POST("/jdbcTableMetadata/save")    suspend fun save(): kotlin.Unit

}