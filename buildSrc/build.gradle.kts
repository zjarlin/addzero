plugins {
    `kotlin-dsl`
//    `kotlin-dsl-base`

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
    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.mavenPublish)





//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.20")
//    implementation("com.vanniktech.maven.publish:com.vanniktech.maven.publish.gradle.plugin:0.32.0")
//    implementation("com.android.tools.build:gradle:8.3.0")
}
