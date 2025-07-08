package com.addzero.kmp.component.form.number

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

// 小数输入格式化
class DecimalVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = androidx.compose.ui.text.AnnotatedString(text.text.replace(".", ",")), // 可根据需要调整小数分隔符
            offsetMapping = OffsetMapping.Companion.Identity
        )
    }
}
