import de.jensklingenberg.ktorfit.gradle.KtorfitPluginExtension

import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("kmp-shared")
    id("ksp4shared-convention")
    id("de.jensklingenberg.ktorfit") version "+"
}
configure<KtorfitPluginExtension> {
    kotlinVersion.set("2.3.0")
}



dependencies {


    kspCommonMainMetadata(projects.lib.addzeroJdbc2enumProcessor)
    kspCommonMainMetadata(projects.lib.addzeroApiproviderProcessor)

//    with(projects.lib.addzeroJdbc2enumProcessor) {
//        add("kspCommonMainMetadata", this)
//    }

//    with(projects.lib.addzeroApiproviderProcessor) {
//        add("kspCommonMainMetadata", this)
//    }


}
ksp {
    arg(
        "module.main.src.dir",
        project.extensions.getByType<KotlinMultiplatformExtension>().sourceSets.getByName("commonMain").kotlin.srcDirs.first().absolutePath
    )

}



kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)

            implementation(projects.lib.addzeroTool)


            implementation(libs.ktorfit.lib)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            //懒人http
//            implementation(libs.lazy.people.http)
            androidMain.dependencies {
                implementation(libs.ktor.client.cio)
            }
            jvmMain.dependencies {
                implementation(libs.ktor.client.cio)
            }
            wasmJsMain.dependencies {
                implementation(libs.ktor.client.js)
            }

        }
    }
}
