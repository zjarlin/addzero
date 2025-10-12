package site.addzero.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

/**
 * 跟随系统的颜色配置类
 *
 * 根据系统当前的主题模式（浅色/深色）自动选择对应的颜色方案。
 * 当系统设置为浅色模式时使用 [LightColors]，深色模式时使用 [DarkColors]。
 *
 * 使用方式：
 * ```kotlin
 * @Composable
 * fun MyComponent() {
 *     val systemColors = SystemColors.current
 *
 *     // 使用系统颜色
 *     Box(
 *         modifier = Modifier
 *             .background(systemColors.background)
 *             .border(1.dp, systemColors.border)
 *     ) {
 *         Text(
 *             text = "跟随系统的文本",
 *             color = systemColors.foreground
 *         )
 *     }
 * }
 * ```
 */
class SystemColors private constructor(
    private val isDarkTheme: Boolean
) : ShadcnColors {

    override val background: Color
        get() = if (isDarkTheme) DarkColors.background else LightColors.background

    override val foreground: Color
        get() = if (isDarkTheme) DarkColors.foreground else LightColors.foreground

    override val card: Color
        get() = if (isDarkTheme) DarkColors.card else LightColors.card

    override val cardForeground: Color
        get() = if (isDarkTheme) DarkColors.cardForeground else LightColors.cardForeground

    override val popover: Color
        get() = if (isDarkTheme) DarkColors.popover else LightColors.popover

    override val popoverForeground: Color
        get() = if (isDarkTheme) DarkColors.popoverForeground else LightColors.popoverForeground

    override val primary: Color
        get() = if (isDarkTheme) DarkColors.primary else LightColors.primary

    override val primaryForeground: Color
        get() = if (isDarkTheme) DarkColors.primaryForeground else LightColors.primaryForeground

    override val secondary: Color
        get() = if (isDarkTheme) DarkColors.secondary else LightColors.secondary

    override val secondaryForeground: Color
        get() = if (isDarkTheme) DarkColors.secondaryForeground else LightColors.secondaryForeground

    override val muted: Color
        get() = if (isDarkTheme) DarkColors.muted else LightColors.muted

    override val mutedForeground: Color
        get() = if (isDarkTheme) DarkColors.mutedForeground else LightColors.mutedForeground

    override val accent: Color
        get() = if (isDarkTheme) DarkColors.accent else LightColors.accent

    override val accentForeground: Color
        get() = if (isDarkTheme) DarkColors.accentForeground else LightColors.accentForeground

    override val destructive: Color
        get() = if (isDarkTheme) DarkColors.destructive else LightColors.destructive

    override val destructiveForeground: Color
        get() = if (isDarkTheme) DarkColors.destructiveForeground else LightColors.destructiveForeground

    override val border: Color
        get() = if (isDarkTheme) DarkColors.border else LightColors.border

    override val input: Color
        get() = if (isDarkTheme) DarkColors.input else LightColors.input

    override val ring: Color
        get() = if (isDarkTheme) DarkColors.ring else LightColors.ring

    override val chart1: Color
        get() = if (isDarkTheme) DarkColors.chart1 else LightColors.chart1

    override val chart2: Color
        get() = if (isDarkTheme) DarkColors.chart2 else LightColors.chart2

    override val chart3: Color
        get() = if (isDarkTheme) DarkColors.chart3 else LightColors.chart3

    override val chart4: Color
        get() = if (isDarkTheme) DarkColors.chart4 else LightColors.chart4

    override val chart5: Color
        get() = if (isDarkTheme) DarkColors.chart5 else LightColors.chart5

    override val sidebar: Color
        get() = if (isDarkTheme) DarkColors.sidebar else LightColors.sidebar

    override val sidebarForeground: Color
        get() = if (isDarkTheme) DarkColors.sidebarForeground else LightColors.sidebarForeground

    override val sidebarPrimary: Color
        get() = if (isDarkTheme) DarkColors.sidebarPrimary else LightColors.sidebarPrimary

    override val sidebarPrimaryForeground: Color
        get() = if (isDarkTheme) DarkColors.sidebarPrimaryForeground else LightColors.sidebarPrimaryForeground

    override val sidebarAccent: Color
        get() = if (isDarkTheme) DarkColors.sidebarAccent else LightColors.sidebarAccent

    override val sidebarAccentForeground: Color
        get() = if (isDarkTheme) DarkColors.sidebarAccentForeground else LightColors.sidebarAccentForeground

    override val sidebarBorder: Color
        get() = if (isDarkTheme) DarkColors.sidebarBorder else LightColors.sidebarBorder

    override val sidebarRing: Color
        get() = if (isDarkTheme) DarkColors.sidebarRing else LightColors.sidebarRing

    override val snackbar: Color
        get() = if (isDarkTheme) DarkColors.snackbar else LightColors.snackbar

    /**
     * 检查当前是否为深色主题
     */
    val isDarkThemeMode: Boolean
        get() = isDarkTheme

    /**
     * 检查当前是否为浅色主题
     */
    val isLightThemeMode: Boolean
        get() = !isDarkTheme

    companion object {

        /**
         * 创建跟随系统的颜色配置实例
         *
         * @param isDarkTheme 是否为深色主题
         * @return [SystemColors] 实例
         */
        fun create(isDarkTheme: Boolean): SystemColors = SystemColors(isDarkTheme)

        /**
         * 获取当前组件树中的系统颜色配置
         *
         * 这是一个 Composable 函数，会自动检测系统主题并返回相应的颜色配置。
         * 只能在 @Composable 函数中调用。
         *
         * @return 当前系统主题对应的 [SystemColors] 实例
         */
        @Composable
        fun current(): SystemColors {
            val isDarkTheme = isSystemInDarkTheme()
            return remember(isDarkTheme) { create(isDarkTheme) }
        }
    }
}

/**
 * 便捷的 Composable 函数，用于获取当前系统主题的颜色配置
 *
 * 使用方式：
 * ```kotlin
 * @Composable
 * fun MyComponent() {
 *     val colors = rememberSystemColors()
 *
 *     // 使用颜色
 *     Surface(color = colors.background) {
 *         Text(
 *             text = "Hello",
 *             color = colors.foreground
 *         )
 *     }
 * }
 * ```
 *
 * @return 当前系统主题对应的 [SystemColors] 实例
 */
@Composable
fun rememberSystemColors(): SystemColors = SystemColors.current()
