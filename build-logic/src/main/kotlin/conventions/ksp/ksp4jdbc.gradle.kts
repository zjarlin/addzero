import site.addzero.gradle.tool.YmlUtil
import site.addzero.gradle.tool.YmlUtil.replaceEnvInString

plugins {
    id("com.google.devtools.ksp")
}
   val serverProject = project(":backend:server")
   val serverResourceDir = serverProject.projectDir.resolve("src/main/resources").absolutePath
   val ymlPath = "${serverResourceDir}/application.yml"


val activate = YmlUtil.getActivate(ymlPath)
val ymlActivetePath = "${serverResourceDir}/application-${activate}.yml"
val ymlActiveConfig = YmlUtil.loadYmlConfigMap(ymlActivetePath)

val activeDatasource = YmlUtil.getConfigValue<String>(ymlActiveConfig, "spring.datasource.dynamic.primary")

val pgdatasource = YmlUtil.getConfigValue<Map<String,Any>>(ymlActiveConfig, "spring.datasource.dynamic.datasource")

val map = if (pgdatasource == null) {
    YmlUtil.getConfigValue<Map<String, Any>>(ymlActiveConfig, "spring.datasource")
} else {
    pgdatasource[activeDatasource]
}
val datasource = map as Map<String, String>
println("datasource: $datasource")
val jdbcDriver = datasource["driver-class-name"]!!
val url= datasource["url"].replaceEnvInString()
val urlSplit = url.split("?")

val jdbcUrl = urlSplit.first()
val jdbcSchema = urlSplit.last().split("=").last()

val jdbcUsername = datasource["username"].replaceEnvInString()
val jdbcPassword = datasource["password"].replaceEnvInString()
val excludeTables = datasource["exclude-tables"].replaceEnvInString()


ksp {
    // JDBC 配置（用于枚举生成）
    arg("jdbcUrl", jdbcUrl)
    arg("jdbcUsername", jdbcUsername)
    arg("jdbcPassword", jdbcPassword)
    arg("jdbcSchema", jdbcSchema)
    arg("jdbcDriver", jdbcDriver)
    // 可选：指定要排除的表（逗号分隔）
    arg("excludeTables", excludeTables)
    arg("baseEntityPackage", "site.addzero.model.common.BaseEntity")

    println("jdbcUrl: $jdbcUrl")
    println("jdbcUsername: $jdbcUsername")
    println("jdbcPassword: $jdbcPassword")
    println("jdbcSchema: $jdbcSchema")
    println("jdbcDriver: $jdbcDriver")
    println("excludeTables: $excludeTables")
}
