import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("jvm-json")
}
val libs = the<LibrariesForLibs>()

dependencies {
    implementation(libs.addzero.tool.json)
}
