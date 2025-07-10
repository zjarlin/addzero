package com.addzero.kmp.ui.infra

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.addzero.kmp.ui.infra.theme.AppThemeType

/**
 * 🎨 美化的标题组件
 */
@Composable
 fun BeautifulTitle(
    appName: String,
    currentTheme: AppThemeType
) {
    // 🌟 动画效果
    val infiniteTransition = rememberInfiniteTransition(label = "title_animation")

    // 缩放动画
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale_animation"
    )

    // 渐变动画（仅在渐变主题下使用）
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "gradient_animation"
    )

    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.Companion.scale(scale)
    ) {
        // 🌟 装饰性图标
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.Companion.size(20.dp),
            tint = if (currentTheme.isGradient()) {
                // 渐变主题下使用动态颜色
                Color.Companion.hsl(
                    hue = (gradientOffset * 360f) % 360f,
                    saturation = 0.8f,
                    lightness = 0.6f
                )
            } else {
                MaterialTheme.colorScheme.primary
            }
        )

        Spacer(modifier = Modifier.Companion.width(8.dp))

        // 🎨 应用名称
        if (currentTheme.isGradient()) {
            // 渐变主题下使用渐变文字效果
            Box(
                modifier = Modifier.Companion
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.Companion.linearGradient(
                            colors = listOf(
                                Color.Companion.hsl(
                                    hue = (gradientOffset * 360f) % 360f,
                                    saturation = 0.9f,
                                    lightness = 0.7f,
                                    alpha = 0.3f
                                ),
                                Color.Companion.hsl(
                                    hue = ((gradientOffset * 360f) + 120f) % 360f,
                                    saturation = 0.9f,
                                    lightness = 0.7f,
                                    alpha = 0.2f
                                ),
                                Color.Companion.hsl(
                                    hue = ((gradientOffset * 360f) + 240f) % 360f,
                                    saturation = 0.9f,
                                    lightness = 0.7f,
                                    alpha = 0.3f
                                )
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = appName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Companion.Bold
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    textAlign = TextAlign.Companion.Center
                )
            }
        } else {
            // 普通主题下使用简洁样式
            Text(
                text = appName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Companion.SemiBold
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Companion.Center
            )
        }

        Spacer(modifier = Modifier.Companion.width(8.dp))

        // 🌟 装饰性图标
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            modifier = Modifier.Companion.size(20.dp),
            tint = if (currentTheme.isGradient()) {
                // 渐变主题下使用动态颜色（相位偏移）
                Color.Companion.hsl(
                    hue = ((gradientOffset * 360f) + 180f) % 360f,
                    saturation = 0.8f,
                    lightness = 0.6f
                )
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    }
}
