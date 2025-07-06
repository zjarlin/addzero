package org.babyfish.jimmer

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Vars {
    const val myGroup = "io.gitee.zjarlin"
    val myVersion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HHmm"))

    val giturl = "https://gitee.com/zjarlin/addzero"
    val projName = "AddzeroComponent"
    val authorName = myGroup.split(".").last() // zjarlin

    // 应用相关常量
    const val applicationId = "compileOptions.addzero.kmp"
    const val applicationNamespace = "compileOptions.addzero.kmp.component"
    const val sharedNamespace = "com.addzero.kmp.shared"
    const val outputModuleName = "composeApp"

    // 应用主类

    //    const val packageName = "compileOptions.addzero.kmp"
//    const val mainClass = "compileOptions.addzero.kmp.MainKt"
    // 包名和版本
    const val packageName = "com.addzero.kmp"

    const val mainClass = "${packageName}.MainKt"

    // 依赖版本
}

object Versions {
    const val ktorVersion="3.2.1"
    const val pinyin4jVersion = "2.5.1"

    const val fileKitVersion = "0.8.8"
    const val versionCode = 1
    const val versionName = "1.0"

    const val javaVersion = "24"
    const val kotlinVersion = "2.2.0"
    const val kotlinxSerializationVersion = "1.8.0"

    // Android 相关常量
    const val androidCompileSdk = 35
    const val androidMinSdk = 24
    const val androidTargetSdk = 35
    const val composeMultiplatform = "1.8.2"
    const val kotlinxCoroutines = "1.10.2"
    const val androidxActivity = "1.10.1"
    const val androidxLifecycle = "2.9.1"
}
