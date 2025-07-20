package com.addzero.kmp.component.table.header.column

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.table.TableColumnType

/**
 * 渲染表头每一列的高级搜索图标
 * @param [isFiltered] 是否已筛选
 * @param [column] 列定义
 * @param [onColumnAdvSearchClick] 点击筛选图标的回调
 */
@Composable
fun RenderTableHeaderColumnFilterIcon(
    isFiltered: Boolean,
    column: TableColumnType,
    onColumnAdvSearchClick: () -> Unit
) {
    if (!column.metaconfig.searchable) return

    // @RBAC_PERMISSION: table.column.filter - 列过滤权限
    IconButton(
        onClick = onColumnAdvSearchClick,
        modifier = Modifier.size(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.FilterAlt,
            contentDescription = "字段高级搜索",
            tint = if (isFiltered) MaterialTheme.colorScheme.primary
            else LocalContentColor.current.copy(alpha = 0.5f),
            modifier = Modifier.size(16.dp)
        )
    }
}
