
plugins {
//    id("com.google.devtools.ksp")
    id("spring-conventions")
    id("test-conventions")
    id("ksp4backend-convention")
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
    arg("module.main.src.dir", srcDir)
    arg("module.main.resource.dir", resourceDir)

}


//springBoot {
//    mainClass.set("com.addzero.SpringBootAppKt")
//}


dependencies {
    //通用库
    implementation(projects.lib.addzeroTool)
    //通用ksp 解析Controller转为ktorfitApi
    ksp(projects.lib.addzeroController2apiProcessor)
    //通用ksp jdbc元数据转Controller
    ksp(projects.lib.addzeroJdbc2controllerProcessor)
    //内部使用:后台实体转composeApp form
    ksp(projects.lib.addzeroEntity2formProcessor)
    //内部使用:共享业务逻辑
    implementation(projects.shared)


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
