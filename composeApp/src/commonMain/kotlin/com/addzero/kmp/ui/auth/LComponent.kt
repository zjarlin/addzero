package com.addzero.kmp.ui.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LComponent(

    customLogo: @Composable (() -> Unit)? = null,

    logo: ImageVector, firstTitle: String = "欢迎回来", secondTitle: String = "请登录您的账户", content: @Composable () -> Unit
) {
    // Animation state
    var cardVisible by remember { mutableStateOf(false) }

    // 添加社交登录回调

    LaunchedEffect(Unit) {
        cardVisible = true
    }

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        // Animated background
        AnimatedBackground()

        // 添加水波纹效果
        WaterRippleEffect()

        // Floating logo elements
        FloatingLogos(logo)

        // Card with entrance animation
        val cardScale = remember { Animatable(0.8f) }

        LaunchedEffect(cardVisible) {
            if (cardVisible) {
                cardScale.animateTo(
                    targetValue = 1f, animationSpec = tween(800, easing = EaseOutBack)
                )
            }
        }

        AnimatedVisibility(
            visible = cardVisible,
            enter = fadeIn(animationSpec = tween(800)),
            exit = fadeOut(),
            modifier = Modifier.padding(16.dp).scale(cardScale.value)
        ) {
            Card(
                modifier = Modifier.width(700.dp).padding(8.dp),
                shape = RoundedCornerShape(28.dp), // 圆润的边角
                elevation = CardDefaults.cardElevation(3.dp), // 轻微阴影
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.97f) // 更高的不透明度，减少透明感
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 应用Logo和欢迎文字 - 居中显示
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 应用Logo
                        if (customLogo != null) {
                            customLogo()
                        } else {
                            Image(
                                imageVector = logo,
                                contentDescription = "App Logo",
                                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Fit,
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 欢迎标题
                        Text(
                            text = firstTitle,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )

                        // 美化的登录提示文字
                        Text(
                            text = secondTitle,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Medium,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 表单内容区域 - 移除卡片包装
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        tonalElevation = 1.dp,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp), // 增加内边距
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            content()

                        }
                    }


                }
            }
        }
    }
}
