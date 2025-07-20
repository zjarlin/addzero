package com.addzero.kmp.component.form.switch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 开关字段组件
 * 基于 Switch 实现，提供布尔值切换功能
 */
@Composable
fun AddSwitchField(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    label: String,
    enabled: Boolean = true,
    trueText: String = "是",
    falseText: String = "否",
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 标签
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        // 状态文本和开关
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 状态文本
            Text(
                text = if (value) trueText else falseText,
                style = MaterialTheme.typography.bodySmall,
                color = if (value) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )

            // 开关
            Switch(
                checked = value,
                onCheckedChange = onValueChange,
                enabled = enabled
            )
        }
    }
}
