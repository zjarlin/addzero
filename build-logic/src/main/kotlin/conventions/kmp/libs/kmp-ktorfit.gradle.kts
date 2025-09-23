plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("de.jensklingenberg.ktorfit")
}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktorfit.lib)
        }

    }
}
