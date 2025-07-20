package com.addzero.kmp.ui.auth

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

/**
 * Data class representing a particle in the animated background
 */
data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val color: Color,
    val speedMultiplier: Float,
    val offset: Float = Random.Default.nextFloat()
)
