plugins {
    id("kotlin-convention")
    kotlin("plugin.spring")
    id("io.spring.dependency-management")
}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
dependencies {
    implementation(platform(libs.spring.bom))
}


