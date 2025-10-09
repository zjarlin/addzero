import java.text.SimpleDateFormat
import java.util.*
val versionDate: String? = SimpleDateFormat("yyyy.MM.dd").format(Date())
version = versionDate.toString()
// 或者使用setter方法
subprojects {
    if (path.startsWith(":lib:") ) {
        apply(plugin = "site.addzero.publish-buddy")
    }
}
//tasks.named<Wrapper>("wrapper") {
//    gradleVersion = "9.1.0"
//    distributionUrl = "https://mirrors.cloud.tencent.com/gradle/${gradleVersion}/gradle-${gradleVersion}-bin.zip"
//}
plugins {
//    id("site.addzero.publish-buddy") version "+"
//    gradlePluginPublishBuddy
    alias(libs.plugins.addzeroPublishBuddy) apply false
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeHotReload) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
//    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}
val `aliyun google` = "https://maven.aliyun.com/repository/google"
val `aliyun public` = "https://maven.aliyun.com/repository/public"
val `aliyun gradle-plugin` = "https://maven.aliyun.com/repository/gradle-plugin"

fun ArtifactRepository.aliyunProxy() {
    if (this is MavenArtifactRepository) {
        when (name) {
            "Google" -> url = uri(`aliyun google`)
            "MavenRepo", "BintrayJCenter" -> url = uri(`aliyun public`)
            "Gradle Central Plugin Repository" -> url = uri(`aliyun gradle-plugin`)
        }
    }
}

allprojects {
    repositories { all { aliyunProxy() } }

    buildscript { repositories { all { aliyunProxy() } } }
}

// 添加libs.versions.toml中需要的库
//apply(from = "./build-logic/settings.gradle.kts")
