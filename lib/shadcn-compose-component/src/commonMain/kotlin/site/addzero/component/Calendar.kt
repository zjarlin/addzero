package site.addzero.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import site.addzero.themes.ShadcnColors
import site.addzero.themes.radius
import site.addzero.themes.colors
import kotlinx.datetime.Clock
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.todayIn
import kotlinx.datetime.DateTimeUnit
import kotlin.math.roundToInt

// 日期格式化的辅助函数（简化方法）
private fun DayOfWeek.getShortName(): String {
    return when (this) {
        DayOfWeek.MONDAY -> "周一"
        DayOfWeek.TUESDAY -> "周二"
        DayOfWeek.WEDNESDAY -> "周三"
        DayOfWeek.THURSDAY -> "周四"
        DayOfWeek.FRIDAY -> "周五"
        DayOfWeek.SATURDAY -> "周六"
        DayOfWeek.SUNDAY -> "周日"
    }
}

private fun Month.getShortName(): String {
    return when (this) {
        Month.JANUARY -> "1月"
        Month.FEBRUARY -> "2月"
        Month.MARCH -> "3月"
        Month.APRIL -> "4月"
        Month.MAY -> "5月"
        Month.JUNE -> "6月"
        Month.JULY -> "7月"
        Month.AUGUST -> "8月"
        Month.SEPTEMBER -> "9月"
        Month.OCTOBER -> "10月"
        Month.NOVEMBER -> "11月"
        Month.DECEMBER -> "12月"
    }
}

private fun Month.getFullName(): String {
    return when (this) {
        Month.JANUARY -> "一月"
        Month.FEBRUARY -> "二月"
        Month.MARCH -> "三月"
        Month.APRIL -> "四月"
        Month.MAY -> "五月"
        Month.JUNE -> "六月"
        Month.JULY -> "七月"
        Month.AUGUST -> "八月"
        Month.SEPTEMBER -> "九月"
        Month.OCTOBER -> "十月"
        Month.NOVEMBER -> "十一月"
        Month.DECEMBER -> "十二月"
    }
}

// 获取月份天数的辅助函数（kotlinx.datetime 没有提供 month.length 属性）
private fun getDaysInMonth(month: Month, year: Int): Int {
    return when (month) {
        Month.JANUARY -> 31
        Month.FEBRUARY -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
        Month.MARCH -> 31
        Month.APRIL -> 30
        Month.MAY -> 31
        Month.JUNE -> 30
        Month.JULY -> 31
        Month.AUGUST -> 31
        Month.SEPTEMBER -> 30
        Month.OCTOBER -> 31
        Month.NOVEMBER -> 30
        Month.DECEMBER -> 31
    }
}

enum class DateSelectionMode {
    /** 允许选择所有日期。 */
    All,
    /** 允许选择今天和过去的日期。 */
    PastOrToday,
    /** 允许选择今天和未来的日期。 */
    FutureOrToday
}

/**
 * 一个受 Shadcn UI 启发的 Jetpack Compose 日历组件。
 * 允许单日期选择和通过下拉菜单进行月份/年份导航。
 *
 * @param modifier 应用于日历容器的修饰符。
 * @param selectedDate 当前选中的日期。如果没有选中日期则为 null。
 * @param onDateSelected 当选择日期时调用的回调函数。
 * @param initialMonth 初始显示的月份。默认为当前月份。
 * @param dateSelectionMode 定义哪些日期可点击（All、PastOrToday、FutureOrToday）。
 * @param colors [CalendarStyle] 将用于解析此日历使用的颜色样式
 */
@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate? = null,
    onDateSelected: (LocalDate) -> Unit,
    initialMonth: YearMonth = run {
      val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
      YearMonth(today.year, today.month)
  },
    dateSelectionMode: DateSelectionMode = DateSelectionMode.All,
    colors: CalendarStyle = CalendarDefaults.colors()
) {
    val themeColors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    var currentMonth by remember { mutableStateOf(initialMonth) }
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    var showMonthPicker by remember { mutableStateOf(false) }
    var showYearPicker by remember { mutableStateOf(false) }

    // 星期名称（例如："周日"、"周一"）
    val weekdays = remember {
        DayOfWeek.values().map { it.getShortName() }
    }

    Box(
        modifier = modifier
            .width(300.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .border(1.dp, colors.border, RoundedCornerShape(radius.lg))
                .padding(8.dp)
        ) {
            // --- 头部：月份、年份和导航箭头 ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    val newMonth = if (currentMonth.month == Month.JANUARY) {
                        YearMonth(currentMonth.year - 1, Month.DECEMBER)
                    } else {
                        YearMonth(currentMonth.year, Month(currentMonth.month.ordinal))
                    }
                    currentMonth = newMonth
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = "上一月",
                        tint = colors.leftIconTint
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 月份选择器
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(radius.md))
                            .border(1.dp, colors.monthSelectorBorder, RoundedCornerShape(radius.md))
                            .clickable { showMonthPicker = true }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentMonth.month.getShortName(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = colors.monthText
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "选择月份",
                                tint = themeColors.foreground,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // 年份选择器
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(radius.md))
                            .border(1.dp, colors.yearSelectorBorder, RoundedCornerShape(radius.md))
                            .clickable { showYearPicker = true }
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentMonth.year.toString(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = colors.yearText
                            )
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = "选择年份",
                                tint = themeColors.foreground,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                IconButton(onClick = {
                    val newMonth = if (currentMonth.month == Month.DECEMBER) {
                        YearMonth(currentMonth.year + 1, Month.JANUARY)
                    } else {
                        YearMonth(currentMonth.year, Month(currentMonth.month.ordinal + 1))
                    }
                    currentMonth = newMonth
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = "下一月",
                        tint = colors.rightIconTint
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- 星期标题 ---
            Row(modifier = Modifier.fillMaxWidth()) {
                weekdays.forEach { weekday ->
                    Text(
                        text = weekday,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            color = colors.weekDaysText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- 日期网格 ---
            val firstDayOfMonth = LocalDate(currentMonth.year, currentMonth.month, 1)
            val firstDayOfWeekValue = firstDayOfMonth.dayOfWeek.ordinal % 7
            val daysInMonth = getDaysInMonth(currentMonth.month, currentMonth.year)

            // 计算前置空白天数
            val leadingEmptyDays = (firstDayOfWeekValue + 7 - DayOfWeek.SUNDAY.ordinal) % 7

            // 获取前一月信息用于显示前置日期
            val previousMonth = if (currentMonth.month == Month.JANUARY) {
                YearMonth(currentMonth.year - 1, Month.DECEMBER)
            } else {
                YearMonth(currentMonth.year, Month(currentMonth.month.ordinal))
            }
            val daysInPreviousMonth = getDaysInMonth(previousMonth.month, previousMonth.year)

            // 计算要显示的总单元格数（总是完整的周）
            val totalActiveDays = leadingEmptyDays + daysInMonth
            val totalCells = ((totalActiveDays + 6) / 7) * 7
            val numRows = totalCells / 7

            Column {
                for (row in 0 until numRows) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        for (col in 0 until 7) {
                            val cellIndex = row * 7 + col

                            val (date, isCurrentMonth) = when {
                                // 来自前一月的前置日期
                                cellIndex < leadingEmptyDays -> {
                                    val dayOfMonth = daysInPreviousMonth - (leadingEmptyDays - cellIndex - 1)
                                    Pair(LocalDate(previousMonth.year, previousMonth.month, dayOfMonth), false)
                                }
                                // 当前月的日期
                                cellIndex < leadingEmptyDays + daysInMonth -> {
                                    val dayOfMonth = cellIndex - leadingEmptyDays + 1
                                    Pair(LocalDate(currentMonth.year, currentMonth.month, dayOfMonth), true)
                                }
                                // 来自下一月的后置日期
                                else -> {
                                    val dayOfMonth = cellIndex - leadingEmptyDays - daysInMonth + 1
                                    val nextMonth = if (currentMonth.month == Month.DECEMBER) {
                        YearMonth(currentMonth.year + 1, Month.JANUARY)
                    } else {
                        YearMonth(currentMonth.year, Month(currentMonth.month.ordinal + 1))
                    }
                                    Pair(LocalDate(nextMonth.year, nextMonth.month, dayOfMonth), false)
                                }
                            }

                            val isSelected = date == selectedDate
                            val isToday = date == today

                            // 根据 dateSelectionMode 判断日期可点击性的逻辑
                            val isClickable = when (dateSelectionMode) {
                                DateSelectionMode.All -> true
                                DateSelectionMode.PastOrToday -> date <= today
                                DateSelectionMode.FutureOrToday -> date >= today
                            }

                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed = interactionSource.collectIsPressedAsState().value
                            val cellBgStyle = colors.dateCellBgStyle
                            val cellTextStyle = colors.dateCellTextStyle
                            val backgroundColor: Color = animateColorAsState(
                                targetValue = when {
                                    isSelected -> cellBgStyle.selectedDate
                                    isClickable && isPressed -> cellBgStyle.onPressed
                                    isToday && isCurrentMonth -> cellBgStyle.todayUnselectedBg
                                    else -> cellBgStyle.defaultDateCell
                                },
                                animationSpec = tween(durationMillis = 100), label = "dayBackground"
                            ).value

                            val textColor = animateColorAsState(
                                targetValue = when {
                                    isSelected -> cellTextStyle.selectedDate
                                    isToday && isCurrentMonth -> cellTextStyle.todayUnselected
                                    isCurrentMonth && isClickable -> cellTextStyle.currentMonthUnselected
                                    isCurrentMonth -> {
                                        when (dateSelectionMode) {
                                            DateSelectionMode.All -> cellTextStyle.currentMonthUnselected
                                            DateSelectionMode.PastOrToday -> {
                                                if (date <= today) {
                                                    cellTextStyle.currentMonthUnselected
                                                } else {
                                                    cellTextStyle.currentMonthDisabled
                                                }
                                            }
                                            DateSelectionMode.FutureOrToday -> {
                                                if (date >= today) {
                                                    cellTextStyle.currentMonthUnselected
                                                } else {
                                                    cellTextStyle.currentMonthDisabled
                                                }
                                            }
                                        }
                                    }
                                    // Previous/next month dates - more muted
                                    isClickable -> cellTextStyle.previousAndNextDateMonth
                                    else -> cellTextStyle.previousAndNextDateMonthDisabled
                                },
                                animationSpec = tween(durationMillis = 100), label = "dayText"
                            ).value

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(radius.sm))
                                    .background(backgroundColor)
                                    .clickable(
                                        enabled = isClickable,
                                        interactionSource = interactionSource,
                                        indication = null
                                    ) {
                                        onDateSelected(date)
                                        // 可选：如果选中的日期不是当前月，则导航到该日期所在的月份
                                        if (!isCurrentMonth) {
                                            currentMonth = YearMonth(date.year, date.month)
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString(),
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 14.sp,
                                        fontWeight = when {
                                            isSelected -> FontWeight.SemiBold
                                            isToday && isCurrentMonth -> FontWeight.SemiBold
                                            isCurrentMonth -> FontWeight.Normal
                                            else -> FontWeight.Normal
                                        }
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // 月份选择对话框
    if (showMonthPicker) {
        MonthPickerDialog(
            currentMonth = currentMonth.month,
            onMonthSelected = { month ->
                currentMonth = YearMonth(currentMonth.year, month)
                showMonthPicker = false
            },
            onDismissRequest = { showMonthPicker = false },
            colors = colors.dialogStyle
        )
    }

    // 年份选择对话框
    if (showYearPicker) {
        YearPickerDialog(
            currentYear = currentMonth.year,
            onYearSelected = { year ->
                currentMonth = YearMonth(year, currentMonth.month)
                showYearPicker = false
            },
            onDismissRequest = { showYearPicker = false },
            colors = colors.dialogStyle
        )
    }
}

/**
 * 用于选择月份的对话框。
 */
@Composable
private fun MonthPickerDialog(
    currentMonth: Month,
    onMonthSelected: (Month) -> Unit,
    onDismissRequest: () -> Unit,
    colors: SelectorDialogStyle
) {
    val themeColors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    val density = LocalDensity.current

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(themeColors.popover, RoundedCornerShape(radius.lg))
                .border(1.dp, themeColors.border, RoundedCornerShape(radius.lg))
                .padding(8.dp)
                .height(300.dp)
        ) {
            val months = remember { Month.values().toList() }
            val listState = rememberLazyListState()

            // 初始组合时滚动到当前月份
            LaunchedEffect(Unit) {
                val initialIndex = months.indexOf(currentMonth)
                if (initialIndex != -1) {
                    // 计算偏移量以在视口中居中显示项目
                    // 假设每个项目大约 44.dp 高（py-2 + 文本高度）
                    val itemHeightPx = with(density) { 44.dp.toPx() }
                    val containerHeightPx = with(density) { 300.dp.toPx() }
                    val offsetToCenter = (itemHeightPx / 2f) - (containerHeightPx / 2f)

                    listState.scrollToItem(initialIndex, offsetToCenter.roundToInt())
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(months) { month ->
                    val isSelected = month == currentMonth
                    val backgroundColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedBg else colors.unselectedBg,
                        animationSpec = tween(durationMillis = 100), label = "monthBackground"
                    ).value
                    val textColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedText else colors.unselectedText,
                        animationSpec = tween(durationMillis = 100), label = "monthText"
                    ).value

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(radius.sm))
                            .background(backgroundColor)
                            .clickable { onMonthSelected(month) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = month.getFullName(),
                            style = TextStyle(
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * 用于选择年份的对话框。
 */
@Composable
private fun YearPickerDialog(
    currentYear: Int,
    onYearSelected: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    colors: SelectorDialogStyle
) {
    val themeColors = MaterialTheme.colors
    val radius = MaterialTheme.radius
    val density = LocalDensity.current

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .background(themeColors.popover, RoundedCornerShape(radius.lg))
                .border(1.dp, themeColors.border, RoundedCornerShape(radius.lg))
                .padding(8.dp)
                .height(300.dp)
        ) {
            val currentYear = Clock.System.todayIn(TimeZone.currentSystemDefault()).year
            val years = remember { (1970..currentYear + 5).toList() }
            val listState = rememberLazyListState()

            // 初始组合时滚动到当前年份
            LaunchedEffect(Unit) {
                val initialIndex = years.indexOf(currentYear)
                if (initialIndex != -1) {
                    // 计算偏移量以在视口中居中显示项目
                    // 假设每个项目大约 44.dp 高（py-2 + 文本高度）
                    val itemHeightPx = with(density) { 44.dp.toPx() }
                    val containerHeightPx = with(density) { 300.dp.toPx() }
                    val offsetToCenter = (itemHeightPx / 2f) - (containerHeightPx / 2f)

                    listState.scrollToItem(initialIndex, offsetToCenter.roundToInt())
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(years) { year ->
                    val isSelected = year == currentYear
                    val backgroundColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedBg else colors.unselectedBg,
                        animationSpec = tween(durationMillis = 100), label = "yearBackground"
                    ).value
                    val textColor = animateColorAsState(
                        targetValue = if (isSelected) colors.selectedText else colors.unselectedText,
                        animationSpec = tween(durationMillis = 100), label = "yearText"
                    ).value

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(radius.sm))
                            .background(backgroundColor)
                            .clickable { onYearSelected(year) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = year.toString(),
                            style = TextStyle(
                                color = textColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

data class CalendarStyle(
    val background: Color,
    val border: Color,
    val leftIconTint: Color,
    val rightIconTint: Color,
    val monthText: Color,
    val yearText: Color,
    val monthSelectorBorder: Color,
    val yearSelectorBorder: Color,
    val weekDaysText: Color,
    val dateCellBgStyle: DateCellBackgroundStyle,
    val dateCellTextStyle: DateCellTextStyle,
    val dialogStyle: SelectorDialogStyle
)

data class DateCellBackgroundStyle(
    val selectedDate: Color,
    val todayUnselectedBg: Color,
    val onPressed: Color,
    val defaultDateCell: Color
)

data class DateCellTextStyle(
    val selectedDate: Color,
    val todayUnselected: Color,
    val currentMonthUnselected: Color,
    val currentMonthDisabled: Color,
    val previousAndNextDateMonth: Color,
    val previousAndNextDateMonthDisabled: Color,
)

data class SelectorDialogStyle(
    val selectedBg: Color,
    val selectedText: Color,
    val unselectedBg: Color,
    val unselectedText: Color,
)

object CalendarDefaults {
    @Composable
    private fun colorsFrom(colors: ShadcnColors): CalendarStyle {
        return CalendarStyle(
            background = colors.background,
            border = colors.border,
            leftIconTint = colors.foreground,
            rightIconTint = colors.foreground,
            monthText = Color.Unspecified,
            yearText = Color.Unspecified,
            monthSelectorBorder = colors.border,
            yearSelectorBorder = colors.border,
            weekDaysText = colors.mutedForeground,
            dateCellBgStyle = DateCellBackgroundStyle(
                selectedDate = colors.primary,
                todayUnselectedBg = colors.muted,
                onPressed = colors.accent,
                defaultDateCell = Color.Transparent
            ),
            dateCellTextStyle = DateCellTextStyle(
                selectedDate = colors.primaryForeground,
                todayUnselected = colors.accentForeground,
                currentMonthUnselected = colors.foreground,
                currentMonthDisabled = colors.mutedForeground.copy(alpha = 0.4f),
                previousAndNextDateMonth = colors.mutedForeground,
                previousAndNextDateMonthDisabled = colors.mutedForeground.copy(alpha = 0.3f)
            ),
            dialogStyle = SelectorDialogStyle(
                selectedBg = colors.primary,
                selectedText = colors.primaryForeground,
                unselectedBg = Color.Transparent,
                unselectedText = colors.popoverForeground,
            )
        )
    }

    @Composable
    fun colors(): CalendarStyle {
        val colors = MaterialTheme.colors
        return CalendarStyle(
            background = colors.background,
            border = colors.border,
            leftIconTint = colors.foreground,
            rightIconTint = colors.foreground,
            monthText = Color.Unspecified,
            yearText = Color.Unspecified,
            monthSelectorBorder = colors.border,
            yearSelectorBorder = colors.border,
            weekDaysText = colors.mutedForeground,
            dateCellBgStyle = DateCellBackgroundStyle(
                selectedDate = colors.primary,
                todayUnselectedBg = colors.muted,
                onPressed = colors.accent,
                defaultDateCell = Color.Transparent
            ),
            dateCellTextStyle = DateCellTextStyle(
                selectedDate = colors.primaryForeground,
                todayUnselected = colors.accentForeground,
                currentMonthUnselected = colors.foreground,
                currentMonthDisabled = colors.mutedForeground.copy(alpha = 0.4f),
                previousAndNextDateMonth = colors.mutedForeground,
                previousAndNextDateMonthDisabled = colors.mutedForeground.copy(alpha = 0.3f)
            ),
            dialogStyle = SelectorDialogStyle(
                selectedBg = colors.primary,
                selectedText = colors.primaryForeground,
                unselectedBg = Color.Transparent,
                unselectedText = colors.popoverForeground,
            )
        )
    }

    @Composable
    fun colors(overrides: CalendarStyle.() -> CalendarStyle): CalendarStyle {
        val colors = MaterialTheme.colors
        return colorsFrom(colors).overrides()
    }
}
