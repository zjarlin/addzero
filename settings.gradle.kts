import org.apache.tools.ant.util.FileUtils.getRelativePath

rootProject.name =rootDir.name
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

dependencyResolutionManagement {
    repositories {
//        mavenLocal()
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


plugins {
//    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("io.gitee.zjarlin.auto-modules") version "0.0.608"
}

includeBuild("build-logic")
autoModules {
   excludeModules = listOf("build-logic")
}

