import com.google.devtools.ksp.gradle.KspAATask

plugins {
    id("kmp-app")
    id("kmp-koin")
    id("kmp-json-withtool")
//    alias(libs.plugins.composeHotReload)
    id("ksp4projectdir")
    id("kmp-ksp-plugin")
    id("addzero-component")
    id("kmp-config")
}


dependencies {
    kspCommonMainMetadata(libs.addzero.route.processor)
    kspCommonMainMetadata(libs.addzero.compose.props.processor)
    kspCommonMainMetadata(libs.site.addzero.ioc.processor.v20250924)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.sharedCompose)

            implementation("site.addzero:addzero-ioc-core:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-tool:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-network-starter:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-jimmer-model-lowquery:${libs.versions.addzeroLTSVersion.get()}")

            implementation("site.addzero:addzero-route-core:${libs.versions.addzeroLTSVersion.get()}")
            implementation("site.addzero:addzero-compose-props-annotations:${libs.versions.addzeroLTSVersion.get()}")

            implementation("site.addzero:addzero-entity2form-core:${libs.versions.addzeroLTSVersion.get()}")
            implementation(libs.filekit.compose)


            // 协程相关依赖
            implementation(libs.kotlinx.coroutines.core)
            //时间依赖
            implementation(libs.kotlinx.datetime)

            // 通用UI组件
            implementation(libs.multiplatform.markdown.renderer.m3)
            implementation(libs.navigation.compose)

            // 图片加载库现在由 klibs-component 模块提供
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            //拖拽库
            implementation(libs.compose.dnd)

            //富文本 see https://klibs.io/project/MohamedRejeb/compose-rich-editor
            implementation(libs.richeditor.compose)

            implementation(compose.materialIconsExtended)


        }

    }
}

// build.gradle.kts
//kotlin {
//    compilerOptions {
//        freeCompilerArgs.add("-Xcontext-parameters")
//    }
//}


// 为所有KSP任务添加对主KSP任务的依赖
tasks.withType<KspAATask>().configureEach {

    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
//        dependsOn(":lib:compose:addzero-compose-native-component-table-pro:kspCommonMainKotlinMetadata")
    }
    if (name != "kspKotlin") {
        dependsOn(":backend:model:kspKotlin")
        dependsOn(":backend:server:kspKotlin")
    }
}
