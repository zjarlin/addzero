package com.addzero.kmp.component.table.base.header.column

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.TableRowType

/**
 * 渲染单元格
 * @param [column]
 * @param [item]
 */
@Composable
fun RenderCell(
    column: TableColumnType, item: TableRowType
) {

    Surface(
        modifier = Modifier
            .width((column.metaconfig.widthRatio * 150).dp)
            .fillMaxHeight()
            .padding(horizontal = 8.dp),
        // 设置内容对齐方式
        tonalElevation = 0.dp // 可根据需要设置阴影
    ) {
        // 渲染自定义内容
        column.customCellRender(item)
    }
}
