import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}
val libs = the<LibrariesForLibs>()


kotlin {
    jvm()
dependencies {
            implementation(libs.ksp.symbol.processing.api)
        }
    }


