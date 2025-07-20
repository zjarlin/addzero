package com.addzero.kmp.component.form.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import com.addzero.kmp.core.validation.RegexEnum

/**
 * 用户名输入字段组件
 * 基于 AddTextField 实现，提供用户名格式验证
 */
@Composable
fun AddUsernameField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请输入用户名",
    supportingText: String? = null,
    maxLength: Int = 50,
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
        regexEnum = RegexEnum.USERNAME,
        leadingIcon = Icons.Default.Person,
        onValidate = onValidate
    )
}
