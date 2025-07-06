package com.addzero.kmp.ui.infra.theme

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 主题选择按钮
 * 点击打开主题选择对话框
 */
@Composable
fun ThemeSelectionButton(
    modifier: Modifier = Modifier
) {
    var showThemeDialog by remember { mutableStateOf(false) }
    
    IconButton(
        onClick = { showThemeDialog = true },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Palette,
            contentDescription = "选择主题",
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(24.dp)
        )
    }
    
    if (showThemeDialog) {
        ThemeSelectionDialog(
            onDismiss = { showThemeDialog = false }
        )
    }
} 