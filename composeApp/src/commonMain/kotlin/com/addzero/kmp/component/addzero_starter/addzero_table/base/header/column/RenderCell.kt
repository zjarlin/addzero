package com.addzero.kmp.component.addzero_starter.addzero_table.base.header.column

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import com.addzero.kmp.component.assist_fun.getTableCellAlignment

/**
 * 渲染单元格
 * @param [column]
 * @param [item]
 */
@Composable
fun  RenderCell(
    column: TableColumnType, item: TableRowType
) {
    Box(
        modifier = Modifier.Companion.width((column.metaconfig.widthRatio * 150).dp)
            .fillMaxHeight()
            .padding(horizontal = 8.dp),

        contentAlignment = getTableCellAlignment(column)
    ) {
        column.customCellRender(item)
    }
}
