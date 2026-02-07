@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("site.addzero.buildlogic.kmp.kmp-convention")
}

kotlin {
    macosX64()
    macosArm64()
    mingwX64()
    linuxX64()
    jvm()

    dependencies {
        implementation(project.dependencies.platform(libs.koin.bom))
        implementation(libs.koin.annotations)
        implementation(libs.koin.core)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kotlinx.cli)
    }

}
