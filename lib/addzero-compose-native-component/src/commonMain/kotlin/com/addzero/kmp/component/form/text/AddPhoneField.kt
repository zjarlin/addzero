package com.addzero.kmp.component.form.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import com.addzero.kmp.core.validation.RegexEnum

/**
 * 手机号输入字段组件
 * 基于 AddTextField 实现，提供手机号格式验证
 */
@Composable
fun AddPhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请输入手机号",
    supportingText: String? = null,
    maxLength: Int = 11,
    onValidate: ((Boolean) -> Unit)? = null
) {
    AddTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        isRequired = isRequired,
        disable = !enabled,
        placeholder = placeholder,
        maxLength = maxLength,
        regexEnum = RegexEnum.PHONE,
        leadingIcon = Icons.Default.Phone,
        onValidate = onValidate
    )
}
