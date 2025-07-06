import org.gradle.internal.impldep.com.google.api.services.storage.Storage

plugins {
    id("com.google.devtools.ksp")
}
ksp {


//    val srcDir = sourceSets.main.get().kotlin.srcDirs.first().absolutePath
//    val resourceDir = sourceSets.main.get().resources.srcDirs.first().absolutePath
//    arg("module.main.src.dir", srcDir)
//    arg("module.main.resource.dir", resourceDir)


    val sharedSourceDir = project(":shared").projectDir.resolve("src/commonMain/kotlin").absolutePath
    //同构体生成相关参数
    // 需要生成到目标模块的源代码目录

    //文件名已存在,则不生成(设为false每次都覆盖同构体)
    arg("skipExistsFiles", "false")
    //同构体输出目录
    arg("sharedSourceDir", sharedSourceDir)
    //同构体生成的包
    arg("isomorphicPackageName", "com.addzero.kmp.isomorphic")
    //api生成的包
//    arg("apiClientPackageName", "com.addzero.kmp.generated.api")
    arg("apiClientPackageName", "com.addzero.kmp.api")


    //from生成的包
    arg("formPackageName", "com.addzero.kmp.forms")


    //同构体类名后缀
    arg("isomorphicClassSuffix", "Iso")




    //autoddl ksp参数

    arg("dbType", "pg") //可选项仅有mysql oracle pg dm h2
    arg("idType", "bigint")
    arg("id", "id")
    arg("createBy", "create_by")
    arg("updateBy", "update_by")
    arg("createTime", "create_time")
    arg("updateTime", "update_time")

    arg("metaJsonSavePath", "db/autoddl/meta")
    arg("metaJsonSaveName", "jimmer_ddlcontext.json")

    //建议检查生成的sql后放到flyway的规范目录(以下为resource资源目录的相对目录)
    arg("sqlSavePath", "db/autoddl")


    // JDBC元数据处理器配置
    arg("jdbcUrl", "jdbc:postgresql://localhost:15432/postgres")
    arg("jdbcUsername", "postgres")
    arg("jdbcPassword", "postgres")
    arg("jdbcSchema", "public")
    arg("jdbcDriver", "org.postgresql.Driver")
    arg("outputPackage", "com.addzero.kmp.jdbc.meta")

    // 可选：指定要排除的表（逗号分隔）
    arg("excludeTables", "flyway_schema_history,vector_store")

    arg("jimmer.dto.dirs", "src/main/kotlin")
    arg("jimmer.dto.defaultNullableInputModifier", "dynamic")
    arg("jimmer.dto.mutable", "true")

}
