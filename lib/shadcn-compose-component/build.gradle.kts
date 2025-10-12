@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kmp-component")
    id("kmp-json-withtool")
    id("kmp-datetime")
}
kotlin {
    dependencies {
        implementation(projects.lib.toolKmp.toolExpect)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor3)
    }
}
