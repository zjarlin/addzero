package site.addzero.kcp.i18n.plugin

import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey

/**
 * 国际化插件配置
 */
class I18NPluginConfiguration {
    companion object {
        // 定义编译器配置键
        val TARGET_LOCALE = CompilerConfigurationKey<String>("targetLocale")
        val RESOURCE_BASE_PATH = CompilerConfigurationKey<String>("resourceBasePath")
        
        // 从编译器配置中获取目标语言
        fun getTargetLocale(configuration: CompilerConfiguration): String {
            return configuration.get(TARGET_LOCALE) ?: "en"
        }
        
        // 从编译器配置中获取资源基础路径
        fun getResourceBasePath(configuration: CompilerConfiguration): String {
            return configuration.get(RESOURCE_BASE_PATH) ?: "src/main/resources/i18n"
        }
    }
}