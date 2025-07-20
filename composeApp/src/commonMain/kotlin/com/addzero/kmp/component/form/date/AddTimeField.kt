package com.addzero.kmp.component.form.date

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.core.validation.RegexEnum
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

/**
 * 时间输入字段组件
 * 专门用于处理时间输入，支持选择小时和分钟
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddTimeField(
    value: LocalTime?,
    onValueChange: (LocalTime?) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请选择时间",
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    // 使用简单的字符串状态管理
    var inputText by remember(value) {
        mutableStateOf(
            if (value == null) {
                ""
            } else {
                "${value.hour.toString().padStart(2, '0')}:${value.minute.toString().padStart(2, '0')}"
            }
        )
    }

    // 标记是否是从时间选择器更新的，避免光标问题
    var isFromTimePicker by remember { mutableStateOf(false) }

    // 解析时间的函数
    fun parseTime(text: String): LocalTime? {
        return try {
            if (text.isBlank()) {
                null
            } else {
                // 支持 "HH:mm" 格式
                val timeParts = text.split(':')
                if (timeParts.size >= 2) {
                    val hour = timeParts[0].toInt()
                    val minute = timeParts[1].toInt()
                    // 验证时间范围
                    if (hour in 0..23 && minute in 0..59) {
                        LocalTime(hour, minute)
                    } else null
                } else null
            }
        } catch (e: Exception) {
            null
        }
    }

    // 当外部 value 变化时，同步更新 inputText
    LaunchedEffect(value) {
        val newText = if (value == null) {
            ""
        } else {
            "${value.hour.toString().padStart(2, '0')}:${value.minute.toString().padStart(2, '0')}"
        }

        // 只有当文本真的不同且不是从时间选择器更新时才更新
        if (inputText != newText && !isFromTimePicker) {
            inputText = newText
        }
        isFromTimePicker = false
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

        // 时间输入框
        AddTextField(
            regexEnum = RegexEnum.TIME_MINUTE,
            value = inputText,
            onValueChange = { newText ->
                // 只更新输入文本，不进行解析和校验
                inputText = newText
            },
            onValidate = { isValid ->
                // 失去焦点时，如果格式验证通过，则进行解析并通知外部
                if (isValid && !isFromTimePicker) {
                    val parsed = parseTime(inputText)
                    onValueChange(parsed)
                }
            },
            disable = !enabled,
            placeholder = placeholder,
            leadingIcon = Icons.Default.Schedule,
            label = label,
            isRequired = isRequired,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                // 时间选择按钮
                IconButton(
                    onClick = {
                        if (enabled) {
                            showTimePicker = true
                        }
                    },
                    enabled = enabled
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "选择时间",
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

    // 时间选择器对话框
    if (showTimePicker) {
        TimePickerDialog(
            currentValue = value,
            onValueChange = { newValue ->
                // 从时间选择器选择时间时，直接更新外部值和显示文本
                isFromTimePicker = true
                inputText = if (newValue == null) {
                    ""
                } else {
                    "${newValue.hour.toString().padStart(2, '0')}:${newValue.minute.toString().padStart(2, '0')}"
                }
                // 直接调用外部回调，不需要解析
                onValueChange(newValue)
                onValueChange(newValue)
                showTimePicker = false
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

/**
 * 时间选择器对话框
 * 只显示时间选择器
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun TimePickerDialog(
    currentValue: LocalTime?,
    onValueChange: (LocalTime?) -> Unit,
    onDismiss: () -> Unit
) {
    val currentTime = currentValue ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

    // 时间选择器状态
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            modifier = Modifier
                .widthIn(min = 480.dp, max = 600.dp)
                .heightIn(min = 500.dp, max = 700.dp)
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "选择时间",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))

                // 时间选择器
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
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
                                val finalTime = LocalTime(timePickerState.hour, timePickerState.minute)
                                onValueChange(finalTime)
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
