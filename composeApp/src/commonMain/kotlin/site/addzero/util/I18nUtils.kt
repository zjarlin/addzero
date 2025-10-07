package site.addzero.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.text.intl.Locale

/**
 * 国际化工具类 - 适用于KMP项目 (编译时生成资源接口)
 */
object I18nUtils {
    var currentLanguage by mutableStateOf("")

    init {
        // 初始化时设置为系统当前语言
        currentLanguage = Locale.current.language
    }

    /**
     * 获取支持的语言列表
     */
    fun getSupportedLanguages(): List<String> {
        return listOf("zh", "en") // 支持中文和英文，可根据需要扩展
    }

    /**
     * 检查语言是否受支持
     */
    fun isLanguageSupported(language: String): Boolean {
        return language in getSupportedLanguages()
    }

    /**
     * 根据语言代码获取语言名称
     */
    fun getLanguageDisplayName(languageCode: String): String {
        return when (languageCode) {
            "zh" -> "中文"
            "en" -> "English"
            else -> languageCode
        }
    }
}

/**
 * 自动编译生成的I18n资源接口
 * 开发者只需要定义接口，具体实现会根据资源文件自动生成
 */
interface AppStringResources {
    val welcomeMessage: String
    val goodbyeMessage: String
    val helloUser: String
    val settings: String
    val language: String
    val counter: String
    val increaseCount: String
    val reset: String
    val currentLanguage: String
}

/**
 * 中文资源实现 - 可以通过编译时注解处理器生成
 */
object ChineseResources : AppStringResources {
    override val welcomeMessage: String get() = "欢迎使用我们的应用"
    override val goodbyeMessage: String get() = "再见"
    override val helloUser: String get() = "你好, {0}!"
    override val settings: String get() = "设置"
    override val language: String get() = "语言"
    override val counter: String get() = "计数器: {0}"
    override val increaseCount: String get() = "增加计数"
    override val reset: String get() = "重置"
    override val currentLanguage: String get() = "当前语言: {0}"
}

/**
 * 英文资源实现 - 可以通过编译时注解处理器生成
 */
object EnglishResources : AppStringResources {
    override val welcomeMessage: String get() = "Welcome to our application"
    override val goodbyeMessage: String get() = "Goodbye"
    override val helloUser: String get() = "Hello, {0}!"
    override val settings: String get() = "Settings"
    override val language: String get() = "Language"
    override val counter: String get() = "Counter: {0}"
    override val increaseCount: String get() = "Increase Count"
    override val reset: String get() = "Reset"
    override val currentLanguage: String get() = "Current Language: {0}"
}

/**
 * 根据语言代码获取对应的字符串资源
 */
fun getAppStringResources(language: String): AppStringResources {
    return when (language.lowercase()) {
        "zh" -> ChineseResources
        "en" -> EnglishResources
        else -> ChineseResources // 默认使用中文
    }
}

/**
 * CompositionLocal 用于提供本地化资源
 */
val LocalI18n = staticCompositionLocalOf<AppStringResources> {
    ChineseResources // 默认使用中文资源
}

/**
 * I18n Provider组件，用于包装应用程序并提供本地化资源
 */
@Composable
fun I18nProvider(
    language: String? = null, // 如果为null，则使用系统默认语言
    content: @Composable () -> Unit
) {
    val currentLanguage = language ?: Locale.current.language
    val stringResources = remember(currentLanguage) {
        getAppStringResources(currentLanguage)
    }

    CompositionLocalProvider(LocalI18n provides stringResources) {
        content()
    }
}

/**
 * 直接访问资源属性的扩展函数
 */
@Composable
fun t(resource: AppStringResources.() -> String): String {
    val resources = LocalI18n.current
    return resources.resource()
}

/**
 * 带参数的翻译函数，用于格式化字符串
 */
@Composable
fun t(resource: AppStringResources.() -> String, vararg formatArgs: Any): String {
    val translatedString = t(resource)
    return try {
        formatArgs.foldIndexed(translatedString) { index, acc, arg ->
            acc.replace("{${index}}", arg.toString())
        }
    } catch (e: Exception) {
        translatedString
    }
}

/**
 * 全局翻译函数（非Composable环境）
 * 注意：在Composable环境中请使用上面的t函数
 */
@ReadOnlyComposable
fun translate(resource: AppStringResources.() -> String): String {
    // 在非Composable环境中使用默认语言
    val resources = getAppStringResources(I18nUtils.currentLanguage)
    return resources.resource()
}

/**
 * 带参数的全局翻译函数（非Composable环境）
 */
@ReadOnlyComposable
fun translate(resource: AppStringResources.() -> String, vararg formatArgs: Any): String {
    val translatedString = translate(resource)
    return try {
        formatArgs.foldIndexed(translatedString) { index, acc, arg ->
            acc.replace("{${index}}", arg.toString())
        }
    } catch (e: Exception) {
        translatedString
    }
}
