package com.addzero.ai.document.customSplitter

import org.springframework.ai.document.Document
import org.springframework.ai.transformer.splitter.TextSplitter

class CustomTextSplitter : TextSplitter() {
    override fun apply(documents: List<Document>): List<Document> {
        return super.apply(documents)
    }

    override fun splitText(text: String): List<String> {
        return listOf()
    }
}
