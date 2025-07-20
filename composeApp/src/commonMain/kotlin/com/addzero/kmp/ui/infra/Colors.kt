package com.addzero.kmp.ui.infra

import androidx.compose.ui.graphics.Color

/**
 * 应用全局颜色配置
 * 收集所有硬编码的颜色值，统一管理
 */
object Colors {
    // ==================== 荧光卡片颜色 ====================

    /** JetBrains 荧光绿 */
    val JetBrainsGreen = Color(0xFF00FF88)

    /** 荧光紫色 */
    val GlowPurple = Color(0xFF6C5CE7)

    /** 荧光青色 */
    val GlowCyan = Color(0xFF00CEC9)

    /** 荧光橙色 */
    val GlowOrange = Color(0xFFE17055)

    /** 荧光黄色 */
    val GlowYellow = Color(0xFFFDCB6E)

    /** 荧光红色 */
    val GlowRed = Color(0xFFFF6B6B)

    /** 荧光蓝色 */
    val GlowBlue = Color(0xFF74B9FF)

    /** 荧光粉色 */
    val GlowPink = Color(0xFFE84393)


    // ==================== 基础颜色 ====================

    /** 纯黑色 */
    val Black = Color.Black

    /** 透明色 */
    val Transparent = Color.Transparent

    // ==================== 主题颜色 ====================

    // 蓝色系
    /** 深蓝色 - 主要色 */
    val DeepBlue = Color(0xFF0D65C2)

    /** 浅蓝色容器 */
    val LightBlueContainer = Color(0xFFADD8F7)

    /** 更深的次要蓝色 */
    val DarkSecondaryBlue = Color(0xFF0A4B9A)

    /** 次要蓝色容器 */
    val SecondaryBlueContainer = Color(0xFFBBD6F2)

    /** 浅蓝色表面 */
    val LightBlueSurface = Color(0xFFE9F5FF)

    /** 更浅的蓝色背景 */
    val VeryLightBlueBackground = Color(0xFFF5FAFF)

    /** 暗色蓝色主要色 */
    val DarkBluePrimary = Color(0xFF90CAF9)

    /** 暗色蓝色容器 */
    val DarkBluePrimaryContainer = Color(0xFF1565C0)

    /** 暗色蓝色次要色 */
    val DarkBlueSecondary = Color(0xFF64B5F6)

    /** 暗色蓝色次要容器 */
    val DarkBlueSecondaryContainer = Color(0xFF0D47A1)

    // 绿色系
    /** 绿色主要色 */
    val GreenPrimary = Color(0xFF43A047)

    /** 绿色容器 */
    val GreenContainer = Color(0xFFC8E6C9)

    /** 深绿色次要色 */
    val DarkGreenSecondary = Color(0xFF2E7D32)

    /** 绿色次要容器 */
    val GreenSecondaryContainer = Color(0xFFCEEBD0)

    /** 暗色绿色主要色 */
    val DarkGreenPrimary = Color(0xFF81C784)

    /** 暗色绿色容器 */
    val DarkGreenPrimaryContainer = Color(0xFF2E7D32)

    /** 暗色绿色次要色 */
    val DarkGreenSecondaryColor = Color(0xFFA5D6A7)

    /** 暗色绿色次要容器 */
    val DarkGreenSecondaryContainer = Color(0xFF1B5E20)

    // 紫色系
    /** 紫色主要色 */
    val PurplePrimary = Color(0xFF7B1FA2)

    /** 紫色容器 */
    val PurpleContainer = Color(0xFFE1BEE7)

    /** 深紫色次要色 */
    val DarkPurpleSecondary = Color(0xFF6A1B9A)

    /** 紫色次要容器 */
    val PurpleSecondaryContainer = Color(0xFFE9CAF0)

    /** 暗色紫色主要色 */
    val DarkPurplePrimary = Color(0xFFCE93D8)

    /** 暗色紫色容器 */
    val DarkPurplePrimaryContainer = Color(0xFF6A1B9A)

    /** 暗色紫色次要色 */
    val DarkPurpleSecondaryColor = Color(0xFFBA68C8)

    /** 暗色紫色次要容器 */
    val DarkPurpleSecondaryContainer = Color(0xFF4A148C)

    // ==================== 渐变主题颜色 ====================

    // 彩虹渐变
    /** 彩虹渐变 - 粉色 */
    val RainbowPink = Color(0xFFFF6B9D)

    /** 彩虹渐变 - 紫色 */
    val RainbowPurple = Color(0xFF9B59B6)

    /** 彩虹渐变 - 蓝色 */
    val RainbowBlue = Color(0xFF3498DB)

    /** 彩虹渐变 - 绿色 */
    val RainbowGreen = Color(0xFF2ECC71)

    /** 彩虹渐变 - 橙色 */
    val RainbowOrange = Color(0xFFF39C12)

    /** 彩虹渐变 - 红色 */
    val RainbowRed = Color(0xFFE74C3C)

    // 日落渐变
    /** 日落渐变 - 橙红 */
    val SunsetOrangeRed = Color(0xFFFF6B35)

    /** 日落渐变 - 橙色 */
    val SunsetOrange = Color(0xFFFF8C42)

    /** 日落渐变 - 浅橙 */
    val SunsetLightOrange = Color(0xFFFFA726)

    /** 日落渐变 - 黄色 */
    val SunsetYellow = Color(0xFFFFD54F)

    // 海洋渐变
    /** 海洋渐变 - 深蓝 */
    val OceanDeepBlue = Color(0xFF0077BE)

    /** 海洋渐变 - 青色 */
    val OceanCyan = Color(0xFF00A8CC)

    /** 海洋渐变 - 浅青 */
    val OceanLightCyan = Color(0xFF26C6DA)

    /** 海洋渐变 - 极浅青 */
    val OceanVeryLightCyan = Color(0xFF80DEEA)

    // 森林渐变
    /** 森林渐变 - 深绿 */
    val ForestDarkGreen = Color(0xFF2E7D32)

    /** 森林渐变 - 绿色 */
    val ForestGreen = Color(0xFF4CAF50)

    /** 森林渐变 - 浅绿 */
    val ForestLightGreen = Color(0xFF66BB6A)

    /** 森林渐变 - 极浅绿 */
    val ForestVeryLightGreen = Color(0xFF81C784)

    // 极光渐变
    /** 极光渐变 - 紫色 */
    val AuroraPurple = Color(0xFF7C4DFF)

    /** 极光渐变 - 绿色 */
    val AuroraGreen = Color(0xFF00E676)

    /** 极光渐变 - 青色 */
    val AuroraCyan = Color(0xFF00BCD4)

    /** 极光渐变 - 粉色 */
    val AuroraPink = Color(0xFFE91E63)

    // 霓虹渐变
    /** 霓虹渐变 - 红色 */
    val NeonRed = Color(0xFFFF1744)

    /** 霓虹渐变 - 粉色 */
    val NeonPink = Color(0xFFFF6EC7)

    /** 霓虹渐变 - 青色 */
    val NeonCyan = Color(0xFF00E5FF)

    /** 霓虹渐变 - 绿色 */
    val NeonGreen = Color(0xFF76FF03)


    // ==================== 文件类型颜色 ====================

    /** 图片文件颜色 - 绿色 */
    val FileImageColor = Color(0xFF4CAF50)

    /** 音频文件颜色 - 蓝色 */
    val FileAudioColor = Color(0xFF2196F3)

    /** 视频文件颜色 - 粉色 */
    val FileVideoColor = Color(0xFFE91E63)

    /** 代码文件颜色 - 蓝灰色 */
    val FileCodeColor = Color(0xFF607D8B)

    /** PDF文件颜色 - 红色 */
    val FilePdfColor = Color(0xFFF44336)

    // ==================== 卡片背景颜色 ====================

    /** 卡片默认背景色 */
    val CardDefaultBackground = Color(0xFF1A1A1F).copy(alpha = 0.9f)

    /** 卡片渐变背景色1 */
    val CardGradient1 = Color(0xFF2D1B69).copy(alpha = 0.8f)

    /** 卡片渐变背景色2 */
    val CardGradient2 = Color(0xFF1A1A1F).copy(alpha = 0.9f)

    /** 卡片渐变背景色3 */
    val CardGradient3 = Color(0xFF0F4C3A).copy(alpha = 0.6f)

    // ==================== 文本颜色 ====================

    /** 主要文本颜色 - 白色 */
    val TextPrimary = Color.White

    /** 次要文本颜色 - 80% 透明度白色 */
    val TextSecondary = Color.White.copy(alpha = 0.8f)

    /** 三级文本颜色 - 70% 透明度白色 */
    val TextTertiary = Color.White.copy(alpha = 0.7f)

    /** 四级文本颜色 - 60% 透明度白色 */
    val TextQuaternary = Color.White.copy(alpha = 0.6f)

    /** 按钮文本颜色 - 黑色 */
    val ButtonTextColor = Color.Black

    /** 按钮背景颜色 - 白色 */
    val ButtonBackgroundColor = Color.White

    // ==================== 渐变主题方案颜色 ====================

    /** 渐变主题表面颜色 */
    val GradientSurface = Color(0xFFFFFBFE)

    /** 渐变主题表面容器颜色 */
    val GradientSurfaceContainer = Color(0xFFF7F2FA)

    /** 渐变主题背景颜色 */
    val GradientBackground = Color(0xFFFFFBFE)

    /** 渐变主题文本颜色 */
    val GradientOnSurface = Color(0xFF1C1B1F)

    /** 渐变主题背景文本颜色 */
    val GradientOnBackground = Color(0xFF1C1B1F)

    // 特定渐变主题的容器颜色
    /** 彩虹主题容器颜色 */
    val RainbowContainer = Color(0xFFFFF0F5)

    /** 日落主题容器颜色 */
    val SunsetContainer = Color(0xFFFFF4F0)

    /** 海洋主题容器颜色 */
    val OceanContainer = Color(0xFFE6F3FF)

    /** 森林主题容器颜色 */
    val ForestContainer = Color(0xFFE8F5E8)

    /** 极光主题容器颜色 */
    val AuroraContainer = Color(0xFFF3E5F5)

    /** 霓虹主题容器颜色 */
    val NeonContainer = Color(0xFFFFF0F3)

    // 霓虹主题特殊颜色
    /** 霓虹主题表面颜色 */
    val NeonSurface = Color(0xFF121212)

    /** 霓虹主题表面容器颜色 */
    val NeonSurfaceContainer = Color(0xFF1E1E1E)

    /** 霓虹主题背景颜色 */
    val NeonBackground = Color(0xFF121212)
}
