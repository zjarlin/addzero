package com.addzero.kmp.component.form.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.runtime.Composable
import com.addzero.kmp.core.validation.RegexEnum

/**
 * URL 输入字段组件
 * 基于 AddTextField 实现，提供 URL 格式验证
 */
@Composable
fun AddUrlField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请输入网址",
    supportingText: String? = null,
    maxLength: Int = 500,
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
        regexEnum = RegexEnum.URL,
        leadingIcon = Icons.Default.Link,
        onValidate = onValidate
    )
}
