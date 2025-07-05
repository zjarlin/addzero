import org.babyfish.defIos
import org.babyfish.jimmer.Vars
import org.babyfish.jimmer.Versions
import org.babyfish.jimmer.Versions.javaVersion
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
     jvmTarget.set(JvmTarget.fromTarget(Versions.javaVersion))
        }
    }

    val defIos = defIos()


    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
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
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }
        commonTest.dependencies {
            implementation("org.jetbrains.kotlin:kotlin-test")
        }
    }
}

android {

    namespace = Vars.sharedNamespace
    compileSdk = Versions.androidCompileSdk
    compileOptions {
        val toVersion = JavaVersion.toVersion(javaVersion)
        sourceCompatibility = toVersion
        targetCompatibility = toVersion
    }
    defaultConfig {

        minSdk =Versions.androidMinSdk
    }

}
