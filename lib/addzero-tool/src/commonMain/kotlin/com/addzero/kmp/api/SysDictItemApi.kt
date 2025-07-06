package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_dict_item.controller.SysDictItemController
 * 基础路径: /sysDictItem
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysDictItemApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysDictItem/page
 * 返回类型: kotlin.Unit
 */
    @GET("/sysDictItem/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /sysDictItem/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysDictItem/save")    suspend fun save(): kotlin.Unit

}