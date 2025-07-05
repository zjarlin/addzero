import org.jetbrains.kotlin.serialization.js.ast.JsAstProtoBuf

plugins {
    `kotlin-dsl`
//    `kotlin-dsl-base`
//    alias(libs.plugins.androidApplication) apply false
//    alias(libs.plugins.androidLibrary) apply false
//    alias(libs.plugins.composeHotReload) apply false
//    alias(libs.plugins.composeMultiplatform) apply false
//    alias(libs.plugins.composeCompiler) apply false
//    alias(libs.plugins.kotlinMultiplatform) apply false

}

repositories {
    mavenCentral()
    google()
    gradlePluginPortal()
}


val kotlinVersion = "2.2.0"
dependencies {
    implementation("com.android.tools.build:gradle:8.10.0")
    implementation("org.jetbrains.dokka:dokka-gradle-plugin:2.0.0")
    implementation("org.jetbrains.compose:compose-gradle-plugin:1.8.2")
    implementation("org.jetbrains.kotlin.plugin.compose:org.jetbrains.kotlin.plugin.compose.gradle.plugin:$kotlinVersion")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    implementation("com.vanniktech:gradle-maven-publish-plugin:0.33.0")
}
