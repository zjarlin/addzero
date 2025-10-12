package site.addzero.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import site.addzero.themes.LightColors

val LocalShadcnColors = staticCompositionLocalOf<ShadcnColors> { LightColors }
val LocalShadcnRadius = staticCompositionLocalOf<ShadcnRadius> { Radius }
val LocalShadcnCardShadow = staticCompositionLocalOf<ShadcnShadows> { Shadows }


/**
 * 通过 [CompositionLocalProvider] 提供 [ShadcnColors] 和 [ShadcnRadius] 供 Shadcn Compose 组件使用。
 * 同时应用带有提供或默认 Material 颜色和排版的 MaterialTheme。
 * 注意事项：
 * - 对 Material Design 组件使用 MaterialTheme.colorScheme。
 * - 对 ShadCN 特定样式使用 MaterialTheme.shadcnColors。
 * - 对 ShadCN 特定样式使用 MaterialTheme.radius。
 *
 * @param isDarkTheme 主题应为深色还是浅色。默认为系统设置。
 * @param shadcnLightColors 用于浅色主题的 [ShadcnColors]。默认为 [LightColors]。
 * @param shadcnDarkColors 用于深色主题的 [ShadcnColors]。默认为 [DarkColors]。
 * @param materialLightColors 用于浅色主题的 Material 3 [ColorScheme]。默认为 [lightColorScheme]。
 * @param materialDarkColors 用于深色主题的 Material 3 [ColorScheme]。默认为 [darkColorScheme]。
 * @param shadcnShadows 用于卡片阴影的 [ShadcnShadows]。默认为 [Shadows]。
 * @param shadcnRadius 要使用的 [ShadcnRadius]。默认为 [Radius]。
 * @param typography 要使用的 Material 3 [Typography]。默认为 [DefaultTypography]。
 * @param content 要应用主题的可组合内容。
 */
@Composable
fun ShadcnTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    shadcnLightColors: ShadcnColors = LightColors,
    shadcnDarkColors: ShadcnColors = DarkColors,
    materialLightColors: ColorScheme = lightColorScheme(),
    materialDarkColors: ColorScheme = darkColorScheme(),
    shadcnShadows: ShadcnShadows = Shadows,
    shadcnRadius: ShadcnRadius = Radius,
    typography: Typography? = null,
    content: @Composable () -> Unit,
) {
    val colors = if (isDarkTheme) shadcnDarkColors else shadcnLightColors
    val materialColorScheme = if (isDarkTheme) materialDarkColors else materialLightColors
    CompositionLocalProvider(
        LocalShadcnColors provides colors,
        LocalShadcnRadius provides shadcnRadius,
        LocalShadcnCardShadow provides shadcnShadows
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = typography ?: DefaultTypography,
            content = content
        )
    }
}

val MaterialTheme.colors: ShadcnColors
    @Composable
    @ReadOnlyComposable
    get() = LocalShadcnColors.current

val MaterialTheme.radius: ShadcnRadius
    @Composable
    @ReadOnlyComposable
    get() = LocalShadcnRadius.current

val MaterialTheme.shadow: ShadcnShadows
    @Composable
    @ReadOnlyComposable
    get() = LocalShadcnCardShadow.current
