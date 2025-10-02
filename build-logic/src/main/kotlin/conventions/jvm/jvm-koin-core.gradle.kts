import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the
plugins {
    kotlin("jvm")
    id("com.google.devtools.ksp")
}
val libs = the<LibrariesForLibs>()
dependencies {
    ksp(libs.koin.ksp.compiler)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
}
