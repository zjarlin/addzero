package com.addzero.kmp.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.api.CheckSignInput
import com.addzero.kmp.api.CheckSignInput.USERNAME
import com.addzero.kmp.component.form.AddEmailField
import com.addzero.kmp.component.form.AddPasswordField
import com.addzero.kmp.component.form.AddTextField
import com.addzero.kmp.component.form.RemoteValidationConfig
import com.addzero.kmp.enums.RegexEnum
import com.addzero.kmp.kt_util.add
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterComponent(logo: ImageVector = Icons.Default.EmojiPeople, onReg: () -> Unit, onBackToLogin: () -> Unit, onSendCode: (String) -> Unit) {
//    viewModel
    val viewModel = koinViewModel<LoginViewModel>()

//    val viewModel = LoginUtil.viewModel
    val userRegFormState = viewModel.userRegFormState
    var errorMsgs by remember { mutableStateOf(listOf<String>()) }

    val validators = listOf(
        { it: String -> it == userRegFormState.password } to "两次输入的密码不一致"
    )
    LComponent(
        logo = logo, firstTitle = "创建新账号", secondTitle = "请填写以下信息完成注册"
    ) {


        // 用户名输入框
        val checkSignInput = viewModel.checkSignInput
        AddTextField(
            value = userRegFormState.username,
            onValueChange = { viewModel.userRegFormState = userRegFormState.copy(username = it) },
            label = "用户名",
            regexEnum = RegexEnum.USERNAME,
            leadingIcon = Icons.Default.PeopleAlt,
            disable = checkSignInput == USERNAME,
            onErrMsgChange = { input, msg -> errorMsgs.add(msg) },


            remoteValidationConfig = RemoteValidationConfig(
                tableName = "sys_user",
                column = "username",
            )


//            , errorMessages = errorMessages
        )


        // 邮箱输入框
        AddEmailField(
            value = userRegFormState.email,
            onValueChange = { viewModel.userRegFormState = userRegFormState.copy(email = it) },
            label = "邮箱地址",
            disable = checkSignInput == CheckSignInput.EMAIL,
            showCheckEmail = true,
            onSendCode = onSendCode, onErrMsgChange = { input, msg -> errorMsgs.add(msg) },
            remoteValidationConfig = RemoteValidationConfig(
                tableName = "sys_user",
                column = "email",
            )
//            , errorMessages = errorMessages
        )

        // 手机号输入框
        AddTextField(
            value = userRegFormState.phone ?: "",
            onValueChange = { viewModel.userRegFormState = userRegFormState.copy(phone = it) },
            label = "手机号",
            isRequired = false,
            regexEnum = RegexEnum.PHONE,
            leadingIcon = Icons.Default.Phone,
            disable = checkSignInput == CheckSignInput.PHONE, onErrMsgChange = { input, msg -> errorMsgs.add(msg) },
            remoteValidationConfig = RemoteValidationConfig(
                tableName = "sys_user",
                column = "phone",
            )

//            , errorMessages = errorMessages
        )


        // 密码输入框
        AddPasswordField(
            value = userRegFormState.password,
            onValueChange = { viewModel.userRegFormState = userRegFormState.copy(password = it) },
            onErrMsgChange = { input, msg -> errorMsgs.add(msg) },
//            errorMessages = errorMessages
        )


        // 确认密码输入框
        AddPasswordField(
            value = viewModel.confirmPassword,
            onValueChange = { viewModel.confirmPassword = it },
            label = "确认密码",
            validators = validators, onErrMsgChange = { input, msg -> errorMsgs.add(msg) }
//            , errorMessages = errorMessages
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 注册按钮 - Apple 风格
        Button(
            enabled = errorMsgs.isEmpty(),
            onClick = onReg,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp), // 更圆润的按钮
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Text(
                text = "创建账号",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
        if (errorMsgs.isNotEmpty()) {

            Surface {
                Column {
                    errorMsgs.forEach {
                        Text(text = it, style = MaterialTheme.typography.bodyLarge)
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 返回登录
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "已有账号？",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
            TextButton(
                onClick = onBackToLogin
            ) {
                Text(
                    text = "返回登录",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

    }
}
