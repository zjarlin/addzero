import org.babyfish.defIos
import org.babyfish.doIos
import org.babyfish.jimmer.Vars
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
val javaVersion = libs.versions.jdk.get()

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")

}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class) compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(javaVersion))
        }
    }

    val defIos = defIos()
    doIos(defIos)


    jvm()

    @OptIn(ExperimentalWasmDsl::class) wasmJs {
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
    }



    sourceSets {
        //生成的代码
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")

        }
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
            // put your Multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {

    namespace = Vars.sharedNamespace
    compileSdk =   libs.versions.android.compileSdk.get().toInt()



    compileOptions {
        val toVersion = JavaVersion.toVersion(javaVersion)
        sourceCompatibility = toVersion
        targetCompatibility = toVersion
    }
    defaultConfig {

        minSdk = libs.versions.android.minSdk.get().toInt()
    }


}

//tasks.withType<KotlinCompilationTask<*>>().all {
//    val kspCommonMainKotlinMetadata = "kspCommonMainKotlinMetadata"
//    if (name != kspCommonMainKotlinMetadata) {
//        dependsOn(kspCommonMainKotlinMetadata)
//    }
//}
