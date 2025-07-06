// lib/addzero-ksp-support/src/commonMain/kotlin/com/addzero/kmp/util/BeanUtil.kt
package com.addzero.kmp.util

import com.addzero.kmp.context.Settings

object BeanUtil {
    fun Settings.toMap(): Map<String, String> = mapOf(
        "dbType" to dbType,
        "idType" to idType,
        "id" to id,
        "createBy" to createBy,
        "updateBy" to updateBy,
        "createTime" to createTime,
        "updateTime" to updateTime,
        "skipExistsFiles" to skipExistsFiles,
        "sharedSourceDir" to sharedSourceDir,
        "isomorphicPackageName" to isomorphicPackageName,
        "isomorphicClassSuffix" to isomorphicClassSuffix,
        "formPackageName" to formPackageName,
        "apiClientPackageName" to apiClientPackageName
    )

    fun mapToBean(map: Map<String, String>): Settings = Settings(
        dbType = map["dbType"] ?: "",
        idType = map["idType"] ?: "",
        id = map["id"] ?: "",
        createBy = map["createBy"] ?: "",
        updateBy = map["updateBy"] ?: "",
        createTime = map["createTime"] ?: "",
        updateTime = map["updateTime"] ?: "",
        skipExistsFiles = map["skipExistsFiles"] ?: "",
        sharedSourceDir = map["sharedSourceDir"] ?: "",
        isomorphicPackageName = map["isomorphicPackageName"] ?: "",
        isomorphicClassSuffix = map["isomorphicClassSuffix"] ?: "",
        formPackageName = map["formPackageName"] ?: "",
        apiClientPackageName = map["apiClientPackageName"] ?: ""
    )
}