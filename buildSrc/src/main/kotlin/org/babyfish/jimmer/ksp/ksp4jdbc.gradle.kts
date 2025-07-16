import org.babyfish.jimmer.Vars.jvmMainKspBuildMetaDataDir
import org.babyfish.jimmer.Vars.jvmMainSourceDir

plugins {
    id("com.google.devtools.ksp")
}
ksp {

    // JDBC 配置（用于枚举生成）
    arg("jdbcUrl", "jdbc:postgresql://localhost:15432/postgres")
    arg("jdbcUsername", "postgres")
    arg("jdbcPassword", "postgres")
    arg("jdbcSchema", "public")
    arg("jdbcDriver", "org.postgresql.Driver")
    // 可选：指定要排除的表（逗号分隔）
    arg("excludeTables", "flyway_schema_history,vector_store")

}
