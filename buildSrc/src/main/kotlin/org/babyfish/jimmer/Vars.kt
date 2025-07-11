package org.babyfish.jimmer

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
    const val outputModuleName = "composeApp"

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

}

