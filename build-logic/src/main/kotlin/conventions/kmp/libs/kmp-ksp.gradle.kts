import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}
val libs = the<LibrariesForLibs>()


kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ksp.symbol.processing.api)
        }
    }
}


