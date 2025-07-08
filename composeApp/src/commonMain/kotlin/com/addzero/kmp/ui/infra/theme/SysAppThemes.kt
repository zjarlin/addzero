package com.addzero.kmp.ui.infra.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 渐变背景配置
 */
data class GradientConfig(
    val colors: List<Color>,
    val startX: Float = 0f,
    val startY: Float = 0f,
    val endX: Float = 1000f,
    val endY: Float = 1000f
)

/**
 * 应用主题类型
 */
//@Deprecated("改用字典")
enum class AppThemeType {
    LIGHT_DEFAULT,
    DARK_DEFAULT,
    LIGHT_BLUE,
    DARK_BLUE,
    LIGHT_GREEN,
    DARK_GREEN,
    LIGHT_PURPLE,
    DARK_PURPLE,
    // 新增炫彩主题
    GRADIENT_RAINBOW,
    GRADIENT_SUNSET,
    GRADIENT_OCEAN,
    GRADIENT_FOREST,
    GRADIENT_AURORA,
    GRADIENT_NEON;

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
            GRADIENT_RAINBOW -> "彩虹渐变"
            GRADIENT_SUNSET -> "日落渐变"
            GRADIENT_OCEAN -> "海洋渐变"
            GRADIENT_FOREST -> "森林渐变"
            GRADIENT_AURORA -> "极光渐变"
            GRADIENT_NEON -> "霓虹渐变"
        }
    }

    /**
     * 是否为暗色主题
     */
    fun isDark(): Boolean {
        return this == DARK_DEFAULT || this == DARK_BLUE || this == DARK_GREEN || this == DARK_PURPLE
    }

    /**
     * 是否为渐变主题
     */
    fun isGradient(): Boolean {
        return this == GRADIENT_RAINBOW || this == GRADIENT_SUNSET || this == GRADIENT_OCEAN ||
               this == GRADIENT_FOREST || this == GRADIENT_AURORA || this == GRADIENT_NEON
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

    // 炫彩渐变主题
    private val GradientRainbowScheme = lightColorScheme(
        primary = Color(0xFFFF6B9D),
        primaryContainer = Color(0xFFFFF0F5),
        secondary = Color(0xFF9B59B6),
        secondaryContainer = Color(0xFFF3E5F5),
        surface = Color(0xFFFFFBFE),
        surfaceContainer = Color(0xFFF7F2FA),
        background = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Color(0xFF1C1B1F),
        onBackground = Color(0xFF1C1B1F)
    )

    private val GradientSunsetScheme = lightColorScheme(
        primary = Color(0xFFFF6B35),
        primaryContainer = Color(0xFFFFF4F0),
        secondary = Color(0xFFFF8C42),
        secondaryContainer = Color(0xFFFFF7F3),
        surface = Color(0xFFFFFBFE),
        surfaceContainer = Color(0xFFFAF5F2),
        background = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Color(0xFF1C1B1F),
        onBackground = Color(0xFF1C1B1F)
    )

    private val GradientOceanScheme = lightColorScheme(
        primary = Color(0xFF0077BE),
        primaryContainer = Color(0xFFE6F3FF),
        secondary = Color(0xFF00A8CC),
        secondaryContainer = Color(0xFFE0F7FA),
        surface = Color(0xFFFFFBFE),
        surfaceContainer = Color(0xFFE8F4F8),
        background = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Color(0xFF1C1B1F),
        onBackground = Color(0xFF1C1B1F)
    )

    private val GradientForestScheme = lightColorScheme(
        primary = Color(0xFF2E7D32),
        primaryContainer = Color(0xFFE8F5E8),
        secondary = Color(0xFF4CAF50),
        secondaryContainer = Color(0xFFE8F5E8),
        surface = Color(0xFFFFFBFE),
        surfaceContainer = Color(0xFFE8F5E8),
        background = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Color(0xFF1C1B1F),
        onBackground = Color(0xFF1C1B1F)
    )

    private val GradientAuroraScheme = lightColorScheme(
        primary = Color(0xFF7C4DFF),
        primaryContainer = Color(0xFFF3E5F5),
        secondary = Color(0xFF00E676),
        secondaryContainer = Color(0xFFE8F5E8),
        surface = Color(0xFFFFFBFE),
        surfaceContainer = Color(0xFFF0F4FF),
        background = Color(0xFFFFFBFE),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onSurface = Color(0xFF1C1B1F),
        onBackground = Color(0xFF1C1B1F)
    )

    private val GradientNeonScheme = lightColorScheme(
        primary = Color(0xFFFF1744),
        primaryContainer = Color(0xFFFFF0F3),
        secondary = Color(0xFF00E5FF),
        secondaryContainer = Color(0xFFE0F7FA),
        surface = Color(0xFF121212),
        surfaceContainer = Color(0xFF1E1E1E),
        background = Color(0xFF121212),
        onPrimary = Color.White,
        onSecondary = Color.Black,
        onSurface = Color.White,
        onBackground = Color.White
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
            AppThemeType.GRADIENT_RAINBOW -> GradientRainbowScheme
            AppThemeType.GRADIENT_SUNSET -> GradientSunsetScheme
            AppThemeType.GRADIENT_OCEAN -> GradientOceanScheme
            AppThemeType.GRADIENT_FOREST -> GradientForestScheme
            AppThemeType.GRADIENT_AURORA -> GradientAuroraScheme
            AppThemeType.GRADIENT_NEON -> GradientNeonScheme
        }
    }

    /**
     * 获取渐变配置
     */
    fun getGradientConfig(themeType: AppThemeType): GradientConfig? {
        return when (themeType) {
            AppThemeType.GRADIENT_RAINBOW -> GradientConfig(
                colors = listOf(
                    Color(0xFFFF6B9D), // 粉色
                    Color(0xFF9B59B6), // 紫色
                    Color(0xFF3498DB), // 蓝色
                    Color(0xFF2ECC71), // 绿色
                    Color(0xFFF39C12), // 橙色
                    Color(0xFFE74C3C)  // 红色
                )
            )
            AppThemeType.GRADIENT_SUNSET -> GradientConfig(
                colors = listOf(
                    Color(0xFFFF6B35), // 橙红
                    Color(0xFFFF8C42), // 橙色
                    Color(0xFFFFA726), // 浅橙
                    Color(0xFFFFD54F)  // 黄色
                )
            )
            AppThemeType.GRADIENT_OCEAN -> GradientConfig(
                colors = listOf(
                    Color(0xFF0077BE), // 深蓝
                    Color(0xFF00A8CC), // 青色
                    Color(0xFF26C6DA), // 浅青
                    Color(0xFF80DEEA)  // 极浅青
                )
            )
            AppThemeType.GRADIENT_FOREST -> GradientConfig(
                colors = listOf(
                    Color(0xFF2E7D32), // 深绿
                    Color(0xFF4CAF50), // 绿色
                    Color(0xFF66BB6A), // 浅绿
                    Color(0xFF81C784)  // 极浅绿
                )
            )
            AppThemeType.GRADIENT_AURORA -> GradientConfig(
                colors = listOf(
                    Color(0xFF7C4DFF), // 紫色
                    Color(0xFF00E676), // 绿色
                    Color(0xFF00BCD4), // 青色
                    Color(0xFFE91E63)  // 粉色
                )
            )
            AppThemeType.GRADIENT_NEON -> GradientConfig(
                colors = listOf(
                    Color(0xFFFF1744), // 霓虹红
                    Color(0xFFFF6EC7), // 霓虹粉
                    Color(0xFF00E5FF), // 霓虹青
                    Color(0xFF76FF03)  // 霓虹绿
                )
            )
            else -> null
        }
    }
}

/**
 * 渐变背景组件
 */
@Composable
fun GradientBackground(
    gradientConfig: GradientConfig,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientConfig.colors,
                    start = androidx.compose.ui.geometry.Offset(
                        gradientConfig.startX,
                        gradientConfig.startY
                    ),
                    end = androidx.compose.ui.geometry.Offset(
                        gradientConfig.endX,
                        gradientConfig.endY
                    )
                )
            )
    ) {
        content()
    }
}
