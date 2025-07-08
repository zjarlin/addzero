package com.addzero.kmp.ui.infra.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

/**
 * 渐变主题包装器
 * 为渐变主题提供背景渐变效果
 */
@Composable
fun GradientThemeWrapper(
    themeType: AppThemeType,
    content: @Composable () -> Unit
) {
    val gradientConfig = AppThemes.getGradientConfig(themeType)
    
    if (gradientConfig != null && themeType.isGradient()) {
        // 渐变主题 - 应用渐变背景
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            gradientConfig.colors.first().copy(alpha = 0.15f),
                            gradientConfig.colors[1].copy(alpha = 0.1f),
                            gradientConfig.colors.getOrNull(2)?.copy(alpha = 0.08f) ?: Color.Transparent,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                        ),
                        radius = 1500f
                    )
                )
        ) {
            content()
        }
    } else {
        // 普通主题 - 直接显示内容
        content()
    }
}

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
        // 渐变主题 - 应用侧边栏渐变
        Box(
            modifier = modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            gradientConfig.colors.first().copy(alpha = 0.25f),
                            gradientConfig.colors[1].copy(alpha = 0.15f),
                            gradientConfig.colors.last().copy(alpha = 0.08f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
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
 * 菜单项渐变背景
 * 为选中的菜单项提供渐变效果
 */
@Composable
fun MenuItemGradientBackground(
    themeType: AppThemeType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val gradientConfig = AppThemes.getGradientConfig(themeType)
    
    val backgroundModifier = if (gradientConfig != null && themeType.isGradient() && isSelected) {
        // 渐变主题且选中状态 - 应用渐变背景
        modifier.background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    gradientConfig.colors.first().copy(alpha = 0.4f),
                    gradientConfig.colors[1].copy(alpha = 0.25f),
                    gradientConfig.colors.getOrNull(2)?.copy(alpha = 0.1f) ?: Color.Transparent
                )
            )
        )
    } else if (gradientConfig != null && themeType.isGradient()) {
        // 渐变主题未选中状态 - 轻微渐变
        modifier.background(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    gradientConfig.colors.first().copy(alpha = 0.08f),
                    Color.Transparent,
                    Color.Transparent
                )
            )
        )
    } else if (isSelected) {
        // 普通主题选中状态 - 使用主色调
        modifier.background(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        )
    } else {
        // 普通主题未选中状态 - 透明背景
        modifier.background(
            color = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.3f)
        )
    }
    
    Box(modifier = backgroundModifier) {
        content()
    }
}

/**
 * 渐变文本组件
 */
@Composable
fun GradientText(
    text: String,
    themeType: AppThemeType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    val gradientConfig = AppThemes.getGradientConfig(themeType)

    if (gradientConfig != null && themeType.isGradient() && isSelected) {
        // 渐变主题选中状态 - 使用渐变色（这里先用第一个颜色，后续可以实现真正的渐变文本）
        Text(
            text = text,
            modifier = modifier,
            style = style,
            color = gradientConfig.colors.first(),
            maxLines = maxLines,
            overflow = overflow
        )
    } else {
        // 普通文本
        Text(
            text = text,
            modifier = modifier,
            style = style,
            color = getMenuItemTextColor(themeType, isSelected),
            maxLines = maxLines,
            overflow = overflow
        )
    }
}

/**
 * 获取菜单项文本颜色
 */
@Composable
fun getMenuItemTextColor(themeType: AppThemeType, isSelected: Boolean): Color {
    val gradientConfig = AppThemes.getGradientConfig(themeType)

    return if (gradientConfig != null && themeType.isGradient() && isSelected) {
        // 渐变主题选中状态 - 使用渐变色
        gradientConfig.colors.first()
    } else if (isSelected) {
        // 普通主题选中状态 - 使用主色调
        MaterialTheme.colorScheme.primary
    } else {
        // 未选中状态 - 使用默认文本色
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
    }
}

/**
 * 获取菜单项图标颜色
 */
@Composable
fun getMenuItemIconColor(themeType: AppThemeType, isSelected: Boolean): Color {
    val gradientConfig = AppThemes.getGradientConfig(themeType)
    
    return if (gradientConfig != null && themeType.isGradient() && isSelected) {
        // 渐变主题选中状态 - 使用渐变色
        gradientConfig.colors.first()
    } else if (isSelected) {
        // 普通主题选中状态 - 使用主色调
        MaterialTheme.colorScheme.primary
    } else {
        // 未选中状态 - 使用默认图标色
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
        // 渐变主题 - 应用主内容渐变
        Box(
            modifier = modifier
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.Transparent,
                            gradientConfig.colors.last().copy(alpha = 0.05f),
                            gradientConfig.colors.first().copy(alpha = 0.03f),
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
