package com.addzero.kmp.component.form

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

/**
 * todo 加入默认强秘密校验lambda (String)->Boolean
 * 密码输入框
 * @param [value]
 * @param [onValueChange]
 * @param [modifier]
 * @param [label]
 * @param [enabled]
 * @param [isError]
 * @param [supportingText]
 */
@Composable
@Deprecated("use AddPasswordField instead", replaceWith = ReplaceWith("AddPasswordField"))
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "密码",
    enabled: Boolean = true,
    isError: Boolean = false,
    supportingText: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        enabled = enabled,
        isError = isError,
        supportingText = supportingText?.let { { Text(it) } },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (passwordVisible) "隐藏密码" else "显示密码"
                )
            }
        },
        modifier = modifier
    )
}
