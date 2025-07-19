package com.addzero.kmp.component.form.text

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.addzero.kmp.core.validation.RegexEnum
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class RemoteValidationConfig(
    val tableName: String,
    val column: String,
    val debounceTime: Long = 500L // 防抖时间，单位毫秒
)

@Composable
fun AddTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "请输入$label",
    isRequired: Boolean = true,
    validators: List<Pair<(String) -> Boolean, String>> = emptyList(),
    regexEnum: RegexEnum? = null,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    maxLength: Int? = null,
    onValidate: ((Boolean) -> Unit)? = null, // 可选：外部监听校验结果
    leadingIcon: ImageVector? = null,
    disable: Boolean = false,
    supportingText: @Composable (() -> Unit)? = {
        // 显示最大长度提示
        maxLength?.let {
            Text(
                text = "${value.length}/$it",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    },
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onErrMsgChange: ((String, String) -> Unit)? = null,
    errorMessages: List<String> = emptyList(),
    remoteValidationConfig: RemoteValidationConfig? = null,
    remoteValidationConfigPredicate: (RemoteValidationConfig, String) -> Boolean = { _, _ -> true },
) {
    var isError by remember { mutableStateOf(false) }
    var errorMessages by remember { mutableStateOf(errorMessages) }
    var hasFocus by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var remoteValidationJob by remember { mutableStateOf<Job?>(null) }

    // 添加组件属性
    val textFieldShape = RoundedCornerShape(12.dp)
    val textFieldModifier = Modifier.fillMaxWidth()

    // 校验逻辑
    fun validate(input: String): Boolean {
        val errors = mutableListOf<String>()

        // 1. 检查必填
        if (isRequired && input.isBlank()) {
            errors.add("${label}为必填项")
        }

        // 2. 检查长度限制
        if (maxLength != null && input.length > maxLength) {
            errors.add("输入内容不能超过${maxLength}个字符")
        }

        // 3. 检查正则验证
        if (regexEnum != null && input.isNotBlank()) {
            val (valid, message) = RegexEnum.validate(input, regexEnum)
            if (!valid) {
                errors.add(message)
            }
        }

        // 4. 检查自定义验证规则列表
        validators.forEach { (validator, errorMsg) ->
            if (!validator(input)) {
                errors.add(errorMsg)
                onErrMsgChange?.let { it(input, errorMsg) }
            }
        }

        // 更新错误状态
        isError = errors.isNotEmpty()
        errorMessages = errors

        // 5. 远程校验
        if (remoteValidationConfig != null && !isError && input.isNotBlank()) {
            remoteValidationJob?.cancel() // 取消之前的远程校验任务
            remoteValidationJob = coroutineScope.launch {
                delay(remoteValidationConfig.debounceTime)
                val exists = remoteValidationConfigPredicate(remoteValidationConfig, input)

                if (exists) {
                    errors.add("${label}已存在")
                    isError = true
                    errorMessages = errors
                }
                onValidate?.invoke(!isError)
            }
        } else {
            onValidate?.invoke(!isError)
        }
        return !isError
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            enabled = !disable,
            trailingIcon = trailingIcon,
            supportingText = supportingText,
            value = value,
            onValueChange = { newValue ->
                // 处理最大长度限制
                val finalValue = if (maxLength != null && newValue.length > maxLength) {
                    newValue.take(maxLength)
                } else newValue

                onValueChange(finalValue)

                // 仅在有错误时，输入过程中消除错误提示（实时清除错误）
                if (isError) {
                    val errors = mutableListOf<String>()

                    // 只检查必填和长度，不做复杂校验
                    if (isRequired && finalValue.isBlank()) {
                        errors.add("${label}为必填项")
                    }
                    if (maxLength != null && finalValue.length > maxLength) {
                        errors.add("输入内容不能超过${maxLength}个字符")
                    }

                    // 如果基础错误消除了，清除错误状态
                    if (errors.isEmpty()) {
                        isError = false
                        errorMessages = emptyList()
                    }
                }
            },
            leadingIcon = {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = if (isError) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                }
            },
            label = { Text(if (isRequired) "$label *" else label) },
            placeholder = { Text(placeholder) },
            isError = isError,
            singleLine = maxLines == 1,
            maxLines = maxLines,
            shape = textFieldShape,
            modifier = textFieldModifier.onFocusChanged { focusState: FocusState ->
                hasFocus = focusState.isFocused

                if (!focusState.isFocused) {
                    // 失去焦点时进行完整校验
                    validate(value)
                } else {
                    // 获得焦点时，清除远程校验的错误信息，以便重新输入
                    if (remoteValidationConfig != null) {
                        errorMessages = errorMessages.filter { it != "${label}已存在" }
                        isError = errorMessages.isNotEmpty()
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = ImeAction.Done
            ),
            visualTransformation = visualTransformation,
            keyboardActions = KeyboardActions(
                onDone = { validate(value) })
        )
        // 显示所有错误信息
        if (isError && errorMessages.isNotEmpty()) {
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                errorMessages.forEach { message ->
                    Text(
                        text = "• $message",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
