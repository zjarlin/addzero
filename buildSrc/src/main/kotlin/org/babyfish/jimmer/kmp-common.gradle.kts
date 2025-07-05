//import gradle.kotlin.dsl.accessors._b55130a681ce14eb1683c120e489db15.android
//import gradle.kotlin.dsl.accessors._b55130a681ce14eb1683c120e489db15.compose
//import gradle.kotlin.dsl.accessors._b55130a681ce14eb1683c120e489db15.debugImplementation
//import gradle.kotlin.dsl.accessors._b55130a681ce14eb1683c120e489db15.desktop
//import gradle.kotlin.dsl.accessors._b55130a681ce14eb1683c120e489db15.kotlin
//import gradle.kotlin.dsl.accessors._b55130a681ce14eb1683c120e489db15.sourceSets
//import org.babyfish.jimmer.P.myVersion
//import org.babyfish.jimmer.defIos
//import org.babyfish.jimmer.doIos
//import org.jetbrains.compose.desktop.application.dsl.TargetFormat
//import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
//import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
//import org.jetbrains.kotlin.gradle.dsl.JvmTarget
//import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
//import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
//import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//
//plugins {
//    id("org.jetbrains.kotlin.multiplatform")
//}
//
//
//
//
//
//
//kotlin {
//    androidTarget {
//        @OptIn(ExperimentalKotlinGradlePluginApi::class)
//        compilerOptions {
//            jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
//            jvmTarget.set(JvmTarget.JVM_11)
//        }
//    }
//
//    val defIos = defIos()
//
//
//    jvm("desktop")
//
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        outputModuleName.set("composeApp")
//        browser {
//            val rootDirPath = project.rootDir.path
//            val projectDirPath = project.projectDir.path
//            commonWebpackConfig {
//                outputFileName = "composeApp.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(rootDirPath)
//                        add(projectDirPath)
//                    }
//                }
//            }
//        }
//        binaries.executable()
//    }
//
//    sourceSets {
//        val desktopMain by getting
//
//        androidMain.dependencies {
//            implementation(compose.preview)
//            implementation("androidx.activity:activity-compose:1.10.1")
//        }
//        commonMain.dependencies {
//            // Enables FileKit with Composable utilities
//            implementation("io.github.vinceglb:filekit-compose:0.8.8")
//
//            implementation(compose.materialIconsExtended)
//            implementation(compose.runtime)
//            implementation(compose.foundation)
//            implementation(compose.material3)
//            implementation(compose.ui)
//            implementation(compose.components.resources)
//            implementation(compose.components.uiToolingPreview)
//            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel:2.9.1")
//            implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.9.1")
//        }
//        commonTest.dependencies {
//            implementation("org.jetbrains.kotlin:kotlin-test")
//        }
//        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing")
//        }
//    }
//}
//
//android {
//    namespace = "compileOptions.addzero.kmp.component"
//    compileSdk = 35
//
//    defaultConfig {
//        applicationId = "compileOptions.addzero.kmp"
//        minSdk = 24
//        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"
//    }
//
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = false
//        }
//    }
//
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }
//}
//
//dependencies {
//    debugImplementation(compose.uiTooling)
//}
//
//compose.desktop {
//    application {
//        mainClass = "compileOptions.addzero.kmp.MainKt"
//
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "compileOptions.addzero.kmp"
//            packageVersion = "1.0.0"
//        }
//    }
//}
