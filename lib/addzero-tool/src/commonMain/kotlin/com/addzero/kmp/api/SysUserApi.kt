package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_user.controller.SysUserController
 * 基础路径: /sysUser
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysUserApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysUser/page
 * 返回类型: kotlin.Unit
 */
    @GET("/sysUser/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /sysUser/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysUser/save")    suspend fun save(): kotlin.Unit

}