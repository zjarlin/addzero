package com.addzero.kmp.component.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.ui.infra.theme.*
import com.addzero.kmp.component.button.AddFloatingActionButton
import org.koin.compose.viewmodel.koinViewModel

/**
 * 主题切换器组件
 */
@Composable
fun ThemeSwitcher(
    modifier: Modifier = Modifier,
    onThemeSelected: (AppThemeType) -> Unit = {}
) {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val currentTheme = themeViewModel.currentTheme
    val allThemes = themeViewModel.getAllThemes()

    Card(
        modifier = modifier.width(300.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "选择主题",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(400.dp)
            ) {
                items(allThemes) { theme ->
                    ThemeItem(
                        theme = theme,
                        isSelected = theme == currentTheme,
                        onClick = {
                            themeViewModel.setTheme(theme)
                            onThemeSelected(theme)
                        }
                    )
                }
            }
        }
    }
}

/**
 * 主题项组件
 */
@Composable
private fun ThemeItem(
    theme: AppThemeType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val gradientConfig = AppThemes.getGradientConfig(theme)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (isSelected) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 主题预览色块
        if (gradientConfig != null && theme.isGradient()) {
            // 渐变主题预览
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradientConfig.colors.take(3) // 只取前3个颜色
                        )
                    )
            )
        } else {
            // 普通主题预览
            val colorScheme = AppThemes.getColorScheme(theme)
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorScheme.primary)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // 主题名称
        Text(
            text = theme.getDisplayName(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )

        // 选中标识
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "已选中",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

/**
 * 渐变主题预览卡片
 */
@Composable
fun GradientThemePreview(
    theme: AppThemeType,
    modifier: Modifier = Modifier
) {
    val gradientConfig = AppThemes.getGradientConfig(theme)

    if (gradientConfig != null && theme.isGradient()) {
        Card(
            modifier = modifier.size(120.dp, 80.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradientConfig.colors
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = theme.getDisplayName(),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 快速主题切换按钮
 * 使用 AddFloatingActionButton 高阶组件实现
 */
@Composable
fun QuickThemeToggle(
    modifier: Modifier = Modifier
) {
       val themeViewModel = koinViewModel<ThemeViewModel>()

    val currentTheme = themeViewModel.currentTheme

    AddFloatingActionButton(
        text = "切换渐变主题 (${currentTheme.getDisplayName()})",
        imageVector = Icons.Default.Palette,
        modifier = modifier,
        onClick = {
            // 循环切换渐变主题
            val gradientThemes = listOf(
                AppThemeType.GRADIENT_RAINBOW,
                AppThemeType.GRADIENT_SUNSET,
                AppThemeType.GRADIENT_OCEAN,
                AppThemeType.GRADIENT_FOREST,
                AppThemeType.GRADIENT_AURORA,
                AppThemeType.GRADIENT_NEON
            )

            val currentIndex = gradientThemes.indexOf(currentTheme)
            val nextTheme = if (currentIndex >= 0) {
                gradientThemes[(currentIndex + 1) % gradientThemes.size]
            } else {
                gradientThemes.first()
            }

            themeViewModel.setTheme(nextTheme)
        }
    )
}
