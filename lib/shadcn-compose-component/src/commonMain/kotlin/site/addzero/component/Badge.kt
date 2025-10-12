package site.addzero.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.radius
import site.addzero.themes.colors

enum class BadgeVariant {
    Default,
    Secondary,
    Destructive,
    Outline
}

/**
 * 显示一个小型的信息标签，可根据变体进行自定义样式设计。
 *
 * 此徽章组件允许不同的视觉外观（变体），如 Default、Secondary、Destructive 和 Outline。
 * 它可以具有自定义背景色和圆角半径。徽章的内容通过可组合 lambda 提供。
 *
 * @param modifier 此徽章的可选 [Modifier]。
 * @param variant 徽章的视觉样式。如果未被覆盖，则确定默认颜色和边框。
 *   参见 [BadgeVariant] 获取可用选项。默认为 [BadgeVariant.Default]。
 * @param backgroundColor 徽章的可选显式背景色。如果为 null，
 *   则颜色由所选 [variant] 和当前主题确定。
 * @param roundedSize 徽章形状的圆角半径。默认为完全圆形
 *   （例如，`Radius.full` 可能对应 `CircleShape` 或较大的 Dp 值）。
 * @param content 定义徽章内显示内容的可组合 lambda。
 *   通常这将是一个 [androidx.compose.material3.Text] 可组合项。
 */
@Composable
fun Badge(
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.Default,
    backgroundColor: Color? = null,
    roundedSize: Dp = MaterialTheme.radius.full,
    content: (@Composable () -> Unit)? = null
) {
    val colors = MaterialTheme.colors

    val containerColor = backgroundColor ?: when (variant) {
        BadgeVariant.Default -> colors.primary
        BadgeVariant.Secondary -> colors.secondary
        BadgeVariant.Destructive -> colors.destructive
        BadgeVariant.Outline -> colors.background
    }

    val contentColor = when (variant) {
        BadgeVariant.Default -> colors.primaryForeground
        BadgeVariant.Secondary -> colors.secondaryForeground
        BadgeVariant.Destructive -> colors.destructiveForeground
        BadgeVariant.Outline -> colors.foreground
    }

    val borderStroke = when (variant) {
        BadgeVariant.Outline -> BorderStroke(1.dp, colors.input)
        else -> null
    }

    val size = if (content != null) 16.dp else 6.dp

    val shape = if (content != null) {
        RoundedCornerShape(roundedSize)
    } else {
        CircleShape
    }

    val borderShape = if (content != null) {
        RoundedCornerShape(roundedSize)
    } else {
        CircleShape
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = size, minHeight = size)
            .background(containerColor, borderShape)
            .then(borderStroke?.let { Modifier.border(it, shape) }
                ?: Modifier) // 如果存在边框则应用
            .then(
                if (content != null)
                    Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (content != null) {
            ProvideTextStyle(
                value = TextStyle(
                    color = contentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            ) {
                content()
            }
        }
    }
}
