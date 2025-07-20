package com.addzero.kmp.component.form.number

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyYen
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/**
 * 根据货币类型获取对应的图标
 */
fun getCurrencyIcon(currency: String): ImageVector {
    return when (currency.uppercase()) {
        "CNY", "RMB", "¥", "人民币" -> Icons.Default.CurrencyYen
        "USD", "DOLLAR", "$", "美元" -> Icons.Default.AttachMoney
        "EUR", "EURO", "€", "欧元" -> Icons.Default.AttachMoney
        "GBP", "POUND", "£", "英镑" -> Icons.Default.AttachMoney
        "JPY", "YEN", "日元" -> Icons.Default.CurrencyYen
        else -> Icons.Default.AttachMoney // 默认使用美元图标
    }
}

/**
 * 过滤整数输入，只允许数字和负号
 * @param input 输入的字符串
 * @param allowNegative 是否允许负数
 * @return 过滤后的字符串
 */
fun filterIntegerInput(input: String, allowNegative: Boolean = true): String {
    if (input.isEmpty()) return input

    return buildString {
        var hasNegative = false

        for (i in input.indices) {
            val char = input[i]
            when {
                // 数字字符始终允许
                char.isDigit() -> append(char)

                // 负号只能在开头，且只允许一个
                char == '-' && allowNegative && i == 0 && !hasNegative -> {
                    append(char)
                    hasNegative = true
                }

                // 其他字符都过滤掉
                else -> { /* 忽略非法字符 */
                }
            }
        }
    }
}

/**
 * 过滤小数输入，只允许数字、小数点和负号
 * @param input 输入的字符串
 * @param allowNegative 是否允许负数
 * @return 过滤后的字符串
 */
fun filterDecimalInput(input: String, allowNegative: Boolean = true): String {
    if (input.isEmpty()) return input

    return buildString {
        var hasDecimalPoint = false
        var hasNegative = false

        for (i in input.indices) {
            val char = input[i]
            when {
                // 数字字符始终允许
                char.isDigit() -> append(char)

                // 小数点只允许一个
                char == '.' && !hasDecimalPoint -> {
                    append(char)
                    hasDecimalPoint = true
                }

                // 负号只能在开头，且只允许一个
                char == '-' && allowNegative && i == 0 && !hasNegative -> {
                    append(char)
                    hasNegative = true
                }

                // 其他字符都过滤掉
                else -> { /* 忽略非法字符 */
                }
            }
        }
    }
}

// 小数输入格式化
class DecimalVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            text = androidx.compose.ui.text.AnnotatedString(text.text.replace(".", ",")), // 可根据需要调整小数分隔符
            offsetMapping = OffsetMapping.Companion.Identity
        )
    }
}