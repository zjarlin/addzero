package com.addzero.kmp.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * 封装按钮
 * @param [displayName] 按钮显示文本
 * @param [icon] 按钮图标
 * @param [onClick] 点击事件处理
 * @param [modifier] 修饰符
 * @param [containerColor] 按钮背景色
 * @param [contentColor] 按钮内容颜色
 * @param [elevation] 按钮阴影
 * @param [backgroundBrush] 按钮背景渐变
 */
@Composable
fun AddButton(
    displayName: String,
    icon: ImageVector = Icons.Default.Add,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    backgroundBrush: Brush? = null
) {
    Button(
        onClick = onClick,
        elevation = elevation,
        modifier = if (backgroundBrush != null) {
            modifier.background(brush = backgroundBrush, shape = MaterialTheme.shapes.small)
        } else {
            modifier
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = if (backgroundBrush != null) Color.Transparent else containerColor,
            contentColor = contentColor
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = displayName,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

