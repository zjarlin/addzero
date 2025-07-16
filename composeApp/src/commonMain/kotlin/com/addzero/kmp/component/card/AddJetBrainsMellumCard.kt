package com.addzero.kmp.component.card

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 🎨 JetBrains Mellum风格卡片组件
 *
 * 参考JetBrains官方设计的现代化材质卡片，具有：
 * - 清晰的渐变背景
 * - 微妙的边框效果
 * - 流畅的悬浮动画
 * - 自动适配的文字颜色
 *
 * @param onClick 点击事件回调
 * @param modifier 修饰符
 * @param cornerRadius 圆角大小
 * @param elevation 阴影高度
 * @param padding 内边距
 * @param backgroundType 背景类型
 * @param animationDuration 动画持续时间
 * @param content 卡片内容插槽
 */
@Composable
fun AddJetBrainsMellumCard(
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 4.dp,
    padding: Dp = 20.dp,
    backgroundType: MellumCardType = MellumCardType.Light,
    animationDuration: Int = 300,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // 悬浮动画
    val scaleAnimation by animateFloatAsState(
        targetValue = if (isHovered) 1.02f else 1f,
        animationSpec = tween(durationMillis = animationDuration, easing = EaseOutCubic),
        label = "scale_animation"
    )

    val elevationAnimation by animateDpAsState(
        targetValue = if (isHovered) elevation + 4.dp else elevation,
        animationSpec = tween(durationMillis = animationDuration, easing = EaseOutCubic),
        label = "elevation_animation"
    )

    // 使用Surface而不是Box，确保正确的Material Design行为
    Surface(
        modifier = modifier
            .scale(scaleAnimation)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onClick() }
                } else Modifier
            ),
        shape = RoundedCornerShape(cornerRadius),
        tonalElevation = elevationAnimation,
        shadowElevation = elevationAnimation,
        color = backgroundType.backgroundColor
    ) {
        // 直接使用Column布局，避免Box嵌套
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = backgroundType.backgroundBrush,
                    shape = RoundedCornerShape(cornerRadius)
                )
                .border(
                    width = 1.dp,
                    color = backgroundType.borderColor.copy(
                        alpha = if (isHovered) 0.4f else 0.2f
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(padding)
        ) {
            // 提供LocalContentColor，确保文字颜色正确
            CompositionLocalProvider(
                LocalContentColor provides backgroundType.contentColor
            ) {
                content()
            }
        }
    }
}

/**
 * 🎨 Mellum卡片类型枚举
 * 
 * 定义不同的背景渐变样式，参考JetBrains产品的配色方案
 */
enum class MellumCardType(
    val backgroundBrush: Brush,
    val hoverColor: Color,
    val backgroundColor: Color,
    val borderColor: Color,
    val contentColor: Color
) {
    Light(
        backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFFAFAFA),
                Color(0xFFF0F0F0),
                Color(0xFFE0E0E0)
            )
        ),
        hoverColor = Color(0xFF6B7280),
        backgroundColor = Color(0xFFFAFAFA),
        borderColor = Color(0xFFE0E0E0),
        contentColor = Color(0xFF000000)
    ),
    Purple(
        backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF2D1B69),
                Color(0xFF1A0E3D),
                Color(0xFF0F0A1F)
            )
        ),
        hoverColor = Color(0xFF6B73FF),
        backgroundColor = Color(0xFF2D1B69),
        borderColor = Color(0xFF6B73FF),
        contentColor = Color(0xFFFFFFFF)
    ),
    Blue(
        backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF1E3A8A),
                Color(0xFF1E293B),
                Color(0xFF0F172A)
            )
        ),
        hoverColor = Color(0xFF3B82F6),
        backgroundColor = Color(0xFF1E3A8A),
        borderColor = Color(0xFF3B82F6),
        contentColor = Color(0xFFFFFFFF)
    ),
    Teal(
        backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF134E4A),
                Color(0xFF1F2937),
                Color(0xFF111827)
            )
        ),
        hoverColor = Color(0xFF14B8A6),
        backgroundColor = Color(0xFF134E4A),
        borderColor = Color(0xFF14B8A6),
        contentColor = Color(0xFFFFFFFF)
    ),
    Orange(
        backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF9A3412),
                Color(0xFF7C2D12),
                Color(0xFF431407)
            )
        ),
        hoverColor = Color(0xFFF97316),
        backgroundColor = Color(0xFF9A3412),
        borderColor = Color(0xFFF97316),
        contentColor = Color(0xFFFFFFFF)
    ),
    Dark(
        backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF374151),
                Color(0xFF1F2937),
                Color(0xFF111827)
            )
        ),
        hoverColor = Color(0xFF6B7280),
        backgroundColor = Color(0xFF374151),
        borderColor = Color(0xFF6B7280),
        contentColor = Color(0xFFFFFFFF)
    ),
    Rainbow(
        backgroundBrush = Brush.linearGradient(
            colors = listOf(
                Color(0xFF8B5CF6),
                Color(0xFF3B82F6),
                Color(0xFF10B981),
                Color(0xFF1F2937)
            )
        ),
        hoverColor = Color(0xFF8B5CF6),
        backgroundColor = Color(0xFF8B5CF6),
        borderColor = Color(0xFF8B5CF6),
        contentColor = Color(0xFFFFFFFF)
    );
}

/**
 * 🎨 预设的JetBrains风格卡片组件
 * 
 * 提供一些常用的预设样式，方便快速使用
 */
object JetBrainsMellumCards {
    
    /**
     * Koog Agent风格卡片
     */
    @Composable
    fun KoogAgentCard(
        onClick: (() -> Unit)? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AddJetBrainsMellumCard(
            onClick = onClick,
            modifier = modifier,
            backgroundType = MellumCardType.Purple,
            content = content
        )
    }
    
    /**
     * Hackathon风格卡片
     */
    @Composable
    fun HackathonCard(
        onClick: (() -> Unit)? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AddJetBrainsMellumCard(
            onClick = onClick,
            modifier = modifier,
            backgroundType = MellumCardType.Blue,
            content = content
        )
    }
    
    /**
     * Deploy Mellum风格卡片
     */
    @Composable
    fun DeployMellumCard(
        onClick: (() -> Unit)? = null,
        modifier: Modifier = Modifier,
        content: @Composable () -> Unit
    ) {
        AddJetBrainsMellumCard(
            onClick = onClick,
            modifier = modifier,
            backgroundType = MellumCardType.Teal,
            content = content
        )
    }
}
