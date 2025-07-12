import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("kmp")
    id("com.google.devtools.ksp")

//    alias(libs.plugins.compose.hot.reload)
}

dependencies {
    kspCommonMainMetadata(projects.lib.addzeroRouteProcessor)
    kspCommonMainMetadata(projects.lib.addzeroComposePropsProcessor)
    kspCommonMainMetadata(projects.lib.addzeroEntity2formProcessor)
    kspCommonMainMetadata(libs.koin.ksp.compiler)

}


//dependencies {
//    with(projects.lib.addzeroRouteProcessor) {
//        add("kspCommonMainMetadata", this)
//    }
//    //koin依赖注入
//    with("io.insert-koin:koin-ksp-compiler:+") {
//        add("kspCommonMainMetadata", this)
//    }
//
////    with("io.github.ltttttttttttt:LazyPeopleHttp:${lazyhttpV}") {
////        add("kspCommonMainMetadata", this)
////    }
//}

ksp {
//    arg("generated.dir", defaultKspGenDir)
//    arg("generatedsrc.dir", "src/commonMain/kotlin")
//    arg("project.dir", rootProject.projectDir.absolutePath)
    // 或者使用更精确的路径获取方式
    arg(
        "module.main.src.dir",
        project.extensions.getByType<KotlinMultiplatformExtension>().sourceSets.getByName("commonMain").kotlin.srcDirs.first().absolutePath
    )


    // JDBC元数据处理器配置
    arg("jdbcUrl", "jdbc:postgresql://localhost:15432/postgres")
    arg("jdbcUsername", "postgres")
    arg("jdbcPassword", "postgres")
    arg("jdbcSchema", "public")
    arg("jdbcDriver", "org.postgresql.Driver")
    arg("outputPackage", "com.addzero.kmp.jdbc.meta")
    // 可选：指定要包含的表（逗号分隔）
    // arg("includeTables", "user_info,product")
    // 可选：指定要排除的表（逗号分隔）
    arg("excludeTables", "flyway_schema_history,vector_store")


}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.lib.addzeroTool)
            // Enables FileKit with Composable utilities
            implementation(libs.filekit.compose)
            // Enables FileKit with Composable utilities
//            implementation(libs.filekit.compose)


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

            //图片加载库
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

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


            implementation(libs.ktorfit.lib)

            // Koin 注解处理
            // Koin Annotations


            implementation(project.dependencies.platform(libs.koin.bom))

            implementation(libs.koin.annotations)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)


            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.compose.viewmodel.navigation)


        }
    }
}
