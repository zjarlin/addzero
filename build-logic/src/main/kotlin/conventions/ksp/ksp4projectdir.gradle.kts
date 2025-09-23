import site.addzero.gradle.tool.getProjectDirConfigMap
plugins {
    id("com.google.devtools.ksp")
}

ksp {
    getProjectDirConfigMap().forEach { (key, value) ->
        arg(key, value)
    }
}
