package com.addzero.ksp

import org.yaml.snakeyaml.Yaml
import kotlin.collections.get
import kotlin.io.path.Path

object YmlUtil {
    private val yaml = Yaml()


    fun <T> loadYmlConfig(dir: String): T {
        val path = Path(dir)
        val toFile = path.toFile()

        val config = yaml.load<T>(toFile.inputStream())
        return config
    }

    fun loadYmlConfigMap(dir: String): Map<String, Any> {
        val bool = loadYmlConfig<Map<String, Any>>(dir)
        return bool
    }

    fun getActivate(dir: String): String {
        val loadYmlConfigMap = loadYmlConfigMap(dir)
        val configValue = getConfigValue<String>(loadYmlConfigMap, "spring.profiles.active")
        return configValue?:"local"
    }


    fun <T> getConfigValue(config: Map<String, Any>, path: String): T? {
        val keys = path.split(".")
        var current: Any? = config

        for (key in keys) {
            current = when (current) {
                is Map<*, *> -> current[key]
                else -> return null
            }
        }

        @Suppress("UNCHECKED_CAST")
        return current as? T
    }


}
