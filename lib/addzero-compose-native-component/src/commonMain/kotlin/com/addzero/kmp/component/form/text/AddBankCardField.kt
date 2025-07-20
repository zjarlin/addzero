package com.addzero.kmp.component.form.text

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.runtime.Composable
import com.addzero.kmp.core.validation.RegexEnum

/**
 * 银行卡号输入字段组件
 * 基于 AddTextField 实现，提供银行卡号格式验证
 */
@Composable
fun AddBankCardField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请输入银行卡号",
    supportingText: String? = null,
    maxLength: Int = 19,
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
        regexEnum = RegexEnum.BANK_CARD,
        leadingIcon = Icons.Default.CreditCard,
        onValidate = onValidate
    )
}
