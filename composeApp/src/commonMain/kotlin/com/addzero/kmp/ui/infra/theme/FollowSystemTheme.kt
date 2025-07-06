package com.addzero.kmp.ui.infra.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun FollowSystemTheme(
    colorScheme: ColorScheme = when {
        isSystemInDarkTheme() -> darkColorScheme()
        else -> lightColorScheme()
    },
    shapes: Shapes = MaterialTheme.shapes,
    typography: Typography = MaterialTheme.typography,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        shapes = shapes,
        typography = typography,
        content = content,
    )
}



