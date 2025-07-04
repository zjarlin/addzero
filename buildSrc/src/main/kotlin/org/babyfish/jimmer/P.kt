package org.babyfish.jimmer

object P {
    const val javaVersion = "11"
    const val myGroup = "io.gitee.zjarlin"
    const val myVersion = "1.0.0"
    // Android 相关常量
    const val androidCompileSdk = 35
    const val androidMinSdk = 24
    const val androidTargetSdk = 35

    // 应用相关常量
    const val applicationId = "com.addzero.kmp"
    const val applicationNamespace = "com.addzero.kmp"
    const val sharedNamespace = "com.addzero.kmp.shared"
    const val versionCode = 1
    const val versionName = "1.0"

    // JVM相关常量
    const val jvmTargetVersion = 11

    // 应用主类
    const val mainClass = "com.addzero.kmp.MainKt"

    // 包名和版本
    const val packageName = "com.addzero.kmp"
    const val packageVersion = "1.0.0"

    // 依赖版本
    object Versions {
        const val composeMultiplatform = "1.8.2"
        const val kotlinxCoroutines = "1.10.2"
        const val androidxActivity = "1.10.1"
        const val androidxLifecycle = "2.9.1"
    }
}
