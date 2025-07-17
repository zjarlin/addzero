plugins {
    id("kmp-shared")
    id("de.jensklingenberg.ktorfit") version "+"
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktorfit.lib)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
        }

        //懒人http
//            implementation(libs.lazy.people.http)
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
