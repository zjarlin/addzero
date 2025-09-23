import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.the
plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.google.devtools.ksp")
}
val libs = the<LibrariesForLibs>()
dependencies {
    kspCommonMainMetadata(libs.koin.ksp.compiler)
}
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.annotations)
            implementation(libs.koin.core)
        }

    }
}
