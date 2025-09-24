plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.multiplatform")

}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)

        }
    }

}
