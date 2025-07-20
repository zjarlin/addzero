package com.addzero.kmp.ui.infra.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 侧边栏渐变背景
 * 为侧边栏提供与主题匹配的渐变背景
 */
@Composable
fun SidebarGradientBackground(
    themeType: AppThemeType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

    val gradientConfig = AppThemes.getGradientConfig(themeType)

    if (gradientConfig != null && themeType.isGradient()) {
        // 渐变主题 - 应用侧边栏渐变（调整透明度，与主内容区协调）
        Box(
            modifier = modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientConfig.colors.first().copy(alpha = 0.12f), // 🎨 降低透明度
                            gradientConfig.colors[1].copy(alpha = 0.08f),      // 🎨 降低透明度
                            gradientConfig.colors.last().copy(alpha = 0.04f),  // 🎨 降低透明度
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.95f) // 🎨 提高背景透明度
                        )
                    )
                )
        ) {
            content()
        }
    } else {
        // 普通主题 - 使用默认背景
        Box(modifier = modifier) {
            content()
        }
    }
}

/**
 * 获取菜单项文本颜色
 * 参考 Android Developer 网站样式，选中状态使用深色文本
 */
@Composable
fun getMenuItemTextColor(themeType: AppThemeType, isSelected: Boolean): Color {
    val gradientConfig = AppThemes.getGradientConfig(themeType)

    return if (gradientConfig != null && themeType.isGradient() && isSelected) {
        // 渐变主题选中状态 - 使用渐变色的深色版本
        gradientConfig.colors.first().copy(alpha = 0.9f)
    } else if (isSelected) {
        // 普通主题选中状态 - 参考 Android Developer 使用深色文本
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        // 未选中状态 - 使用默认文本色
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
    }
}

/**
 * 主内容区域渐变背景
 * 为主内容区域提供渐变背景效果
 */
@Composable
fun MainContentGradientBackground(
    themeType: AppThemeType,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val gradientConfig = AppThemes.getGradientConfig(themeType)

    if (gradientConfig != null && themeType.isGradient()) {
        // 渐变主题 - 应用主内容渐变（调整透明度，与侧边栏协调）
        Box(
            modifier = modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            gradientConfig.colors.last().copy(alpha = 0.08f),  // 🎨 提高透明度
                            gradientConfig.colors.first().copy(alpha = 0.06f), // 🎨 提高透明度
                            gradientConfig.colors[1].copy(alpha = 0.04f),       // 🎨 添加中间色
                            Color.Transparent
                        ),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 800f)
                    )
                )
        ) {
            content()
        }
    } else {
        // 普通主题 - 使用默认背景
        Box(modifier = modifier) {
            content()
        }
    }
}
