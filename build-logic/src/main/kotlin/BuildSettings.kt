object BuildSettings {
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
