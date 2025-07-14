package com.addzero.kmp.component.table

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 📊 通用表格组件
 * 
 * 基于 LazyColumn 的高性能表格组件，提供：
 * - 动态列配置
 * - 排序功能
 * - 行选择
 * - 自定义渲染
 * - 分页支持
 * 
 * @param T 数据类型
 * @param data 表格数据
 * @param columns 列配置
 * @param modifier 修饰符
 * @param onRowClick 行点击回调
 * @param onRowSelect 行选择回调
 * @param selectedRows 已选择的行
 * @param sortable 是否支持排序
 * @param striped 是否显示斑马纹
 * @param bordered 是否显示边框
 * @param compact 是否紧凑模式
 */
@Composable
fun <T> AddTable(
    data: List<T>,
    columns: List<AddTableColumn<T>>,
    modifier: Modifier = Modifier,
    onRowClick: ((T) -> Unit)? = null,
    onRowSelect: ((T, Boolean) -> Unit)? = null,
    selectedRows: Set<T> = emptySet(),
    sortable: Boolean = true,
    striped: Boolean = true,
    bordered: Boolean = true,
    compact: Boolean = false
) {
    var sortColumn by remember { mutableStateOf<String?>(null) }
    var sortAscending by remember { mutableStateOf(true) }
    
    // 排序数据
    val sortedData = remember(data, sortColumn, sortAscending) {
        if (sortColumn != null) {
            val column = columns.find { it.key == sortColumn }
            if (column?.sortable == true && column.sortComparator != null) {
                if (sortAscending) {
                    data.sortedWith(column.sortComparator)
                } else {
                    data.sortedWith(column.sortComparator.reversed())
                }
            } else {
                data
            }
        } else {
            data
        }
    }
    
    val rowHeight = if (compact) 40.dp else 56.dp
    val headerHeight = if (compact) 36.dp else 48.dp
    
    Column(
        modifier = modifier.then(
            if (bordered) {
                Modifier.border(1.dp, MaterialTheme.colorScheme.outline)
            } else {
                Modifier
            }
        )
    ) {
        // 表头
        TableHeader(
            columns = columns,
            sortColumn = sortColumn,
            sortAscending = sortAscending,
            onSort = { columnKey ->
                if (sortable) {
                    if (sortColumn == columnKey) {
                        sortAscending = !sortAscending
                    } else {
                        sortColumn = columnKey
                        sortAscending = true
                    }
                }
            },
            height = headerHeight,
            bordered = bordered,
            onSelectAll = if (onRowSelect != null) {
                { selectAll ->
                    sortedData.forEach { row ->
                        onRowSelect(row, selectAll)
                    }
                }
            } else null,
            allSelected = selectedRows.size == sortedData.size && sortedData.isNotEmpty()
        )
        
        // 表格内容
        LazyColumn {
            itemsIndexed(sortedData) { index, row ->
                TableRow(
                    row = row,
                    columns = columns,
                    index = index,
                    isSelected = selectedRows.contains(row),
                    onRowClick = onRowClick,
                    onRowSelect = onRowSelect,
                    height = rowHeight,
                    striped = striped,
                    bordered = bordered
                )
            }
        }
    }
}

/**
 * 表头组件
 */
@Composable
private fun <T> TableHeader(
    columns: List<AddTableColumn<T>>,
    sortColumn: String?,
    sortAscending: Boolean,
    onSort: (String) -> Unit,
    height: Dp,
    bordered: Boolean,
    onSelectAll: ((Boolean) -> Unit)?,
    allSelected: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(
                if (bordered) {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline)
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 选择列
        if (onSelectAll != null) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Checkbox(
                    checked = allSelected,
                    onCheckedChange = onSelectAll
                )
            }
        }
        
        // 数据列
        columns.forEach { column ->
            TableHeaderCell(
                column = column,
                isSorted = sortColumn == column.key,
                sortAscending = sortAscending,
                onSort = { onSort(column.key) },
                bordered = bordered
            )
        }
    }
}

/**
 * 表头单元格
 */
@Composable
private fun <T> RowScope.TableHeaderCell(
    column: AddTableColumn<T>,
    isSorted: Boolean,
    sortAscending: Boolean,
    onSort: () -> Unit,
    bordered: Boolean
) {
    Box(
        modifier = Modifier
            .weight(column.weight)
            .fillMaxHeight()
            .then(
                if (column.sortable) {
                    Modifier.clickable { onSort() }
                } else {
                    Modifier
                }
            )
            .then(
                if (bordered) {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp),
        contentAlignment = when (column.align) {
            TableAlign.START -> Alignment.CenterStart
            TableAlign.CENTER -> Alignment.Center
            TableAlign.END -> Alignment.CenterEnd
        }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = column.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (column.sortable && isSorted) {
                Icon(
                    imageVector = if (sortAscending) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                    contentDescription = if (sortAscending) "升序" else "降序",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * 表格行组件
 */
@Composable
private fun <T> TableRow(
    row: T,
    columns: List<AddTableColumn<T>>,
    index: Int,
    isSelected: Boolean,
    onRowClick: ((T) -> Unit)?,
    onRowSelect: ((T, Boolean) -> Unit)?,
    height: Dp,
    striped: Boolean,
    bordered: Boolean
) {
    val backgroundColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        striped && index % 2 == 1 -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        else -> Color.Transparent
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(backgroundColor)
            .then(
                if (onRowClick != null) {
                    Modifier.clickable { onRowClick(row) }
                } else {
                    Modifier
                }
            )
            .then(
                if (bordered) {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline)
                } else {
                    Modifier
                }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 选择列
        if (onRowSelect != null) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { checked ->
                        onRowSelect(row, checked)
                    }
                )
            }
        }
        
        // 数据列
        columns.forEach { column ->
            TableDataCell(
                row = row,
                column = column,
                bordered = bordered
            )
        }
    }
}

/**
 * 数据单元格
 */
@Composable
private fun <T> RowScope.TableDataCell(
    row: T,
    column: AddTableColumn<T>,
    bordered: Boolean
) {
    Box(
        modifier = Modifier
            .weight(column.weight)
            .fillMaxHeight()
            .then(
                if (bordered) {
                    Modifier.border(1.dp, MaterialTheme.colorScheme.outline)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp),
        contentAlignment = when (column.align) {
            TableAlign.START -> Alignment.CenterStart
            TableAlign.CENTER -> Alignment.Center
            TableAlign.END -> Alignment.CenterEnd
        }
    ) {
        if (column.render != null) {
            // 自定义渲染
            column.render.invoke(row)
        } else {
            // 默认文本渲染
            val value = column.getValue(row)
            Text(
                text = value?.toString() ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = when (column.align) {
                    TableAlign.START -> TextAlign.Start
                    TableAlign.CENTER -> TextAlign.Center
                    TableAlign.END -> TextAlign.End
                }
            )
        }
    }
}

/**
 * 📋 表格列配置
 */
data class AddTableColumn<T>(
    val key: String,
    val title: String,
    val weight: Float = 1f,
    val align: TableAlign = TableAlign.START,
    val sortable: Boolean = false,
    val sortComparator: Comparator<T>? = null,
    val getValue: (T) -> Any? = { null },
    val render: (@Composable (T) -> Unit)? = null
)

/**
 * 表格对齐方式
 */
enum class TableAlign {
    START, CENTER, END
}

/**
 * 🎯 简化的表格组件（用于快速原型）
 */
@Composable
fun <T> AddSimpleTable(
    data: List<T>,
    headers: List<String>,
    getValue: (T, Int) -> String,
    modifier: Modifier = Modifier,
    onRowClick: ((T) -> Unit)? = null
) {
    val columns = headers.mapIndexed { index, header ->
        AddTableColumn<T>(
            key = "col_$index",
            title = header,
            getValue = { row -> getValue(row, index) }
        )
    }

    AddTable(
        data = data,
        columns = columns,
        modifier = modifier,
        onRowClick = onRowClick,
        sortable = false,
        striped = true,
        bordered = true
    )
}
