package site.addzero.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.radius
import site.addzero.themes.colors
import site.addzero.themes.shadow

data class BoxShadow(
    val offsetX: Dp = 0.dp,
    val offsetY: Dp = 0.dp,
    val blurRadius: Dp = 0.dp,
    val spread: Dp = 0f.dp,
    val color: Color = Color.Gray
)

/**
 * 一个受Shadcn UI启发的Jetpack Compose卡片组件。
 * 这是卡片的主要容器。
 *
 * @param modifier 应用于卡片容器的修饰符。
 * @param shadow 卡片的阴影效果。
 * @param radius 卡片的圆角半径。
 * @param background 卡片的背景颜色。
 * @param border 卡片的边框颜色。
 * @param content 卡片的可组合内容。
 */
@Composable
fun Card(
    modifier: Modifier = Modifier,
    shadow: BoxShadow = MaterialTheme.shadow.md,
    radius: Dp = MaterialTheme.radius.lg,
    background: Color? = null,
    border: Color? = MaterialTheme.colors.border,
    content: @Composable ColumnScope.() -> Unit
) {
    val applyBg = background ?: MaterialTheme.colors.card
    Box(
        modifier = modifier
            .drawBehind {
                // 注意：Framework paint是平台特定的，使用跨平台阴影方法
                // 目前我们使用简单的阴影效果
                val spreadPixel = shadow.spread.toPx()
                val leftPixel = (0f - spreadPixel) + shadow.offsetX.toPx()
                val topPixel = (0f - spreadPixel) + shadow.offsetY.toPx()
                val rightPixel = (size.width + spreadPixel)
                val bottomPixel = (size.height + spreadPixel)

                drawRoundRect(
                    color = shadow.color,
                    topLeft = androidx.compose.ui.geometry.Offset(leftPixel, topPixel),
                    size = androidx.compose.ui.geometry.Size(rightPixel - leftPixel, bottomPixel - topPixel),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius.toPx()),
                    alpha = shadow.color.alpha / 255f
                )
            }
            .background(applyBg, RoundedCornerShape(radius))
            .clip(RoundedCornerShape(radius))
            .then(
                border?.let {
                    Modifier.border(1.dp, it, RoundedCornerShape(radius))
                } ?: Modifier
            )
    ) {
        Column(content = content)
    }
}

/**
 * 卡片头部的可组合组件。
 *
 * @param modifier 应用于头部的修饰符。
 * @param content 头部的可组合内容。
 */
@Composable
fun CardHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        content = content
    )
}

/**
 * 卡片标题的可组合组件。
 * 应该在 [CardHeader] 内使用。
 *
 * @param modifier 应用于标题文本的修饰符。
 * @param content 标题的可组合内容。
 */
@Composable
fun CardTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colors
    ProvideTextStyle(
        value = TextStyle(
            color = colors.cardForeground,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    ) {
        content()
    }
}

/**
 * 卡片描述的可组合组件。
 * 应该在 [CardHeader] 内使用。
 *
 * @param modifier 应用于描述文本的修饰符。
 * @param content 描述的可组合内容。
 */
@Composable
fun CardDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colors
    ProvideTextStyle(
        value = TextStyle(
            color = colors.mutedForeground,
            fontSize = 14.sp
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * 卡片主要内容区域的可组合组件。
 *
 * @param modifier 应用于内容区域的修饰符。
 * @param content 主要区域的可组合内容。
 */
@Composable
fun CardContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colors
    ProvideTextStyle(
        value = TextStyle(
            color = colors.foreground,
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            content = content
        )
    }
}

/**
 * 卡片底部区域的可组合组件。
 *
 * @param modifier 应用于底部的修饰符。
 * @param content 底部的可组合内容。
 */
@Composable
fun CardFooter(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colors
    ProvideTextStyle(
        value = TextStyle(
            color = colors.foreground,
        )
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content
        )
    }
}

object CardDefaults {
    fun shadows(elevation: Dp = 2.dp): BoxShadow {
        val offsetY = elevation * 0.5f
        val blurRadius = elevation * 0.75f
        val spread = elevation * 0.25f
        val shadowAlpha = (0.2f + (elevation.value / 20f)).coerceIn(0f, 0.6f)

        return BoxShadow(
            offsetX = 0.dp,
            offsetY = offsetY,
            blurRadius = blurRadius,
            spread = spread,
            color = Color.Gray.copy(alpha = shadowAlpha)
        )
    }

    fun shadows(boxShadow: BoxShadow): BoxShadow {
        return boxShadow
    }

}
