package site.addzero.themes

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// 默认 Material 排版样式集，作为基础配置使用
/**
 * 默认排版配置
 *
 * 定义了应用的基础文字样式，包括字体族、字重、字号、行高和字间距等属性
 * 使用 Material Design 的默认字体系统和标准样式规范
 */
val DefaultTypography: Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
