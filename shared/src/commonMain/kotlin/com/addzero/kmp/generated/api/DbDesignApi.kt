package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.ai.agent.dbdesign.DbDesignController
 * 基础路径: 
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface DbDesignApi {

/**
 * dbdesign
 * HTTP方法: GET
 * 路径: dbdesign
 * 参数:
 *   - modelName: kotlin.String (RequestParam)
 *   - ques: kotlin.String (RequestParam)
 * 返回类型: com.addzero.kmp.entity.ai.FormDTO?
 */
    @GET("dbdesign")    suspend fun dbdesign(
        @Query("modelName") modelName: kotlin.String,
        @Query("ques") ques: kotlin.String
    ): com.addzero.kmp.entity.ai.FormDTO?

}