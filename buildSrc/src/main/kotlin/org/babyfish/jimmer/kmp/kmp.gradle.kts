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
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization")
}
//val libs = the<LibrariesForLibs>()


//group = myGroup
//version = myVersion

kotlin {
    val defIos = defIos()

    doIos(defIos)
    jvm("desktop")

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {

        outputModuleName.set(COMPOSE_APP)
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "${COMPOSE_APP}.js"
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
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
        }


        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)

            implementation(compose.materialIconsExtended)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

//android {
//
//    namespace = Vars.applicationNamespace
//
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//
//    compileOptions {
//        val toVersion = JavaVersion.toVersion(libs.versions.jdk.get())
//        sourceCompatibility = toVersion
//        targetCompatibility = toVersion
//    }
//
//    defaultConfig {
//
//        applicationId = Vars.applicationId
//
//        minSdk = libs.versions.android.minSdk.get().toInt()
//        targetSdk = libs.versions.android.compileSdk.get().toInt()
////        versionCode = findProperty("version").toString().toInt()
////        versionName = findProperty("version").toString()
//
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
//}


compose.desktop {
    application {

        mainClass = Vars.mainClass

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = Vars.packageName
            packageVersion = "1.0.0"
        }
    }
}



// 确保 KSP 任务的执行顺序：先执行 shared 模块的 KSP，再执行 backend 模块的 KSP
//tasks.configureEach {
//    when (name) {
//        "kspKotlin" -> {
//            // backend 模块的 KSP 任务依赖 shared 模块的 KSP 任务
//            dependsOn(":shared:kspCommonMainKotlinMetadata")
//        }
//        "compileKotlin" -> {
//            // Kotlin 编译任务依赖 KSP 任务
//            dependsOn("kspKotlin")
//        }
//    }
//}


//tasks.withType<KotlinCompilationTask<*>>().all {
//    val kspCommonMainKotlinMetadata = "kspCommonMainKotlinMetadata"
//
//    // 只处理 shared 模块内部的依赖，避免跨模块循环依赖
//    if (name != kspCommonMainKotlinMetadata &&
//        project.tasks.findByName(kspCommonMainKotlinMetadata) != null) {
//        dependsOn(kspCommonMainKotlinMetadata)
//    }
//}

