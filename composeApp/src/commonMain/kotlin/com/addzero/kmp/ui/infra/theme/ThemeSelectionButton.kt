package com.addzero.kmp.ui.infra.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.*
import com.addzero.kmp.component.button.AddIconButton

/**
 * 主题选择按钮
 * 点击打开主题选择对话框
 */
@Composable
fun ThemeSelectionButton() {
    var showThemeDialog by remember { mutableStateOf(false) }
    AddIconButton(
        imageVector = Icons.Default.Palette,
        text = "选择主题",
    ) {
        showThemeDialog = true
    }


    if (showThemeDialog) {
        ThemeSelectionDialog(
            onDismiss = { showThemeDialog = false }
        )
    }
}
