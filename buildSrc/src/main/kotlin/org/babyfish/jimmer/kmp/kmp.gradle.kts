import org.babyfish.defIos
import org.babyfish.doIos
import org.babyfish.jimmer.Vars
import org.babyfish.jimmer.Vars.myGroup
import org.babyfish.jimmer.Vars.myVersion
import org.babyfish.jimmer.Versions
import org.babyfish.jimmer.Versions.androidTargetSdk
import org.babyfish.jimmer.Versions.javaVersion
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}

//val libs = the<LibrariesForLibs>()

//group = myGroup
//version = myVersion

val androidxLifecycle = Versions.androidxLifecycle
kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(Versions.javaVersion))

        }
    }
    val defIos = defIos()

    doIos(defIos)
    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {

        outputModuleName.set(Vars.outputModuleName)
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "${Vars.outputModuleName}.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {

        //生成的代码
        commonMain{
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }


        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation("androidx.activity:activity-compose:${Versions.androidxActivity}")
        }
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinxSerializationVersion}")

            implementation(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:$androidxLifecycle")
            implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:$androidxLifecycle")
        }
        commonTest.dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test")
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing")
        }
    }
}

android {

    namespace = Vars.applicationNamespace

    compileSdk = Versions.androidCompileSdk

    compileOptions {
        val toVersion = JavaVersion.toVersion(javaVersion)
        sourceCompatibility = toVersion
        targetCompatibility = toVersion
    }

    defaultConfig {

        applicationId = Vars.applicationId

        minSdk = Versions.androidMinSdk
        targetSdk = androidTargetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName
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

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {

        mainClass = Vars.mainClass

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName =Vars.packageName
            packageVersion ="1.0.0"
        }
    }
}
