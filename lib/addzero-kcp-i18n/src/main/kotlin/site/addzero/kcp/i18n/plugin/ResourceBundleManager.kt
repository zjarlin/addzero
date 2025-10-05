package site.addzero.kcp.i18n.plugin

import java.util.*
import java.io.File
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

/**
 * 资源包管理器，用于从项目资源目录加载和管理多语言资源
 */
class ResourceBundleManager {

    private val bundles = mutableMapOf<String, ResourceBundle>()

    /**
     * 从项目资源目录获取指定语言的资源包
     * @param pluginContext 编译器插件上下文，用于获取项目信息
     * @param locale 目标语言
     * @param basePath 资源基础路径
     */
    fun getResourceBundle(pluginContext: IrPluginContext, locale: String, basePath: String = "i18n"): ResourceBundle? {
        return try {
            // 在实际实现中，我们需要从pluginContext获取项目的实际资源路径
            // 这里简化处理，实际项目中需要更复杂的逻辑

            bundles.getOrPut(locale) {
                // 尝试从classpath加载资源文件
                val bundleName = "$basePath/$locale"
                ResourceBundle.getBundle(bundleName, Locale.forLanguageTag(locale))
            }
        } catch (e: Exception) {
            System.err.println("Failed to load resource bundle for locale $locale: ${e.message}")
            null
        }
    }

    /**
     * 根据键和语言从项目资源中获取翻译文本
     * @param pluginContext 编译器插件上下文
     * @param key 翻译键
     * @param locale 目标语言
     * @param basePath 资源基础路径
     */
    fun getTranslation(pluginContext: IrPluginContext, key: String, locale: String, basePath: String = "i18n"): String? {
        return try {
            val bundle = getResourceBundle(pluginContext, locale, basePath)
            if (bundle != null && bundle.containsKey(key)) {
                bundle.getString(key)
            } else {
                null
            }
        } catch (e: Exception) {
            System.err.println("Failed to get translation for key '$key' in locale $locale: ${e.message}")
            null
        }
    }

    /**
     * 根据源字符串和目标语言从项目资源中获取翻译文本
     * 用于直接将源字符串作为键来查找翻译
     * @param pluginContext 编译器插件上下文
     * @param source 源字符串（用作键）
     * @param targetLocale 目标语言
     * @param basePath 资源基础路径
     */
    fun translateString(pluginContext: IrPluginContext, source: String, targetLocale: String, basePath: String = "i18n"): String? {
        return getTranslation(pluginContext, source, targetLocale, basePath)
    }

    /**
     * 根据文件名、函数名、组件名、参数名和值生成国际化键
     * 格式: i18n_文件名_函数名_组件名_参数名_value
     */
    fun generateI18nKey(fileName: String?, functionName: String?, componentName: String?, paramName: String?, value: String): String {
        return buildString {
            append("i18n")
            if (fileName != null) {
                append("_")
                append(fileName)
            }
            if (functionName != null) {
                append("_")
                append(functionName)
            }
            if (componentName != null) {
                append("_")
                append(componentName)
            }
            if (paramName != null) {
                append("_")
                append(paramName)
            }
            append("_")
            append(value)
        }
    }

    /**
     * 根据文件名、函数名、组件名和值生成资源键
     * 格式: 文件名_函数名_组件名_参数名_value
     * 注意：这里参数名固定为"text"，因为在IR层面难以准确获取参数名
     */
    fun generateResourceKey(fileName: String, functionName: String?, componentName: String?, value: String): String {
        return buildString {
            append(fileName)
            if (functionName != null) {
                append("_")
                append(functionName)
            }
            if (componentName != null) {
                append("_")
                append(componentName)
            }
            append("_")
            // 参数名固定为"text"
            append("text")
            append("_")
            append(value)
        }
    }

    /**
     * 根据生成的键和目标语言从项目资源中获取翻译文本
     * @param pluginContext 编译器插件上下文
     * @param fileName 文件名
     * @param functionName 函数名
     * @param componentName 组件名
     * @param paramName 参数名
     * @param value 值
     * @param targetLocale 目标语言
     * @param basePath 资源基础路径
     */
    fun translateWithGeneratedKey(pluginContext: IrPluginContext, fileName: String?, functionName: String?, componentName: String?, paramName: String?, value: String, targetLocale: String, basePath: String = "i18n"): String? {
        val key = generateI18nKey(fileName, functionName, componentName, paramName, value)
        return getTranslation(pluginContext, key, targetLocale, basePath)
    }
}
