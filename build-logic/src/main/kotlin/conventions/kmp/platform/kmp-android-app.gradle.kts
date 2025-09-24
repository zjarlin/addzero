@file:OptIn(ExperimentalWasmDsl::class)

import BuildSettings.PACKAGE_NAME
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

plugins {
    id("com.android.application")
    id("kmp-compose-common")
}
kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jdk.get()))
        }
    }


    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
    }
}

android {
    namespace = PACKAGE_NAME
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        applicationId = BuildSettings.Android.ANDROID_APP_ID
        minSdk = libs.versions.android.minSdk.get().toInt()
        //noinspection OldTargetApi
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        val vname = libs.versions.android.biz.version.get()
        versionName = vname
        versionCode = vname.toDouble().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            pickFirsts += "META-INF/INDEX.LIST"
        }
    }
    buildTypes {
        getByName(BuildSettings.Android.BUILD_TYPE) {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        val toVersion = JavaVersion.toVersion(libs.versions.jdk.get())
        sourceCompatibility = toVersion
        targetCompatibility = toVersion
    }
}
dependencies {
    debugImplementation(compose.uiTooling)
}
