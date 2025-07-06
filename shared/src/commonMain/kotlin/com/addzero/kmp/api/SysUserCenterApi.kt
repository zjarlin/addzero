package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.isomorphic.SysUserIso

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_user.controller.SysUserCenterController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysUserCenterApi {

/**
 * getCurrentUser
 * HTTP方法: GET
 * 路径: /sysUser/getCurrentUser
 * 返回类型: com.addzero.kmp.SysUserIso
 */
    @GET("/sysUser/getCurrentUser")    suspend fun getCurrentUser(): SysUserIso

/**
 * updatePassword
 * HTTP方法: POST
 * 路径: /sysUser/updatePassword
 * 参数:
 *   - newPassword: kotlin.String (RequestBody)
 * 返回类型: kotlin.Boolean
 */
    @POST("/sysUser/updatePassword")    suspend fun updatePassword(
        @Body newPassword: String
    ): Boolean

/**
 * logout
 * HTTP方法: POST
 * 路径: /sysUser/logout
 * 返回类型: kotlin.Boolean
 */
    @POST("/sysUser/logout")    suspend fun logout(): Boolean

}