package com.addzero.kmp.screens.ai

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

/**
 * AI思考动画组件
 * 显示可爱的Labubu风格思考动画
 */
@Composable
fun AiThinkingAnimation(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    // 使用key来确保AnimatedVisibility的稳定性
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(300, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // AI头像
            AiThinkingAvatar()
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 思考气泡
            AiThinkingBubble()
        }
    }
}

/**
 * AI思考头像 - 带动画效果
 */
@Composable
private fun AiThinkingAvatar() {
    // 头像缩放动画
    val scale by rememberInfiniteTransition(label = "avatarScale").animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "avatarScale"
    )
    
    Box(
        modifier = Modifier
            .size(32.dp)
            .scale(scale)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        LabubuColors.SoftBlue,
                        LabubuColors.MintGreen
                    )
                ),
                CircleShape
            )
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🤖",
            fontSize = 16.sp
        )
    }
}

/**
 * AI思考气泡 - 带跳动的点点动画
 */
@Composable
private fun AiThinkingBubble() {
    Box(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        LabubuColors.LightPink
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 20.dp
                )
            )
            .border(
                1.dp,
                LabubuColors.PrimaryPink.copy(alpha = 0.3f),
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 20.dp
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // 思考文字
            Text(
                text = "AI正在思考",
                color = LabubuColors.DarkText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
            
            // 跳动的点点
            ThinkingDots()
        }
    }
}

/**
 * 跳动的思考点点动画
 */
@Composable
private fun ThinkingDots() {
    val dots = listOf(0, 1, 2)
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        dots.forEach { index ->
            ThinkingDot(
                delay = index * 200L,
                color = when (index) {
                    0 -> LabubuColors.PrimaryPink
                    1 -> LabubuColors.SecondaryPurple
                    else -> LabubuColors.AccentYellow
                }
            )
        }
    }
}

/**
 * 单个思考点动画
 */
@Composable
private fun ThinkingDot(
    delay: Long = 0L,
    color: Color = LabubuColors.PrimaryPink
) {
    var isAnimating by remember { mutableStateOf(false) }
    
    // 启动动画
    LaunchedEffect(Unit) {
        delay(delay)
        isAnimating = true
    }
    
    // 缩放动画
    val scale by animateFloatAsState(
        targetValue = if (isAnimating) 1.2f else 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "dotScale"
    )
    
    // 透明度动画
    val alpha by animateFloatAsState(
        targetValue = if (isAnimating) 1f else 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ), label = "dotAlpha"
    )
    
    Box(
        modifier = Modifier
            .size(6.dp)
            .scale(scale)
            .background(
                color.copy(alpha = alpha),
                CircleShape
            )
    )
}

/**
 * 高级思考动画 - 带更多视觉效果
 */
@Composable
fun AdvancedAiThinkingAnimation(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    thinkingText: String = "AI正在深度思考中..."
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { -it },
            animationSpec = tween(400, easing = EaseOutBack)
        ) + fadeIn(animationSpec = tween(400)),
        exit = slideOutHorizontally(
            targetOffsetX = { -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300)),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            // 增强版AI头像
            EnhancedAiAvatar()
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // 增强版思考气泡
            EnhancedThinkingBubble(thinkingText)
        }
    }
}

/**
 * 增强版AI头像 - 带光环效果
 */
@Composable
private fun EnhancedAiAvatar() {
    val rotation by rememberInfiniteTransition(label = "rotation").animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )
    
    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // 旋转光环
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    Brush.sweepGradient(
                        colors = listOf(
                            LabubuColors.PrimaryPink.copy(alpha = 0.3f),
                            LabubuColors.AccentYellow.copy(alpha = 0.6f),
                            LabubuColors.SoftBlue.copy(alpha = 0.3f),
                            LabubuColors.PrimaryPink.copy(alpha = 0.3f)
                        )
                    ),
                    CircleShape
                )
        )
        
        // 主头像
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            LabubuColors.SoftBlue,
                            LabubuColors.MintGreen
                        )
                    ),
                    CircleShape
                )
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🤖",
                fontSize = 16.sp
            )
        }
    }
}

/**
 * 增强版思考气泡
 */
@Composable
private fun EnhancedThinkingBubble(text: String) {
    Box(
        modifier = Modifier
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        LabubuColors.LightPink.copy(alpha = 0.8f)
                    )
                ),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 20.dp
                )
            )
            .border(
                1.dp,
                LabubuColors.PrimaryPink.copy(alpha = 0.3f),
                RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp,
                    bottomStart = 4.dp,
                    bottomEnd = 20.dp
                )
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 思考文字
            Text(
                text = text,
                color = LabubuColors.DarkText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                lineHeight = 18.sp
            )
            
            // 进度指示器
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp),
                color = LabubuColors.PrimaryPink,
                trackColor = LabubuColors.PrimaryPink.copy(alpha = 0.2f)
            )
        }
    }
}
