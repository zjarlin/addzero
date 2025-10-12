package site.addzero.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import site.addzero.themes.radius
import site.addzero.themes.colors

/**
 * 进度条组件
 *
 * @param progress 进度值，范围从 0.0 到 1.0
 * @param modifier 应用于进度条的修饰符
 * @param height 进度条的高度
 * @param trackColor 轨道颜色
 * @param indicatorColor 指示器颜色
 */
@Composable
fun Progress(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    trackColor: Color = MaterialTheme.colors.muted,
    indicatorColor: Color = MaterialTheme.colors.primary
) {
    val radius = MaterialTheme.radius
    val clampedProgress = progress.coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = clampedProgress,
        animationSpec = tween(durationMillis = 500), label = "progressAnimation"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(radius.full))
            .background(trackColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(animatedProgress)
                .height(height)
                .clip(RoundedCornerShape(radius.full))
                .background(indicatorColor)
        )
    }
}
