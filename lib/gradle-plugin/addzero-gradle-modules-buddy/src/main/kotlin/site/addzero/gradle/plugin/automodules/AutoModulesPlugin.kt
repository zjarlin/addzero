package site.addzero.gradle.plugin.automodules

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import site.addzero.gradle.tool.autoIncludeBuild

/**
 * 自动模块插件扩展配置
 */
open class AutoModulesPluginExtension {
    /**
     * 默认的黑名单目录列表
     */
    var excludeModules: List<String> = listOf()
}

/**
 * 自动模块插件
 * 自动扫描项目目录并包含所有Gradle子项目
 */
class AutoModulesPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        val extension = settings.extensions.create("autoModules", AutoModulesPluginExtension::class.java)
        settings.autoIncludeBuild(extension.excludeModules)
    }
}
