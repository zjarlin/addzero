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
//    kspCommonMainMetadata(libs.addzero.compose.props.processor)
    kspCommonMainMetadata(libs.addzero.ioc.processor)
}

kotlin {
    dependencies {
//      implementation(libs.bundles.addzero.component)
        implementation(libs.ktorfit.lib)
//   todo 研究这个kmp库     composehook
//        implementation("xyz.junerver.compose:hooks2:+")

        implementation(projects.shared)
        implementation(projects.sharedCompose)
        implementation(projects.lib.shadcnComposeComponent)
        implementation(projects.lib.youtrackSidebar)

        implementation(libs.addzero.ioc.core)
        implementation(libs.addzero.tool)
        implementation(libs.network.starter)
        implementation(libs.jimmer.model.lowquery)

        implementation(libs.route.core)
        implementation(libs.compose.props.annotations)

        implementation(libs.addzero.entity2form.core)
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
//tasks.withType<KspAATask>().configureEach {
//    val kspMetadataTask = tasks.findByName("kspCommonMainKotlinMetadata")
//    if (kspMetadataTask != null && name != "kspCommonMainKotlinMetadata") {
//        dependsOn(kspMetadataTask)
//    }
//    if (name != "kspKotlin") {
//        dependsOn(":backend:model:kspKotlin")
//        dependsOn(":backend:server:kspKotlin")
//    }
//}
