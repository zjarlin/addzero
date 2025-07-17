import org.babyfish.defIos
import org.babyfish.doIos
import org.babyfish.jimmer.Vars
import org.babyfish.jimmer.Vars.Modules.COMPOSE_APP
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
plugins {
    id("com.android.application")

//    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.multiplatform")
//    id("org.jetbrains.kotlin.plugin.compose")
//    kotlin("plugin.serialization")
}

kotlin{
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
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

        applicationId = Vars.applicationId

        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.compileSdk.get().toInt()
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





