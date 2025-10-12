package site.addzero.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import site.addzero.themes.colors

/**
 * 受 Shadcn UI 启发的 Jetpack Compose 开关组件。
 * 提供用于布尔状态的切换开关。
 *
 * @param checked 此开关是否被选中。
 * @param onCheckedChange 当开关的选中状态改变时调用的回调函数。
 * @param modifier 应用于开关的修饰符。
 * @param enabled 控制开关的启用状态。
 * @param colors [SwitchStyle] 将用于解析此开关颜色的样式
 */
@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: SwitchStyle = SwitchDefaults.colors()
) {
    val density = LocalDensity.current

    val switchWidth = 40.dp
    val switchHeight = 18.dp
    val thumbSize = 16.dp
    val borderWidth = 1.dp

    val thumbOffset by animateFloatAsState(
        targetValue = with(density) {
            if (checked) (switchWidth - thumbSize - borderWidth * 2).toPx() else borderWidth.toPx()
        },
        animationSpec = tween(durationMillis = 150), label = "thumbOffset"
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) colors.checkedTrack else colors.uncheckedTrack,
        animationSpec = tween(durationMillis = 150), label = "trackColor"
    )

    val borderColor by animateColorAsState(
        targetValue = if (checked) colors.checkedBorder else colors.uncheckedBorder,
        animationSpec = tween(durationMillis = 150), label = "borderColor"
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) colors.checkedThumb else colors.uncheckedThumb,
        animationSpec = tween(durationMillis = 150), label = "thumbColor"
    )

    val disabledAlpha = 0.5f // 禁用状态的透明度
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(width = switchWidth, height = switchHeight)
            .border(1.dp, borderColor.copy(alpha = if (enabled) 1f else disabledAlpha), CircleShape)
            .toggleable(
                value = checked,
                onValueChange = onCheckedChange ?: { /* 如果为 null 则不做任何操作 */ },
                enabled = enabled,
                role = Role.Switch,
                interactionSource = interactionSource,
                indication = null
            )
            .drawBehind {
                val trackCornerRadius = CornerRadius(switchHeight.toPx() / 2f)
                val thumbRadius = thumbSize.toPx() / 2f

                // 绘制轨道
                drawRoundRect(
                    color = trackColor.copy(alpha = if (enabled) 1f else disabledAlpha),
                    size = Size(size.width, size.height),
                    cornerRadius = trackCornerRadius
                )

                // 绘制滑块
                drawCircle(
                    color = thumbColor.copy(alpha = if (enabled) 1f else disabledAlpha),
                    radius = thumbRadius,
                    center = Offset(
                        x = thumbOffset + thumbRadius,
                        y = size.height / 2f
                    )
                )
            }
    )
}

data class SwitchStyle(
    val checkedBorder: Color,
    val checkedTrack: Color,
    val checkedThumb: Color,
    val uncheckedBorder: Color,
    val uncheckedTrack: Color,
    val uncheckedThumb: Color,
)

object SwitchDefaults {
    @Composable
    fun colors(): SwitchStyle {
        val colors = MaterialTheme.colors
        return SwitchStyle(
            checkedBorder = colors.primary,
            checkedTrack = colors.primary,
            checkedThumb = colors.primaryForeground,
            uncheckedBorder = colors.input,
            uncheckedTrack = colors.input,
            uncheckedThumb = colors.primary
        )
    }
}
