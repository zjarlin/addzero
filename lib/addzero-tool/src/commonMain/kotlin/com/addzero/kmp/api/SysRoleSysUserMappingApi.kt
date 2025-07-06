package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_role_sys_user_mapping.controller.SysRoleSysUserMappingController
 * 基础路径: /sysRoleSysUserMapping
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysRoleSysUserMappingApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysRoleSysUserMapping/page
 * 返回类型: kotlin.Unit
 */
    @GET("/sysRoleSysUserMapping/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /sysRoleSysUserMapping/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysRoleSysUserMapping/save")    suspend fun save(): kotlin.Unit

}