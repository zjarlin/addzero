package site.addzero.themes

import androidx.compose.ui.graphics.Color

/**
 * 深色主题颜色配置，实现了 [ShadcnColors] 接口
 *
 * 定义了深色模式下所有组件使用的颜色常量，包括背景、前景、卡片、
 * 弹出框、主要、次要、静音、强调、危险、边框、输入框、环形、图表、
 * 侧边栏和通知栏等颜色。
 */
object DarkColors : ShadcnColors {
    override val background: Color = Color(0xFF0A0A0A)
    override val foreground: Color = Color(0xFFFAFAFA)
    override val card: Color = Color(0xFF171717)
    override val cardForeground: Color = Color(0xFFFAFAFA)
    override val popover: Color = Color(0xFF262626)
    override val popoverForeground: Color = Color(0xFFFAFAFA)
    override val primary: Color = Color(0xFFE5E5E5)
    override val primaryForeground: Color = Color(0xFF171717)
    override val secondary: Color = Color(0xFF262626)
    override val secondaryForeground: Color = Color(0xFFFAFAFA)
    override val muted: Color = Color(0xFF262626)
    override val mutedForeground: Color = Color(0xFFA1A1A1)
    override val accent: Color = Color(0xFF404040)
    override val accentForeground: Color = Color(0xFFFAFAFA)
    override val destructive: Color = Color(0xFFFF6467)
    override val destructiveForeground: Color = Color(0xFFFAFAFA)
    override val border: Color = Color(0xFF282828)
    override val input: Color = Color(0xFF343434)
    override val ring: Color = Color(0xFF737373)

    override val chart1: Color = Color(0xFF91C5FF)
    override val chart2: Color = Color(0xFF3A81F6)
    override val chart3: Color = Color(0xFF2563EF)
    override val chart4: Color = Color(0xFF1A4EDA)
    override val chart5: Color = Color(0xFF1F3FAD)

    override val sidebar: Color = Color(0xFF171717)
    override val sidebarForeground: Color = Color(0xFFFAFAFA)
    override val sidebarPrimary: Color = Color(0xFF1447E6) // 已更新
    override val sidebarPrimaryForeground: Color = Color(0xFFFAFAFA)
    override val sidebarAccent: Color = Color(0xFF262626)
    override val sidebarAccentForeground: Color = Color(0xFFFAFAFA)
    override val sidebarBorder: Color = Color(0xFF282828)
    override val sidebarRing: Color = Color(0xFF525252)
    override val snackbar: Color = Color(0xFF262626)
}
