package org.babyfish.jimmer

import org.babyfish.jimmer.Vars.Modules.COMPOSE_APP
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Vars {
    const val myGroup = "io.gitee.zjarlin"
    val authorName = myGroup.split(".").last()
    val email = "$authorName@outlook.com"
    val myVersion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd.HHmm"))
    val projName = "addzero"

    const val projectDescription = "jimmer kmp"

    // 应用相关常量
    const val applicationId = "compileOptions.addzero.kmp"
    const val applicationNamespace = "compileOptions.addzero.kmp.component"
    const val sharedNamespace = "com.addzero.kmp.shared"

    // 应用主类

    //    const val packageName = "compileOptions.addzero.kmp"
//    const val mainClass = "compileOptions.addzero.kmp.MainKt"
    // 包名和版本
    const val packageName = "com.addzero.kmp"

    const val mainClass = "${packageName}.MainKt"

    // 发布配置
    const val gitUrl = "https://gitee.com/zjarlin/addzero.git"
    const val licenseName = "The Apache License, Version 2.0"
    const val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"

    // 计算属性
    val gitBaseUrl = gitUrl.removeSuffix(".git")
    val gitRepoPath = gitUrl.substringAfter("://").substringAfter("/")
    val gitHost = gitUrl.substringAfter("://").substringBefore("/")
    val gitRepoName = gitRepoPath.removeSuffix(".git")



    val commonMainKspBuildMetaDataDir = "build/generated/ksp/metadata/commonMain/kotlin"
    val jvmMainKspBuildMetaDataDir = "build/generated/ksp/main/kotlin"

    val commonMainSourceDir = "src/commonMain/kotlin"
    val jvmMainSourceDir = "src/main/kotlin"



    // 项目模块名称常量
    object Modules {
        // 主要模块
        const val BACKEND = "backend"
        const val COMPOSE_APP = "composeApp"
        const val SHARED = "shared"
        const val LIB = "lib"

        // KSP 支持模块
        val KSP_SUPPORT = listOf(
            "addzero-ksp-support",
            "addzero-ksp-support-jdbc"
        )

        // 实体分析与代码生成
        val ENTITY_ANALYSIS = listOf(
            "addzero-entity2analysed-support",
            "addzero-entity2iso-processor",
            "addzero-entity2form-processor",
            "addzero-entity2form-core"
        )

        // JDBC 相关处理器
        val JDBC_PROCESSORS = listOf(
            "addzero-jdbc2controller-processor",
            "addzero-jdbc2enum-processor"
        )

        // API 相关处理器
        val API_PROCESSORS = listOf(
            "addzero-controller2api-processor",
            "addzero-apiprovider-processor"
        )

        // 路由模块
        val ROUTE_MODULES = listOf(
            "addzero-route-processor",
            "addzero-route-core"
        )

        // Compose 相关模块
        val COMPOSE_MODULES = listOf(
            "addzero-compose-props-annotations",
            "addzero-compose-props-processor"
        )

        // 工具模块
        val TOOL_MODULES = listOf(
            "addzero-tool"
        )

        // 网络模块（暂时注释）
        val NETWORK_MODULES = listOf(
            "addzero-network-starter"
        )
    }

}

