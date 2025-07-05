package com.addzero.kmp.context

val Settings.isomorphicShareOutPutDir: String
    get() {
        val settings = SettingContext.settings
        val replace = settings.isomorphicPackageName
            .replace(".", "/")
        return "${settings.sharedSourceDir}/$replace"
    }


val Settings.formShareOutPutDir: String
    get() {
        val settings = SettingContext.settings
        return "${settings.sharedSourceDir}/${
            settings.formPackageName
                .replace(".", "/")
        }"
    }

/**
 * API客户端输出目录
 */
val Settings.apiclientOutPutDir: String
    get() {
        val settings = SettingContext.settings
        return "${settings.sharedSourceDir}/${
            settings.apiClientPackageName
                .replace(".", "/")
        }"
    }
