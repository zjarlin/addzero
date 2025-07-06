package com.addzero.kmp.ui.auth

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * A composable that displays floating logo elements in the background
 */
@Composable
fun FloatingLogos(
    logo: ImageVector, modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Create several animated values for different logos
    val positions = List(5) { index ->
        // Different animation durations for variety
        val duration = 15000 + index * 2000

        // Animate X position
        val x by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
                animation = tween(duration, easing = LinearEasing), repeatMode = RepeatMode.Reverse
            )
        )

        // Animate Y position
        val y by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 1f, animationSpec = infiniteRepeatable(
                animation = tween(duration + 5000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
            )
        )

        // Animate rotation
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
                animation = tween(duration + 10000, easing = LinearEasing), repeatMode = RepeatMode.Restart
            )
        )

        // Animate scale
        val scale by infiniteTransition.animateFloat(
            initialValue = 0.7f, targetValue = 1.3f, animationSpec = infiniteRepeatable(
                animation = tween(duration - 5000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
            )
        )

        // Animate alpha
        val alpha by infiniteTransition.animateFloat(
            initialValue = 0.1f, targetValue = 0.3f, animationSpec = infiniteRepeatable(
                animation = tween(duration - 8000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
            )
        )

        Triple(
            Offset(x, y), rotation, Pair(scale, alpha)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        positions.forEachIndexed { index, (offset, rotation, scaleAlpha) ->
            val (scale, alpha) = scaleAlpha

            // Calculate position based on parent size
            val xPercent = (index * 0.2f + offset.x * 0.6f).coerceIn(0.1f, 0.9f)
            val yPercent = (index * 0.15f + offset.y * 0.7f).coerceIn(0.1f, 0.9f)

            Image(
                imageVector = logo,
                contentDescription = null,
                modifier = Modifier.size(120.dp).align(Alignment.TopStart).offset(
                    x = (xPercent * 100).dp, y = (yPercent * 100).dp
                ).rotate(rotation).scale(scale).alpha(alpha),
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
        }
    }
}
