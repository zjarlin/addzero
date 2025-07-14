package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.citys.controller.CitysController
 * 基础路径: /citys
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface CitysApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /citys/page
 * 返回类型: kotlin.Unit
 */
    @GET("/citys/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /citys/save
 * 返回类型: kotlin.Unit
 */
    @POST("/citys/save")    suspend fun save(): kotlin.Unit

}