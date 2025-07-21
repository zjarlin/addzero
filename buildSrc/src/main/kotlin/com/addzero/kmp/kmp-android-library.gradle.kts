import com.addzero.Vars
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
plugins {
    id("com.android.library")

//    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.multiplatform")
//    id("org.jetbrains.kotlin.plugin.compose")
//    kotlin("plugin.serialization")
}

kotlin {
    androidTarget {
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jdk.get()))

        }
    }

}

android {
    namespace = Vars.applicationNamespace

    compileSdk = libs.versions.android.compileSdk.get().toInt()

    compileOptions {
        val toVersion = JavaVersion.toVersion(libs.versions.jdk.get())
        sourceCompatibility = toVersion
        targetCompatibility = toVersion
    }

    defaultConfig {


        minSdk = libs.versions.android.minSdk.get().toInt()
        testOptions.targetSdk = libs.versions.android.compileSdk.get().toInt()

//        versionCode = findProperty("version").toString().toInt()
//        versionName = findProperty("version").toString()

    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

}





