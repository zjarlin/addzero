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
import com.addzero.kmp.ui.infra.Colors

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
        primary = Colors.DeepBlue,
        primaryContainer = Colors.LightBlueContainer,
        secondary = Colors.DarkSecondaryBlue,
        secondaryContainer = Colors.SecondaryBlueContainer,
        surface = Colors.LightBlueSurface,
        background = Colors.VeryLightBlueBackground,
        onPrimary = Color.White,
        onSecondary = Color.White
    )

    private val DarkBlueScheme = darkColorScheme(
        primary = Colors.DarkBluePrimary,
        primaryContainer = Colors.DarkBluePrimaryContainer,
        secondary = Colors.DarkBlueSecondary,
        secondaryContainer = Colors.DarkBlueSecondaryContainer,
        onPrimary = Colors.Black,
        onSecondary = Colors.Black
    )

    // 绿色主题
    private val LightGreenScheme = lightColorScheme(
        primary = Colors.GreenPrimary,
        primaryContainer = Colors.GreenContainer,
        secondary = Colors.DarkGreenSecondary,
        secondaryContainer = Colors.GreenSecondaryContainer,
        onPrimary = Color.White,
        onSecondary = Color.White
    )

    private val DarkGreenScheme = darkColorScheme(
        primary = Colors.DarkGreenPrimary,
        primaryContainer = Colors.DarkGreenPrimaryContainer,
        secondary = Colors.DarkGreenSecondaryColor,
        secondaryContainer = Colors.DarkGreenSecondaryContainer,
        onPrimary = Colors.Black,
        onSecondary = Colors.Black
    )

    // 紫色主题
    private val LightPurpleScheme = lightColorScheme(
        primary = Colors.PurplePrimary,
        primaryContainer = Colors.PurpleContainer,
        secondary = Colors.DarkPurpleSecondary,
        secondaryContainer = Colors.PurpleSecondaryContainer,
        onPrimary = Color.White,
        onSecondary = Color.White
    )

    private val DarkPurpleScheme = darkColorScheme(
        primary = Colors.DarkPurplePrimary,
        primaryContainer = Colors.DarkPurplePrimaryContainer,
        secondary = Colors.DarkPurpleSecondaryColor,
        secondaryContainer = Colors.DarkPurpleSecondaryContainer,
        onPrimary = Colors.Black,
        onSecondary = Colors.Black
    )

    // 炫彩渐变主题
    private val GradientRainbowScheme = lightColorScheme(
        primary = Colors.RainbowPink,
        primaryContainer = Colors.RainbowContainer,
        secondary = Colors.RainbowPurple,
        secondaryContainer = Colors.GradientSurfaceContainer,
        surface = Colors.GradientSurface,
        surfaceContainer = Colors.GradientSurfaceContainer,
        background = Colors.GradientBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Colors.GradientOnSurface,
        onBackground = Colors.GradientOnBackground
    )

    private val GradientSunsetScheme = lightColorScheme(
        primary = Colors.SunsetOrangeRed,
        primaryContainer = Colors.SunsetContainer,
        secondary = Colors.SunsetOrange,
        secondaryContainer = Colors.GradientSurfaceContainer,
        surface = Colors.GradientSurface,
        surfaceContainer = Colors.GradientSurfaceContainer,
        background = Colors.GradientBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Colors.GradientOnSurface,
        onBackground = Colors.GradientOnBackground
    )

    private val GradientOceanScheme = lightColorScheme(
        primary = Colors.OceanDeepBlue,
        primaryContainer = Colors.OceanContainer,
        secondary = Colors.OceanCyan,
        secondaryContainer = Colors.GradientSurfaceContainer,
        surface = Colors.GradientSurface,
        surfaceContainer = Colors.GradientSurfaceContainer,
        background = Colors.GradientBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Colors.GradientOnSurface,
        onBackground = Colors.GradientOnBackground
    )

    private val GradientForestScheme = lightColorScheme(
        primary = Colors.ForestDarkGreen,
        primaryContainer = Colors.ForestContainer,
        secondary = Colors.ForestGreen,
        secondaryContainer = Colors.ForestContainer,
        surface = Colors.GradientSurface,
        surfaceContainer = Colors.ForestContainer,
        background = Colors.GradientBackground,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onSurface = Colors.GradientOnSurface,
        onBackground = Colors.GradientOnBackground
    )

    private val GradientAuroraScheme = lightColorScheme(
        primary = Colors.AuroraPurple,
        primaryContainer = Colors.AuroraContainer,
        secondary = Colors.AuroraGreen,
        secondaryContainer = Colors.ForestContainer,
        surface = Colors.GradientSurface,
        surfaceContainer = Colors.GradientSurfaceContainer,
        background = Colors.GradientBackground,
        onPrimary = Color.White,
        onSecondary = Colors.Black,
        onSurface = Colors.GradientOnSurface,
        onBackground = Colors.GradientOnBackground
    )

    private val GradientNeonScheme = lightColorScheme(
        primary = Colors.NeonRed,
        primaryContainer = Colors.NeonContainer,
        secondary = Colors.NeonCyan,
        secondaryContainer = Colors.GradientSurfaceContainer,
        surface = Colors.NeonSurface,
        surfaceContainer = Colors.NeonSurfaceContainer,
        background = Colors.NeonBackground,
        onPrimary = Color.White,
        onSecondary = Colors.Black,
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
                    Colors.RainbowPink,
                    Colors.RainbowPurple,
                    Colors.RainbowBlue,
                    Colors.RainbowGreen,
                    Colors.RainbowOrange,
                    Colors.RainbowRed
                )
            )
            AppThemeType.GRADIENT_SUNSET -> GradientConfig(
                colors = listOf(
                    Colors.SunsetOrangeRed,
                    Colors.SunsetOrange,
                    Colors.SunsetLightOrange,
                    Colors.SunsetYellow
                )
            )
            AppThemeType.GRADIENT_OCEAN -> GradientConfig(
                colors = listOf(
                    Colors.OceanDeepBlue,
                    Colors.OceanCyan,
                    Colors.OceanLightCyan,
                    Colors.OceanVeryLightCyan
                )
            )
            AppThemeType.GRADIENT_FOREST -> GradientConfig(
                colors = listOf(
                    Colors.ForestDarkGreen,
                    Colors.ForestGreen,
                    Colors.ForestLightGreen,
                    Colors.ForestVeryLightGreen
                )
            )
            AppThemeType.GRADIENT_AURORA -> GradientConfig(
                colors = listOf(
                    Colors.AuroraPurple,
                    Colors.AuroraGreen,
                    Colors.AuroraCyan,
                    Colors.AuroraPink
                )
            )
            AppThemeType.GRADIENT_NEON -> GradientConfig(
                colors = listOf(
                    Colors.NeonRed,
                    Colors.NeonPink,
                    Colors.NeonCyan,
                    Colors.NeonGreen
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
