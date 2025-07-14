package com.addzero.kmp.component.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.addzero.kmp.screens.ai.LabubuColors

/**
 * 聊天背景组件系统
 * 参考Avatar组件的设计模式，提供灵活的背景配置
 */

/**
 * 背景类型枚举
 */
enum class ChatBackgroundType {
    GRADIENT,           // 渐变背景
    IMAGE,             // 图片背景
    SOLID_COLOR,       // 纯色背景
    PATTERN,           // 图案背景
    ANIMATED_GRADIENT, // 动画渐变背景
    CUSTOM             // 自定义背景
}

/**
 * 背景配置数据类
 */
data class ChatBackgroundConfig(
    val type: ChatBackgroundType = ChatBackgroundType.GRADIENT,
    val imageUrl: String? = null,
    val colors: List<Color> = listOf(
        LabubuColors.LightPink,
        LabubuColors.SoftGray,
        Color.White
    ),
    val alpha: Float = 1f,
    val contentScale: ContentScale = ContentScale.Crop,
    val overlay: Boolean = false,
    val overlayColor: Color = Color.Black.copy(alpha = 0.1f)
)

/**
 * 主要的聊天背景组件
 * 类似Avatar组件的设计，支持多种背景类型
 */
@Composable
fun ChatBackground(
    config: ChatBackgroundConfig = ChatBackgroundConfig(),
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        // 渲染背景
        when (config.type) {
            ChatBackgroundType.GRADIENT -> {
                GradientBackground(
                    colors = config.colors,
                    alpha = config.alpha,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ChatBackgroundType.IMAGE -> {
                ImageBackground(
                    imageUrl = config.imageUrl,
                    contentScale = config.contentScale,
                    alpha = config.alpha,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ChatBackgroundType.SOLID_COLOR -> {
                SolidColorBackground(
                    color = config.colors.firstOrNull() ?: LabubuColors.LightPink,
                    alpha = config.alpha,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ChatBackgroundType.PATTERN -> {
                PatternBackground(
                    colors = config.colors,
                    alpha = config.alpha,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ChatBackgroundType.ANIMATED_GRADIENT -> {
                AnimatedGradientBackground(
                    colors = config.colors,
                    alpha = config.alpha,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ChatBackgroundType.CUSTOM -> {
                // 自定义背景由外部提供
            }
        }
        
        // 覆盖层
        if (config.overlay) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(config.overlayColor)
            )
        }
        
        // 内容
        content()
    }
}

/**
 * 渐变背景组件
 */
@Composable
private fun GradientBackground(
    colors: List<Color>,
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(colors = colors)
            )
            .alpha(alpha)
    )
}

/**
 * 图片背景组件
 */
@Composable
private fun ImageBackground(
    imageUrl: String?,
    contentScale: ContentScale = ContentScale.Crop,
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    if (!imageUrl.isNullOrBlank()) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "聊天背景",
            modifier = modifier.alpha(alpha),
            contentScale = contentScale
        )
    }
}

/**
 * 纯色背景组件
 */
@Composable
private fun SolidColorBackground(
    color: Color,
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(color)
            .alpha(alpha)
    )
}

/**
 * 图案背景组件
 */
@Composable
private fun PatternBackground(
    colors: List<Color>,
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    // 创建点状图案背景
    Box(
        modifier = modifier
            .background(
                Brush.radialGradient(
                    colors = colors,
                    radius = 100f
                )
            )
            .alpha(alpha)
    )
}

/**
 * 动画渐变背景组件
 */
@Composable
private fun AnimatedGradientBackground(
    colors: List<Color>,
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    // 简化版动画渐变，可以后续扩展
    var currentColors by remember { mutableStateOf(colors) }
    
    LaunchedEffect(Unit) {
        // 这里可以添加颜色动画逻辑
    }
    
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(colors = currentColors)
            )
            .alpha(alpha)
    )
}

/**
 * 预设背景配置
 */
object ChatBackgroundPresets {
    
    /**
     * Labubu默认渐变背景
     */
    val LabubuDefault = ChatBackgroundConfig(
        type = ChatBackgroundType.GRADIENT,
        colors = listOf(
            LabubuColors.LightPink,
            LabubuColors.SoftGray,
            Color.White
        )
    )
    
    /**
     * 粉色梦幻背景
     */
    val PinkDream = ChatBackgroundConfig(
        type = ChatBackgroundType.GRADIENT,
        colors = listOf(
            LabubuColors.PrimaryPink.copy(alpha = 0.3f),
            LabubuColors.LightPink,
            Color.White
        )
    )
    
    /**
     * 蓝色清新背景
     */
    val BlueFresh = ChatBackgroundConfig(
        type = ChatBackgroundType.GRADIENT,
        colors = listOf(
            LabubuColors.SoftBlue.copy(alpha = 0.3f),
            LabubuColors.LightPink.copy(alpha = 0.5f),
            Color.White
        )
    )
    
    /**
     * 薄荷绿背景
     */
    val MintGreen = ChatBackgroundConfig(
        type = ChatBackgroundType.GRADIENT,
        colors = listOf(
            LabubuColors.MintGreen.copy(alpha = 0.2f),
            LabubuColors.SoftGray,
            Color.White
        )
    )
    
    /**
     * 纯白背景
     */
    val PureWhite = ChatBackgroundConfig(
        type = ChatBackgroundType.SOLID_COLOR,
        colors = listOf(Color.White)
    )
    
    /**
     * 自定义图片背景
     */
    fun customImage(imageUrl: String, alpha: Float = 0.8f, overlay: Boolean = true) = 
        ChatBackgroundConfig(
            type = ChatBackgroundType.IMAGE,
            imageUrl = imageUrl,
            alpha = alpha,
            overlay = overlay,
            overlayColor = Color.White.copy(alpha = 0.7f)
        )
}

/**
 * 背景选择器组件
 */
@Composable
fun ChatBackgroundSelector(
    currentConfig: ChatBackgroundConfig,
    onConfigChange: (ChatBackgroundConfig) -> Unit,
    modifier: Modifier = Modifier
) {
    // 这里可以实现背景选择UI
    // 类似于Avatar选择器的设计
    Column(modifier = modifier) {
        // 预设背景选项
        // 自定义背景选项
        // 图片上传选项
    }
}
