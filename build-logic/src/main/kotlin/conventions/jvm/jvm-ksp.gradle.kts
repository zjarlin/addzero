plugins {
    id("kotlin-convention")
}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
kotlin {
    dependencies {
        implementation(libs.ksp.symbol.processing.api)
    }
}
