plugins {
    id("org.jetbrains.kotlin.multiplatform")
//    kotlin("plugin.serialization")
}
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {

//            implementation(projects.lib.addzeroKspSupport)


//            implementation(project(:addzero-ksp))
        }
        jvmMain.dependencies {
            implementation(libs.ksp.symbol.processing.api)
        }
    }
}
