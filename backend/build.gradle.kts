
plugins {
//    id("com.google.devtools.ksp")
    id("spring-conventions")
    id("test-conventions")
    id("ksp4backend-convention")
    id("org.jetbrains.kotlin.plugin.noarg") version "+"
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
    val springaiVersion = "+"
    implementation("org.springframework.ai:spring-ai-starter-mcp-server-webflux:+")
    implementation(platform("org.springframework.ai:spring-ai-bom:$springaiVersion"))
//   implementation platform("org.springframework.ai:spring-ai-bom:1.0.0-M7")
// Spring AI PGVector Store Starter
    implementation("org.springframework.ai:spring-ai-starter-vector-store-pgvector")
    implementation("com.github.victools:jsonschema-generator:4.38.0")
// Spring AI Ollama Starter
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")

// Spring AI Tika Document Reader
    implementation("org.springframework.ai:spring-ai-tika-document-reader")

// Spring AI OpenAI Starter
//    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    implementation("org.springframework.ai:spring-ai-starter-model-deepseek:+")








    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.10.2")

// 添加spring-webflux依赖，解决NoClassDefFoundError: reactor/core/publisher/Mono错误
    implementation("org.springframework.boot:spring-boot-starter-webflux")


    implementation("org.dromara.x-file-storage:x-file-storage-spring:+")


    implementation("org.tomlj:tomlj:+")
    implementation("org.springframework.boot:spring-boot-starter-web")



    implementation("org.springframework.boot:spring-boot-starter-validation")


    implementation("org.aspectj:aspectjweaver:+")



    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")



    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("com.alibaba.fastjson2:fastjson2-kotlin:+")

    implementation("io.minio:minio:+")



    implementation("cn.dev33:sa-token-spring-boot3-starter:+")



    implementation("com.belerweb:pinyin4j:+")

    implementation("cn.idev.excel:fastexcel:+")
//    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")

//    implementation("com.github.xiaoymin:knife4j-openapi3-ui:+")
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:+")
    implementation("cn.hutool:hutool-all:+")


// 引入数据库驱动

    runtimeOnly("org.postgresql:postgresql")

    runtimeOnly("com.mysql:mysql-connector-j")


    runtimeOnly("com.dameng:DmJdbcDriver18:+")

    runtimeOnly("com.h2database:h2")


    implementation("org.babyfish.jimmer:jimmer-spring-boot-starter:+")

    ksp("org.babyfish.jimmer:jimmer-ksp:+")


    implementation("org.flywaydb:flyway-core:+")
    implementation("org.flywaydb:flyway-database-postgresql:+")

}
