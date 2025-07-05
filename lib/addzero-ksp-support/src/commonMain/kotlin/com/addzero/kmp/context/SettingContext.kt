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

val Settings.isomorphicShareOutPutDir: String
    get() {
        val settings = SettingContext.settings
        val replace = settings.isomorphicPackageName
            .replace(".", "/")
        return "${settings.sharedSourceDir}/$replace"
    }


val Settings.formShareOutPutDir: String
    get() {
        val settings = SettingContext.settings
        return "${settings.sharedSourceDir}/${
            settings.formPackageName
                .replace(".", "/")
        }"
    }

/**
 * API客户端输出目录
 */
val Settings.apiclientOutPutDir: String
    get() {
        val settings = SettingContext.settings
        return "${settings.sharedSourceDir}/${
            settings.apiClientPackageName
                .replace(".", "/")
        }"
    }


/**
 * 全局配置上下文
 */
object SettingContext {

    lateinit var settings: Settings

    val context: Settings
        get() = settings

    /**
     * 初始化配置
     * @param op gradle/ksp传入的options
     */
    fun initialize(op: Map<String, String>) {

        settings = Settings(
            dbType = op["dbType"] ?: throw IllegalArgumentException("ksp option dbType required"),
            idType = op["idType"] ?: throw IllegalArgumentException("ksp option idType required"),
            id = op["id"] ?: throw IllegalArgumentException("ksp option id required"),
            createBy = op["createBy"] ?: throw IllegalArgumentException("ksp option createBy required"),
            updateBy = op["updateBy"] ?: throw IllegalArgumentException("ksp option updateBy required"),
            createTime = op["createTime"] ?: throw IllegalArgumentException("ksp option createTime required"),
            updateTime = op["updateTime"] ?: throw IllegalArgumentException("ksp option updateTime required"),
            skipExistsFiles = op["skipExistsFiles"] ?: throw IllegalArgumentException("ksp option skipExistsFiles required"),

            sharedSourceDir = op["sharedSourceDir"] ?: throw IllegalArgumentException("ksp option sharedSourceDir required"),
            isomorphicPackageName = op["isomorphicPackageName"] ?: throw IllegalArgumentException("ksp option isomorphicPackageName required"),
            isomorphicClassSuffix = op["isomorphicClassSuffix"] ?: throw IllegalArgumentException("ksp option isomorphicClassSuffix required"),

            formPackageName = op["formPackageName"] ?: throw IllegalArgumentException("ksp option formPackageName required"),
            apiClientPackageName = op["apiClientPackageName"] ?: throw IllegalArgumentException("ksp option apiClientPackageName required")
        )

    }
}
