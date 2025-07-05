
plugins {
    id("org.jetbrains.kotlin.multiplatform")
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
