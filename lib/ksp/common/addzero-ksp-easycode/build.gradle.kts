@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kmp-ksp")
    id("kmp-json-withtool")
}


kotlin {
    dependencies {
        api(projects.lib.ksp.common.addzeroKspEasycodeCore)

        implementation(projects.lib.ksp.common.addzeroKspSupport)

    }
    sourceSets {
        jvmMain.dependencies {
            implementation("org.apache.velocity:velocity-engine-core:2.3")
        }
    }

}
