package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.biz_tag.controller.BizTagController
 * 基础路径: /bizTag
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface BizTagApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /bizTag/page
 * 返回类型: kotlin.Unit
 */
    @GET("/bizTag/page")    suspend fun page(): Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /bizTag/save
 * 返回类型: kotlin.Unit
 */
    @POST("/bizTag/save")    suspend fun save(): Unit

}