package com.addzero.kmp.component.form.date

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

/**
 * 日期选择字段组件
 * 基于 kotlinx.datetime 实现，支持日期和日期时间选择
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
    dateType: DateType = DateType.DATETIME,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // 格式化显示文本
    val displayText = remember(value, dateType) {
        when {
            value == null -> ""
            dateType == DateType.DATE -> value.date.toString()
            dateType == DateType.DATETIME -> value.toString().replace('T', ' ')
            dateType == DateType.TIME -> value.time.toString()
            else -> value.toString()
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

        // 日期输入框
        OutlinedTextField(
            value = displayText,
            onValueChange = { },
            readOnly = true,
            enabled = enabled,
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "选择日期"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { showDatePicker = true }
        )
    }

    // 日期选择器对话框
    if (showDatePicker) {
        DatePickerDialog(
            currentValue = value,
            dateType = dateType,
            onValueChange = { newValue ->
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
    currentValue: LocalDateTime?,
    dateType: DateType,
    onValueChange: (LocalDateTime?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = currentValue?.toInstant(TimeZone.currentSystemDefault())?.toEpochMilliseconds()
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
                        val instant = Instant.fromEpochMilliseconds(selectedMillis)
                        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

                        // 根据日期类型调整时间部分
                        val finalDateTime = when (dateType) {
                            DateType.DATE -> LocalDateTime(localDateTime.date, LocalTime(0, 0))
                            DateType.DATETIME -> currentValue?.let {
                                LocalDateTime(localDateTime.date, it.time)
                            } ?: LocalDateTime(localDateTime.date, LocalTime(0, 0))
                            DateType.TIME -> currentValue ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                        }

                        onValueChange(finalDateTime)
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
