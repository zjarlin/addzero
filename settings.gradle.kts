rootProject.name = "addzero"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }

}
//include(":addzero-ksp:addzero-route")
include(":composeApp")
include(":shared")
include("lib")
include("lib:addzero-route-processor")
findProject(":lib:addzero-route-processor")?.name = "addzero-route-processor"
include("lib:addzero-route-core")
findProject(":lib:addzero-route-core")?.name = "addzero-route-core"
include("lib:addzero-controller2api-processor")
findProject(":lib:addzero-controller2api-processor")?.name = "addzero-controller2api-processor"
include("lib:addzero-ksp-support")
findProject(":lib:addzero-ksp-support")?.name = "addzero-ksp-support"
