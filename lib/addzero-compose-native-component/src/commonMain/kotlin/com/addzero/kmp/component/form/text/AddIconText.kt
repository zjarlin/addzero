package com.addzero.kmp.component.form.text

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 填写说明组件
 * @param [showFlag] 受控条件
 * @param [describe] 描述
 */
@Composable
fun AddIconText(showFlag: Boolean = true, imageVector: ImageVector = Icons.Default.Info, describe: String) {
    if (!showFlag) {
        return
    }
    Surface(
        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.Companion.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically,
            modifier = Modifier.Companion.padding(12.dp)
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.Companion.width(12.dp))
            Text(
                describe,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
