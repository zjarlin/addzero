import site.addzero.gradle.tool.getProjectDirConfigMap
import site.addzero.gradle.tool.getProjectDirConfigMapWithOutLib

plugins {
    id("com.google.devtools.ksp")
}

val rootDir1 = project.rootDir
ksp {
    val projectDirConfigMapWithOutLib = getProjectDirConfigMapWithOutLib(rootDir1)
    projectDirConfigMapWithOutLib.forEach { (key, value) ->
        arg(key, value)
    }
}
