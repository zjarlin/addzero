package com.addzero.kmp.component.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddFloatingActionButton
import com.addzero.kmp.ui.infra.theme.AppThemeType
import com.addzero.kmp.ui.infra.theme.AppThemes
import com.addzero.kmp.ui.infra.theme.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel

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
