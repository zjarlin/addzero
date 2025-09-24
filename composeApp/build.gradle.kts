import com.google.devtools.ksp.gradle.KspAATask

plugins {
    id("kmp-app")
    id("kmp-koin")
    id("kmp-json-withtool")
//    alias(libs.plugins.composeHotReload)
    id("ksp4projectdir")
    id("kmp-ksp-plugin")
//    id("addzero-component")
    id("kmp-config")
}


dependencies {
    kspCommonMainMetadata("site.addzero:addzero-route-processor:+")
    kspCommonMainMetadata("site.addzero:addzero-compose-props-processor:+")
    kspCommonMainMetadata("site.addzero:addzero-ioc-processor:+")
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("site.addzero:addzero-ioc-core:+")
            implementation(projects.shared)
            implementation(projects.sharedCompose)

            implementation("site.addzero:addzero-tool:+")


            implementation("site.addzero:addzero-network-starter:+")
            implementation("site.addzero:addzero-jimmer-model-lowquery:+")

            // 组件库依赖
            implementation("site.addzero:addzero-compose-native-component:+")
            implementation("site.addzero:addzero-compose-native-component-assist:+")
            implementation("site.addzero:addzero-compose-native-component-button:+")
            implementation("site.addzero:addzero-compose-native-component-card:+")
            implementation("site.addzero:addzero-compose-native-component-form:+")
            implementation("site.addzero:addzero-compose-native-component-high-level:+")
            implementation("site.addzero:addzero-compose-native-component-hook:+")
            implementation("site.addzero:addzero-compose-native-component-searchbar:+")
            implementation("site.addzero:addzero-compose-native-component-select:+")
            implementation("site.addzero:addzero-compose-native-component-table:+")
            implementation("site.addzero:addzero-compose-native-component-table-core:+")
            implementation("site.addzero:addzero-compose-native-component-table-pro:+")
            implementation("site.addzero:addzero-compose-native-component-tree:+")


            implementation("site.addzero:addzero-compose-native-component-hook:+")

            // 原来的 FileKit 依赖现在由 klibs-component 模块提供
            implementation(libs.filekit.compose)

            //自定义三方组件库
            implementation("site.addzero:addzero-compose-klibs-component:+")


            //注解处理器核心包
            implementation("site.addzero:addzero-route-core:+")
            implementation("site.addzero:addzero-compose-props-annotations:+")

            implementation("site.addzero:addzero-entity2form-core:+")

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
