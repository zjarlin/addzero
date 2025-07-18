package com.addzero.kmp.component.table.base.header.column

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.TableRowType
import com.addzero.kmp.component.table.getTableCellAlignment

/**
 * 渲染单元格
 * @param [column]
 * @param [item]
 */
@Composable
fun RenderCell(
    column: TableColumnType, item: TableRowType
) {
    // 使用Box来确保内容居中对齐，并且背景透明
    Box(
        modifier = Modifier
            .width((column.metaconfig.widthRatio * 150).dp)
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = getTableCellAlignment(column)
    ) {
        // 渲染自定义内容
        column.customCellRender(item)
    }
}
