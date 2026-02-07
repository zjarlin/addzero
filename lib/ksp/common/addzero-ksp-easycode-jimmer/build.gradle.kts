@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("site.addzero.buildlogic.kmp.libs.kmp-ksp")
//    id("kmp-json-withtool")
//    id("kmp-koin")

}
kotlin {
    dependencies {
        implementation(projects.lib.ksp.common.addzeroKspSupport)
        api(projects.lib.ksp.common.addzeroKspEasycode)

    }
    sourceSets {
        jvmMain.dependencies {
            implementation("org.apache.velocity:velocity-engine-core:2.3")
        }
    }

}
