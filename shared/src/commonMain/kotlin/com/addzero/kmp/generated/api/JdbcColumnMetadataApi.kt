package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.controller.JdbcColumnMetadataController
 * 基础路径: /jdbcColumnMetadata
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface JdbcColumnMetadataApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /jdbcColumnMetadata/page
 * 返回类型: kotlin.Unit
 */
    @GET("/jdbcColumnMetadata/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /jdbcColumnMetadata/save
 * 返回类型: kotlin.Unit
 */
    @POST("/jdbcColumnMetadata/save")    suspend fun save(): kotlin.Unit

}