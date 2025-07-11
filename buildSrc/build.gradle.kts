plugins {
    `kotlin-dsl`
}
//val kotlinVersion = "2.2.0"
//val kspVersion = "2.2.0-2.0.2"
val kotlinVersion = libs.versions.kotlin.get()
val jdkVersion = libs.versions.jdk.get().toInt()
val kspVersion = libs.versions.ksp.get()

// 🔧 配置 JVM 工具链，解决版本兼容性问题
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jdkVersion))
    }
}

kotlin {
    jvmToolchain(jdkVersion)
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    gradlePluginPortal()
}





dependencies {
//    api(gradleApi())
    implementation("com.android.tools.build:gradle:8.10.1")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.0.0")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.8.2")
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    // 确保添加序列化插件依赖
    implementation("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion") // 与Kotlin版本一致
//    implementation("org.jetbrains.kotlin.plugin.serialization:$kotlinVersion")
    implementation("com.vanniktech:gradle-maven-publish-plugin:0.33.0")
//    implementation("com.vanniktech:gradle-maven-publish-plugin:+")




    implementation("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${kspVersion}")

    implementation("org.jetbrains.kotlin.plugin.spring:org.jetbrains.kotlin.plugin.spring.gradle.plugin:${kotlinVersion}")
    implementation("io.spring.gradle:dependency-management-plugin:1.1.7")
    implementation("org.springframework.boot:org.springframework.boot.gradle.plugin:3.5.3")

}
