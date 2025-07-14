package com.addzero.kmp.component.dropdown

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

/**
 * 通用下拉菜单组件
 *
 * @param options 选项列表
 * @param expanded 是否展开
 * @param getLabel 获取选项显示文本
 * @param onOptionSelected 选项点击回调
 * @param onDismissRequest 关闭菜单回调
 */
@Composable
fun <T> AddDropDown(
    options: List<T>,
    expanded: Boolean,
    getLabel: (T) -> String,
    onOptionSelected: (T) -> Unit,
    onDismissRequest: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 4.dp),
        properties = PopupProperties(focusable = true)
    ) {
        options.forEach { option ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = getLabel(option),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                onClick = { onOptionSelected(option) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = MaterialTheme.colorScheme.surface)
            )
        }
    }
}