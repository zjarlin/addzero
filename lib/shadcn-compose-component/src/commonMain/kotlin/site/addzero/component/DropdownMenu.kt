package site.addzero.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem as ComposeDropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import site.addzero.themes.radius
import site.addzero.themes.colors

// --- 3. 下拉菜单组件 ---

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 下拉菜单组件。
 * 在触发时显示弹出菜单。
 *
 * @param expanded 控制下拉菜单可见性的布尔状态。
 * @param onDismissRequest 当用户尝试关闭菜单时调用的回调。
 * @param trigger 将作为下拉菜单触发器的可组合内容。
 * @param modifier 应用于下拉菜单容器的修饰符。
 * @param offset 下拉菜单相对于其锚点的偏移量。
 * @param content 下拉菜单项的可组合内容。
 */
@Composable
fun DropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    trigger: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    offset: DpOffset = DpOffset(0.dp, 4.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    Column {
        // 触发器可组合项（例如，按钮）
        trigger()

        // 下拉菜单本身
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
                .background(colors.popover, RoundedCornerShape(radius.md))
                .border(1.dp, colors.border, RoundedCornerShape(radius.md))
                .padding(4.dp),
            offset = offset
        ) {
            // 内容由调用者提供，允许自定义菜单项
            content()
        }
    }
}

/**
 * ShadcnDropdownMenu 的样式化菜单项。
 *
 * @param onClick 点击项目时调用的回调。
 * @param modifier 应用于菜单项的修饰符。
 * @param enabled 项目是否启用交互。
 * @param content 菜单项的可组合内容。
 */
@Composable
fun DropdownMenuItem(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = animateColorAsState(
        targetValue = if (isPressed) colors.accent else colors.background,
        animationSpec = tween(durationMillis = 100), label = "menuItemContainerColor"
    ).value

    val contentColor = animateColorAsState(
        targetValue = if (enabled) colors.foreground else colors.mutedForeground,
        animationSpec = tween(durationMillis = 100), label = "menuItemContentColor"
    ).value

    ComposeDropdownMenuItem(
        text = {
            ProvideTextStyle(
                value = TextStyle(
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        },
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .background(containerColor, RoundedCornerShape(radius.sm))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        enabled = enabled,
        colors = MenuDefaults.itemColors(
            textColor = contentColor,
            leadingIconColor = contentColor,
            disabledTextColor = colors.mutedForeground,
            trailingIconColor = contentColor,
            disabledLeadingIconColor = colors.mutedForeground,
            disabledTrailingIconColor = colors.mutedForeground,
        ),
        contentPadding = PaddingValues(0.dp), // 移除默认内边距，因为我们使用 modifier.padding 来处理
        interactionSource = interactionSource
    )
}

/**
 * ShadcnDropdownMenu 的样式化分隔符。
 *
 * @param modifier 应用于分隔符的修饰符。
 */
@Composable
fun DropdownMenuSeparator(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colors
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(colors.muted)
            .padding(vertical = 4.dp)
    )
}
