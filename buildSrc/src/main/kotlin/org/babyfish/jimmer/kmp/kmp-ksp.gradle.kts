
plugins {
    id("org.jetbrains.kotlin.multiplatform")
//    kotlin("plugin.serialization")
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
        }
        jvmMain.dependencies {
            implementation("com.google.devtools.ksp:symbol-processing-api:+")
        }
    }
}
