import org.babyfish.jimmer.Versions
import org.babyfish.jimmer.Versions.kotlinxCoroutinesVersion
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("kmp")
    id("com.google.devtools.ksp")

//    alias(libs.plugins.compose.hot.reload)
}

dependencies {
    kspCommonMainMetadata(projects.lib.addzeroRouteProcessor)
    kspCommonMainMetadata(projects.lib.addzeroComposePropsProcessor)
    kspCommonMainMetadata("io.insert-koin:koin-ksp-compiler:${Versions.koinVersion}")

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
            implementation("io.github.vinceglb:filekit-compose:${Versions.fileKitVersion}")
            // Enables FileKit with Composable utilities
//            implementation("io.github.vinceglb:filekit-compose:+")


            //日志库
//            implementation("co.touchlab:kermit:+") // 基础库
            // 添加SLF4J实现
//            implementation("org.slf4j:slf4j-simple:+")

            //注解处理器核心包
//            implementation(projects.addzeroKspCore)
            implementation(projects.lib.addzeroRouteCore)
            implementation(projects.lib.addzeroComposePropsAnnotations)

            // KMP 兼容的生命周期管理不能引入这个.启动skia报错

//            implementation("org.jetbrains.androidx.lifecycle:lifecycle-viewmodel-compose:+")
            // 添加 Skia 依赖
//            implementation("org.jetbrains.skia:skia:+")
            // 协程相关依赖
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
            //时间依赖
            implementation(libs.kotlinx.datetime)

            // 通用UI组件
            implementation("com.mikepenz:multiplatform-markdown-renderer-m3:0.36.0-b02")
            implementation(libs.navigation.compose)

            //图片加载库
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            //拖拽库

            implementation("com.mohamedrejeb.dnd:compose-dnd:0.3.0")

            //富文本 see https://klibs.io/project/MohamedRejeb/compose-rich-editor
            implementation("com.mohamedrejeb.richeditor:richeditor-compose:1.0.0-rc11")
//            fluent-ui
//            implementation("com.konyaco:fluent:0.0.1-dev.8")
//            implementation("com.konyaco:fluent-icons-extended:0.0.1-dev.8") // If you want to use full fluent icons.


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


           implementation(project.dependencies.platform("io.insert-koin:koin-bom:4.1.0"))

            implementation("io.insert-koin:koin-annotations:2.1.0")
//            implementation("io.insert-koin:koin-core:4.1.0")
//            implementation("io.insert-koin:koin-compose:4.1.0")
//            implementation("io.insert-koin:koin-compose-viewmodel:4.1.0")
//            implementation("io.insert-koin:koin-compose-viewmodel-navigation:4.1.0")


            implementation("io.insert-koin:koin-core")
            implementation("io.insert-koin:koin-compose")
            implementation("io.insert-koin:koin-compose-viewmodel")
            implementation("io.insert-koin:koin-compose-viewmodel-navigation")


        }
    }
}
