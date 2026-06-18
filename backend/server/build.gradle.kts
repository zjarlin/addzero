import com.google.devtools.ksp.gradle.KspAATask
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.JavaPluginExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
//    id("com.google.devtools.ksp")
    id("site.addzero.buildlogic.spring.spring-app")
    id("site.addzero.buildlogic.ksp.ksp4iso")
    id("site.addzero.buildlogic.ksp.ksp4dict")
    id("site.addzero.buildlogic.ksp.ksp4projectdir")
    id("org.jetbrains.kotlin.plugin.noarg") version "2.4.0"

}

val catalogLibs = extensions.getByType<VersionCatalogsExtension>().named("libs")
fun catalog(alias: String) = catalogLibs.findLibrary(alias).get()

extensions.configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

extensions.configure<KotlinJvmProjectExtension> {
    jvmToolchain(17)
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

// ehcache:3.11.1 需要 jaxb-runtime:[2.2,3)，但仓库里只有 4.x
// 直接排除 ehcache 整个传递依赖树，fastexcel 不需要它
configurations.all {
    exclude(group = "org.ehcache", module = "ehcache")
}
//    id("ksp4controller2lazypeoplehttp")

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
    implementation(catalog("org-jetbrains-kotlinx-kotlinx-datetime"))

    implementation(catalog("site-addzero-tool-mqtt"))
    implementation(catalog("site-addzero-compose-model-component"))
    implementation(catalog("site-addzero-tool-json"))
    implementation(catalog("site-addzero-jimmer-ext-lowquery"))
    implementation(catalog("site-addzero-tool-common-jvm"))
    implementation("site.addzero:tool-jackson:2026.06.19")
    implementation("site.addzero:tool-jackson-extra:2026.06.19")
    implementation(catalog("site-addzero-tool-spring"))
    implementation(catalog("site-addzero-jimmer-ext-lowquery"))
    implementation(catalog("site-addzero-tool-jdbc"))
    implementation(catalog("site-addzero-tool"))
    implementation(catalog("site-addzero-tool-api-jvm"))
    ksp(catalog("site-addzero-controller2api-processor"))
    ksp(catalog("site-addzero-controller2iso2dataprovider-processor"))
    implementation(catalog("site-addzero-jimmer-ext-dynamic-datasource"))
    implementation(projects.shared)
    implementation(projects.backend.model)

// 引入 Spring AI 相关依赖
    implementation(catalog("org-springframework-ai-spring-ai-starter-mcp-server-webflux"))
    // 引入 BOM（管理版本）
    implementation(platform(catalog("org-springframework-ai-spring-ai-bom")))
//   implementation platform("org.springframework.ai:spring-ai-bom:1.0.0-M7")
// Spring AI PGVector Store Starter
    implementation(catalog("org-springframework-ai-spring-ai-starter-vector-store-pgvector"))
    implementation(catalog("com-github-victools-jsonschema-generator"))
// Spring AI Ollama Starter
    implementation(catalog("org-springframework-ai-spring-ai-starter-model-ollama"))

// Spring AI Tika Document Reader
    implementation(catalog("org-springframework-ai-spring-ai-tika-document-reader"))

// Spring AI OpenAI Starter
//    implementation("org.springframework.ai:spring-ai-starter-model-openai")

    // 添加 deepseek starter
    implementation(catalog("org-springframework-ai-spring-ai-starter-model-deepseek"))

    implementation(catalog("org-jetbrains-kotlinx-kotlinx-coroutines-reactor"))

// 添加spring-webflux依赖，解决NoClassDefFoundError: reactor/core/publisher/Mono错误
    implementation(catalog("org-springframework-boot-spring-boot-starter-webflux"))


    implementation(catalog("org-dromara-x-file-storage-x-file-storage-spring"))


    implementation(catalog("org-tomlj-tomlj"))
    implementation(catalog("org-springframework-boot-spring-boot-starter-web"))



    implementation(catalog("org-springframework-boot-spring-boot-starter-validation"))


    implementation(catalog("org-aspectj-aspectjweaver"))



    implementation(catalog("org-springframework-boot-spring-boot-starter-thymeleaf"))



    implementation(catalog("com-fasterxml-jackson-module-jackson-module-kotlin"))

//    implementation(libs.kotlin.reflect)

    implementation(catalog("com-alibaba-fastjson2-fastjson2-kotlin"))

    implementation(catalog("io-minio-minio"))



    implementation(catalog("cn-dev33-sa-token-spring-boot3-starter"))



    implementation(catalog("com-belerweb-pinyin4j"))

    implementation(catalog("cn-idev-excel-fastexcel"))
//    implementation("org.springdoc:springdoc-openapi-ui:1.6.9")

//    implementation("com.github.xiaoymin:knife4j-openapi3-ui:+")
//    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:+")
    implementation(catalog("cn-hutool-hutool-all"))


// 引入数据库驱动

    runtimeOnly(catalog("org-postgresql-postgresql"))

    runtimeOnly(catalog("mysql-mysql-connector-java"))


    runtimeOnly(catalog("com-dameng-dm-jdbc-driver18"))

    runtimeOnly(catalog("com-h2database-h2-h2"))


    implementation(catalog("org-babyfish-jimmer-jimmer-spring-boot-starter"))

    ksp(catalog("org-babyfish-jimmer-jimmer-ksp"))


    implementation(catalog("org-flywaydb-flyway-core"))
    implementation(catalog("org-flywaydb-flyway-database-postgresql"))
//    implementation(libs.flyway.database.h2)

}
// server/build.gradle.kts
kotlin {
    sourceSets.main {
        kotlin.srcDir("build/generated/ksp/main/kotlin")
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
