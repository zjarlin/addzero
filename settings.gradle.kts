import org.apache.tools.ant.property.LocalProperties

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
        mavenCentral{
//            credentials {
//                username = localProps.getProperty("sonaTokenUser")
//                password = localProps.getProperty("sonaToken")
//            }
        }
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
        mavenCentral{

        }
    }

}
include(":backend")
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
include("lib:addzero-ksp-support-jdbc")
findProject(":lib:addzero-ksp-support-jdbc")?.name = "addzero-ksp-support-jdbc"
include("lib:addzero-jdbc2controller-processor")
findProject(":lib:addzero-jdbc2controller-processor")?.name = "addzero-jdbc2controller-processor"
include("lib:addzero-entity2form-processor")
findProject(":lib:addzero-entity2form-processor")?.name = "addzero-entity2form-processor"
include("lib:addzero-jdbc2enum-processor")
findProject(":lib:addzero-jdbc2enum-processor")?.name = "addzero-jdbc2enum-processor"
include("lib:addzero-apiprovider-processor")
findProject(":lib:addzero-apiprovider-processor")?.name = "addzero-apiprovider-processor"
include("lib:addzero-tool")
findProject(":lib:addzero-tool")?.name = "addzero-tool"
