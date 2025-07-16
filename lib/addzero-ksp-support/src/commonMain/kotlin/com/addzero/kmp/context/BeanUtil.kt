package com.addzero.kmp.context

object BeanUtil {
    fun Settings.toMap(): Map<String, String> = mapOf(
        // 数据库相关配置
        "dbType" to dbType,
        "idType" to idType,
        "id" to id,
        "createBy" to createBy,
        "updateBy" to updateBy,
        "createTime" to createTime,
        "updateTime" to updateTime,

        "composeSourceDir" to composeSourceDir,
        "composeBuildDir" to composeBuildDir,

        "sharedSourceDir" to sharedSourceDir,
        "sharedBuildDir" to sharedBuildDir,

        "serverSourceDir" to serverSourceDir,
        "serverBuildDir" to serverBuildDir,


        "modelSourceDir" to modelSourceDir,
        "modelBuildDir" to modelBuildDir,
        "modelPackageName" to modelPackageName,

        // 包名配置（小驼峰命名）
        "isomorphicPackageName" to isomorphicPackageName,
        "formPackageName" to formPackageName,
        "enumOutputPackage" to enumOutputPackage,
        "apiClientPackageName" to apiClientPackageName,

        // 其他配置
        "isomorphicClassSuffix" to isomorphicClassSuffix,

        // JDBC 配置
        "jdbcUrl" to jdbcUrl,
        "jdbcUsername" to jdbcUsername,
        "jdbcPassword" to jdbcPassword,
        "jdbcSchema" to jdbcSchema,
        "jdbcDriver" to jdbcDriver,

        // 字典表配置
        "dictTableName" to dictTableName,
        "dictIdColumn" to dictIdColumn,
        "dictCodeColumn" to dictCodeColumn,
        "dictNameColumn" to dictNameColumn,
        "dictItemTableName" to dictItemTableName,
        "dictItemForeignKeyColumn" to dictItemForeignKeyColumn,
        "dictItemCodeColumn" to dictItemCodeColumn,
        "dictItemNameColumn" to dictItemNameColumn
    )

    fun mapToBean(map: Map<String, String>): Settings = Settings(
        // 数据库相关配置
        dbType = map["dbType"] ?: "",
        idType = map["idType"] ?: "",
        id = map["id"] ?: "",
        createBy = map["createBy"] ?: "",
        updateBy = map["updateBy"] ?: "",
        createTime = map["createTime"] ?: "",
        updateTime = map["updateTime"] ?: "",

        composeSourceDir = map["composeSourceDir"] ?: "",
        composeBuildDir = map["composeBuildDir"] ?: "",
        sharedSourceDir = map["sharedSourceDir"] ?: "",
        sharedBuildDir = map["sharedBuildDir"] ?: "",

        serverSourceDir = map["serverSourceDir"] ?: "",
        serverBuildDir = map["serverBuildDir"] ?: "",

        modelSourceDir = map["modelSourceDir"] ?: "",
        modelBuildDir = map["modelBuildDir"] ?: "",
        modelPackageName = map["modelPackageName"] ?: "",


        // 包名配置（小驼峰命名）
        isomorphicPackageName = map["isomorphicPackageName"] ?: "com.addzero.kmp.generated.isomorphic",
        formPackageName = map["formPackageName"] ?: "com.addzero.kmp.generated.forms",
        enumOutputPackage = map["enumOutputPackage"] ?: "com.addzero.kmp.generated.enums",
        apiClientPackageName = map["apiClientPackageName"] ?: "com.addzero.kmp.generated.api",

        // 其他配置
        isomorphicClassSuffix = map["isomorphicClassSuffix"] ?: "Iso",

        // JDBC 配置
        jdbcUrl = map["jdbcUrl"] ?: "",
        jdbcUsername = map["jdbcUsername"] ?: "",
        jdbcPassword = map["jdbcPassword"] ?: "",
        jdbcSchema = map["jdbcSchema"] ?: "",
        jdbcDriver = map["jdbcDriver"] ?: "",

        // 字典表配置
        dictTableName = map["dictTableName"] ?: "sys_dict",
        dictIdColumn = map["dictIdColumn"] ?: "id",
        dictCodeColumn = map["dictCodeColumn"] ?: "dict_code",
        dictNameColumn = map["dictNameColumn"] ?: "dict_name",
        dictItemTableName = map["dictItemTableName"] ?: "sys_dict_item",
        dictItemForeignKeyColumn = map["dictItemForeignKeyColumn"] ?: "dict_id",
        dictItemCodeColumn = map["dictItemCodeColumn"] ?: "item_value",
        dictItemNameColumn = map["dictItemNameColumn"] ?: "item_text"
    )
}