package com.addzero.kmp.demo.icon

import addzerokmp.composeapp.generated.resources.Res
import addzerokmp.composeapp.generated.resources.wechat
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import com.addzero.kmp.annotation.Route

import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
@Route("组件示例", "微信图标", routePath = "/component/wechatIcon")
fun WeChatIcon() {
    Image(
        painter = painterResource(Res.drawable.wechat),
        contentDescription = "WeChat"
    )
}