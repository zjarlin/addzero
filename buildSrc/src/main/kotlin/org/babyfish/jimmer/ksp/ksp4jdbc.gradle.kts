import org.babyfish.jimmer.ksp.YmlUtil
import org.babyfish.jimmer.util.AppConfig
import org.babyfish.jimmer.util.SpringConfigReader
import java.nio.file.Paths

plugins {
    id("com.google.devtools.ksp")
}



   val serverProject = project(":backend:server")
   val serverResourceDir = serverProject.projectDir.resolve("src/main/resources").absolutePath
   val ymlPath = "${serverResourceDir}/application.yml"


val activate = YmlUtil.getActivate(ymlPath)
val ymlActivetePath = "${serverResourceDir}/application-${activate}.yml"
val ymlActiveConfig = YmlUtil.loadYmlConfigMap(ymlActivetePath)

val activeDatasource = YmlUtil.getConfigValue<String>(ymlActiveConfig, "spring.datasource.dynamic.primary")!!

val pgdatasource = YmlUtil.getConfigValue<Map<String,Any>>(ymlActiveConfig, "spring.datasource.dynamic.datasource")!!

val datasource = pgdatasource[activeDatasource] as Map<String, String>
println("datasource: $datasource")
val driverClassName = datasource["driver-class-name"]!!
val url= datasource["url"]!!
val urlSplit = url.split("?")
val jdbcUrl = urlSplit.first()
val jdbcSchema = urlSplit.last().split("=").last()
val username = datasource["username"]?:"postgres"
val password = datasource["password"]?:"postgres"
val excludeTables = datasource["exclude-tables"]?:""


ksp {

    // JDBC 配置（用于枚举生成）
    arg("jdbcUrl", jdbcUrl)
    arg("jdbcUsername", username)
    arg("jdbcPassword", password)
    arg("jdbcSchema", jdbcSchema)
    arg("jdbcDriver", driverClassName)
    // 可选：指定要排除的表（逗号分隔）
    arg( "excludeTables", excludeTables )
    println("jdbcUrl: $jdbcUrl")
    println("jdbcUsername: $username")
    println("jdbcPassword: $password")
    println("jdbcSchema: $jdbcSchema")
    println("jdbcDriver: $driverClassName")
    println("excludeTables: $excludeTables")

}
