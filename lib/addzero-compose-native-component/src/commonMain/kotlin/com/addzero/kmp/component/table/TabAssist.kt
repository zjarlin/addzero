package com.addzero.kmp.component.table

import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.addzero.kmp.entity.low_table.EnumColumnAlignment
import com.addzero.kmp.entity.low_table.EnumSortDirection
import com.addzero.kmp.entity.low_table.StateSearchForm
import com.addzero.kmp.entity.low_table.StateSort

fun getTableCellAlignment(column: TableColumnType): Alignment = when (column.metaconfig.alignment) {
    EnumColumnAlignment.LEFT -> Alignment.CenterStart
    EnumColumnAlignment.CENTER -> Alignment.Center
    EnumColumnAlignment.RIGHT -> Alignment.CenterEnd
}

fun getTableTextAlign(column: TableColumnType): TextAlign = when (column.metaconfig.alignment) {
    EnumColumnAlignment.LEFT -> TextAlign.Start
    EnumColumnAlignment.CENTER -> TextAlign.Center
    EnumColumnAlignment.RIGHT -> TextAlign.End
}

fun getSortDirection(
    key: String, stateSorts:
    MutableSet<StateSort>
):
        EnumSortDirection {
    val sortDirection = stateSorts.find { it.columnKey == key }?.direction ?: EnumSortDirection.NONE
    return sortDirection
}

// 判断某列是否有过滤条件
fun getIsFiltered(columnKey: String, filterStateMap: Map<String, StateSearchForm>): Boolean {
    return filterStateMap.containsKey(columnKey)
}

