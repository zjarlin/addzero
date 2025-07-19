package com.addzero.web.modules.controller

import cn.hutool.ai.model.deepseek.DeepSeekService
import cn.hutool.ai.model.doubao.DoubaoCommon
import cn.hutool.ai.model.doubao.DoubaoService
import com.addzero.kmp.entity.VisionRequest
import com.addzero.kmp.exp.BizException
import com.addzero.model.entity.SysAiPrompt
import org.springframework.ai.tool.ToolCallbackProvider
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/ai")
@Deprecated("use ChatController")
class AiController(
    private val deepSeekService: DeepSeekService,
    private val doubaoService: DoubaoService
) {

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

    /**
     * 🎥 生成视频任务
     *
     * @param visionRequest 视频生成请求，包含提示词和一张图片
     * @return 视频任务信息
     * @throws BizException 当上传多张图片时抛出异常
     */
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

    /**
     * 📽️ 查询视频生成任务进度
     *
     * @param taskkId 任务ID
     * @return 无返回值，目前仅仅调用服务函数获取任务信息
     */
    @GetMapping("/getAiVideoProgres")
    fun getAiVideoProgres(taskkId: String): Unit {

//查询视频生成任务信息
        val videoTasksInfo: String? = doubaoService.getVideoTasksInfo(taskkId)
    }




}