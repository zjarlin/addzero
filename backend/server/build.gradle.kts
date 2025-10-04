import com.google.devtools.ksp.gradle.KspAATask
import site.addzero.gradle.constant.Disposable.KSP_BUILD_DIR_JVM

plugins {
//    id("com.google.devtools.ksp")
    id("spring-convention")
    id("test-conventions")
    id("ksp4iso")
    id("ksp4dict")
    id("ksp4projectdir")
    id("org.jetbrains.kotlin.plugin.noarg") version libs.versions.kotlin

//    id("ksp4controller2lazypeoplehttp")

}
noArg {
    annotation("site.addzero.common.anno.NoArg")
}
allOpen {
    annotation("site.addzero.common.anno.NoArg")
}
ksp {
    // 包名配置（小驼峰命名，outputDir 由扩展属性计算）
    arg("apiClientPackageName", "site.addzero.generated.api")
    arg("jimmer.dto.dirs", "src/main/kotlin")
    arg("jimmer.dto.defaultNullableInputModifier", "dynamic")
    arg("jimmer.dto.mutable", "true")
}
//springBoot {
//    mainClass.set("site.addzero.SpringBootAppKt")
//}
dependencies {
//    implementation(projects.lib.ksp.common.addzeroKspEasycodeJimmer)
//    implementation(projects.lib.ksp.common.addzeroKspEasycodeCore)
//    implementation("org.apache.velocity:velocity-engine-core:2.3")
//    implementation(projects.lib.ksp.metadata.addzeroEntity2analysedSupport)
    implementation(libs.kotlinx.datetime)

    implementation(libs.addzero.tool.mqtt)
    implementation(libs.addzero.compose.model.component)
    implementation(libs.addzero.tool.json)
    implementation(libs.addzero.jimmer.ext.lowquery)
    implementation(libs.addzero.tool.common.jvm)
    implementation(libs.addzero.tool.spring)
    implementation(libs.addzero.jimmer.ext.lowquery)
    implementation(libs.addzero.tool.jdbc)
    implementation(libs.addzero.tool)
    implementation(libs.addzero.tool.api.jvm)
    ksp(libs.addzero.controller2api.processor)
    ksp(libs.addzero.controller2iso2dataprovider.processor)
    implementation(libs.site.addzero.addzero.jimmer.ext.dynamic.datasource)
    implementation(projects.shared)
    implementation(projects.backend.model)

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

//    implementation(libs.kotlin.reflect)

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
//    implementation(libs.flyway.database.h2)

}
// server/build.gradle.kts
kotlin {
    sourceSets.main {
        kotlin.srcDir(KSP_BUILD_DIR_JVM)
    }

}

tasks.withType<KspAATask>().configureEach {
    if (name != "kspKotlin") {
        dependsOn(":backend:model:kspKotlin")
    }
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn(":shared:kspCommonMainKotlinMetadata")
    }


}
