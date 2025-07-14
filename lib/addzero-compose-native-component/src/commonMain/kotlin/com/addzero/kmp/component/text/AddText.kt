package com.addzero.kmp.component.text

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 📝 增强的文本组件
 * 
 * 基于 Material3 Text 的增强版本，提供：
 * - 预设样式快捷方式
 * - 状态颜色支持
 * - 可选择性控制
 * - 常用文本模式
 * 
 * @param text 文本内容
 * @param modifier 修饰符
 * @param style 文本样式
 * @param color 文本颜色
 * @param textAlign 文本对齐方式
 * @param overflow 溢出处理方式
 * @param maxLines 最大行数
 * @param selectable 是否可选择
 * @param fontWeight 字体粗细
 * @param textDecoration 文本装饰
 */
@Composable
fun AddText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int = Int.MAX_VALUE,
    selectable: Boolean = false,
    fontWeight: FontWeight? = null,
    textDecoration: TextDecoration? = null
) {
    val finalStyle = style.copy(
        fontWeight = fontWeight ?: style.fontWeight,
        textDecoration = textDecoration ?: style.textDecoration
    )
    
    if (selectable) {
        SelectionContainer {
            Text(
                text = text,
                modifier = modifier,
                style = finalStyle,
                color = color,
                textAlign = textAlign,
                overflow = overflow,
                maxLines = maxLines
            )
        }
    } else {
        Text(
            text = text,
            modifier = modifier,
            style = finalStyle,
            color = color,
            textAlign = textAlign,
            overflow = overflow,
            maxLines = maxLines
        )
    }
}

/**
 * 📋 标题文本组件
 */
@Composable
fun AddHeadlineText(
    text: String,
    modifier: Modifier = Modifier,
    level: HeadlineLevel = HeadlineLevel.MEDIUM,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null,
    selectable: Boolean = false
) {
    val style = when (level) {
        HeadlineLevel.LARGE -> MaterialTheme.typography.headlineLarge
        HeadlineLevel.MEDIUM -> MaterialTheme.typography.headlineMedium
        HeadlineLevel.SMALL -> MaterialTheme.typography.headlineSmall
    }
    
    AddText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        selectable = selectable,
        fontWeight = FontWeight.Bold
    )
}

/**
 * 🏷️ 标签文本组件
 */
@Composable
fun AddLabelText(
    text: String,
    modifier: Modifier = Modifier,
    isRequired: Boolean = false,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    AddText(
        text = if (isRequired) "$text *" else text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = FontWeight.Medium
    )
}

/**
 * 💬 正文文本组件
 */
@Composable
fun AddBodyText(
    text: String,
    modifier: Modifier = Modifier,
    size: BodySize = BodySize.MEDIUM,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null,
    maxLines: Int = Int.MAX_VALUE,
    selectable: Boolean = false
) {
    val style = when (size) {
        BodySize.LARGE -> MaterialTheme.typography.bodyLarge
        BodySize.MEDIUM -> MaterialTheme.typography.bodyMedium
        BodySize.SMALL -> MaterialTheme.typography.bodySmall
    }
    
    AddText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        textAlign = textAlign,
        maxLines = maxLines,
        selectable = selectable
    )
}

/**
 * ⚠️ 状态文本组件
 */
@Composable
fun AddStatusText(
    text: String,
    status: TextStatus,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight? = null
) {
    val color = when (status) {
        TextStatus.SUCCESS -> MaterialTheme.colorScheme.primary
        TextStatus.WARNING -> MaterialTheme.colorScheme.tertiary
        TextStatus.ERROR -> MaterialTheme.colorScheme.error
        TextStatus.INFO -> MaterialTheme.colorScheme.onSurfaceVariant
        TextStatus.DISABLED -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    }
    
    AddText(
        text = text,
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = fontWeight
    )
}

/**
 * 🔗 链接文本组件
 */
@Composable
fun AddLinkText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    style: TextStyle = MaterialTheme.typography.bodyMedium
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = PaddingValues(0.dp)
    ) {
        AddText(
            text = text,
            style = style,
            color = color,
            textDecoration = TextDecoration.Underline
        )
    }
}

/**
 * 📊 数值文本组件
 */
@Composable
fun AddNumberText(
    value: Number,
    modifier: Modifier = Modifier,
    prefix: String = "",
    suffix: String = "",
    decimalPlaces: Int? = null,
    color: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Medium
) {
    val formattedValue = when {
        decimalPlaces != null && value is Double -> {
            "%.${decimalPlaces}f".format(value)
        }
        decimalPlaces != null && value is Float -> {
            "%.${decimalPlaces}f".format(value)
        }
        else -> value.toString()
    }
    
    AddText(
        text = "$prefix$formattedValue$suffix",
        modifier = modifier,
        style = style,
        color = color,
        fontWeight = fontWeight
    )
}

/**
 * 📝 描述文本组件
 */
@Composable
fun AddDescriptionText(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 3,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    AddText(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodySmall,
        color = color,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

/**
 * 🏷️ 标签芯片文本组件
 */
@Composable
fun AddChipText(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onSecondaryContainer
) {
    Surface(
        modifier = modifier,
        color = backgroundColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.small
    ) {
        AddText(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = contentColor
        )
    }
}

/**
 * 标题级别枚举
 */
enum class HeadlineLevel {
    LARGE, MEDIUM, SMALL
}

/**
 * 正文大小枚举
 */
enum class BodySize {
    LARGE, MEDIUM, SMALL
}

/**
 * 文本状态枚举
 */
enum class TextStatus {
    SUCCESS, WARNING, ERROR, INFO, DISABLED
}
