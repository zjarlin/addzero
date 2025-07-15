package com.addzero.common.util.translate

/**
 * Markdown 翻译工具类
 * @author zjarlin
 */
object MarkdownTranslator {
    private val translator: ITranslator = BaiduTranslator()

    /**
     * 翻译中文 Markdown 为英文并追加到原文上方
     * @param content 原始内容
     * @param apiKey DashScope API Key
     * @return 处理后的内容
     */
    fun translateAndAppend(content: String): String {
        // 如果已经包含英文部分，直接返回
        if (content.startsWith("### English:")) {
            return content
        }

        // 提取需要翻译的文本
        val textToTranslate = content.split("\n").joinToString("\n") { line ->
            when {
                line.startsWith("# ") -> line.substring(2)
                line.startsWith("## ") -> line.substring(3)
                line.startsWith("### ") -> line.substring(4)
                line.startsWith("- ") -> line.substring(2)
                else -> line
            }
        }

        // 调用翻译 API
        val translatedText = translateToEnglish(textToTranslate)

        // 构建最终内容
        return buildString {
            appendLine("### English:")
            appendLine()
            appendLine(translatedText)
            appendLine()
            appendLine("### 中文：")
            appendLine()
            append(content)
        }
    }

    /**
     * 调用翻译器进行翻译
     */
    private fun translateToEnglish(text: String): String {
        return translator.translateToEnglish(text)
    }
}
