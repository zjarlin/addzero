import org.gradle.kotlin.dsl.the

plugins {
    id("org.jetbrains.kotlin.multiplatform")
}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
        }
    }

}
