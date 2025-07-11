package com.addzero.kmp.ui.infra.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import com.addzero.kmp.component.button.AddIconButton
import org.koin.compose.viewmodel.koinViewModel

/**
 * 主题明暗切换按钮
 * 点击时切换明暗主题
 */
@Composable
fun ThemeToggleButton() {
    val themeViewModel = koinViewModel<ThemeViewModel>()
    val isDarkMode = themeViewModel.isDarkMode
    val rotationAngle by animateFloatAsState(targetValue = if (isDarkMode) 180f else 0f)
    AddIconButton(
        text = if (isDarkMode) "切换到亮色模式" else "切换到暗色模式",
        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
        modifier = Modifier.rotate(rotationAngle),
        tint = MaterialTheme.colorScheme.onSurface
    ) { themeViewModel.toggleTheme() }

}
