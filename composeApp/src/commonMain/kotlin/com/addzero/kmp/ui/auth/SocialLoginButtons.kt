package com.addzero.kmp.ui.auth

import addzero.composeapp.generated.resources.Res
import addzero.composeapp.generated.resources.wechat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource

@Composable
fun SocialLoginDivider() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )

        Text(
            text = "或使用第三方账号登录",
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        )
    }
}

@Composable
fun SocialLoginButtons(
    onWeChatLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SocialLoginDivider()

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 微信登录保持原样
            SocialLoginButton(
                imageContent = {
                    Image(
                        painterResource(Res.drawable.wechat),
                        contentDescription = "WeChat",
                        modifier = Modifier.size(28.dp)
                    )
                },
                backgroundColor = Color(0xFF07C160),
                onClick = onWeChatLogin
            )

            // 手机登录
//            AddIconButton(
//                text = "手机登录",
//                imageVector = Icons.Default.Phone,
//                onClick = onPhoneLogin
//            )

            // 邮箱登录
//            AddIconButton(
//                text = "邮箱登录",
//                imageVector = Icons.Default.Email,
//                onClick = onEmailLogin
//            )
        }
    }
}

@Composable
fun SocialLoginButton(
    imageContent: @Composable () -> Unit,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .shadow(elevation = 2.dp, shape = CircleShape, ambientColor = backgroundColor.copy(alpha = 0.2f))
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.scale(1.1f)) { // 稍微放大图标
            imageContent()
        }
    }
}
