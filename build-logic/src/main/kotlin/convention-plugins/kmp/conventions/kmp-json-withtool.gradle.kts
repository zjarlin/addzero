import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("kmp-json")

}
val libs = the<LibrariesForLibs>()

kotlin {

    sourceSets {
        commonMain.dependencies {
            implementation(libs.addzero.tool.json)

        }
    }

}
