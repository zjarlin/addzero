package com.addzero.kmp.demo.icon

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import com.addzero.kmp.annotation.Route
import io.gitee.zjarlin.composeapp.generated.resources.Res
import io.gitee.zjarlin.composeapp.generated.resources.wechat

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
