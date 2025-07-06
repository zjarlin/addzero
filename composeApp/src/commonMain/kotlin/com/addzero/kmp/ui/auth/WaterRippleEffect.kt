package com.addzero.kmp.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.min
import kotlin.random.Random

/**
 * 苹果风格的水波纹效果
 */
@Composable
fun WaterRippleEffect(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
    rippleCount: Int = 6
) {
    val infiniteTransition = rememberInfiniteTransition()

    // 生成多个随机波纹源点
    val rippleSources = remember {
        List(rippleCount) {
            RippleSource(
                x = Random.nextFloat() * 0.8f + 0.1f,
                y = Random.nextFloat() * 0.8f + 0.1f,
                maxRadius = Random.nextFloat() * 0.15f + 0.05f,
                durationMillis = Random.nextInt(4000, 8000),
                delayMillis = Random.nextInt(0, 3000)
            )
        }
    }

    // 动画进度
    val animations = rippleSources.map { source ->
        val animation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = source.durationMillis,
                    delayMillis = source.delayMillis,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )
        Pair(source, animation)
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        animations.forEach { (source, progress) ->
            drawRipple(source, progress, color)
        }
    }
}

private fun DrawScope.drawRipple(
    source: RippleSource,
    progress: Float,
    color: Color
) {
    // 计算中心点
    val center = Offset(
        x = source.x * size.width,
        y = source.y * size.height
    )

    // 最大半径（屏幕尺寸的一部分）
    val maxRadius = min(size.width, size.height) * source.maxRadius

    // 当前半径
    val currentRadius = maxRadius * progress

    // 透明度随着半径增大而减小
    val alpha = (1f - progress) * 0.7f

    // 画3个同心圆形成波纹效果
    for (i in 0..2) {
        val radiusFactor = 1f - (i * 0.15f) // 每个圆的大小差异
        val innerRadius = currentRadius * radiusFactor
        val strokeWidth = maxRadius * 0.01f // 线条宽度

        drawCircle(
            color = color.copy(alpha = alpha * (1f - (i * 0.3f))),
            radius = innerRadius,
            center = center,
            style = Stroke(width = strokeWidth)
        )
    }
}

data class RippleSource(
    val x: Float, // 波纹源点X坐标（0-1）
    val y: Float, // 波纹源点Y坐标（0-1）
    val maxRadius: Float, // 相对屏幕的最大半径
    val durationMillis: Int, // 动画持续时间
    val delayMillis: Int // 动画延迟
)
