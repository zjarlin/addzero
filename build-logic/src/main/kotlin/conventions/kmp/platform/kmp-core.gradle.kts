@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.accessors.dm.LibrariesForLibs
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

val libs = the<LibrariesForLibs>()
plugins {
    id("org.jetbrains.kotlin.multiplatform")
}
kotlin {
    jvm {}
    wasmJs {
        binaries.executable()
        nodejs()
    }
//    iosX64()
    iosArm64()
    iosSimulatorArm64()
//    macosX64()
    macosArm64()
//    watchosArm32()
//    watchosArm64()
//    watchosSimulatorArm64()
//    watchosX64()
//    tvosArm64()
//    tvosSimulatorArm64()
//    tvosX64()
//    mingwX64()
//    linuxX64()
//    linuxArm64()
    sourceSets {
    }
}

