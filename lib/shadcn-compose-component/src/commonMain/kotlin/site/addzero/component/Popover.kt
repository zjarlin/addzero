package site.addzero.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import site.addzero.themes.radius
import site.addzero.themes.colors
import kotlin.math.roundToInt

/**
 * 受 Shadcn UI 启发的 Jetpack Compose 弹出框组件。
 * 在触发时显示一个浮动的内容面板（弹出框）。
 *
 * @param open 控制弹出框可见性的布尔状态。
 * @param onDismissRequest 当用户尝试关闭弹出框时调用的回调函数（例如，通过点击外部区域）。
 * @param modifier 应用于弹出框内容容器的修饰符。
 * @param trigger 将作为弹出框触发器的可组合内容。
 * @param content 要在弹出框内部显示的可组合内容。
 */
@Composable
fun Popover(
    open: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: (() -> Unit)? = null,
    trigger: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    var triggerWidthPx by remember { mutableIntStateOf(0) }
    var triggerHeightPx by remember { mutableIntStateOf(0) }
    var triggerXPositionPx by remember { mutableIntStateOf(0) }
    var triggerYPositionPx by remember { mutableIntStateOf(0) }

    Column {
        Box(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                triggerWidthPx = coordinates.size.width
                triggerHeightPx = coordinates.size.height
                val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                triggerXPositionPx = position?.x?.roundToInt() ?: 0
                triggerYPositionPx = position?.y?.roundToInt() ?: 0
            }
        ) {
            trigger()
        }

        if (open) {
            Popup(
                onDismissRequest = onDismissRequest,
                alignment = Alignment.TopStart,
                offset = IntOffset((triggerXPositionPx - triggerWidthPx) / 2, triggerYPositionPx + triggerHeightPx + 12)
            ) {
                Box(
                    modifier = Modifier.shadow(1.dp, RoundedCornerShape(radius.md)) // 阴影-md
                ) {
                    Column(
                        modifier = modifier
                            .background(colors.popover, RoundedCornerShape(radius.md))
                            .border(1.dp, colors.border, RoundedCornerShape(radius.md))
                            .padding(12.dp)
                    ) {
                        ProvideTextStyle(
                            value = TextStyle(
                                color = colors.popoverForeground,
                                fontSize = 14.sp
                            )
                        ) {
                            content()
                        }
                    }
                }
            }
        }
    }
}
