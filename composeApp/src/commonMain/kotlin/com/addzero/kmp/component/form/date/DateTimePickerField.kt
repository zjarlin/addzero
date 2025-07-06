@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.addzero.kmp.component.form.date.formatDateTime
import com.addzero.kmp.core.ext.DateFormatPattern
import kotlinx.datetime.*
import kotlinx.datetime.format.byUnicodePattern
import kotlin.time.ExperimentalTime

@Composable
@Deprecated("todo")
fun DateTimePickerField(
    value: String?,
    onValueChange: (String?) -> Unit,
    label: String,
    dateTimeFormat: DateFormatPattern = DateFormatPattern.DATETIME_MEDIUM,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) {
    var showDialog by remember { mutableStateOf(false) }

    val initialDateTime = remember(value) {
        try {
            value?.let {
                LocalDateTime.parse(it, LocalDateTime.Format { byUnicodePattern(dateTimeFormat.pattern) })
            }
        } catch (e: Exception) {
            null
        }
    }

    var selectedDate by remember(showDialog) {
        mutableStateOf(
            initialDateTime?.date ?: Clock.System.now().toLocalDateTime(timeZone).date
        )
    }
    val timePickerState = remember {
        val t = initialDateTime?.time ?: LocalTime(0, 0)
        TimePickerState(t.hour, t.minute, true)
    }

    OutlinedTextField(
        value = value ?: "",
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        enabled = enabled,
        trailingIcon = {
            IconButton(onClick = { showDialog = true }, enabled = enabled) {
                Icon(Icons.Default.DateRange, contentDescription = "选择日期时间")
            }
        },
        modifier = modifier.fillMaxWidth()
    )

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 8.dp,
                shadowElevation = 16.dp,
                modifier = Modifier
                    .widthIn(min = 320.dp, max = 480.dp)
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("选择日期和时间", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 日期选择器
                        val datePickerState = rememberDatePickerState(
                            initialSelectedDateMillis = selectedDate
                                .atTime(LocalTime(timePickerState.hour, timePickerState.minute))
                                .toInstant(timeZone)
                                .toEpochMilliseconds()
                        )
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            DatePicker(
                                state = datePickerState,
                                showModeToggle = false,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        LaunchedEffect(datePickerState.selectedDateMillis) {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = Instant.fromEpochMilliseconds(millis)
                                selectedDate = instant.toLocalDateTime(timeZone).date
                            }
                        }

                        Spacer(modifier = Modifier.width(32.dp))

                        // 时间选择器
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = MaterialTheme.shapes.large,
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            TimePicker(
                                state = timePickerState,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showDialog = false }) { Text("取消") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            val ldt = LocalDateTime(
                                selectedDate,
                                LocalTime(timePickerState.hour, timePickerState.minute)
                            ).toInstant(timeZone).toEpochMilliseconds()
                            val formatted = formatDateTime(ldt, dateTimeFormat, timeZone)
                            onValueChange(formatted)
                            showDialog = false
                        }) { Text("确认") }
                    }
                }
            }
        }
    }
}
