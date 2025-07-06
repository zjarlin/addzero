@file:OptIn(ExperimentalMaterial3Api::class)

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.addzero.kmp.component.form.date.formatDateTime
import com.addzero.kmp.component.form.date.parseDateTimeStringToMillis
import com.addzero.kmp.core.ext.DateFormatPattern
import kotlinx.datetime.TimeZone
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun DatePickerField(
    value: String?,
    onValueChange: (String?) -> Unit,
    label: String,
    placeholder: String = "请选择日期",
    dateFormat: DateFormatPattern = DateFormatPattern.DATE_SHORT,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    timeZone: TimeZone = TimeZone.currentSystemDefault()
) {
    var showModal by remember { mutableStateOf(false) }
    // 解析字符串为 millis
    val millis = remember(value) { parseDateTimeStringToMillis(value, dateFormat, timeZone) }

    OutlinedTextField(
        value = value ?: "",
        onValueChange = onValueChange, // 禁止手动输入
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        readOnly = true,
        enabled = enabled,
        trailingIcon = {
            IconButton(
                onClick = { showModal = true },
                enabled = enabled
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "选择日期"
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                if (enabled) {
                    awaitEachGesture {
                        awaitFirstDown()
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = millis
        )
        DatePickerDialog(
            onDismissRequest = { showModal = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val formatted = formatDateTime(selectedMillis, dateFormat, timeZone)
                            onValueChange(formatted)
                        }
                        showModal = false
                    }
                ) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { showModal = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false
            )
        }
    }
}
