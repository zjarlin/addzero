package com.addzero.kmp.component.form.number

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.form.DecimalVisualTransformation

@Composable
fun NumberInputField(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    label: String,
    isDecimal: Boolean = false,
    modifier: Modifier = Modifier.width(120.dp)
) {
    var textValue by remember(value) {
        mutableStateOf(value?.toString() ?: "")
    }

    var isError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textValue,
        onValueChange = { newValue ->
            if (newValue.isEmpty()) {
                textValue = newValue
                isError = false
                onValueChange(null)
                return@OutlinedTextField
            }

            // 验证数字输入
            val isValid = if (isDecimal) {
                newValue.matches(Regex("^-?\\d*\\.?\\d*$"))
            } else {
                newValue.matches(Regex("^-?\\d*$"))
            }

            if (isValid) {
                textValue = newValue
                isError = false

                // 转换为相应数字类型
                val numberValue = when {
                    isDecimal -> newValue.toDoubleOrNull()
                    else -> newValue.toLongOrNull()
                }
                onValueChange(numberValue)
            } else {
                isError = true
            }
        },
        label = { Text(label) },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isDecimal) KeyboardType.Decimal else KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        isError = isError,
        visualTransformation = if (isDecimal) DecimalVisualTransformation() else VisualTransformation.None
    )
}
