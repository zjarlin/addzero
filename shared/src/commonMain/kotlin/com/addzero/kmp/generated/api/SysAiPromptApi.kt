package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.web.modules.controller.SysAiPromptController
 * 基础路径: /sysAiPrompt
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface SysAiPromptApi {

/**
 * getPrompts
 * HTTP方法: GET
 * 路径: /sysAiPrompt/getPrompts
 * 返回类型: kotlin.collections.List<com.addzero.kmp.generated.isomorphic.SysAiPromptIso>
 */
    @GET("/sysAiPrompt/getPrompts")    suspend fun getPrompts(): kotlin.collections.List<com.addzero.kmp.generated.isomorphic.SysAiPromptIso>

}