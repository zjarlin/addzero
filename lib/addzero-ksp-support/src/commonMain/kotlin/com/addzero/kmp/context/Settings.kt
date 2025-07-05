package com.addzero.kmp.context

data class Settings(
    val dbType: String,
    val idType: String,
    val id: String,
    val createBy: String,
    val updateBy: String,
    val createTime: String,
    val updateTime: String,
    val skipExistsFiles: String = "false", // 是否跳过已存在的文件
    val sharedSourceDir: String,
    val isomorphicPackageName: String,
    val isomorphicClassSuffix: String,
    val formPackageName: String,
    val apiClientPackageName: String = "com.addzero.kmp.generated.api"
)