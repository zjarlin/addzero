import de.jensklingenberg.ktorfit.gradle.KtorfitPluginExtension
import org.babyfish.jimmer.Versions
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
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:+")
            implementation(projects.lib.addzeroTool)
            implementation("de.jensklingenberg.ktorfit:ktorfit-lib:+")
            implementation("io.ktor:ktor-client-core:${Versions.ktorVersion}")
            implementation("io.ktor:ktor-client-content-negotiation:${Versions.ktorVersion}")
            implementation("io.ktor:ktor-serialization-kotlinx-json:${Versions.ktorVersion}")
            implementation("io.ktor:ktor-client-logging:${Versions.ktorVersion}")

            //懒人http
//            implementation("io.github.ltttttttttttt:LazyPeopleHttp-lib:2.2.2")
            androidMain.dependencies {
                implementation("io.ktor:ktor-client-cio:${Versions.ktorVersion}")
            }
            jvmMain.dependencies {
                implementation("io.ktor:ktor-client-cio:${Versions.ktorVersion}")
            }
            wasmJsMain.dependencies {
                implementation("io.ktor:ktor-client-js:${Versions.ktorVersion}")
            }

        }
    }
}
//configure<KtorfitPluginExtension> {
//    kotlinVersion.set("2.3.0")
//}
