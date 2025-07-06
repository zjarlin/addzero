package com.addzero.common.util.translate

/**
 * 翻译器接口
 */
interface ITranslator {
    /**
     * 将中文文本翻译为英文
     * @param text 待翻译的文本
     * @param apiKey API密钥
     * @return 翻译后的英文文本
     */
    fun translateToEnglish(text: String): String

    val apiKey: String
}
