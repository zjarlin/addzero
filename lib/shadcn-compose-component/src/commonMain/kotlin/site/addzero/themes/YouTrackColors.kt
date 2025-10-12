package site.addzero.themes

import androidx.compose.ui.graphics.Color

/**
 * YouTrack 风格的暗色主题颜色配置
 *
 * 基于 YouTrack 的暗夜微光蓝设计风格，提供高级感的深色主题
 * 背景色采用深邃的暗蓝色调，配合微妙的蓝色光晕效果
 */
object YouTrackColors : ShadcnColors {
    // 主要背景色 - 暗夜微光蓝
    override val background: Color = Color(0xFF0F1419)
    override val foreground: Color = Color(0xFFE1E8F7)

    // 卡片和弹出框 - 玻璃态效果
    override val card: Color = Color(0xFF1A1F2E).copy(alpha = 0.6f)
    override val cardForeground: Color = Color(0xFFE1E8F7)
    override val popover: Color = Color(0xFF1A1F2E).copy(alpha = 0.9f)
    override val popoverForeground: Color = Color(0xFFE1E8F7)

    // 主要颜色 - 亮蓝色光晕
    override val primary: Color = Color(0xFF4A9EFF)
    override val primaryForeground: Color = Color(0xFF0F1419)

    // 次要颜色
    override val secondary: Color = Color(0xFF2A2E4A)
    override val secondaryForeground: Color = Color(0xFFE1E8F7)

    // 静音颜色
    override val muted: Color = Color(0xFF1E2139)
    override val mutedForeground: Color = Color(0xFF8892B0)

    // 强调颜色
    override val accent: Color = Color(0xFF3B82F6).copy(alpha = 0.2f)
    override val accentForeground: Color = Color(0xFF4A9EFF)

    // 危险颜色
    override val destructive: Color = Color(0xFFEF4444)
    override val destructiveForeground: Color = Color(0xFFFFFFFF)

    // 边框和输入框
    override val border: Color = Color(0xFF2D3561).copy(alpha = 0.6f)
    override val input: Color = Color(0xFF1E2139)
    override val ring: Color = Color(0xFF4A9EFF).copy(alpha = 0.5f)

    // 图表颜色 - 蓝色系列
    override val chart1: Color = Color(0xFF4A9EFF)
    override val chart2: Color = Color(0xFF6366F1)
    override val chart3: Color = Color(0xFF818CF8)
    override val chart4: Color = Color(0xFFA5B4FC)
    override val chart5: Color = Color(0xFFC4B5FD)

    // 侧边栏专属颜色
    override val sidebar: Color = Color(0xFF0F1419)
    override val sidebarForeground: Color = Color(0xFFE1E8F7)
    override val sidebarPrimary: Color = Color(0xFF4A9EFF)
    override val sidebarPrimaryForeground: Color = Color(0xFFFFFFFF)
    override val sidebarAccent: Color = Color(0xFF2A2E4A)
    override val sidebarAccentForeground: Color = Color(0xFF4A9EFF)
    override val sidebarBorder: Color = Color(0xFF2D3561).copy(alpha = 0.3f)
    override val sidebarRing: Color = Color(0xFF4A9EFF).copy(alpha = 0.3f)

    // 通知栏
    override val snackbar: Color = Color(0xFF1A1F2E)

    // YouTrack 特有的高级感颜色
    val youTrackGlow: Color = Color(0xFF4A9EFF).copy(alpha = 0.15f)
    val youTrackSurface: Color = Color(0xFF1A1F2E)
    val youTrackBorder: Color = Color(0xFF2D3561)
    val youTrackHover: Color = Color(0xFF3B82F6).copy(alpha = 0.1f)
    val youTrackSelected: Color = Color(0xFF4A9EFF).copy(alpha = 0.2f)
    val youTrackText: Color = Color(0xFFE1E8F7)
    val youTrackTextSecondary: Color = Color(0xFF8892B0)
}

/**
 * YouTrack 风格的颜色扩展函数
 */
val ShadcnColors.youTrackGlow: Color
    get() = when (this) {
        is YouTrackColors -> youTrackGlow
        is YouTrackLightColors -> youTrackGlow
        else -> Color.Transparent
    }

val ShadcnColors.youTrackSurface: Color
    get() = when (this) {
        is YouTrackColors -> youTrackSurface
        is YouTrackLightColors -> youTrackSurface
        else -> Color.Transparent
    }

val ShadcnColors.youTrackBorder: Color
    get() = when (this) {
        is YouTrackColors -> youTrackBorder
        is YouTrackLightColors -> youTrackBorder
        else -> Color.Transparent
    }

val ShadcnColors.youTrackHover: Color
    get() = when (this) {
        is YouTrackColors -> youTrackHover
        is YouTrackLightColors -> youTrackHover
        else -> Color.Transparent
    }

val ShadcnColors.youTrackSelected: Color
    get() = when (this) {
        is YouTrackColors -> youTrackSelected
        is YouTrackLightColors -> youTrackSelected
        else -> Color.Transparent
    }

val ShadcnColors.youTrackText: Color
    get() = when (this) {
        is YouTrackColors -> youTrackText
        is YouTrackLightColors -> youTrackText
        else -> Color.Unspecified
    }

val ShadcnColors.youTrackTextSecondary: Color
    get() = when (this) {
        is YouTrackColors -> youTrackTextSecondary
        is YouTrackLightColors -> youTrackTextSecondary
        else -> Color.Unspecified
    }

/**
 * YouTrack 风格的浅色主题颜色配置
 */
object YouTrackLightColors : ShadcnColors {
    // 主要背景色 - 明亮白色
    override val background: Color = Color(0xFFFFFFFF)
    override val foreground: Color = Color(0xFF0F172A)

    // 卡片和弹出框
    override val card: Color = Color(0xFFF8FAFC)
    override val cardForeground: Color = Color(0xFF0F172A)
    override val popover: Color = Color(0xFFFFFFFF)
    override val popoverForeground: Color = Color(0xFF0F172A)

    // 主要颜色 - 亮蓝色
    override val primary: Color = Color(0xFF0EA5E9)
    override val primaryForeground: Color = Color(0xFFFFFFFF)

    // 次要颜色
    override val secondary: Color = Color(0xFFF1F5F9)
    override val secondaryForeground: Color = Color(0xFF0F172A)

    // 静音颜色
    override val muted: Color = Color(0xFFF8FAFC)
    override val mutedForeground: Color = Color(0xFF64748B)

    // 强调颜色
    override val accent: Color = Color(0xFF0EA5E9).copy(alpha = 0.1f)
    override val accentForeground: Color = Color(0xFF0EA5E9)

    // 危险颜色
    override val destructive: Color = Color(0xFFEF4444)
    override val destructiveForeground: Color = Color(0xFFFFFFFF)

    // 边框和输入框
    override val border: Color = Color(0xFFE2E8F0)
    override val input: Color = Color(0xFFF8FAFC)
    override val ring: Color = Color(0xFF0EA5E9).copy(alpha = 0.3f)

    // 图表颜色 - 蓝色系列
    override val chart1: Color = Color(0xFF0EA5E9)
    override val chart2: Color = Color(0xFF6366F1)
    override val chart3: Color = Color(0xFF818CF8)
    override val chart4: Color = Color(0xFFA5B4FC)
    override val chart5: Color = Color(0xFFC4B5FD)

    // 侧边栏专属颜色
    override val sidebar: Color = Color(0xFFF8FAFC)
    override val sidebarForeground: Color = Color(0xFF0F172A)
    override val sidebarPrimary: Color = Color(0xFF0EA5E9)
    override val sidebarPrimaryForeground: Color = Color(0xFFFFFFFF)
    override val sidebarAccent: Color = Color(0xFFF1F5F9)
    override val sidebarAccentForeground: Color = Color(0xFF0EA5E9)
    override val sidebarBorder: Color = Color(0xFFE2E8F0)
    override val sidebarRing: Color = Color(0xFF0EA5E9).copy(alpha = 0.3f)

    // 通知栏
    override val snackbar: Color = Color(0xFFFFFFFF)

    // YouTrack 浅色主题的高级感颜色
    val youTrackGlow: Color = Color(0xFF0EA5E9).copy(alpha = 0.08f)
    val youTrackSurface: Color = Color(0xFFF8FAFC)
    val youTrackBorder: Color = Color(0xFFE2E8F0)
    val youTrackHover: Color = Color(0xFF0EA5E9).copy(alpha = 0.05f)
    val youTrackSelected: Color = Color(0xFF0EA5E9).copy(alpha = 0.1f)
    val youTrackText: Color = Color(0xFF0F172A)
    val youTrackTextSecondary: Color = Color(0xFF64748B)
}