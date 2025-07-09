plugins {
    id("org.jetbrains.kotlin.multiplatform")
//    kotlin("plugin.serialization")
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {

//            implementation(projects.lib.addzeroKspSupport)


//            implementation(project(:addzero-ksp))
        }
        jvmMain.dependencies {
            implementation("com.google.devtools.ksp:symbol-processing-api:+")
        }
    }
}
