import org.babyfish.jimmer.Vars.commonMainKspBuildMetaDataDir
import org.babyfish.jimmer.Vars.commonMainSourceDir
import org.babyfish.jimmer.Vars.jvmMainKspBuildMetaDataDir
import org.babyfish.jimmer.Vars.jvmMainSourceDir

plugins {
//    id("com.google.devtools.ksp")
    id("spring-conventions")
    id("test-conventions")
    id("com.google.devtools.ksp")
    id("ksp4jdbc")

//    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.noarg") version libs.versions.kotlin
//    application
}

noArg {
    annotation("com.addzero.common.anno.NoArg")
}
allOpen {
    annotation("com.addzero.common.anno.NoArg")
}


ksp {

    val srcDir = sourceSets.main.get().kotlin.srcDirs.first().absolutePath
    val resourceDir = sourceSets.main.get().resources.srcDirs.first().absolutePath
    // 计算各模块目录（使用常量字符串）


    val serverProject = project(":backend:server")
    val composeProject = project(":composeApp")
    val sharedProject = project(":shared")


    val serverSourceDir = serverProject.projectDir.resolve(jvmMainSourceDir).absolutePath
    val serverBuildDir = serverProject.projectDir.resolve(jvmMainKspBuildMetaDataDir).absolutePath




    val composeSourceDir = composeProject.projectDir.resolve(commonMainSourceDir).absolutePath
    val composeBuildDir = composeProject.projectDir.resolve(commonMainKspBuildMetaDataDir).absolutePath

    val sharedSourceDir = sharedProject.projectDir.resolve(commonMainSourceDir).absolutePath
    val sharedBuildDir = sharedProject.projectDir.resolve(commonMainKspBuildMetaDataDir).absolutePath



    // 模块目录配置（小驼峰命名）
    arg("moduleMainSrcDir", srcDir)
    arg("moduleMainResourceDir", resourceDir)


    arg("serverSourceDir", serverSourceDir)
    arg("serverBuildDir", serverBuildDir)



    arg("composeSourceDir", composeSourceDir)
    arg("composeBuildDir", composeBuildDir)
    arg("sharedSourceDir", sharedSourceDir)
    arg("sharedBuildDir", sharedBuildDir)

    // 包名配置（小驼峰命名，outputDir 由扩展属性计算）
    arg("isomorphicPackageName", "com.addzero.kmp.generated.isomorphic")
    arg("formPackageName", "com.addzero.kmp.generated.forms")
    arg("enumOutputPackage", "com.addzero.kmp.generated.enums")
    arg("apiClientPackageName", "com.addzero.kmp.generated.api")
    arg("iso2DataProviderPackage", "com.addzero.kmp.generated.forms.dataprovider")

    arg("outputPackage", "com.addzero.kmp.jdbc.meta")

    // 字典表配置（小驼峰命名）
    arg("dictTableName", "sys_dict")
    arg("dictIdColumn", "id")
    arg("dictCodeColumn", "dict_code")
    arg("dictNameColumn", "dict_name")
    arg("dictItemTableName", "sys_dict_item")
    arg("dictItemForeignKeyColumn", "dict_id")
    arg("dictItemCodeColumn", "item_value")
    arg("dictItemNameColumn", "item_text")

    // 其他配置（小驼峰命名）
    arg("skipExistsFiles", "false")

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


    arg("jimmer.dto.dirs", "src/main/kotlin")
    arg("jimmer.dto.defaultNullableInputModifier", "dynamic")
    arg("jimmer.dto.mutable", "true")


}


springBoot {
    mainClass.set("com.addzero.SpringBootAppKt")
}


dependencies {
    implementation(libs.kotlinx.datetime)

    //通用库
    implementation(projects.lib.addzeroTool)
    //天气库
    implementation(projects.lib.addzeroToolWeather)


    //内部使用:共享业务逻辑
    implementation(projects.shared)
    implementation(projects.backend.model)


    // 阶段2: 同构体生成（依赖枚举）
    ksp(projects.lib.addzeroEntity2isoProcessor)

    //jdbc元数据转controller
    ksp(projects.lib.addzeroJdbc2controllerProcessor)

    // 阶段3: 依赖同构体的处理器
    ksp(projects.lib.addzeroController2apiProcessor)
    ksp(projects.lib.addzeroEntity2formProcessor)
    ksp(projects.lib.addzeroEntity2mcpProcessor)

    // 阶段4: 控制器转 Iso2DataProvider（生成到 shared 编译目录）
    ksp(projects.lib.addzeroController2iso2dataproviderProcessor)


// 引入 Spring AI 相关依赖
    implementation(libs.spring.ai.starter.mcp.server.webflux)
    // 引入 BOM（管理版本）
    implementation(platform(libs.spring.ai.bom))
//   implementation platform("org.springframework.ai:spring-ai-bom:1.0.0-M7")
// Spring AI PGVector Store Starter
    implementation(libs.spring.ai.starter.vector.store.pgvector)
    implementation(libs.jsonschema.generator)
// Spring AI Ollama Starter
    implementation(libs.spring.ai.starter.model.ollama)

// Spring AI Tika Document Reader
    implementation(libs.spring.ai.tika.document.reader)

// Spring AI OpenAI Starter
//    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    // 添加 deepseek starter
    implementation(libs.spring.ai.deepseek)






    implementation(libs.kotlinx.coroutines.reactor)

// 添加spring-webflux依赖，解决NoClassDefFoundError: reactor/core/publisher/Mono错误
    implementation(libs.spring.boot.starter.webflux)


    implementation(libs.x.file.storage.spring)


    implementation(libs.tomlj)
    implementation(libs.spring.boot.starter.web)



    implementation(libs.spring.boot.starter.validation)


    implementation(libs.aspectjweaver)



    implementation(libs.spring.boot.starter.thymeleaf)



    implementation(libs.jackson.module.kotlin)

    implementation(libs.kotlin.reflect)

    implementation(libs.fastjson2.kotlin)

    implementation(libs.minio)



    implementation(libs.sa.token.spring.boot3.starter)



    implementation(libs.pinyin4j)

    implementation(libs.fastexcel)
//    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")

//    implementation("com.github.xiaoymin:knife4j-openapi3-ui:+")
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:+")
    implementation(libs.hutool.all)


// 引入数据库驱动

    runtimeOnly(libs.postgresql.driver)

    runtimeOnly(libs.mysql.connector.java)


    runtimeOnly(libs.dameng.jdbc.driver)

    runtimeOnly(libs.h2)


    implementation(libs.jimmer.spring.boot.starter)

    ksp(libs.jimmer.ksp)


    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

}
