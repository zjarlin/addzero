package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.sys_ai_prompt.controller.SysAiPromptController
 * 基础路径: /sysAiPrompt
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface SysAiPromptApi {

/**
 * page
 * HTTP方法: GET
 * 路径: /sysAiPrompt/page
 * 返回类型: kotlin.Unit
 */
    @GET("/sysAiPrompt/page")    suspend fun page(): kotlin.Unit

/**
 * save
 * HTTP方法: POST
 * 路径: /sysAiPrompt/save
 * 返回类型: kotlin.Unit
 */
    @POST("/sysAiPrompt/save")    suspend fun save(): kotlin.Unit

}