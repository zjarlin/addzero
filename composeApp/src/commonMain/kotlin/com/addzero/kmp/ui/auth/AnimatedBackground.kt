package com.addzero.kmp.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * A composable that creates an animated particle background
 */
@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier.Companion,
    particleCount: Int = 70, // 增加粒子数量以获得更丰富效果
) {
    // Create infinite transition for continuous animations
    val infiniteTransition = rememberInfiniteTransition()

    // Animated gradient colors - Apple inspired
    val colors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f)
    )

    // Animate gradient angle
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        )
    )

    // Generate random particles
    val particles = remember {
        List(particleCount) {
            Particle(
                x = Random.Default.nextFloat(),
                y = Random.Default.nextFloat(),
                radius = Random.Default.nextFloat() * 12f + 4f,
                color = colors[Random.Default.nextInt(colors.size)].copy(alpha = Random.Default.nextFloat() * 0.3f + 0.1f),
                speedMultiplier = Random.Default.nextFloat() * 0.6f + 0.2f
            )
        }
    }

    // Animate particles
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        )
    )

    // Wave animation
    val waveProgress by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        )
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Animated gradient background
        Canvas(modifier = Modifier.Companion.fillMaxSize()) {
            // Calculate gradient center and radius
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.width.coerceAtLeast(size.height)

            // Create gradient brush with rotating angle
            val angleInRadians = angle * PI.toFloat() / 180f
            val distance = radius / 2
            val offsetX = cos(angleInRadians) * distance
            val offsetY = sin(angleInRadians) * distance
            val gradientOffset = androidx.compose.ui.geometry.Offset(offsetX, offsetY)

            val brush = Brush.Companion.linearGradient(
                colors = colors, start = center - gradientOffset, end = center + gradientOffset
            )

            // Draw background
            drawRect(brush = brush, size = size)

            // Draw particles
            particles.forEach { particle ->
                // 改进的苹果风格水波纹效果 - 更自然的波动
                val x = particle.x * size.width
                val primaryWave = sin((particle.x * 3 + waveProgress) * 2 * PI.toFloat()) * 15
                val secondaryWave = cos((particle.y * 2 + waveProgress * 0.7f) * 2 * PI.toFloat()) * 10
                val waveOffset = primaryWave + secondaryWave
                val y = (particle.y * size.height) + waveOffset

                // Calculate movement
                val moveX = sin((particleProgress + particle.offset) * 2 * PI.toFloat()) * 50 * particle.speedMultiplier
                val moveY = cos((particleProgress + particle.offset) * 2 * PI.toFloat()) * 30 * particle.speedMultiplier

                // Draw particle
                drawCircle(
                    color = particle.color,
                    radius = particle.radius,
                    center = androidx.compose.ui.geometry.Offset(x + moveX, y + moveY),
                    alpha = 0.7f
                )
            }
        }
    }
}
