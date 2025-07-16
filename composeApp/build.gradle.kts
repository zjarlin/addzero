import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("kmp")
    id("com.google.devtools.ksp")

//    alias(libs.plugins.compose.hot.reload)
}

dependencies {
    kspCommonMainMetadata(projects.lib.addzeroRouteProcessor)
    kspCommonMainMetadata(projects.lib.addzeroComposePropsProcessor)
    kspCommonMainMetadata(libs.koin.ksp.compiler)
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.lib.addzeroTool)

            // 组件库依赖
//            implementation(projects.lib.addzeroComposeNativeComponent)
//            implementation(projects.lib.addzeroComposeKlibsComponent)

            // 原来的 FileKit 依赖现在由 klibs-component 模块提供
            implementation(libs.filekit.compose) // 移除，由组件模块提供


            //日志库
//            implementation("co.touchlab:kermit:+") // 基础库
            // 添加SLF4J实现
            implementation("org.slf4j:slf4j-simple:2.1.0-alpha1")

            //注解处理器核心包
//            implementation(projects.addzeroKspCore)
            implementation(projects.lib.addzeroRouteCore)
            implementation(projects.lib.addzeroComposePropsAnnotations)
//            implementation(projects.lib.addzeroEntity2formCore)

            // KMP 兼容的生命周期管理不能引入这个.启动skia报错

//            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:+")
            // 添加 Skia 依赖
//            implementation("org.jetbrains.skia:skia:+")
            // 协程相关依赖
            implementation(libs.kotlinx.coroutines.core)
            //时间依赖
            implementation(libs.kotlinx.datetime)

            // 通用UI组件
            implementation(libs.multiplatform.markdown.renderer.m3)
            implementation(libs.navigation.compose)

            // 图片加载库现在由 klibs-component 模块提供
            implementation(libs.coil.compose) // 移除，由组件模块提供
            implementation(libs.coil.network.ktor3) // 移除，由组件模块提供

            //拖拽库
            implementation(libs.compose.dnd)

            //富文本 see https://klibs.io/project/MohamedRejeb/compose-rich-editor
            implementation(libs.richeditor.compose)
//            fluent-ui
//            implementation(libs.fluent)
//            implementation(libs.fluent.icons.extended) // If you want to use full fluent icons.


            // Provides the core functions of Sketch as well as singletons and extension
// functions that rely on singleton implementations
//            implementation("io.github.panpf.sketch4:sketch-compose:+")

// Provides the ability to load network images
//            implementation("io.github.panpf.sketch4:sketch-http:+")


            implementation(compose.materialIconsExtended)

//            implementation("io.github.ltttttttttttt:LazyPeopleHttp-lib:$lazyhttpV")


            // 网络和依赖注入现在由 klibs-component 模块提供
            implementation(libs.ktorfit.lib) // 移除，由组件模块提供
            implementation(project.dependencies.platform(libs.koin.bom)) // 移除，由组件模块提供
            implementation(libs.koin.annotations) // 移除，由组件模块提供
            implementation(libs.koin.core) // 移除，由组件模块提供
            implementation(libs.koin.compose) // 移除，由组件模块提供
            implementation(libs.koin.compose.viewmodel) // 移除，由组件模块提供
            implementation(libs.koin.compose.viewmodel.navigation) // 移除，由组件模块提供


        }
    }
}


// 确保所有 Kotlin 编译任务都依赖于 shared 模块的 KSP 元数据生成
tasks.withType<KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn(":backend:model:kspKotlin")
        dependsOn(":backend:server:kspKotlin")
        dependsOn(":shared:kspCommonMainKotlinMetadata")
        dependsOn(":composeApp:kspCommonMainKotlinMetadata")
    }
}



