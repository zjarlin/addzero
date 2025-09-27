package site.addzero.context

import kotlinx.serialization.Serializable
import site.addzero.util.str.withPkg


@Serializable
data class Settings(
    val baseEntityPackage: String = "",
    // 数据库相关配置
    val dbType: String = "",
    val idType: String = "",
    val id: String = "",
    val createBy: String = "",
    val updateBy: String = "",
    val createTime: String = "",
    val updateTime: String = "",
    val composeAppSourceDir: String = "",
    val composeAppBuildDir: String = "",
    val sharedSourceDir: String = "",
    val sharedBuildDir: String = "",
    val backendServerSourceDir: String = "",
    val backendServerBuildDir: String = "",
    val sharedComposeSourceDir: String = "",
    val sharedComposeBuildDir: String = "",
    val backendModelSourceDir: String = "",
    val backendModelBuildDir: String = "",
    val modelPackageName: String = "",
    // 包名配置（小驼峰命名）
    val isomorphicPackageName: String = "site.addzero.generated.isomorphic",
    val formPackageName: String = "site.addzero.generated.forms",
    val enumOutputPackage: String = "site.addzero.generated.enums",
    val apiClientPackageName: String = "site.addzero.generated.api",

    // 其他配置
    val isomorphicClassSuffix: String = "Iso",

    // JDBC 配置
    val jdbcUrl: String = "",
    val jdbcUsername: String = "",
    val jdbcPassword: String = "",
    val jdbcSchema: String = "",
    val jdbcDriver: String = "",

    // 字典表配置
    val dictTableName: String = "sys_dict",
    val dictIdColumn: String = "id",
    val dictCodeColumn: String = "dict_code",
    val dictNameColumn: String = "dict_name",
    val dictItemTableName: String = "sys_dict_item",
    val dictItemForeignKeyColumn: String = "dict_id",
    val dictItemCodeColumn: String = "item_value",
    val dictItemNameColumn: String = "item_text",
    val controllerOutPackage: String = "site.addzero.web.modules.controller"
) {

    /**
     * 同构体输出目录（shared 源码目录）
     */
    val isomorphicOutputDir: String = sharedSourceDir.withPkg(isomorphicPackageName)

    /**
     * 表单输出目录（composeApp 源码目录）
     */
    val formOutputDir: String = sharedComposeSourceDir.withPkg(formPackageName)

    /**
     * 枚举输出目录（shared 编译目录）
     */
    val enumOutputDir: String = sharedSourceDir.withPkg(enumOutputPackage)

    /**
     * API客户端输出目录（shared 源码目录）
     */
    val apiClientOutputDir: String = sharedComposeSourceDir.withPkg(apiClientPackageName)

    /**
     * 从jdbc生成的模型输出目录
     */
    val modelOutputDir: String = backendModelSourceDir.withPkg(modelPackageName)


}
