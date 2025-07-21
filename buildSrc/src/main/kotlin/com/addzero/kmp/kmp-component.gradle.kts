plugins {
    id("kmp")
    id("kmp-android-library")
    id("com.google.devtools.ksp")
}

val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()
dependencies {
    kspCommonMainMetadata(libs.koin.ksp.compiler)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.annotations)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)

        }
    }
}

// 修复 KSP 任务依赖问题
afterEvaluate {
    // 确保所有 Kotlin 编译任务都依赖于 KSP 生成
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        if (name != "kspCommonMainKotlinMetadata") {
            dependsOn("kspCommonMainKotlinMetadata")
        }
    }

    // 特别处理 Kotlin2JsCompile 任务（包括 WasmJs）
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>().configureEach {
        if (name != "kspCommonMainKotlinMetadata") {
            dependsOn("kspCommonMainKotlinMetadata")
        }
    }

    // 确保特定的编译任务依赖
    listOf(
        "compileKotlinDesktop",
        "compileKotlinWasmJs",
        "compileKotlinJs",
        "compileKotlinAndroid",
        "compileKotlinIosX64",
        "compileKotlinIosArm64",
        "compileKotlinIosSimulatorArm64"
    ).forEach { taskName ->
        tasks.findByName(taskName)?.dependsOn("kspCommonMainKotlinMetadata")
    }
}

