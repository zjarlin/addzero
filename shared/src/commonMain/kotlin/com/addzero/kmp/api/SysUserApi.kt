package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.entity.PageResult

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_user.controller.SysUserController
 * 基础路径: /sysUser
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysUserApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysUser/page
 * 返回类型: com.addzero.kmp.entity.PageResult<com.addzero.kmp.isomorphic.SysUserIso>
 */
    @GET("/sysUser/page")    suspend fun page(): com.addzero.kmp.entity.PageResult<com.addzero.kmp.isomorphic.SysUserIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /sysUser/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysUser/save")    suspend fun save(): kotlin.Unit

/**
 * tree
 * HTTP方法: GET
 * 路径: /sysUser/tree
 * 参数:
 *   - keyword: kotlin.String (RequestParam)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.isomorphic.SysUserIso>
 */
    @GET("/sysUser/tree")    suspend fun tree(
        @Query("keyword") keyword: kotlin.String
    ): kotlin.collections.List<com.addzero.kmp.isomorphic.SysUserIso>

}