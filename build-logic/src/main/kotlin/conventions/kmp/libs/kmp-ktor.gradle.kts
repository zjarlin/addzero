import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("kmp-lib")
}
val libs = the<LibrariesForLibs>()

kotlin {
    dependencies{

        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.content.negotiation)
        implementation(libs.ktor.serialization.kotlinx.json)
        implementation(libs.ktor.client.logging)


        implementation(libs.logback)
    }
    sourceSets {

        androidMain.dependencies {
            api(libs.ktor.client.cio)
        }
        jvmMain.dependencies {
            api(libs.ktor.client.cio)
        }
        wasmJsMain.dependencies {
            api(libs.ktor.client.js)
        }
        nativeMain.dependencies {
            api(libs.ktor.client.darwin)
        }

    }
}
