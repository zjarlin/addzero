package com.addzero.kmp.ui.infra.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

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
 * 菜单项背景
 * 参考 Android Developer 网站的侧边栏样式，提供现代化的选中效果
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemGradientBackground(
    themeType: AppThemeType,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val gradientConfig = AppThemes.getGradientConfig(themeType)

    // 使用 Surface 提供统一的悬浮和选中效果
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = when {
            // 渐变主题选中状态 - 使用渐变主色
            gradientConfig != null && themeType.isGradient() && isSelected -> {
                gradientConfig.colors.first().copy(alpha = 0.15f)
            }
            // 普通主题选中状态 - 参考 Android Developer 的浅蓝色背景
            isSelected -> {
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
            }
            // 未选中状态 - 完全透明
            else -> Color.Transparent
        },
        tonalElevation = 0.dp, // 移除高度，保持平面设计
        shadowElevation = 0.dp, // 移除阴影
        onClick = { /* 点击由外层处理 */ }
    ) {
        // 渐变主题选中状态的额外渐变层
        if (gradientConfig != null && themeType.isGradient() && isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                gradientConfig.colors.first().copy(alpha = 0.2f),
                                gradientConfig.colors[1].copy(alpha = 0.12f),
                                gradientConfig.colors.getOrNull(2)?.copy(alpha = 0.06f) ?: Color.Transparent
                            )
                        )
                    )
            ) {
                content()
            }
        } else {
            content()
        }
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
 * 获取菜单项图标颜色
 * 参考 Android Developer 网站样式，选中状态使用深色图标
 */
@Composable
fun getMenuItemIconColor(themeType: AppThemeType, isSelected: Boolean): Color {
    val gradientConfig = AppThemes.getGradientConfig(themeType)

    return if (gradientConfig != null && themeType.isGradient() && isSelected) {
        // 渐变主题选中状态 - 使用渐变色的深色版本
        gradientConfig.colors.first().copy(alpha = 0.9f)
    } else if (isSelected) {
        // 普通主题选中状态 - 参考 Android Developer 使用深色图标
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        // 未选中状态 - 使用默认图标色
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
