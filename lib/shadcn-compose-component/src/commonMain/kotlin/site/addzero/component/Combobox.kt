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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import site.addzero.themes.radius
import site.addzero.themes.colors
import kotlin.math.roundToInt

/**
 * 一个受Shadcn UI下拉菜单启发的Jetpack Compose组合框组件。
 * 提供可搜索的下拉列表用于选择选项，以弹出窗口形式显示。
 *
 * @param options 组合框中显示的字符串选项列表。
 * @param selectedOption 当前选中的选项。如果没有选中选项则为null。
 * @param onOptionSelected 选择选项时调用的回调。提供选中的字符串。
 * @param modifier 应用于组合框容器的修饰符。
 * @param placeholder 当没有选中选项时显示的占位符文本。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBox(
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "选择选项..."
) {
    val colors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    var expanded by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf(selectedOption ?: "") } // Initialize with selected option

    // 用于保存输入字段位置和宽度的状态
    var inputWidthPx by remember { mutableIntStateOf(0) }
    var inputHeightPx by remember { mutableIntStateOf(0) }
    var inputXPositionPx by remember { mutableIntStateOf(0) }
    var inputYPositionPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current

    // 当外部selectedOption更改时更新searchText，但仅在下拉菜单未展开时
    // 这可以防止在用户正在输入时覆盖其搜索内容
    if (selectedOption != searchText && !expanded) {
        searchText = selectedOption ?: ""
    }

    val filteredOptions = remember(options, searchText) {
        if (searchText.isBlank()) {
            options
        } else {
            options.filter { it.contains(searchText, ignoreCase = true) }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentBorderColor by animateColorAsState(
        targetValue = if (isFocused || isPressed || expanded) colors.ring else colors.border,
        animationSpec = tween(150), label = "comboboxBorderColor"
    )

    Column(modifier = modifier) {
        // 触发下拉菜单的输入字段
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onGloballyPositioned { coordinates ->
                    // 获取输入字段的大小和位置
                    inputWidthPx = coordinates.size.width
                    inputHeightPx = coordinates.size.height
                    val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                    inputXPositionPx = position?.x?.roundToInt() ?: 0
                    inputYPositionPx = position?.y?.roundToInt() ?: 0
                }
                .clip(RoundedCornerShape(radius.md))
                .border(1.dp, currentBorderColor, RoundedCornerShape(radius.md)) // 边框：border-input 或 border-ring
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    expanded = !expanded
                    if (expanded) { // 如果打开组合框
                        searchText = "" // 打开时始终清除搜索文本
                    } else { // 如果关闭组合框
                        // 关闭时，如果没有选中项且有输入文本，则清除文本
                        if (selectedOption == null && searchText.isNotBlank()) {
                            searchText = ""
                        }
                    }
                }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 显示选中选项或占位符
                Text(
                    text = selectedOption ?: placeholder,
                    color = if (selectedOption != null) colors.foreground else colors.mutedForeground,
                    fontSize = 14.sp, // text-sm
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

        // 下拉弹出窗口
        if (expanded) {
            Popup(
                // 相对于输入字段定位弹出窗口
                offset = IntOffset(inputXPositionPx, inputYPositionPx + inputHeightPx),
                properties = PopupProperties(focusable = true), // 使弹出窗口可获得焦点以处理外部点击
                onDismissRequest = {
                    expanded = false
                    // 如果关闭时没有选中选项，则重置搜索文本
                    if (selectedOption == null) {
                        searchText = ""
                    }
                }
            ) {
                // 下拉内容容器
                Box(
                    modifier = Modifier
                        .shadow(1.dp, RoundedCornerShape(radius.lg))
                ) {
                    Column(
                        modifier = Modifier
                            .width(with(density) { inputWidthPx.toDp() })
                            .clip(RoundedCornerShape(radius.lg))
                            .background(colors.popover)
                            .border(1.dp, colors.border, RoundedCornerShape(radius.lg))
                            .padding(8.dp)
                    ) {
                        // 下拉菜单内的搜索文本框
                        Input(
                            value = searchText,
                            onValueChange = { searchText = it },
                            variant = InputVariant.Underlined,
                            placeholder = "搜索选项...",
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
                            singleLine = true,
                        )

                        // 显示过滤后的选项或"无结果"消息
                        if (filteredOptions.isEmpty()) {
                            Text(
                                text = "未找到结果。",
                                color = colors.mutedForeground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                textAlign = TextAlign.Center
                            )
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp) // 可滚动选项的固定高度
                            ) {
                                items(filteredOptions) { option ->
                                    val isSelected = option == selectedOption
                                    val optionBackgroundColor by animateColorAsState(
                                        targetValue = if (isSelected) colors.accent else Color.Transparent,
                                        animationSpec = tween(100), label = "optionBackgroundColor"
                                    )

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(radius.sm))
                                            .background(optionBackgroundColor)
                                            .clickable {
                                                onOptionSelected(option)
                                                searchText = option // 将搜索文本更新为选中的选项
                                                expanded = false // 选择时关闭弹出窗口
                                            }
                                            .padding(horizontal = 8.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = option,
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
}
