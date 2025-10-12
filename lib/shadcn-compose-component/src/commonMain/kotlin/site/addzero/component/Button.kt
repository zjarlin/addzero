package site.addzero.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.ShadcnColors
import site.addzero.themes.radius
import site.addzero.themes.colors

enum class ButtonVariant {
    Default,
    Destructive,
    Outline,
    Secondary,
    Ghost,
    Link
}

enum class ButtonSize {
    Default,
    Sm,
    Lg,
    Icon
}

@Composable
internal fun getButtonColors(
    variant: ButtonVariant,
    isPressed: Boolean,
    shadcnColors: ShadcnColors
): ButtonColors {
    return when (variant) {
        ButtonVariant.Default -> {
            val containerColor = if (isPressed) shadcnColors.primary.copy(alpha = 0.8f) else shadcnColors.primary
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnColors.primaryForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = shadcnColors.primary.copy(alpha = 0.5f),
                disabledContentColor = shadcnColors.primaryForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Destructive -> {
            val containerColor = if (isPressed) shadcnColors.destructive.copy(alpha = 0.8f) else shadcnColors.destructive
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnColors.destructiveForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = shadcnColors.destructive.copy(alpha = 0.5f),
                disabledContentColor = shadcnColors.destructiveForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Outline -> {
            val containerColor = if (isPressed) shadcnColors.muted else shadcnColors.background
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnColors.foreground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.outlinedButtonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = shadcnColors.foreground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Secondary -> {
            val containerColor = if (isPressed) shadcnColors.secondary.copy(alpha = 0.8f) else shadcnColors.secondary
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnColors.secondaryForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = shadcnColors.secondary.copy(alpha = 0.5f),
                disabledContentColor = shadcnColors.secondaryForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Ghost -> {
            val containerColor = if (isPressed) shadcnColors.accent else Color.Transparent
            val contentColor = if (isPressed) shadcnColors.accentForeground else shadcnColors.foreground
            val animatedContentColor = animateColorAsState(
                targetValue = contentColor,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = shadcnColors.foreground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Link -> {
            val contentColor = if (isPressed) shadcnColors.primary.copy(alpha = 0.8f) else shadcnColors.primary
            val animatedContentColor = animateColorAsState(
                targetValue = contentColor,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = shadcnColors.primary.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * @param onClick: () -> Unit - 点击按钮时调用的Lambda函数。
 * @param modifier: Modifier (可选, 默认值: Modifier) - 应用于按钮的修饰符。
 * @param variant: ButtonVariant (可选, 默认值: ButtonVariant.Default) - 按钮的视觉样式。
 *      参见 ButtonVariant 枚举获取可用选项。
 * @param size: ButtonSize (可选, 默认值: ButtonSize.Default) - 按钮的大小，
 *      影响其内边距和最小高度/宽度。参见 ButtonSize 枚举获取可用选项。
 * @param enabled: Boolean (可选, 默认值: true) - 控制按钮的启用状态。
 *      当为 false 时，按钮将在视觉上被禁用且不响应用户输入。
 * @param shape: Shape (可选, 默认值: RoundedCornerShape(Radius.md)) - 
 *      按钮容器的形状。
 * @param content: @Composable RowScope.() -> Unit - 显示在按钮内部的内容。
 *      这是一个以 RowScope 为接收者的可组合Lambda，允许在按钮内灵活
 *      布局内容（例如，文本、图标）。
 */
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Default,
    size: ButtonSize = ButtonSize.Default,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(MaterialTheme.radius.md),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val colors = MaterialTheme.colors
    val isPressed = interactionSource.collectIsPressedAsState().value

    val buttonColors = getButtonColors(variant, isPressed, colors)

    val borderStroke = when (variant) {
        ButtonVariant.Outline -> BorderStroke(1.dp, colors.input)
        else -> null
    }

    val contentPadding = when (size) {
        ButtonSize.Default -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ButtonSize.Sm -> PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ButtonSize.Lg -> PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ButtonSize.Icon -> PaddingValues(0.dp)
    }

    val minHeightModifier = when (size) {
        ButtonSize.Default -> Modifier.height(40.dp)
        ButtonSize.Sm -> Modifier.height(36.dp)
        ButtonSize.Lg -> Modifier.height(44.dp)
        ButtonSize.Icon -> Modifier
            .height(40.dp)
            .width(40.dp)
    }

    // 按钮的通用文本样式
    val buttonTextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )

    // 为链接样式按钮添加下划线
    val linkTextStyle = if (variant == ButtonVariant.Link) {
        buttonTextStyle.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
    } else {
        buttonTextStyle
    }

    // 对于幽灵和链接样式按钮使用TextButton以匹配行为并移除默认海拔阴影
    if (variant == ButtonVariant.Ghost || variant == ButtonVariant.Link) {
        TextButton(
            onClick = onClick,
            modifier = modifier.then(minHeightModifier),
            enabled = enabled,
            shape = shape,
            colors = buttonColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource
        ) {
            ButtonContent(
                textStyle = linkTextStyle,
                content = content
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = modifier.then(minHeightModifier),
            enabled = enabled,
            shape = shape,
            colors = buttonColors,
            border = borderStroke,
            contentPadding = contentPadding,
            interactionSource = interactionSource
        ) {
            ButtonContent(
                textStyle = linkTextStyle,
                content = content
            )
        }
    }
}

@Composable
private fun ButtonContent(
    content: @Composable RowScope.() -> Unit,
    textStyle: TextStyle
) {
    ProvideTextStyle(textStyle) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}
