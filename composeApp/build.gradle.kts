import com.google.devtools.ksp.gradle.KspAATask

plugins {
    id("site.addzero.buildlogic.kmp.cmp-app")
    id("site.addzero.buildlogic.kmp.kmp-koin")
    id("site.addzero.buildlogic.kmp.kmp-json-withtool")
//    alias(libs.plugins.composeHotReload)
    id("site.addzero.buildlogic.ksp.ksp4projectdir")
    id("site.addzero.buildlogic.kmp.kmp-ksp-plugin")
//    id("site.addzero.buildlogic.conventions.addzero-component")
//    id("kmp-config")
//    id("kmp-config-ui")
//    id("kmp-config-link")
}


dependencies {
    kspCommonMainMetadata(libs.addzero.route.processor)
    // todo 这个ksp弃用了,打包参数的等kotlin更新函数整体传参和展开运算符才能二次封装,不然打包整体参数无意义,二次封装用起来还是割裂
//    kspCommonMainMetadata(libs.addzero.compose.props.processor)
    kspCommonMainMetadata(libs.addzero.ioc.processor)
}

kotlin {
    dependencies {
      implementation(libs.bundles.addzero.component)
        implementation(libs.ktorfit.lib)
//   todo 研究这个kmp库     composehook
//        implementation("xyz.junerver.compose:hooks2:+")

        implementation(projects.shared)
        implementation(projects.sharedCompose)
        implementation(projects.lib.shadcnComposeComponent)
        implementation(projects.lib.youtrackSidebar)

        implementation("site.addzero:ioc-core:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:tool:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:network-starter:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:jimmer-model-lowquery:${libs.versions.addzeroLTSVersion.get()}")

        implementation("site.addzero:route-core:${libs.versions.addzeroLTSVersion.get()}")
        implementation("site.addzero:compose-props-annotations:${libs.versions.addzeroLTSVersion.get()}")

        implementation("site.addzero:entity2form-core:${libs.versions.addzeroLTSVersion.get()}")
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
