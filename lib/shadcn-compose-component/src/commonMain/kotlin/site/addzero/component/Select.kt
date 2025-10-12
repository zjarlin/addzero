package site.addzero.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import site.addzero.themes.radius
import site.addzero.themes.colors
import kotlin.math.roundToInt

/**
 * 受 Shadcn UI 启发的 Jetpack Compose 选择组件。
 * 提供一个下拉列表用于选择选项，以弹出框的形式显示。
 *
 * @param options 在选择下拉列表中显示的字符串选项列表。
 * @param selectedOption 当前选中的选项。如果没有选中选项则为 null。
 * @param onOptionSelected 当选项被选中时调用的回调函数，提供选中的字符串。
 * @param modifier 应用于选择容器的修饰符。
 * @param placeholder 当没有选中选项时显示的占位符文本。
 */
@Composable
fun Select(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "选择选项..."
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    var expanded by remember { mutableStateOf(false) }

    // 保存输入框位置和宽度的状态
    var inputWidthPx by remember { mutableIntStateOf(0) }
    var inputHeightPx by remember { mutableIntStateOf(0) }
    var inputXPositionPx by remember { mutableIntStateOf(0) }
    var inputYPositionPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor by animateColorAsState(
        targetValue = if (isFocused || isPressed || expanded) colors.ring else colors.border,
        animationSpec = tween(150), label = "selectBorderColor"
    )

    Column(modifier = modifier) {
        // 触发下拉菜单的输入框
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onGloballyPositioned { coordinates ->
                    // 获取输入框的大小和位置
                    inputWidthPx = coordinates.size.width
                    inputHeightPx = coordinates.size.height
                    val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                    inputXPositionPx = position?.x?.roundToInt() ?: 0
                    inputYPositionPx = position?.y?.roundToInt() ?: 0
                }
                .clip(RoundedCornerShape(radius.md))
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md))
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    expanded = !expanded
                }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 显示选中的选项或占位符
                Text(
                    text = selectedOption ?: placeholder,
                    color = if (selectedOption != null) colors.foreground else colors.mutedForeground,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "下拉箭头",
                    tint = colors.mutedForeground,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // 下拉弹出框
        if (expanded) {
            Popup(
                // 相对于输入框定位弹出框
                offset = IntOffset(inputXPositionPx, inputYPositionPx + inputHeightPx),
                properties = PopupProperties(focusable = true), // 使弹出框可获得焦点以处理外部点击
                onDismissRequest = { expanded = false }
            ) {
                // 下拉内容容器
                Box(
                    modifier = Modifier.shadow(1.dp, RoundedCornerShape(radius.lg))
                ) {
                    Column(
                        modifier = Modifier
                            .width(with(density) { inputWidthPx.toDp() }) // 匹配输入框的宽度
                            .clip(RoundedCornerShape(radius.lg))
                            .background(colors.popover)
                            .border(1.dp, colors.border, RoundedCornerShape(radius.lg))
                            .padding(8.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        ) {
                            items(options) { option ->
                                val isSelected = option == selectedOption
                                val optionBackgroundColor by animateColorAsState(
                                    targetValue = if (isSelected) colors.accent else colors.popover,
                                    animationSpec = tween(100), label = "optionBackgroundColor"
                                )
                                val optionTextColor by animateColorAsState(
                                    targetValue = if (isSelected) colors.accentForeground else colors.popoverForeground,
                                    animationSpec = tween(100), label = "optionTextColor"
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(radius.sm))
                                        .background(optionBackgroundColor)
                                        .clickable {
                                            onOptionSelected(option)
                                            expanded = false
                                        }
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = option,
                                        color = optionTextColor,
                                        fontSize = 14.sp,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "已选中",
                                            tint = colors.accentForeground,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
