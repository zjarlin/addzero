package site.addzero.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.colors

/**
 * 受 Shadcn UI 启发的 Jetpack Compose 单选按钮组组件。
 * 管理一组 [RadioButtonWithLabel] 的选择状态。
 *
 * @param selectedValue 组中当前选中的值。
 * @param onValueChange 选择改变时调用的回调函数，提供新的选中值。
 * @param modifier 应用于单选按钮组容器的修饰符。
 * @param content 表示单选按钮及其标签的可组合内容。
 * 每个单选按钮都应该包装在一个可选行中，并带有其标签。
 * @param orientation 单选按钮组的方向（水平或垂直）。
 */
@Composable
fun <T> RadioGroup(
    selectedValue: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    orientation: LayoutOrientation = LayoutOrientation.Vertical, // 方向的新参数
    content: @Composable () -> Unit
) {
    when (orientation) {
        LayoutOrientation.Vertical -> Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
        LayoutOrientation.Horizontal -> Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}

/**
 * 布局方向的辅助枚举。
 */
enum class LayoutOrientation {
    Vertical,
    Horizontal
}

/**
 * 一个便捷的可组合函数，用于将原生 [RadioButton] 与 [Text] 标签组合，
 * 样式设计为匹配 Shadcn UI。这应该作为子组件在 [RadioGroup] 中使用。
 *
 * @param value 与此单选按钮关联的值。
 * @param label 此单选按钮的文本标签。
 * @param selectedValue 父组当前选中的值。
 * @param onValueChange 当此单选按钮被选中时的回调函数。
 * @param modifier 应用于包含单选按钮和标签的行的修饰符。
 * @param enabled 控制单选按钮和标签的启用状态。
 * @param colors 单选按钮和标签的可选自定义颜色。
 */
@Composable
fun <T> RadioButtonWithLabel(
    value: T,
    label: String,
    selectedValue: T,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: RadioButtonColors? = null
) {
    val themeColors = MaterialTheme.colors
    val isSelected = (selectedValue == value)

    Row(
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = { onValueChange(value) },
                enabled = enabled,
                role = Role.RadioButton,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            enabled = enabled,
            colors = colors ?: RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.primary,
                unselectedColor = MaterialTheme.colors.input,
                disabledSelectedColor = MaterialTheme.colors.primary.copy(alpha = 0.5f),
                disabledUnselectedColor = MaterialTheme.colors.mutedForeground.copy(alpha = 0.5f)
            ),
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = TextStyle(
                color = if (enabled) themeColors.foreground else themeColors.mutedForeground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
