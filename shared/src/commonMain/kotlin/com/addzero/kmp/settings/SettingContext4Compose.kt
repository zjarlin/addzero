package com.addzero.kmp.settings

import com.addzero.kmp.generated.enums.EnumSysTheme

object SettingContext4Compose {
    const val APP_NAME = "Addzero"
    const val WELCOME_MSG = "登陆成功,欢迎回来!"

    const val AI_SYS_PROMT = "你是后台管理助手,你的回答应该遵循标准Markdown格式"
    const val AI_NAME = "AI助手"
    const val AI_DESCRIPTION = "在线 • 随时为您服务"
    const val AI_AVATAR = "https://q0.itc.cn/q_70/images03/20250623/a3636c5c89234c41856ca35d5d91428f.jpeg"

    //    const val AI_AVATAR_1 = "https://c-ssl.dtstatic.com/uploads/blog/202407/08/LyS2zDOlfqNXvle.thumb.1000_0.jpeg"
    const val AI_AVATAR_1 =
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fsafe-img.xhscdn.com%2Fbw1%2F4f6ad1c2-abe2-4c57-98e5-cdd51e9b11b6%3FimageView2%2F2%2Fw%2F1080%2Fformat%2Fjpg&refer=http%3A%2F%2Fsafe-img.xhscdn.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1755090345&t=0c50823713ad3cc01a78ca5b74e87774"


    val DEFAULT_THEME = EnumSysTheme.LAN_SE_LIANGSE

    const val BASE_URL = "http://localhost:12344"

    /*
    填入正确的路由地址即可
    目前RouteKeys是自动生成的,没有放在shared共享目录,如果是自定义页面填入全限定名称
    */
    const val HOME_SCREEN = "/home"


}
