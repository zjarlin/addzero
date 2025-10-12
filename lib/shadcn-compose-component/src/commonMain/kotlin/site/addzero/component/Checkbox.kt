package site.addzero.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import site.addzero.themes.radius
import site.addzero.themes.colors

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 复选框组件。
 *
 * @param checked 此复选框是否被选中。
 * @param onCheckedChange 复选框选中状态更改时调用的回调。
 * @param modifier 应用于复选框的修饰符。
 * @param enabled 控制复选框的启用状态。当为 `false` 时，此复选框将不可交互。
 * @param colors 用于解析此复选框在不同状态下使用的颜色的 [CheckboxColors]。
 *      参见 [CheckboxDefaults.colors]。
 */
@Composable
fun Checkbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: CheckboxColors = CheckboxDefaults.colors()
) {
    val radius = MaterialTheme.radius
    
    // 背景颜色动画
    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledColors
            checked -> colors.checkedColors
            else -> colors.uncheckedColors
        },
        animationSpec = tween(durationMillis = 100), label = "checkboxBackgroundColor"
    )

    // 边框颜色动画
    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledColors
            checked -> colors.checkedBorderColors
            else -> colors.unCheckedBorderColors
        },
        animationSpec = tween(durationMillis = 100), label = "checkboxBorderColor"
    )

    // 勾选标记颜色动画
    val checkmarkColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledCheckMarkColor
            checked -> colors.checkedCheckmarkColor
            else -> colors.uncheckedCheckmarkColor
        },
        animationSpec = tween(durationMillis = 100), label = "checkboxCheckmarkColor"
    )

    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor = if (isPressed && enabled) {
        MaterialTheme.colors.ring
    } else borderColor // 按下时显示环状颜色

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(radius.sm))
            .background(backgroundColor)
            .border(1.dp, currentBorderColor, RoundedCornerShape(radius.sm))
            .toggleable(
                value = checked,
                onValueChange = { newChecked -> onCheckedChange?.invoke(newChecked) },
                enabled = enabled,
                role = Role.Checkbox,
                interactionSource = interactionSource,
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "已选中",
                tint = checkmarkColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

data class CheckboxColors(
    val checkedColors: Color,
    val uncheckedColors: Color,
    val disabledColors: Color,
    val disabledCheckMarkColor: Color,
    val checkedCheckmarkColor: Color,
    val uncheckedCheckmarkColor: Color,
    val checkedBorderColors: Color,
    val unCheckedBorderColors: Color,
)

object CheckboxDefaults {
    @Composable
    fun colors(): CheckboxColors {
        val colors = MaterialTheme.colors
        return CheckboxColors(
            colors.primary,
            Color.Transparent,
            colors.muted,
            colors.foreground,
            colors.primaryForeground,
            Color.Transparent,
            colors.primary,
            colors.input
        )
    }
}
