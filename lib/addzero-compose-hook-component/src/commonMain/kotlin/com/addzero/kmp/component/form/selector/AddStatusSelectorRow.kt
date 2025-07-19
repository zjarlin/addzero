package com.addzero.kmp.component.form.selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 状态切换输入组件
 * @param [value]
 * @param [onValueChange]
 * @param [states]
 * @param [labelBuilder]
 * @param [colorBuilder]
 * @param [modifier]
 */
@Composable
fun <T> AddStatusSelectorRow(
    value: T,
    onValueChange: (T) -> Unit,
    states: List<T>,
    labelBuilder: @Composable (T) -> String,
    colorBuilder: @Composable (T) -> Color,
    modifier: Modifier = Modifier.Companion
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Companion.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "状态：",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            states.forEach { state ->
                val selected = value == state
                Button(
                    onClick = { onValueChange(state) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selected) colorBuilder(state) else MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = MaterialTheme.shapes.small,
                    enabled = !selected
                ) {
                    Text(
                        text = labelBuilder(state),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}