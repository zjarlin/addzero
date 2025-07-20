package com.addzero.kmp.component.form.text

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddIconButton

private val commonEmailSuffixes = listOf(
    "@qq.com", "@gmail.com", "@163.com", "@126.com", "@outlook.com", "@hotmail.com", "@yahoo.com", "@foxmail.com"
)


/**
 * Email input field with validation
 *
 * @param value Current text value
 * @param onValueChange Callback when the value changes
 * @param label Label text for the field
 * @param isRequired Whether the field is required
 * @param validator Custom validator function
 * @param errorMsg Custom error message
 * @param modifier Modifier for styling
 * @param onValidate Callback for validation result
 * @param disable Whether the field is disabled
 * @param trailingIcon Trailing icon for the field
 * @param trailingIconText Text for the trailing icon
 * @param onTrailingIconClick Callback when trailing icon is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "邮箱",
    isRequired: Boolean = true,
    validators: List<Pair<(String) -> Boolean, String>> = emptyList(),
    errorMsg: String? = null,
    modifier: Modifier = Modifier,
    onValidate: ((Boolean) -> Unit)? = null,
    disable: Boolean = false,
    otherTrailingIcon: @Composable (() -> Unit)? = null,
    showCheckEmail: Boolean = false,
    checkEmailIcon: ImageVector = Icons.AutoMirrored.Filled.Send,
    onSendCode: (String) -> Unit = {},
    oncheckEmailSuccess: (() -> Unit)? = null,
    onErrMsgChange: ((String, String) -> Unit)? = null,
    errorMessages: List<String> = emptyList<String>(),
    remoteValidationConfig: RemoteValidationConfig? = null,

    ) {
    var errorMessages by remember { mutableStateOf(errorMessages) }

    var localValue by remember(value) {
        mutableStateOf(value.substringBefore("@", value))
    }
    // 修复：根据 value 初始化 selectedSuffix
    var selectedSuffix by remember(value) {
        val suffix = value.substringAfter("@", "")
        val match = commonEmailSuffixes.find { it.removePrefix("@") == suffix }
        mutableStateOf(match ?: commonEmailSuffixes[0])
    }
    var expanded by remember { mutableStateOf(false) }
    var checkEmail by remember { mutableStateOf(false) }
    var showInputCode by remember { mutableStateOf(false) }
    var inputCode by remember { mutableStateOf("") }
    var sentCode by remember { mutableStateOf("") }

    // 当本地值改变时，更新完整的邮箱地址
    val updateEmail = { newLocalValue: String ->
        if (!disable) {
            onValueChange(newLocalValue + selectedSuffix)
        }
    }

    @Composable
    fun suffixInput() {
        // 邮箱后缀下拉选择
        ExposedDropdownMenuBox(
            expanded = expanded, onExpandedChange = { if (!disable) expanded = it }, modifier = Modifier.width(200.dp)
        ) {

//            AddTextField(
//                value = selectedSuffix,
//                onValueChange = {},
//                disable = disable,
//                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, !disable),
//            )


            OutlinedTextField(
                value = selectedSuffix,
                onValueChange = {},
                readOnly = true,
                enabled = !disable,
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, !disable),
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                singleLine = true
            )
            ExposedDropdownMenu(
                expanded = expanded, onDismissRequest = { expanded = false }) {
                commonEmailSuffixes.forEach { suffix ->
                    DropdownMenuItem(text = { Text(suffix) }, onClick = {
                        selectedSuffix = suffix
                        expanded = false
                        updateEmail(localValue)
                    })
                }
            }
        }

    }

    fun reSend() {
        if (localValue.isBlank()) {
            errorMessages += "邮箱前缀不能为空"
            return
        }
        if (!checkEmail) {
            // 产生6位随机数字并通过回调发送
            val code = (100000..999999).random().toString()
            sentCode = code
            onSendCode(code)
            // 展示验证码输入框
            showInputCode = true
        }

    }

    val showYanzhengmaInput = showInputCode && !checkEmail


    // 邮箱前缀输入框
    AddTextField(
        remoteValidationConfig = remoteValidationConfig,
        value = localValue, onValueChange = {
            if (!disable) {
                localValue = it
                updateEmail(it)
            }
        }, label = label, placeholder = "请输入邮箱前缀", isRequired = isRequired, validators = validators,
//                modifier = Modifier.weight(1f),
        onValidate = onValidate, leadingIcon = Icons.Default.Email, disable = disable, trailingIcon = {
//                Column {

            Row {
                if (showCheckEmail) {

                    if (showYanzhengmaInput) {
                        OutlinedTextField(
                            value = inputCode,
                            onValueChange = { newValue ->
                                // 限制输入长度为6位
                                if (newValue.length <= 6) {
                                    inputCode = newValue
                                    // 当输入满6位时进行校验
                                    if (newValue.length == 6) {
                                        if (newValue == sentCode) {
                                            checkEmail = true
                                            showInputCode = false
                                            oncheckEmailSuccess?.let { it() }
                                        } else {
                                            // 验证码错误时更新错误信息
                                            errorMessages = errorMessages + "验证码错误"
                                            onErrMsgChange?.let { it(newValue, "验证码不正确，请重新输入") }
                                        }
                                    }
                                }
                            },
                            label = { Text("输入验证码") },
                            singleLine = true,
                            isError = errorMessages.isNotEmpty(),
                            modifier = Modifier.padding(start = 8.dp).width(120.dp)
                        )
                    }
                    AddIconButton(
                        text = if (checkEmail) "已验证" else if (showYanzhengmaInput) "重新发送" else "校验邮箱",
                        imageVector = if (checkEmail) Icons.Filled.Check else checkEmailIcon
                    ) {
                        errorMessages = emptyList()  // 重新发送时清除错误信息
                        reSend()
                    }



                    suffixInput()

                }

                otherTrailingIcon?.let { it() }

            }


//                }


        }, onErrMsgChange = onErrMsgChange, errorMessages = errorMessages
    )


}
