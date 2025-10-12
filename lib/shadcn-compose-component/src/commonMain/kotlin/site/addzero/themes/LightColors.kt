package site.addzero.themes

import androidx.compose.ui.graphics.Color

/**
 * 浅色主题颜色配置，实现了 [ShadcnColors] 接口
 *
 * 定义了浅色模式下所有组件使用的颜色常量，包括背景、前景、卡片、
 * 弹出框、主要、次要、静音、强调、危险、边框、输入框、环形、图表、
 * 侧边栏和通知栏等颜色。
 */
object LightColors : ShadcnColors {
    override val background: Color = Color(0xFFFFFFFF)
    override val foreground: Color = Color(0xFF0A0A0A)
    override val card: Color = Color(0xFFFFFFFF)
    override val cardForeground: Color = Color(0xFF0A0A0A)
    override val popover: Color = Color(0xFFFFFFFF)
    override val popoverForeground: Color = Color(0xFF0A0A0A)
    override val primary: Color = Color(0xFF171717)
    override val primaryForeground: Color = Color(0xFFFAFAFA)
    override val secondary: Color = Color(0xFFF5F5F5)
    override val secondaryForeground: Color = Color(0xFF171717)
    override val muted: Color = Color(0xFFF5F5F5)
    override val mutedForeground: Color = Color(0xFF737373)
    override val accent: Color = Color(0xFFF5F5F5)
    override val accentForeground: Color = Color(0xFF171717)
    override val destructive: Color = Color(0xFFE7000B)
    override val destructiveForeground: Color = Color(0xFFFFFFFF)
    override val border: Color = Color(0xFFE5E5E5)
    override val input: Color = Color(0xFFE5E5E5)
    override val ring: Color = Color(0xFFA1A1A1)

    override val chart1: Color = Color(0xFFB2D4FF)
    override val chart2: Color = Color(0xFF3A81F6)
    override val chart3: Color = Color(0xFF2563EF)
    override val chart4: Color = Color(0xFF1A4EDA)
    override val chart5: Color = Color(0xFF1F3FAD)

    override val sidebar: Color = Color(0xFFFAFAFA)
    override val sidebarForeground: Color = Color(0xFF0A0A0A)
    override val sidebarPrimary: Color = Color(0xFF171717)
    override val sidebarPrimaryForeground: Color = Color(0xFFFAFAFA)
    override val sidebarAccent: Color = Color(0xFFF5F5F5)
    override val sidebarAccentForeground: Color = Color(0xFF171717)
    override val sidebarBorder: Color = Color(0xFFE5E5E5)
    override val sidebarRing: Color = Color(0xFFA1A1A1)
    override val snackbar: Color = Color(0xFFFFFFFF)
}
