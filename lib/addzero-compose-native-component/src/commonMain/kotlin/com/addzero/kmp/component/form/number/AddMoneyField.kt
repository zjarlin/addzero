package com.addzero.kmp.component.form.number

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.component.form.text.RemoteValidationConfig
import com.addzero.kmp.core.validation.RegexEnum

/**
 * 金额输入框
 * 基于 AddTextField 的金额专用输入组件，使用 RegexEnum.MONEY 验证
 * 根据 currency 参数自动选择合适的货币图标
 *
 * @param value 当前值
 * @param onValueChange 值变化回调
 * @param label 输入框标签
 * @param placeholder 占位符文本
 * @param isRequired 是否必填
 * @param modifier 修饰符
 * @param maxLength 最大长度限制
 * @param onValidate 验证结果回调
 * @param leadingIcon 前置图标，如果为null则根据currency自动选择
 * @param disable 是否禁用
 * @param supportingText 支持文本
 * @param trailingIcon 后置图标
 * @param onErrMsgChange 错误信息变化回调
 * @param errorMessages 外部错误信息
 * @param remoteValidationConfig 远程验证配置
 * @param currency 货币类型，支持：CNY/RMB/¥/人民币(显示¥图标)、USD/DOLLAR/$/美元(显示$图标)等
 */
@Composable
fun AddMoneyField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "请输入金额",
    isRequired: Boolean = true,
    modifier: Modifier = Modifier.Companion,
    maxLength: Int? = null,
    onValidate: ((Boolean) -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    disable: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onErrMsgChange: ((String, String) -> Unit)? = null,
    errorMessages: List<String> = emptyList(),
    remoteValidationConfig: RemoteValidationConfig? = null,
    currency: String = ""
) {
    // 根据货币类型自动选择图标，如果没有指定leadingIcon的话
    val finalLeadingIcon = leadingIcon ?: getCurrencyIcon(currency)
    AddTextField(
        value = value,
        onValueChange = { newValue ->
            // 过滤输入，只允许数字和小数点（金额通常不允许负数）
            val filteredValue = filterDecimalInput(newValue, allowNegative = false)
            onValueChange(filteredValue)
        },
        label = label,
        placeholder = placeholder,
        isRequired = isRequired,
        regexEnum = RegexEnum.MONEY,
        modifier = modifier,
        maxLength = maxLength,
        onValidate = onValidate,
        leadingIcon = finalLeadingIcon,
        disable = disable,
        supportingText = supportingText,
        trailingIcon = trailingIcon,
        keyboardType = KeyboardType.Companion.Decimal,
        onErrMsgChange = onErrMsgChange,
        errorMessages = errorMessages,
        remoteValidationConfig = remoteValidationConfig
    )
}