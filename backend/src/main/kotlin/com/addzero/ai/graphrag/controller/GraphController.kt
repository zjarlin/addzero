package com.addzero.ai.graphrag.controller

import com.addzero.ai.config.DefaultCtx
import com.addzero.ai.util.ai.ai_abs_builder.AiUtil
import com.addzero.ai.util.ai.ai_abs_builder.toGraphQuestion
import com.addzero.kmp.entity.graph.entity.GraphPO
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/graph")
class GraphController {

    /**
     * 对话graph抽取
     * 0 = "dashScopeAiVLChatModel"
     * 1 = "dashScopeAiChatModel"
     * 2 = "moonshotChatModel"
     * 3 = "ollamaChatModel"
     * 4 = "openAiChatModel"
     * 5 = "zhiPuAiChatModel"
     *
     * @param ques
     * @return [GraphPO]
     */
    @GetMapping("extractQues")
    @Operation(summary = "对话graph抽取")
    fun createGraph(
        @RequestParam ques: String, @RequestParam modelName: String?
    ): GraphPO {
        return ques.toGraphQuestion(modelName!!)
    }


    /**
     * 文件graph抽com.alibaba.dashscope.exception.ApiException: {"statusCode":400,"message":"Access denied, please make sure your account is in good standing.","code":"Arrearage","isJson":true,"requestId":"9018ebd9-25fe-98cc-8429-d901a0b8f528"}
     * 取
     * 0 = "dashScopeAiVLChatModel"
     * 1 = "dashScopeAiChatModel"
     * 2 = "moonshotChatModel"
     * 3 = "ollamaChatModel"
     * 4 = "openAiChatModel"
     * 5 = "zhiPuAiChatModel"
     *
     * @param file
     * @return [GraphPO]
     */
    @PostMapping("extractGraphFile")
    @Operation(summary = "附件graph抽取")
    fun createGraph(
        @RequestParam modelName: String = DefaultCtx.defaultChatModelName, @RequestParam file: MultipartFile
    ): List<GraphPO> {
// 读取文件内容
        val readContent = AiUtil.readContent(file)
        val toList = readContent.map {
            val it1 = it
            val content = it1.text.toGraphQuestion(modelName)
            content
        }
        return toList
    }


}
