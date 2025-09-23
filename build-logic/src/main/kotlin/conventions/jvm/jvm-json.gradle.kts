import org.gradle.accessors.dm.LibrariesForLibs
plugins {
    kotlin("plugin.serialization")
    id("kotlin-convention")
}
val libs = the<LibrariesForLibs>()

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
