package com.addzero.kmp.component.form.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.runtime.Composable
import com.addzero.kmp.core.validation.RegexEnum

/**
 * 身份证号输入字段组件
 * 基于 AddTextField 实现，提供身份证号格式验证
 */
@Composable
fun AddIdCardField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请输入身份证号",
    supportingText: String? = null,
    maxLength: Int = 18,
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
        regexEnum = RegexEnum.ID_CARD,
        leadingIcon = Icons.Default.Badge,
        onValidate = onValidate
    )
}
