package site.addzero.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.ShadcnColors
import site.addzero.themes.radius
import site.addzero.themes.colors

enum class InputVariant {
    Outlined,
    Underlined
}

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 输入组件。
 * 提供具有 Shadcn 样式的可定制文本输入框。
 *
 * @param value 输入框的当前文本值。
 * @param onValueChange 输入框文本更改时调用的回调。
 * @param modifier 应用于输入框的修饰符。
 * @param placeholder 输入框为空时显示的占位符文本。
 * @param enabled 输入框是否启用交互。
 * @param readOnly 输入框是否为只读。
 * @param isError 输入框是否处于错误状态。
 * @param visualTransformation 应用于输入框文本的视觉转换。
 * @param interactionSource 用于在与输入框交互时分发事件的 [MutableInteractionSource]。
 * @param leadingIcon 在输入框开始处显示的可选可组合项。
 * @param trailingIcon 在输入框末尾显示的可选可组合项。
 * @param singleLine 为 true 时，此文本框不允许输入多行文本。
 * @param maxLines 输入框的最大行数。
 * @param minLines 输入框的最小行数。
 * @param keyboardOptions 包含类型、大小写、自动更正和操作的软件键盘选项。
 * @param keyboardActions 当输入服务发出 IME 操作时，将调用相应的回调。
 * @param variant 输入框的视觉样式（Outlined 或 Underlined）。
 * @param supportingText 在输入框下方显示的支持文本的可选可组合项。
 * @param colors 用于解析此输入框颜色的 [InputStyle]
 */
@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    interactionSource: MutableInteractionSource? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    variant: InputVariant = InputVariant.Outlined,
    colors: InputStyle = InputDefaults.colors()
) {
    val themeColors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    val currentInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isFocused by currentInteractionSource.collectIsFocusedAsState()

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.border.error
            isFocused -> colors.border.focus
            else -> colors.border.default
        },
        animationSpec = tween(durationMillis = 150), label = "borderColor"
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (enabled) colors.background else colors.disableBackground,
        animationSpec = tween(durationMillis = 150), label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (enabled) colors.text else colors.disableText,
        animationSpec = tween(durationMillis = 150), label = "textColor"
    )

    val placeholderColor = colors.placeholder
    val borderStyle = when (variant) {
        InputVariant.Outlined -> Modifier.border(1.dp, borderColor, RoundedCornerShape(radius.md))
        InputVariant.Underlined -> Modifier.drawBehind {
            val strokeWidth = 1.dp.toPx()
            val y = size.height - strokeWidth / 2
            drawLine(
                color = borderColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 44.dp)
                .background(backgroundColor, RoundedCornerShape(radius.md))
                .then(borderStyle),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = TextStyle(
                color = textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = currentInteractionSource,
            cursorBrush = SolidColor(themeColors.foreground),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    leadingIcon?.let {
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            ProvideTextStyle(value = TextStyle(color = themeColors.mutedForeground)) {
                                it()
                            }
                        }
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (value.isEmpty() && !isFocused) {
                            Text(
                                text = placeholder,
                                style = TextStyle(
                                    color = placeholderColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            )
                        }
                        innerTextField()
                    }

                    trailingIcon?.let {
                        Box(modifier = Modifier.padding(start = 8.dp)) {
                            ProvideTextStyle(value = TextStyle(color = themeColors.mutedForeground)) {
                                it()
                            }
                        }
                    }
                }
            }
        )

        // 支持文本
        supportingText?.let {
            Spacer(modifier = Modifier.height(2.dp))
            ProvideTextStyle(
                value = TextStyle(
                    color = colors.supportingText,
                    fontSize = 12.sp
                )
            ) {
                it()
            }
        }
    }
}

data class InputBorderStyle(
    val default: Color,
    val focus: Color,
    val error: Color,
)
data class InputStyle(
    val background: Color,
    val disableBackground: Color,
    val text: Color,
    val disableText: Color,
    val placeholder: Color,
    val border: InputBorderStyle,
    val supportingText: Color,
)

object InputDefaults {
    private fun colorsFrom(colors: ShadcnColors): InputStyle {
        return InputStyle(
            background = Color.Unspecified,
            disableBackground = colors.muted,
            text = Color.Unspecified,
            disableText = colors.mutedForeground,
            placeholder = colors.mutedForeground.copy(alpha = 0.5f),
            border = InputBorderStyle(
                default = colors.input,
                error = colors.destructive,
                focus = colors.ring
            ),
            supportingText = colors.mutedForeground
        )
    }

    @Composable
    fun colors(): InputStyle {
        val colors = MaterialTheme.colors
        return InputStyle(
            background = Color.Unspecified,
            disableBackground = colors.muted,
            text = Color.Unspecified,
            disableText = colors.mutedForeground,
            placeholder = colors.mutedForeground.copy(alpha = 0.5f),
            border = InputBorderStyle(
                default = colors.input,
                error = colors.destructive,
                focus = colors.ring
            ),
            supportingText = colors.mutedForeground
        )
    }

    @Composable
    fun colors(overrides: InputStyle.() -> InputStyle): InputStyle {
        val colors = MaterialTheme.colors
        return colorsFrom(colors).overrides()
    }
}
