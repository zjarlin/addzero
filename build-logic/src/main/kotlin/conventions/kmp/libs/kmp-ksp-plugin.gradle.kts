plugins {
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.multiplatform")
}

kotlin {
    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
    }
}
