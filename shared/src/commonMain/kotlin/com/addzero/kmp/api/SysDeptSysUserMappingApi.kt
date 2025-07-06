package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_dept_sys_user_mapping.controller.SysDeptSysUserMappingController
 * 基础路径: /sysDeptSysUserMapping
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysDeptSysUserMappingApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysDeptSysUserMapping/page
 * 返回类型: kotlin.Unit
 */
    @GET("/sysDeptSysUserMapping/page")    suspend fun page(): Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /sysDeptSysUserMapping/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysDeptSysUserMapping/save")    suspend fun save(): Unit

}