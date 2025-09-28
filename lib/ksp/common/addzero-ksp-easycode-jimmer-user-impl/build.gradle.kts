@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kmp-ksp")
}

kotlin {
    dependencies {
        implementation(projects.lib.ksp.common.addzeroKspEasycodeJimmer)
        implementation(projects.backend.server)
    }
    sourceSets {
        jvmMain.dependencies {
            implementation("org.apache.velocity:velocity-engine-core:2.3")
        }
    }
}