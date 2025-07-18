package com.addzero.kmp.ui.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.form.text.AddTextField
import com.addzero.kmp.viewmodel.LoginViewModel
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginComponent(logo: ImageVector = Icons.Default.Email, onWeChatLogin: () -> Unit, onLogin: () -> Unit) {
    val viewModel = koinViewModel<LoginViewModel>()
//    val viewModel = LoginUtil.viewModel

    LComponent(logo = logo) {


        AddTextField(
            value = viewModel.loginContext,
            onValueChange = { viewModel.loginContext = it },
            label = "用户名/邮箱/手机号",
            leadingIcon = Icons.Default.PeopleAlt,
            trailingIcon ={
                AddIconButton(text = "继续", imageVector = Icons.AutoMirrored.Filled .Login, onClick = onLogin)
            },
//            errorMessages = errorMessages
        
        )
        Spacer(modifier = Modifier.height(16.dp))


        // 第三方登录
        SocialLoginButtons(
            onWeChatLogin = onWeChatLogin
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 注册按钮
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
//            Text(
//                "还没有账号？",
//                style = MaterialTheme.typography.bodyMedium.copy(
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                )
//            )
//
//            TextButton(
//                onClick = { viewModel.regFlag = true }
//            ) {
//                Text(
//                    "注册一个",
//                    color = MaterialTheme.colorScheme.primary,
//                    fontWeight = FontWeight.SemiBold
//                )
//            }
//
//
//        }
    }


}
