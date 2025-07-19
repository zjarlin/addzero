package com.addzero.kmp.component.form.date

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.core.validation.RegexEnum

/**
 * 日期时间输入字段组件
 * 专门用于处理日期时间输入，支持同时选择日期和时间
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddDateTimeField(
    value: LocalDateTime?,
    onValueChange: (LocalDateTime?) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请选择日期时间",
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // 分离显示文本和实际值的状态管理
    var inputText by remember(value) {
        mutableStateOf(
            if (value == null) {
                ""
            } else {
                val date = value.date.toString()
                val time = "${value.hour.toString().padStart(2, '0')}:${value.minute.toString().padStart(2, '0')}"
                "$date $time"
            }
        )
    }

    // 当外部 value 变化时，同步更新 inputText
    LaunchedEffect(value) {
        inputText = if (value == null) {
            ""
        } else {
            val date = value.date.toString()
            val time = "${value.hour.toString().padStart(2, '0')}:${value.minute.toString().padStart(2, '0')}"
            "$date $time"
        }
    }

    Column(modifier = modifier) {
        // 标签
        if (label.isNotEmpty()) {
            Text(
                text = if (isRequired) "$label *" else label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // 日期时间输入框
        AddTextField(
            regexEnum = RegexEnum.DATETIME_MINUTE,
            value = inputText,
            onValueChange = { newText ->
                // 实时更新显示文本
                inputText = newText

                // 尝试解析为 LocalDateTime
                val parsedDateTime = try {
                    if (newText.isBlank()) {
                        null
                    } else {
                        // 支持 "yyyy-MM-dd HH:mm" 格式
                        if (newText.contains(' ')) {
                            val parts = newText.split(' ')
                            if (parts.size == 2) {
                                val date = LocalDate.parse(parts[0])
                                val timeParts = parts[1].split(':')
                                if (timeParts.size >= 2) {
                                    val hour = timeParts[0].toInt()
                                    val minute = timeParts[1].toInt()
                                    // 验证时间范围
                                    if (hour in 0..23 && minute in 0..59) {
                                        LocalDateTime(date, LocalTime(hour, minute))
                                    } else null
                                } else null
                            } else null
                        } else {
                            LocalDateTime.parse(newText)
                        }
                    }
                } catch (e: Exception) {
                    // 解析失败时不更新外部值，但保持输入文本
                    null
                }

                // 只有成功解析或为空时才更新外部值
                if (newText.isBlank() || parsedDateTime != null) {
                    onValueChange(parsedDateTime)
                }
            },
            disable = !enabled,
            placeholder = placeholder,
            leadingIcon = Icons.Default.DateRange,
            label = label,
            isRequired = isRequired,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                // 日历选择按钮
                IconButton(
                    onClick = {
                        if (enabled) {
                            showDatePicker = true
                        }
                    },
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "选择日期时间",
                        tint = if (enabled) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                        }
                    )
                }
            }
        )
    }

    // 日期时间选择器对话框
    if (showDatePicker) {
        DateTimePickerDialog(
            currentValue = value,
            onValueChange = { newValue ->
                // 从日期选择器选择日期时，同时更新显示文本和实际值
                inputText = if (newValue == null) {
                    ""
                } else {
                    val date = newValue.date.toString()
                    val time = "${newValue.hour.toString().padStart(2, '0')}:${newValue.minute.toString().padStart(2, '0')}"
                    "$date $time"
                }
                onValueChange(newValue)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

/**
 * 日期时间选择器对话框
 * 同时显示日期选择器和时间选择器
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun DateTimePickerDialog(
    currentValue: LocalDateTime?,
    onValueChange: (LocalDateTime?) -> Unit,
    onDismiss: () -> Unit
) {
    val currentDateTime = currentValue ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    // 日期选择器状态
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    )

    // 时间选择器状态
    val timePickerState = rememberTimePickerState(
        initialHour = currentDateTime.hour,
        initialMinute = currentDateTime.minute,
        is24Hour = true
    )

    // 记住选中的日期
    var selectedDate by remember {
        mutableStateOf(currentDateTime.date)
    }

    // 监听日期选择器的变化
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            val instant = Instant.fromEpochMilliseconds(millis)
            selectedDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            modifier = Modifier
                .widthIn(min = 560.dp, max = 680.dp)
                .heightIn(min = 800.dp, max = 1000.dp)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "选择日期和时间",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 日期选择器
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.6f), // 日期选择器占60%空间
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false,
                                modifier = Modifier.wrapContentSize()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 时间选择器
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.4f), // 时间选择器占40%空间
                        shape = MaterialTheme.shapes.medium,
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TimePicker(
                                state = timePickerState,
                                modifier = Modifier.wrapContentSize()
                            )
                        }
                    }
                }

                // 按钮区域 - 固定在底部
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val finalDateTime = LocalDateTime(
                                selectedDate,
                                LocalTime(timePickerState.hour, timePickerState.minute)
                            )
                            onValueChange(finalDateTime)
                        }
                    ) {
                        Text("确定")
                    }
                    }
                }
            }
        }
    }
}


