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
        api(projects.lib.ksp.common.addzeroKspEasycode)
        implementation(projects.lib.ksp.metadata.addzeroEntity2analysedSupport)
//        implementation(projects.lib.toolKmp.addzeroToolKoin)

    }
    sourceSets {
        jvmMain.dependencies {
            implementation("org.apache.velocity:velocity-engine-core:2.3")
        }
    }

}
