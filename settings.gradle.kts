import org.apache.tools.ant.util.FileUtils.getRelativePath

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

    val rootDir = settings.layout.rootDirectory.asFile
    val findAllProjectDirs = findAllProjectDirs(rootDir)
    val allProjectDirs = findAllProjectDirs
        .filter { it.absolutePath != rootDir.absolutePath } // 排除根项目本身

    val (buildProj, modules) = allProjectDirs.partition {
        val isBuildLogic = it.name.startsWith("build-logic") || it.name.startsWith("buildLogic")
        isBuildLogic
    }
    buildProj.forEach {
        settings.includeBuild(it)
        // 打印调试信息
        println("Auto included build: ${it.name}")
    }
    modules.forEach {
        val relativePath = getRelativePath(rootDir, it)
        val moduleName = ":${relativePath.replace(File.separator, ":")}"
        settings.include(moduleName)
        println("Auto included module: $moduleName")
    }
plugins {
  id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
