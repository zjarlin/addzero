package com.addzero.util

import org.yaml.snakeyaml.Yaml
import java.nio.file.Path
import kotlin.collections.get

/**
 * Spring配置读取工具类
 */
object SpringConfigReader {
    private val yaml = Yaml()


    /**
     * 读取YML配置（自动合并主配置和profile配置）
     * @param ymlPath 主配置文件路径（application.yml）
     * @param activeProfile 指定激活的profile（若为null则自动检测）
     */
    fun loadSpringConfig(ymlPath: Path, activeProfile: String? = null):
            Map<String, Any> {
        val mainConfig = loadYmlFile(ymlPath)
        val profile = activeProfile ?: detectActiveProfile(mainConfig)

        return if (profile != null) {
            val profileConfig = loadProfileConfig(ymlPath, profile)
            mergeConfigs(mainConfig, profileConfig)
        } else {
            mainConfig
        }
    }

    /**
     * 获取配置值（支持点分路径）
     * @param path 如 "spring.datasource.url"
     */
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

    // -- 私有方法 -- //
    private fun loadYmlFile(path: Path): Map<String, Any> {
        return yaml.load(path.toFile().inputStream()) as? Map<String, Any> ?: emptyMap()
    }

    private fun detectActiveProfile(config: Map<String, Any>): String? {
        return getConfigValue(config, "spring.profiles.active")
    }

    private fun loadProfileConfig(mainPath:Path, profile: String):
            Map<String, Any> {
        val profilePath = mainPath.parent.resolve("application-$profile.yml")
        return if (profilePath.toFile().exists()) {
            loadYmlFile(profilePath)
        } else {
            emptyMap()
        }
    }

    private fun mergeConfigs(main: Map<String, Any>, profile: Map<String, Any>): Map<String, Any> {
        return (main.asSequence() + profile.asSequence())
            .distinctBy { it.key }
            .associate { it.key to it.value }
    }
}
