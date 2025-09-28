@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kmp-ksp")
//    id("kmp-json-withtool")
//    id("kmp-koin")

}
kotlin {
    dependencies {
        implementation(projects.lib.ksp.common.addzeroKspSupport)
        implementation(projects.lib.ksp.metadata.addzeroEntity2analysedSupport)
        api(projects.lib.ksp.common.addzeroKspEasycode)

    }
    sourceSets {
        jvmMain.dependencies {
            implementation("org.apache.velocity:velocity-engine-core:2.3")
        }
    }

}
