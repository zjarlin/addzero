import site.addzero.gradle.tool.getProjectDirConfigMap

plugins {
    id("com.google.devtools.ksp")
}

ksp {
    val blackDir = project.rootDir.absolutePath
//    getProjectDirConfigMap(listOf("lib")).forEach { (key,
//                                                                                      value) ->
//        arg(key, value)
//    }
}
