package com.addzero.kmp.generated.api

import de.jensklingenberg.ktorfit.http.*

/**
 * Ktorfit接口 - 由KSP自动生成
 * 原始Controller: com.addzero.ai.util.ai.HttpAiController
 * 基础路径: /aiutil
 * 输出目录: /Users/zjarlin/AquaProjects/addzero/shared/src/commonMain/kotlin/com/addzero/kmp/generated/api
 */
interface HttpAiApi {

/**
 * ask
 * HTTP方法: GET
 * 路径: /aiutilask
 * 参数:
 *   - question: kotlin.String (RequestParam)
 *   - promptTemplate: kotlin.String (RequestParam)
 *   - chatModel: kotlin.String (RequestParam)
 *   - formatJson: kotlin.String (RequestParam)
 *   - formatJsonComment: kotlin.String (RequestParam)
 * 返回类型: kotlin.String
 */
    @GET("/aiutilask")    suspend fun ask(
        @Query("question") question: kotlin.String,
        @Query("promptTemplate") promptTemplate: kotlin.String,
        @Query("chatModel") chatModel: kotlin.String,
        @Query("formatJson") formatJson: kotlin.String,
        @Query("formatJsonComment") formatJsonComment: kotlin.String
    ): kotlin.String

}