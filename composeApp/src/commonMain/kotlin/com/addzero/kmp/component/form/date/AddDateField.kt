package com.addzero.kmp.component.form.date

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.core.validation.RegexEnum
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

/**
 * 日期选择字段组件
 * 基于 kotlinx.datetime 实现，支持日期和日期时间选择
 *
 * 特性：
 * - 支持手动输入日期（实时显示输入内容）
 * - 支持点击日历图标选择日期
 * - 输入格式校验（yyyy-MM-dd）
 * - 最终输出 LocalDate 类型
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun AddDateField(
    value: LocalDate?,
    onValueChange: (LocalDate?) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请选择日期",
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // 分离显示文本和实际值的状态管理
    var inputText by remember(value) {
        mutableStateOf(value?.toString() ?: "")
    }

    // 当外部 value 变化时，同步更新 inputText
    LaunchedEffect(value) {
        inputText = value?.toString() ?: ""
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

        // 日期输入框
        AddTextField(
            regexEnum = RegexEnum.DATE,
            value = inputText,
            onValueChange = { newText ->
                // 实时更新显示文本
                inputText = newText

                // 尝试解析为 LocalDate
                val parsedDate = try {
                    if (newText.isBlank()) {
                        null
                    } else {
                        LocalDate.parse(newText)
                    }
                } catch (e: Exception) {
                    // 解析失败时不更新外部值，但保持输入文本
                    null
                }

                // 只有成功解析或为空时才更新外部值
                if (newText.isBlank() || parsedDate != null) {
                    onValueChange(parsedDate)
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
                        contentDescription = "选择日期",
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

    // 日期选择器对话框
    if (showDatePicker) {
        DatePickerDialog(
            currentValue = value,
            onValueChange = { newValue ->
                // 从日期选择器选择日期时，同时更新显示文本和实际值
                inputText = newValue?.toString() ?: ""
                onValueChange(newValue)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }
}

/**
 * 日期选择器对话框
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun DatePickerDialog(
    currentValue: LocalDate?,
    onValueChange: (LocalDate?) -> Unit,
    onDismiss: () -> Unit
) {
    // 将 LocalDate 转换为毫秒时间戳
    val initialMillis = currentValue?.let { date ->
        // 使用 LocalDateTime 和系统时区转换
        val dateTime = LocalDateTime(date, LocalTime(0, 0))
        dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("选择日期") },
        text = {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val selectedMillis = datePickerState.selectedDateMillis
                    if (selectedMillis != null) {
                        // 将毫秒时间戳转换回 LocalDate
                        val instant = Instant.fromEpochMilliseconds(selectedMillis)
                        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                        val selectedDate = localDateTime.date

                        onValueChange(selectedDate)
                    } else {
                        onValueChange(null)
                    }
                }
            ) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
