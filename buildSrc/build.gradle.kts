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

dependencies {
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.dokka)
    implementation(libs.gradlePlugin.jetbrainsCompose)
    implementation(libs.gradlePlugin.composeCompiler)
    compileOnly(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.mavenPublish)
//    compileOnly("org.jetbrains.compose.hot-reload:1.0.0-alpha11")


//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
//    implementation("com.vanniktech.maven.publish:com.vanniktech.maven.publish.gradle.plugin:0.32.0")
//    implementation("com.android.tools.build:gradle:8.3.0")
}
