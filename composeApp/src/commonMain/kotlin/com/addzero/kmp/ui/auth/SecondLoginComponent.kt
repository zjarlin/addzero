package com.addzero.kmp.ui.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.SwitchAccount
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.api.SignInStatus
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.form.AddPasswordField
import com.addzero.kmp.component.form.AddTextField
import com.addzero.kmp.enums.RegexEnum
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun SecondLoginComponent(
    logo: ImageVector = Icons.Default.EmojiPeople, onSecondLogin: () -> Unit, onFrogetPassword: () -> Unit, customLogo: @Composable (() -> Unit)?
) {
    val viewModel = koinViewModel<LoginViewModel>()

//    val viewModel = LoginUtil.viewModel
    val userRegFormState = viewModel.userRegFormState

    val firstTitle = "检测到已有账号"

    LComponent(
        customLogo = customLogo,
        logo = logo, firstTitle = firstTitle, secondTitle = "请填写密码完成登录"
    ) {


        // 用户名输入框
        AddTextField(
            value = userRegFormState.username, onValueChange = { viewModel.userRegFormState = userRegFormState.copy(username = it) }, label = "用户名", isRequired = false, regexEnum = RegexEnum.USERNAME, leadingIcon = Icons.Default.PeopleAlt, disable = true
//            , errorMessages = errorMessages
        )

        // 邮箱输入框
//        AddEmailField(
//            value = userRegFormState.email, onValueChange = { viewModel.userRegFormState = userRegFormState.copy(email = it) }, label = "邮箱地址", isRequired = false, disable = true
////            , errorMessages = errorMessages
//        )


        // 手机号输入框
//        AddTextField(
//            value = userRegFormState.phone ?: "", onValueChange = { viewModel.userRegFormState = userRegFormState.copy(phone = it) }, label = "手机号", isRequired = false, regexValidator = RegexEnum.PHONE, leadingIcon = Icons.Default.Phone, disable = true
////            , errorMessages = errorMessages
//        )


        // 密码输入框
        AddPasswordField(
            value = userRegFormState.password, onValueChange = { viewModel.userRegFormState = userRegFormState.copy(password = it) },
            otherIcon = {

                AddIconButton(
                    text = "登录", imageVector = Icons.AutoMirrored.Filled.Login, onClick = onSecondLogin
                )

                AddIconButton("切换账号", imageVector = Icons.Default.SwitchAccount) { viewModel.singinStatus = SignInStatus.None }

            },
//        , errorMessages = errorMessages
        )

//        Surface {
//            Row(
//                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
//            ) {
//                AddIconButton("登录", imageVector = Icons.AutoMirrored.Filled.Login, onClick = onSecondLogin)
//
//
//                AddIconButton("切换账号", imageVector = Icons.Default.SwitchAccount, onClick = { viewModel.singinStatus = SignInStatus.None })
//            }
//
//        }
        Spacer(modifier = Modifier.height(24.dp))

        TextButton(
            onClick = onFrogetPassword, modifier = Modifier.padding(end = 0.dp, top = 4.dp)
        ) {
            Text(
                text = "忘记密码?", color = MaterialTheme.colorScheme.primary, fontSize = 14.sp, fontWeight = FontWeight.Medium
            )
        }


    }
}
