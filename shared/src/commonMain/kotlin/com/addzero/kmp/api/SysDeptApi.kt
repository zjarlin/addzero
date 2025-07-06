package com.addzero.kmp.api

import de.jensklingenberg.ktorfit.http.*
import com.addzero.kmp.isomorphic.SysDeptIso
import com.addzero.kmp.jdbc.meta.public.table.impl.ISysUserImpl

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_dept.controller.SysDeptController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/Downloads/AddzeroKmp/shared/src/commonMain/kotlin/com/addzero/kmp/api
 */
interface SysDeptApi {

/**
 * tree
 * HTTP方法: GET
 * 路径: /sysDept/tree
 * 参数:
 *   - keyword: kotlin.String (Query)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.SysDeptIso>
 */
    @GET("/sysDept/tree")    suspend fun tree(
        @Query("keyword") keyword: String
    ): List<SysDeptIso>

/**
 * save
 * HTTP方法: POST
 * 路径: /sysDept/save
 * 参数:
 *   - dept: com.addzero.kmp.SysDeptIso (RequestBody)
 * 返回类型: com.addzero.kmp.SysDeptIso
 */
    @POST("/sysDept/save")    suspend fun save(
        @Body dept: SysDeptIso
    ): SysDeptIso

/**
 * get
 * HTTP方法: GET
 * 路径: /sysDept/get/{id}
 * 参数:
 *   - id: kotlin.Long (Query)
 * 返回类型: com.addzero.kmp.SysDeptIso
 */
    @GET("/sysDept/get/{id}")    suspend fun get(
        @Query("id") id: Long
    ): SysDeptIso

/**
 * delete
 * HTTP方法: DELETE
 * 路径: /sysDept/delete
 * 参数:
 *   - id: kotlin.Long (Query)
 * 返回类型: kotlin.Unit
 */
    @DELETE("/sysDept/delete")    suspend fun delete(
        @Query("id") id: Long
    ): Unit

/**
 * getAvailableUsers
 * HTTP方法: GET
 * 路径: /sysDept/getAvailableUsers
 * 参数:
 *   - lng: kotlin.Long (Query)
 * 返回类型: kotlin.collections.List<com.addzero.kmp.ISysUserImpl>
 */
    @GET("/sysDept/getAvailableUsers")    suspend fun getAvailableUsers(
        @Query("lng") lng: Long
    ): List<ISysUserImpl>

}