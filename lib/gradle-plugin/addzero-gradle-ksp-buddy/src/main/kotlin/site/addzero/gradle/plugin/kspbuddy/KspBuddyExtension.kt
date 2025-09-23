package site.addzero.gradle.plugin.kspbuddy

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property

/**
 * KspBuddy 插件扩展配置
 */
abstract class KspBuddyExtension {
    /**
     * KSP 配置参数映射
     */
    abstract val mustMap: MapProperty<String, String>

    /**
     * KSP 预编译配置脚本生成路径
     * 默认: build-logic/src/main/kotlin/conventions/generated
     */
    abstract val kspScriptOutputDir: Property<String>

    /**
     * SettingContext 生成配置
     */
    abstract val settingContext: Property<SettingContextConfig>

    /**
     * 是否启用预编译文件生成，默认为 true
     */
    abstract val enablePrecompile: Property<Boolean>

    init {
        // 设置默认值
        settingContext.convention(SettingContextConfig())
        enablePrecompile.convention(false)
    }
}

/**
 * SettingContext 生成配置
 */
data class SettingContextConfig(
    /**
     * 生成的 SettingContext 类名，默认为 "SettingContext"
     */
    val contextClassName: String = "SettingContext",

    /**
     * 生成的 Settings 数据类名，默认为 "Settings"
     */
    val settingsClassName: String = "Settings",

    /**
     * 生成文件的包名
     */
    val packageName: String = "site.addzero.context",

    /**
     * 生成文件的输出目录，相对于项目根目录
     */
    val outputDir: String = "src/main/kotlin",

    /**
     * 是否启用 SettingContext 生成，默认为 true
     */
    val enabled: Boolean = true
)
