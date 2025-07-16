package com.addzero.kmp.component.card

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 🎨 JetBrains风格渐变卡片组件
 * 
 * 带有渐变背景的版本，更接近JetBrains官方设计
 * 
 * @param onClick 点击事件回调
 * @param modifier 修饰符
 * @param cornerRadius 圆角大小
 * @param elevation 阴影高度
 * @param padding 内边距
 * @param gradientColors 渐变色列表
 * @param hoverColor 悬浮时的荧光色
 * @param animationDuration 动画持续时间
 * @param content 卡片内容插槽
 */
@Composable
fun AddJetBrainsGradientCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    elevation: Dp = 4.dp,
    padding: Dp = 16.dp,
    gradientColors: List<Color> = listOf(
        Color(0xFF6B73FF),
        Color(0xFF9B59B6),
        Color(0xFFE74C3C)
    ),
    hoverColor: Color = Color(0xFF00D4FF), // 青色荧光
    animationDuration: Int = 300,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    
    // 悬浮动画
    val hoverAlpha by animateFloatAsState(
        targetValue = if (isHovered) 0.2f else 0f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "hover_animation"
    )
    
    val scaleAnimation by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = tween(durationMillis = animationDuration),
        label = "scale_animation"
    )
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onClick() }
                } else Modifier
            ),
        shape = RoundedCornerShape(cornerRadius),
        tonalElevation = elevation,
        shadowElevation = if (isHovered) elevation + 4.dp else elevation,
        color = Color.Transparent
    ) {
        Box {
            // 渐变背景
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
            )
            
            // 内容区域
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                content()
            }
            
            // 悬浮荧光效果
            if (hoverAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    hoverColor.copy(alpha = hoverAlpha),
                                    hoverColor.copy(alpha = hoverAlpha * 0.5f),
                                    Color.Transparent
                                ),
                                radius = 400f
                            )
                        )
                )
            }
        }
    }
}

