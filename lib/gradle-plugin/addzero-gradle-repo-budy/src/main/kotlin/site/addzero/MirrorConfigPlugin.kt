package site.addzero

import org.gradle.api.Plugin
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.maven

// 你的扩展函数
fun RepositoryHandler.applyGoogleRepository() {
    google {
        mavenContent {
            includeGroupAndSubgroups("androidx")
            includeGroupAndSubgroups("com.android")
            includeGroupAndSubgroups("com.google")
        }
    }
}

fun RepositoryHandler.applyCommonRepositories() {
    applyGoogleRepository()
    mavenCentral()
    mavenLocal()
}

fun RepositoryHandler.applyPluginRepositories() {
    applyCommonRepositories()
    gradlePluginPortal()
    maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

// 插件实现类
class RepoConfigPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        // 配置pluginManagement仓库
        settings.pluginManagement.repositories {
            applyPluginRepositories()
        }

        // 配置dependencyResolutionManagement仓库
        settings.dependencyResolutionManagement.repositories {
            applyCommonRepositories()
        }
    }
}
