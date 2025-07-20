package com.addzero.common.util.translate

import cn.hutool.http.HttpRequest
import com.alibaba.fastjson2.JSON
import java.security.MessageDigest
import java.util.*

/**
 * 百度翻译实现类
 */
class BaiduTranslator : ITranslator {

    /**注意: 百度翻译apiKey格式为 appId:secretKey  */
    override val apiKey: String = System.getenv("BAIDUTRANS_KEY")


    companion object {
        private const val TRANSLATION_API = "http://api.fanyi.baidu.com/api/trans/vip/translate"
    }

    override fun translateToEnglish(text: String): String {

        // 解析apiKey (格式: appId:secretKey)
        val (appId, secretKey) = apiKey.split(":", limit = 2)

        // 生成签名
        val salt = UUID.randomUUID().toString()
        val sign = generateSign(appId, text, salt, secretKey)

        // 构建请求参数
        val params = mapOf(
            "q" to text,
            "from" to "zh",
            "to" to "en",
            "appid" to appId,
            "salt" to salt,
            "sign" to sign
        )

        // 发送请求
        val response = HttpRequest.post(TRANSLATION_API)
            .form(params) // 使用表单提交
            .execute()

        val responseBody = response.body()
        if (response.status != 200 || responseBody.isNullOrBlank()) {
            throw RuntimeException("Translation failed: ${response.status}")
        }

        // 解析响应
        val result = JSON.parseObject(responseBody)
        if (result.containsKey("error_code")) {
            throw RuntimeException("Translation failed: ${result.getString("error_msg")}")
        }

        return result.getJSONArray("trans_result")
            ?.getJSONObject(0)
            ?.getString("dst")
            ?.trim()
            ?: throw RuntimeException("Failed to parse translation response")
    }


    /**
     * 生成签名
     * 计算方法：appid + q + salt + 密钥的MD5值
     */
    private fun generateSign(appId: String, text: String, salt: String, secretKey: String): String {
        val content = appId + text + salt + secretKey
        return MessageDigest.getInstance("MD5")
            .digest(content.toByteArray())
            .joinToString("") { "%02x".format(it) }
    }
}
