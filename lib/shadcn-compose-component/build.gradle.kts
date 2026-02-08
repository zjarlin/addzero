@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("site.addzero.buildlogic.kmp.cmp-lib")
    id("site.addzero.buildlogic.kmp.kmp-json-withtool")
    id("site.addzero.buildlogic.kmp.kmp-datetime")
}
kotlin {
    dependencies {
        implementation(projects.lib.toolKmp.toolExpect)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor3)
    }
}
