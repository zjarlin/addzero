package com.addzero.kmp.component.table.base.header

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RenderHeaderMutiSelect(
    enableEditMode: Boolean,
    checked: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    if (!enableEditMode) return
    Box(
        modifier = Modifier.Companion.padding(horizontal = 4.dp).width(40.dp),
        contentAlignment = Alignment.Companion.Center
    ) {
        Checkbox(
            checked = checked, onCheckedChange = onCheckedChange
        )
    }
}
