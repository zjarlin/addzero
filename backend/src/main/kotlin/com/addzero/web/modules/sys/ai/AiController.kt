package com.addzero.web.modules.sys.ai

import cn.hutool.ai.model.deepseek.DeepSeekConfig
import cn.hutool.ai.model.deepseek.DeepSeekService
import cn.hutool.ai.model.doubao.DoubaoCommon
import cn.hutool.ai.model.doubao.DoubaoService
import com.addzero.kmp.api.视觉Request
import com.addzero.kmp.entity.sys.ai.DeepSeekChatResponse
import com.addzero.kmp.exp.BizException
import org.springframework.ai.chat.client.ChatClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class AiController(
    private val deepSeekService: DeepSeekService,
    private val doubaoService: DoubaoService,
    private val deepSeekConfig: DeepSeekConfig,
    @Value("\${deepseek.key}") private val deepseekKey: String,
    @Value("\${doubao.key}") private val doubaoKey: String,

    private val chatClient: ChatClient
){


    /**
     * 对话
     * @param [promt]
     * @return [DeepSeekChatResponse]
     */
    @GetMapping("/ai/ask")
     fun ask(promt: String): String {
//        log.info("promt: $promt")
//        val chat = deepSeekService.chat(promt).parseObject<DeepSeekChatResponse>()
//        val joinToString = chat.choices?.map { it.message }?.joinToString(System.lineSeparator())
//        log.info("chat response: ${joinToString.toNotBlankStr()}")



        val call = chatClient.prompt().user { u: ChatClient.PromptUserSpec ->
            u.text(promt)
//                .params(map)
        }.call()

        val content = call.content()
        return content!!




//        return chat
    }

    /**
     * deepseek余额查询
     * @return [Unit]
     */
    @GetMapping("getDeepSeekBalance")
     fun getDeepSeekBalance(): String? {
        val balance: String? = deepSeekService.balance()
        return balance
    }

    @PostMapping("/ai/chatVision")
    fun 视觉(@RequestBody 视觉Request: 视觉Request): String? {
        val (promt, images) = 视觉Request
        val chatVision = doubaoService.chatVision(promt, images, DoubaoCommon.DoubaoVision.HIGH.detail)
        return chatVision
    }

       @PostMapping("/ai/genVideo")
    fun genVideo(@RequestBody 视觉Request: 视觉Request): String? {

        val (promt, images) = 视觉Request

        if (images.size > 1) {
            throw BizException("暂不支持批量处理")
        }
        val videoTasks = doubaoService.videoTasks(
            promt, images[0]
        )
        return videoTasks

    }

    @GetMapping("/ai/getAiVideoProgres")
    fun getAiVideoProgres(taskkId: String): Unit {

        //查询视频生成任务信息
        val videoTasksInfo: String? = doubaoService.getVideoTasksInfo(taskkId)
    }


}
