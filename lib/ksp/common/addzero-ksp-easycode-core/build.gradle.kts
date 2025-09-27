@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kmp-jvm")
    id("kmp-json-withtool")

}


kotlin {
    dependencies {
        implementation(projects.lib.ksp.common.addzeroKspSupport)

    }
    sourceSets {
        jvmMain.dependencies {
            implementation("org.apache.velocity:velocity-engine-core:2.3")
        }
    }

}
