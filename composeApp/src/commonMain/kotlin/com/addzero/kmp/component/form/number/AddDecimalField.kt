package com.addzero.kmp.component.form.number

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.component.form.text.RemoteValidationConfig
import com.addzero.kmp.core.validation.RegexEnum

/**
 * 小数输入框
 * 基于 AddTextField 的小数专用输入组件
 *
 * @param value 当前值
 * @param onValueChange 值变化回调
 * @param label 输入框标签
 * @param placeholder 占位符文本
 * @param isRequired 是否必填
 * @param modifier 修饰符
 * @param maxLength 最大长度限制
 * @param onValidate 验证结果回调
 * @param leadingIcon 前置图标
 * @param disable 是否禁用
 * @param supportingText 支持文本
 * @param trailingIcon 后置图标
 * @param onErrMsgChange 错误信息变化回调
 * @param errorMessages 外部错误信息
 * @param remoteValidationConfig 远程验证配置
 */
@Composable
fun AddDecimalField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "请输入小数",
    isRequired: Boolean = true,
    modifier: Modifier = Modifier.Companion,
    maxLength: Int? = null,
    onValidate: ((Boolean) -> Unit)? = null,
    leadingIcon: ImageVector? = Icons.Default.Numbers,
    disable: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onErrMsgChange: ((String, String) -> Unit)? = null,
    errorMessages: List<String> = emptyList(),
    remoteValidationConfig: RemoteValidationConfig? = null
) {
    AddTextField(
        value = value,
        onValueChange = { newValue ->
            // 过滤输入，只允许数字、小数点和负号
            val filteredValue = filterDecimalInput(newValue, allowNegative = true)
            onValueChange(filteredValue)
        },
        label = label,
        placeholder = placeholder,
        isRequired = isRequired,
        regexEnum = RegexEnum.DECIMAL,
        modifier = modifier,
        maxLength = maxLength,
        onValidate = onValidate,
        leadingIcon = leadingIcon,
        disable = disable,
        supportingText = supportingText,
        trailingIcon = trailingIcon,
        keyboardType = KeyboardType.Companion.Decimal,
        onErrMsgChange = onErrMsgChange,
        errorMessages = errorMessages,
        remoteValidationConfig = remoteValidationConfig
    )
}