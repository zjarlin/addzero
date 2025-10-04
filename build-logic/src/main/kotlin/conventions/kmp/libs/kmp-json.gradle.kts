plugins {
    kotlin("plugin.serialization")
    id("org.jetbrains.kotlin.multiplatform")

}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

kotlin {

dependencies {
            implementation(libs.kotlinx.serialization.json)

        }
    }

