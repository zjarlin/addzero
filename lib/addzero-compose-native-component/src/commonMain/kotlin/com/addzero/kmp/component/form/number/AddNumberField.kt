package com.addzero.kmp.component.form.number

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

/**
 * 通用数字输入字段组件
 * 基于 OutlinedTextField 实现，提供数字输入和验证功能
 */
@Composable
fun AddNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请输入数字",
    supportingText: String? = null,
    decimalPlaces: Int = 2,
    allowNegative: Boolean = true,
    maxValue: Double? = null,
    minValue: Double? = null,
    onValidate: ((Boolean) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    // 验证输入
    LaunchedEffect(value) {
        val isValid = validateNumber(
            value = value,
            isRequired = isRequired,
            decimalPlaces = decimalPlaces,
            allowNegative = allowNegative,
            maxValue = maxValue,
            minValue = minValue
        )

        isError = !isValid.first
        errorMessage = isValid.second
        onValidate?.invoke(isValid.first)
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = { newValue ->
                // 只允许数字、小数点和负号
                val filteredValue = newValue.filter { char ->
                    char.isDigit() ||
                            char == '.' ||
                            (char == '-' && allowNegative && newValue.indexOf('-') == 0)
                }
                onValueChange(filteredValue)
            },
            label = {
                Text(if (isRequired) "$label *" else label)
            },
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Numbers,
                    contentDescription = "数字输入"
                )
            },
            supportingText = if (isError) {
                { Text(errorMessage, color = MaterialTheme.colorScheme.error) }
            } else if (supportingText != null) {
                { Text(supportingText) }
            } else null,
            isError = isError,
            enabled = enabled,
            keyboardOptions = KeyboardOptions(
                keyboardType = if (decimalPlaces > 0) KeyboardType.Decimal else KeyboardType.Number
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/**
 * 验证数字输入
 */
private fun validateNumber(
    value: String,
    isRequired: Boolean,
    decimalPlaces: Int,
    allowNegative: Boolean,
    maxValue: Double?,
    minValue: Double?
): Pair<Boolean, String> {
    // 空值检查
    if (value.isBlank()) {
        return if (isRequired) {
            false to "此字段为必填项"
        } else {
            true to ""
        }
    }

    // 数字格式检查
    val doubleValue = value.toDoubleOrNull()
    if (doubleValue == null) {
        return false to "请输入有效的数字"
    }

    // 负数检查
    if (!allowNegative && doubleValue < 0) {
        return false to "不允许输入负数"
    }

    // 小数位数检查
    if (decimalPlaces == 0 && value.contains('.')) {
        return false to "不允许输入小数"
    }

    if (decimalPlaces > 0 && value.contains('.')) {
        val decimalPart = value.substringAfter('.')
        if (decimalPart.length > decimalPlaces) {
            return false to "小数位数不能超过 $decimalPlaces 位"
        }
    }

    // 范围检查
    if (minValue != null && doubleValue < minValue) {
        return false to "值不能小于 $minValue"
    }

    if (maxValue != null && doubleValue > maxValue) {
        return false to "值不能大于 $maxValue"
    }

    return true to ""
}
