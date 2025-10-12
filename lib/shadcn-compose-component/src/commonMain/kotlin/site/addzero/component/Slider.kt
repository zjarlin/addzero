package site.addzero.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Slider as ComposeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import site.addzero.themes.radius
import site.addzero.themes.colors

/**
 * 受 Shadcn UI 启发的 Jetpack Compose 滑块组件。
 * 允许用户从连续范围中选择一个值。
 *
 * @param value 滑块的当前值。
 * @param onValueChange 当滑块值改变时调用的回调函数。
 * @param modifier 应用于滑块的修饰符。
 * @param valueRange 滑块可以取的值范围。
 * @param steps 滑块可以采取的离散步数。
 * @param enabled 控制滑块的启用状态。
 * @param colors 滑块的可选自定义颜色。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Slider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    enabled: Boolean = true,
    colors: SliderColors? = null
) {
    val themeColors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    ComposeSlider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
        valueRange = valueRange,
        steps = steps,
        enabled = enabled,
        colors = colors ?: SliderDefaults.colors(
            thumbColor = themeColors.background,
            activeTrackColor = themeColors.primary,
            inactiveTrackColor = themeColors.secondary,
            activeTickColor = themeColors.primaryForeground.copy(alpha = 0.5f),
            inactiveTickColor = themeColors.secondaryForeground.copy(alpha = 0.5f),
            disabledThumbColor = themeColors.mutedForeground.copy(alpha = 0.5f),
            disabledActiveTrackColor = themeColors.primary.copy(alpha = 0.5f),
            disabledInactiveTrackColor = themeColors.secondary.copy(alpha = 0.5f),
            disabledActiveTickColor = themeColors.primaryForeground.copy(alpha = 0.2f),
            disabledInactiveTickColor = themeColors.secondaryForeground.copy(alpha = 0.2f)
        ),
        thumb = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(themeColors.background)
                    .border(2.dp, themeColors.primary, CircleShape)
            )
        },
        track = { sliderPositions ->
            val trackColor = if (enabled) themeColors.secondary else themeColors.secondary.copy(alpha = 0.5f)
            val activeTrackColor = if (enabled) themeColors.primary else themeColors.primary.copy(alpha = 0.5f)
            // 根据值和值范围计算活动轨道的分数
            val activeTrackWidthFraction = (value - valueRange.start) / (valueRange.endInclusive - valueRange.start)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(radius.full))
                    .background(trackColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(activeTrackWidthFraction)
                        .height(8.dp)
                        .clip(RoundedCornerShape(radius.full))
                        .background(activeTrackColor)
                )
            }
        }
    )
}
