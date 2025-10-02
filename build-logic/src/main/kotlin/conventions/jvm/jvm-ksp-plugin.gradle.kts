import site.addzero.gradle.constant.Disposable.KSP_BUILD_DIR_JVM

plugins {
    id("com.google.devtools.ksp")
    id("kotlin-convention")
}

kotlin {

    sourceSets {
        main {
            kotlin.srcDir(KSP_BUILD_DIR_JVM)
        }
    }

}
