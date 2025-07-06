package com.addzero.common.util.translate

import cn.hutool.core.util.StrUtil
import cn.hutool.http.HttpRequest
import com.alibaba.fastjson2.JSON

/**
 * DashScope AI翻译实现类
 */
class DashScopeTranslator private constructor() : ITranslator {
    override val apiKey: String
        get() = System.getenv("DASHSCOPE_API_KEY")


    companion object {
        private const val TRANSLATION_API =
            "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation"
        private var instance: DashScopeTranslator? = null

        @JvmStatic
        fun getInstance(): DashScopeTranslator {
            if (instance == null) {
                instance = DashScopeTranslator()
            }
            return instance!!
        }
    }

    override fun translateToEnglish(text: String): String {
        if (StrUtil.isBlank(apiKey)) {
            throw IllegalArgumentException("DashScope API Key is required")
        }

        // 构建请求参数
        val requestBody = mapOf(
            "model" to "qwen-max",
            "input" to mapOf(
                "messages" to listOf(
                    mapOf(
                        "role" to "system",
                        "content" to "You are a professional translator. Please translate the following Chinese text to English. Keep the original meaning and style, but make it natural in English."
                    ),
                    mapOf(
                        "role" to "user",
                        "content" to text
                    )
                )
            )
        )

        // 发送请求
        val response = HttpRequest.post(TRANSLATION_API)
            .header("Authorization", "Bearer $apiKey")
            .header("Content-Type", "application/json")
            .body(JSON.toJSONString(requestBody))
            .execute()

        val responseBody = response.body()
        if (response.status != 200 || responseBody.isNullOrBlank()) {
            throw RuntimeException("Translation failed: ${response.status}")
        }

        // 解析响应
        val result = JSON.parseObject(responseBody)
        val output = result.getJSONObject("output")
        if (output == null) {
            throw RuntimeException("Failed to parse translation response")
        }

        return output.getString("text")?.trim() ?: throw RuntimeException("Failed to get translated text")
    }
}
