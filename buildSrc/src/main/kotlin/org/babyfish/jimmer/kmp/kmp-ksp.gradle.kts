import org.gradle.internal.impldep.com.google.api.services.storage.Storage

plugins {
    id("org.jetbrains.kotlin.multiplatform")
//    kotlin("plugin.serialization")
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
//            implementation(project(:addzero-ksp))
        }
        jvmMain.dependencies {
            implementation("com.google.devtools.ksp:symbol-processing-api:+")
        }
    }
}
