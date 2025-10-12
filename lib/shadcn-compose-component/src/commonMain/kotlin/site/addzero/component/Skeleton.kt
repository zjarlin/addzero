package site.addzero.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import site.addzero.themes.radius
import site.addzero.themes.colors

/**
 * 受 Shadcn UI 启发的 Jetpack Compose 骨架屏组件。
 * 显示带有闪光效果的占位符加载状态。
 *
 * @param modifier 应用于骨架屏容器的修饰符。
 * @param shape 骨架屏的形状。默认为 `RoundedCornerShape(Radius.md)`。
 * @param baseColor 骨架屏的基础背景颜色。默认为 `MaterialTheme.shadcnColors.muted`。
 * @param shimmerColor 闪光高亮的颜色。默认为 `MaterialTheme.shadcnColors.background.copy(alpha = 0.8f)`。
 * @param animationDurationMillis 一个闪光周期的持续时间（毫秒）。
 * @param gradientWidthRatio 闪光渐变宽度与骨架屏宽度的比率。
 */
@Composable
fun Skeleton(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(MaterialTheme.radius.md),
    baseColor: Color = MaterialTheme.colors.muted,
    shimmerColor: Color = MaterialTheme.colors.muted.copy(alpha = 0.5f),
    animationDurationMillis: Int = 1500,
    gradientWidthRatio: Float = 0.5f
) {
    var size by remember { mutableStateOf(IntSize.Zero) }

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val xShimmerTranslate by transition.animateFloat(
        initialValue = -size.width.toFloat() * (1f + gradientWidthRatio),
        targetValue = size.width.toFloat() * (1f + gradientWidthRatio),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animationDurationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "xShimmerTranslate"
    )

    val gradientColors = listOf(
        baseColor,
        shimmerColor,
        baseColor
    )

    val brush = Brush.linearGradient(
        colors = gradientColors,
        // 渐变的起始和结束 X 坐标，通过 xShimmerTranslate 平移
        start = Offset(xShimmerTranslate, 0f),
        end = Offset(xShimmerTranslate + size.width * gradientWidthRatio, size.height.toFloat())
    )

    Box(
        modifier = modifier
            .onGloballyPositioned {
                size = it.size
            }
            .background(brush, shape)
    )
}
