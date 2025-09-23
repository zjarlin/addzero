import org.apache.tools.ant.util.FileUtils.getRelativePath

rootProject.name = "addzero"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
//includeBuild("build-logic")


pluginManagement {
    repositories {
        mavenLocal()
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
        mavenLocal()
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


fun findAllProjectDirs(rootDir: File): List<File> {
    val result = mutableListOf<File>()
    if (File(rootDir, "build.gradle.kts").exists()) {
        result.add(rootDir)
    }
    rootDir.listFiles { file: File ->
        file.isDirectory
    }?.forEach { subDir ->
        result.addAll(findAllProjectDirs(subDir))
    }
    return result
}

fun autoIncludeBuild(): Unit {
    val rootDir = settings.layout.rootDirectory.asFile
    val findAllProjectDirs = findAllProjectDirs(rootDir)
    val allProjectDirs = findAllProjectDirs
        .filter { it.absolutePath != rootDir.absolutePath } // 排除根项目本身

    val (buildProj, modules) = allProjectDirs.partition {
        val isBuildLogic = it.name.startsWith("build-logic") || it.name.startsWith("buildLogic")
        isBuildLogic
    }
    // 输出扫描结果统计信息
    println(
        "🔍 扫描到 ${buildProj.size} 个构建逻辑模块，${modules.size} 个普通模块",
    )

    buildProj.forEach {
        settings.includeBuild(it)
        // 打印调试信息
        println("🔨 已包含构建逻辑模块: ${it.name}")

    }
    modules.forEach {
        val relativePath = getRelativePath(rootDir, it)
        val moduleName = ":${relativePath.replace(File.separator, ":")}"
        settings.include(moduleName)
        settings.findProject(moduleName)
        println("📦 已包含项目模块: ${moduleName} (路径: ${relativePath})")
    }
    println("✅ 模块自动扫描和包含完成")

}
autoIncludeBuild()
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "+"
//    id("site.addzero.repo-buddy") version "2025.09.23"
//    id("site.addzero.modules-buddy") version "2025.09.23"
}
//autoModules{
//   excludeModules=listOf()
//}
