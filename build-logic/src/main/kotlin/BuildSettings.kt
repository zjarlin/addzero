object BuildSettings {

    const val BASE_HOST = "localhost"
    const val BASE_PORT = 12344

    const val BASE_URL = "http://$BASE_HOST:$BASE_PORT"

    const val APP_NAME = "Addzero"
    const val WELCOME_MSG = "登陆成功,欢迎回来!"

    const val AI_SYS_PROMT = "你是后台管理助手,你的回答应该遵循标准Markdown格式"
    const val AI_NAME = "AI助手"
    const val AI_DESCRIPTION = "在线 • 随时为您服务"
    const val AI_AVATAR = "https://q0.itc.cn/q_70/images03/20250623/a3636c5c89234c41856ca35d5d91428f.jpeg"

    //    const val AI_AVATAR_1 = "https://c-ssl.dtstatic.com/uploads/blog/202407/08/LyS2zDOlfqNXvle.thumb.1000_0.jpeg"
    const val AI_AVATAR_1 =
        "https://miaobi-lite.bj.bcebos.com/miaobi/5mao/b%275Yi65a6i5LyN5YWt5LiD5Zu%2B54mH5aS05YOPXzE3MzMxNzUwMDAuODE1OTQwMQ%3D%3D%27/0.png"


    object Android {
        const val ANDROID_APP_ID = "compileOptions.addzero.kmp"
        const val BUILD_TYPE = "release"
    }

    object Desktop {
        const val MAIN_CLASS = "${PACKAGE_NAME}.MainKt"
    }

    const val PROJECT_DESCRIPTION = "addzero-kmp-scaffold"
    const val AUTH_NAME = "zjarlin"
    const val PACKAGE_NAME = "site.addzero"
    const val GIT_URL = "https://gitee.com/zjarlin/addzero.git"
}

val BuildSettings.email: String
    get() = "$AUTH_NAME@outlook.com"
val BuildSettings.gitBaseUrl: String
    get() = GIT_URL.removeSuffix(".git")

val BuildSettings.gitRepoPath: String
    get() = GIT_URL.substringAfter("://").substringAfter("/")

val BuildSettings.gitHost: String
    get() = GIT_URL.substringAfter("://").substringBefore("/")

val BuildSettings.gitRepoName: String
    get() = gitRepoPath.removeSuffix(".git")
