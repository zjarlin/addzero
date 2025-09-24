import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.the

plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("de.jensklingenberg.ktorfit")
    id("com.google.devtools.ksp")

}
val libs = the<LibrariesForLibs>()

dependencies {
    kspCommonMainMetadata(libs.lazy.people.ksp)
}


kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.lazy.people.http)
        }

    }
}
