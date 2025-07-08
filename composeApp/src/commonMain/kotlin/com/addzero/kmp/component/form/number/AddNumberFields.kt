package com.addzero.kmp.component.form.number

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyYen
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Percent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import com.addzero.kmp.component.form.AddTextField
import com.addzero.kmp.component.form.RemoteValidationConfig
import com.addzero.kmp.enums.RegexEnum

/**
 * 根据货币类型获取对应的图标
 */
private fun getCurrencyIcon(currency: String): ImageVector {
    return when (currency.uppercase()) {
        "CNY", "RMB", "¥", "人民币" -> Icons.Default.CurrencyYen
        "USD", "DOLLAR", "$", "美元" -> Icons.Default.AttachMoney
        "EUR", "EURO", "€", "欧元" -> Icons.Default.AttachMoney
        "GBP", "POUND", "£", "英镑" -> Icons.Default.AttachMoney
        "JPY", "YEN", "日元" -> Icons.Default.CurrencyYen
        else -> Icons.Default.AttachMoney // 默认使用美元图标
    }
}

/**
 * 整数输入框
 * 基于 AddTextField 的整数专用输入组件
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
 * @param allowNegative 是否允许负数，默认true
 */
@Composable
fun AddIntegerField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "请输入整数",
    isRequired: Boolean = true,
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    onValidate: ((Boolean) -> Unit)? = null,
    leadingIcon: ImageVector? = Icons.Default.Numbers,
    disable: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onErrMsgChange: ((String, String) -> Unit)? = null,
    errorMessages: List<String> = emptyList(),
    remoteValidationConfig: RemoteValidationConfig? = null,
    allowNegative: Boolean = true
) {
    // 选择合适的正则验证器
    val regexValidator = if (allowNegative) RegexEnum.INTEGER else RegexEnum.POSITIVE_INTEGER

    AddTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isRequired = isRequired,
        regexEnum = regexValidator,
        modifier = modifier,
        maxLength = maxLength,
        onValidate = onValidate,
        leadingIcon = leadingIcon,
        disable = disable,
        supportingText = supportingText,
        trailingIcon = trailingIcon,
        keyboardType = KeyboardType.Number,
        onErrMsgChange = onErrMsgChange,
        errorMessages = errorMessages,
        remoteValidationConfig = remoteValidationConfig
    )
}

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
    modifier: Modifier = Modifier,
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
        onValueChange = onValueChange,
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
        keyboardType = KeyboardType.Decimal,
        onErrMsgChange = onErrMsgChange,
        errorMessages = errorMessages,
        remoteValidationConfig = remoteValidationConfig
    )
}

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
    modifier: Modifier = Modifier,
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
        onValueChange = onValueChange,
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
        keyboardType = KeyboardType.Decimal,
        onErrMsgChange = onErrMsgChange,
        errorMessages = errorMessages,
        remoteValidationConfig = remoteValidationConfig
    )
}

/**
 * 百分比输入框
 * 基于 AddTextField 的百分比专用输入组件
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
 * @param showPercentSymbol 是否显示百分号，默认true
 */
@Composable
fun AddPercentageField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String = "请输入百分比",
    isRequired: Boolean = true,
    modifier: Modifier = Modifier,
    maxLength: Int? = null,
    onValidate: ((Boolean) -> Unit)? = null,
    leadingIcon: ImageVector? = Icons.Default.Percent,
    disable: Boolean = false,
    supportingText: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onErrMsgChange: ((String, String) -> Unit)? = null,
    errorMessages: List<String> = emptyList(),
    remoteValidationConfig: RemoteValidationConfig? = null,
    showPercentSymbol: Boolean = true
) {
    // 自定义支持文本，显示百分号
    val customSupportingText: @Composable (() -> Unit)? = if (showPercentSymbol) {
        supportingText ?: {
            androidx.compose.material3.Text(
                text = "%",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        supportingText
    }

    AddTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isRequired = isRequired,
        regexEnum = RegexEnum.DECIMAL,
        modifier = modifier,
        maxLength = maxLength,
        onValidate = onValidate,
        leadingIcon = leadingIcon,
        disable = disable,
        supportingText = customSupportingText,
        trailingIcon = trailingIcon,
        keyboardType = KeyboardType.Decimal,
        onErrMsgChange = onErrMsgChange,
        errorMessages = errorMessages,
        remoteValidationConfig = remoteValidationConfig
    )
}
