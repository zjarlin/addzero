package site.addzero.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.radius
import site.addzero.themes.colors

enum class AlertVariant {
    Default, Destructive
}

/**
 * 向用户显示简短而重要的消息。
 *
 * @param modifier 应用于警告容器的修饰符。
 * @param variant 警告的视觉样式（Default 或 Destructive）。
 * @param colors 用于解析此警告在不同状态下使用的颜色的 [CheckboxColors]。
 *   参见 [AlertDefaults.colors]。
 * @param icon 在警告开始处显示的可选图标。
 * @param title 警告标题的可组合内容。
 * @param description 警告描述的可组合内容。
 */
@Composable
fun Alert(
    modifier: Modifier = Modifier,
    variant: AlertVariant = AlertVariant.Default,
    colors: AlertStyle = AlertDefaults.colors(),
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit
) {
    val radius = MaterialTheme.radius
    val titleColor = when (variant) {
        AlertVariant.Default -> colors.titleColor
        AlertVariant.Destructive -> MaterialTheme.colors.destructive
    }

    val descriptionColor = when (variant) {
        AlertVariant.Default -> colors.descriptionColor
        AlertVariant.Destructive -> MaterialTheme.colors.destructive.copy(alpha = 0.8f)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.backgroundColor, RoundedCornerShape(radius.md))
            .border(BorderStroke(1.dp, colors.borderColors), RoundedCornerShape(radius.md))
            .padding(16.dp)
    ) {
        icon?.let {
            // 图标大小和内边距
            Column(modifier = Modifier.padding(end = 12.dp)) {
                ProvideTextStyle(value = TextStyle(color = titleColor)) {
                    icon()
                }
            }
        }

        Column {
            ProvideTextStyle(
                value = TextStyle(
                    color = titleColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                )
            ) {
                title()
            }
            Spacer(modifier = Modifier.height(4.dp))
            ProvideTextStyle(
                value = TextStyle(
                    color = descriptionColor,
                    fontSize = 14.sp,
                )
            ) {
                description()
            }
        }
    }
}

data class AlertStyle(
    val borderColors: Color,
    val backgroundColor: Color,
    val titleColor: Color,
    val descriptionColor: Color
)

object AlertDefaults {
    @Composable
    fun colors(): AlertStyle {
        val colors = MaterialTheme.colors
        return AlertStyle(
            borderColors = colors.border,
            backgroundColor = colors.background,
            titleColor = colors.foreground,
            descriptionColor = colors.mutedForeground
        )
    }
}
