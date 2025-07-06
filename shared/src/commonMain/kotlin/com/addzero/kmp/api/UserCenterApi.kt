package com.addzero.kmp.api

import com.addzero.kmp.isomorphic.SysUserIso
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST


interface UserCenterApi {

    /**
     * 获取当前用户信息
     */
    @GET("/sysUser/getCurrentUser")
    suspend fun getCurrentUser(): SysUserIso

    /**
     * 更新用户密码
     */
    @POST("/sysUser/updatePassword")
    suspend fun updatePassword(@Body newPassword: String): Boolean

    /**
     * 用户登出
     */
    @POST("/sysUser/logout")
    suspend fun logout(): Boolean
}
