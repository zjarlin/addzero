import com.google.devtools.ksp.processing.kspJsArgParser
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

plugins {
    id("kmp-shared")
    id("ksp4shared-convention")
}
val ktorV = libs.versions.ktor.get()
dependencies {
    with(projects.lib.addzeroJdbc2enumProcessor) {
        add("kspCommonMainMetadata", this)
    }

    with(projects.lib.addzeroApiproviderProcessor) {
        add("kspCommonMainMetadata", this)
    }


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
            implementation("io.ktor:ktor-client-core:$ktorV")
            implementation("io.ktor:ktor-client-content-negotiation:$ktorV")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorV")
            implementation("io.ktor:ktor-client-logging:$ktorV")

            //懒人http
//            implementation("io.github.ltttttttttttt:LazyPeopleHttp-lib:2.2.2")
            androidMain.dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorV")
            }
            jvmMain.dependencies {
                implementation("io.ktor:ktor-client-cio:$ktorV")
            }
            wasmJsMain.dependencies {
                implementation("io.ktor:ktor-client-js:$ktorV")
            }

        }
    }
}
//configure<KtorfitPluginExtension> {
//    kotlinVersion.set("2.3.0")
//}
