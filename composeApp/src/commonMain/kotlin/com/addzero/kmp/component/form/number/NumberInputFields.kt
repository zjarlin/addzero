package com.addzero.kmp.component.form.number

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.addzero.kmp.component.form.DecimalVisualTransformation

/**
 * 整数输入框
 * 专用于整数类型(如Long, Int等)的输入
 *
 * @param value 当前值
 * @param onValueChange 值变化回调
 * @param label 输入框标签
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 */
@Composable
fun IntegerInputField(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    placeholder: String = "请输入整数"
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

            // 验证整数输入 (允许负数)
            val isValid = newValue.matches(Regex("^-?\\d*$"))

            if (isValid) {
                textValue = newValue
                isError = false

                // 转换为Long类型
                val longValue = newValue.toLongOrNull()
                onValueChange(longValue)
            } else {
                isError = true
            }
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        isError = isError,
        supportingText = if (isError) {
            { Text("请输入有效的整数") }
        } else null
    )
}

/**
 * 小数输入框
 * 专用于浮点数类型(如Double, Float等)的输入
 *
 * @param value 当前值
 * @param onValueChange 值变化回调
 * @param label 输入框标签
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param precision 小数位数限制，0表示不限制
 */
@Composable
fun DecimalInputField(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    label: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    placeholder: String = "请输入小数",
    precision: Int = 0
) {
    var textValue by remember(value) {
        mutableStateOf(value?.toString() ?: "")
    }

    var isError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    OutlinedTextField(
        value = textValue,
        onValueChange = { newValue ->
            if (newValue.isEmpty()) {
                textValue = newValue
                isError = false
                onValueChange(null)
                return@OutlinedTextField
            }

            // 验证小数输入 (允许负数)
            // 不同精度的正则表达式
            val regex = if (precision > 0) {
                Regex("^-?\\d*(\\.\\d{0,${precision}})?$")  // 限制小数位数
            } else {
                Regex("^-?\\d*\\.?\\d*$")  // 不限制小数位数
            }

            val isValid = newValue.matches(regex)

            if (isValid) {
                textValue = newValue
                isError = false

                // 转换为Double类型
                val doubleValue = newValue.toDoubleOrNull()
                onValueChange(doubleValue)
            } else {
                isError = true
                errorMessage = if (precision > 0) {
                    "请输入有效的小数，最多${precision}位小数位"
                } else {
                    "请输入有效的小数"
                }
            }
        },
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        ),
        isError = isError,
        supportingText = if (isError) {
            { Text(errorMessage, color = MaterialTheme.colorScheme.error) }
        } else null,
        visualTransformation = DecimalVisualTransformation()
    )
}
