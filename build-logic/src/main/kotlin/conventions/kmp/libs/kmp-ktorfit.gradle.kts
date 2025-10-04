plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("de.jensklingenberg.ktorfit")
}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

kotlin {
dependencies {
            implementation(libs.ktorfit.lib)
        }

    }
