package com.addzero.kmp.component.high_level

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

/**
 * 鼠标悬浮上去的提示效果
 * @param [text] 提示文字
 * @param [content] 包裹组件
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTooltipBox(
    text: String,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(), tooltip = {
            PlainTooltip {
                Text(text)
            }
        }, // 悬停时显示的文字
        state = rememberTooltipState()
    ) {
        content()
    }

}
