package com.addzero.web.modules.sys.ai

import cn.hutool.ai.model.deepseek.DeepSeekConfig
import cn.hutool.ai.model.deepseek.DeepSeekService
import cn.hutool.ai.model.doubao.DoubaoCommon
import cn.hutool.ai.model.doubao.DoubaoService
import com.addzero.kmp.entity.VisionRequest
import com.addzero.kmp.entity.sys.ai.DeepSeekChatResponse
import com.addzero.kmp.exp.BizException
import org.springframework.ai.chat.client.ChatClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/ai")
class AiController(
    private val deepSeekService: DeepSeekService,
    private val doubaoService: DoubaoService,
    private val deepSeekConfig: DeepSeekConfig,
//    @Value("\${doubao.key}") private val doubaoKey: String,

    private val chatClient: ChatClient
){


    /**
     * 对话
     * @param [promt]
     * @return [DeepSeekChatResponse]
     */
    @GetMapping("/ask")
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

    @PostMapping("/chatVision")
    fun chatVision(@RequestBody visionRequest: VisionRequest): String? {
        val (promt, images) = visionRequest
        val chatVision = doubaoService.chatVision(promt, images, DoubaoCommon.DoubaoVision.HIGH.detail)
        return chatVision
    }

       @PostMapping("/genVideo")
    fun genVideo(@RequestBody visionRequest: VisionRequest): String? {

        val (promt, images) = visionRequest

        if (images.size > 1) {
            throw BizException("暂不支持批量处理")
        }
        val videoTasks = doubaoService.videoTasks(
            promt, images[0]
        )
        return videoTasks

    }

    @GetMapping("/getAiVideoProgres")
    fun getAiVideoProgres(taskkId: String): Unit {

        //查询视频生成任务信息
        val videoTasksInfo: String? = doubaoService.getVideoTasksInfo(taskkId)
    }


}
