@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kmp-component")
    id("site.addzero.buildlogic.kmp.composition.kmp-json-withtool")
    id("site.addzero.buildlogic.kmp.libs.kmp-datetime")
}
kotlin {
    dependencies {
        implementation(projects.lib.toolKmp.toolExpect)
        implementation(libs.coil.compose)
        implementation(libs.coil.network.ktor3)
    }
}
