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
        mavenCentral {
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
        mavenCentral {

        }
    }

}



// 扩展函数：简化模块声明
fun String.useFile(submodules: List<String> = emptyList()) {
    if (submodules.isEmpty()) {
        include(this)
        findProject(":$this")?.name = this
    } else {
        submodules.forEach { submodule ->
            val fullPath = "$this:$submodule"
            include(fullPath)
            findProject(":$fullPath")?.name = submodule
        }
    }
}

fun Pair<String, List<String>>.useFile() {
    first.useFile(second)
}

// 主要模块
listOf("backend", "composeApp", "shared", "lib").forEach { module ->
    include(module)
}


// KSP 支持模块
("backend" to listOf(
    "server",
    "model"
)).useFile()


// KSP 支持模块
("lib" to listOf(
    "addzero-ksp-support",
    "addzero-ksp-support-jdbc"
)).useFile()

// 实体分析与代码生成
("lib" to listOf(
    "addzero-entity2analysed-support",
    "addzero-entity2iso-processor",
    "addzero-entity2form-processor",
    "addzero-entity2form-core",
    "addzero-entity2mcp-processor"
)).useFile()

// JDBC 相关处理器
("lib" to listOf(
    "addzero-jdbc2controller-processor",
    "addzero-jdbc2enum-processor",
    "addzero-jdbc2entity-processor"
)).useFile()

// API 相关处理器
("lib" to listOf(
    "addzero-controller2api-processor",
    "addzero-controller2iso2dataprovider-processor",
    "addzero-apiprovider-processor"
)).useFile()

// 路由模块
("lib" to listOf(
    "addzero-route-processor",
    "addzero-route-core"
)).useFile()

// Compose 相关模块
("lib" to listOf(
    "addzero-compose-props-annotations",
    "addzero-compose-props-processor"
)).useFile()

// 组件模块
("lib" to listOf(
    "addzero-compose-native-component",
    "addzero-compose-klibs-component"
)).useFile()

("lib" to listOf("addzero-network-starter")).useFile()
// 工具模块
("lib" to listOf(
    "addzero-tool",
    "addzero-tool-api-jvm",
    "addzero-tool-api"
)).useFile()


