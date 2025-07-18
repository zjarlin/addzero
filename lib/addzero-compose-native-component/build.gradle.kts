plugins {
    id("kmp")
    id("kmp-android-library")
    id("com.google.devtools.ksp")
}

dependencies{
    kspCommonMainMetadata(libs.koin.ksp.compiler)
}

kotlin{
   sourceSets{
        commonMain.dependencies {
            implementation(projects.lib.addzeroTool)
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
    tasks.named("compileKotlinDesktop") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
    
    // 确保所有 Kotlin 编译任务都依赖于 KSP 生成
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        if (name != "kspCommonMainKotlinMetadata") {
            dependsOn("kspCommonMainKotlinMetadata")
        }
    }
}

