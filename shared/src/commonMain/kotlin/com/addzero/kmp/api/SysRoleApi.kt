package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_role.controller.SysRoleController
 * 基础路径: /sysRole
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysRoleApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysRole/page
 * 返回类型: kotlin.Unit
 */
    @GET("/sysRole/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /sysRole/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysRole/save")    suspend fun save(): kotlin.Unit

/**
 * tree
 * HTTP方法: GET
 * 路径: /sysRole/tree
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.isomorphic.SysRoleIso>
 */
    @GET("/sysRole/tree")    suspend fun tree(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<com.addzero.kmp.isomorphic.SysRoleIso>

}