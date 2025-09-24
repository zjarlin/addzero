plugins {
    `kotlin-dsl`
}

repositories {
    mavenLocal()
    mavenCentral()
    google()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.gradlePlugin.mavenPublish)
    implementation(libs.org.graalvm.buildtools.native.gradle.plugin)

    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.snakeyaml)
    implementation(libs.gradlePlugin.ktorfit)
    implementation(libs.gradlePlugin.android)
    implementation(libs.gradlePlugin.dokka)
    implementation(libs.gradlePlugin.jetbrainsCompose)
    implementation(libs.gradlePlugin.composeCompiler)
    compileOnly(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.kotlinSerialization)
    implementation(libs.gradlePlugin.mavenPublish)
    implementation(libs.gradlePlugin.ksp)
    implementation(libs.gradlePlugin.kotlinSpring)
    implementation(libs.gradlePlugin.dependencyManagement)
    implementation(libs.gradlePlugin.springBoot)
    implementation(libs.gradlePlugin.buildkonfig)
    implementation(libs.gradlePlugin.buildkonfig.cp)
    implementation(libs.addzero.gradle.tool)
//    implementation(libs.gradlePluginKspBuddy)


//     implementation(libs.plugins.konfig)
//    implementation(files("com.codingfeline.buildkonfig:com.codingfeline.buildkonfig.gradle.plugin:0.17.1"))
//    com.codingfeline.buildkonfig:com.codingfeline.buildkonfig.gradle.plugin:0.17.1
}
