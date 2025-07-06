package com.addzero.kmp.ui.infra.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * 应用主题类型
 */
@Deprecated("改用字典")
enum class AppThemeType {
    LIGHT_DEFAULT,
    DARK_DEFAULT,
    LIGHT_BLUE,
    DARK_BLUE,
    LIGHT_GREEN,
    DARK_GREEN,
    LIGHT_PURPLE,
    DARK_PURPLE;

    /**
     * 获取主题名称
     */
    fun getDisplayName(): String {
        return when (this) {
            LIGHT_DEFAULT -> "默认亮色"
            DARK_DEFAULT -> "默认暗色"
            LIGHT_BLUE -> "蓝色亮色"
            DARK_BLUE -> "蓝色暗色"
            LIGHT_GREEN -> "绿色亮色"
            DARK_GREEN -> "绿色暗色"
            LIGHT_PURPLE -> "紫色亮色"
            DARK_PURPLE -> "紫色暗色"
        }
    }

    /**
     * 是否为暗色主题
     */
    fun isDark(): Boolean {
        return this == DARK_DEFAULT || this == DARK_BLUE || this == DARK_GREEN || this == DARK_PURPLE
    }
}

/**
 * 应用主题配置
 */
object AppThemes {

    // 默认亮色主题
    private val LightDefaultScheme = lightColorScheme()

    // 默认暗色主题
    private val DarkDefaultScheme = darkColorScheme()

    // 蓝色主题
    private val LightBlueScheme = lightColorScheme(
        primary = Color(0xFF0D65C2),         // 更深的蓝色
        primaryContainer = Color(0xFFADD8F7), // 浅蓝色容器
        secondary = Color(0xFF0A4B9A),        // 更深的次要色
        secondaryContainer = Color(0xFFBBD6F2), // 次要容器色
        surface = Color(0xFFE9F5FF),         // 浅蓝色表面
        background = Color(0xFFF5FAFF),      // 更浅的蓝色背景
        onPrimary = Color.White,
        onSecondary = Color.White
    )

    private val DarkBlueScheme = darkColorScheme(
        primary = Color(0xFF90CAF9),
        primaryContainer = Color(0xFF1565C0),
        secondary = Color(0xFF64B5F6),
        secondaryContainer = Color(0xFF0D47A1),
        onPrimary = Color.Black,
        onSecondary = Color.Black
    )

    // 绿色主题
    private val LightGreenScheme = lightColorScheme(
        primary = Color(0xFF43A047),
        primaryContainer = Color(0xFFC8E6C9),
        secondary = Color(0xFF2E7D32),
        secondaryContainer = Color(0xFFCEEBD0),
        onPrimary = Color.White,
        onSecondary = Color.White
    )

    private val DarkGreenScheme = darkColorScheme(
        primary = Color(0xFF81C784),
        primaryContainer = Color(0xFF2E7D32),
        secondary = Color(0xFFA5D6A7),
        secondaryContainer = Color(0xFF1B5E20),
        onPrimary = Color.Black,
        onSecondary = Color.Black
    )

    // 紫色主题
    private val LightPurpleScheme = lightColorScheme(
        primary = Color(0xFF7B1FA2),
        primaryContainer = Color(0xFFE1BEE7),
        secondary = Color(0xFF6A1B9A),
        secondaryContainer = Color(0xFFE9CAF0),
        onPrimary = Color.White,
        onSecondary = Color.White
    )

    private val DarkPurpleScheme = darkColorScheme(
        primary = Color(0xFFCE93D8),
        primaryContainer = Color(0xFF6A1B9A),
        secondary = Color(0xFFBA68C8),
        secondaryContainer = Color(0xFF4A148C),
        onPrimary = Color.Black,
        onSecondary = Color.Black
    )

    /**
     * 根据主题类型获取对应的颜色方案
     */
    fun getColorScheme(themeType: AppThemeType): ColorScheme {
        return when (themeType) {


            AppThemeType.LIGHT_DEFAULT -> LightDefaultScheme
            AppThemeType.DARK_DEFAULT -> DarkDefaultScheme
            AppThemeType.LIGHT_BLUE -> LightBlueScheme
            AppThemeType.DARK_BLUE -> DarkBlueScheme
            AppThemeType.LIGHT_GREEN -> LightGreenScheme
            AppThemeType.DARK_GREEN -> DarkGreenScheme
            AppThemeType.LIGHT_PURPLE -> LightPurpleScheme
            AppThemeType.DARK_PURPLE -> DarkPurpleScheme
        }
    }
}
