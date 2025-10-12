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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import site.addzero.themes.radius
import site.addzero.themes.colors
import kotlin.math.roundToInt
import kotlinx.datetime.Clock
import kotlinx.datetime.todayIn

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 日期选择器组件。
 * 它将一个可点击的输入框与包含日历的弹出框结合在一起。
 *
 * @param modifier 应用于日期选择器容器的修饰符。
 * @param selectedDate 当前选中的日期。如果没有选中日期则为 null。
 * @param onDateSelected 选择日期时调用的回调。
 * @param placeholder 当没有选择日期时显示的占位符文本。
 * @param dateSelectionMode 定义日历中哪些日期可以点击（All, PastOrToday, FutureOrToday）。
 * @param colors [CalendarStyle] 将用于解析此输入框使用的颜色
 * @param leadingIcon 在输入框开始处显示的可选可组合项。
 * @param trailingIcon 在输入框末尾显示的可选可组合项。
 */
@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    placeholder: String = "选择日期",
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors(),
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val themeColors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    var showCalendarPopup by remember { mutableStateOf(false) }

    var inputWidthPx by remember { mutableIntStateOf(0) }
    var inputHeightPx by remember { mutableIntStateOf(0) }
    var inputXPositionPx by remember { mutableIntStateOf(0) }
    var inputYPositionPx by remember { mutableIntStateOf(0) }

    val density = LocalDensity.current
    val formatter = LocalDate.Format {
        monthName(MonthNames.ENGLISH_FULL)
        char(' ')
        dayOfMonth()
        chars(", ")
        year()
    }
    val formattedDate = selectedDate?.format(formatter)

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentBorderColor by animateColorAsState(
        targetValue = if (isFocused || isPressed || showCalendarPopup) themeColors.ring else themeColors.border,
        animationSpec = tween(150), label = "datePickerBorderColor"
    )

    Column(modifier = modifier) {
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
                    showCalendarPopup = !showCalendarPopup
                }
                .padding(horizontal = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = formattedDate ?: placeholder,
                    color = if (selectedDate != null) themeColors.foreground else themeColors.mutedForeground,
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f)
                )
                if (trailingIcon != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    trailingIcon()
                }

            }
        }

        // 日历弹出框
        if (showCalendarPopup) {
            Popup(
                offset = IntOffset(inputXPositionPx, inputYPositionPx + inputHeightPx),
                properties = PopupProperties(focusable = true),
                onDismissRequest = { showCalendarPopup = false }
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(radius.md))
                        .background(themeColors.popover)
                ) {
                    Calendar(
                        modifier = Modifier.width(with(density) { inputWidthPx.toDp() }), // 匹配输入框宽度
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            onDateSelected(date)
                            showCalendarPopup = false // 选择日期后关闭弹出框
                        },
                        initialMonth = selectedDate?.let { YearMonth(it.year, it.month) } ?: run {
                        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                        YearMonth(today.year, today.month)
                    },
                        dateSelectionMode = dateSelectionMode,
                        colors = colors
                    )
                }
            }
        }
    }
}
