package com.addzero.kmp.component.table.base.header.column

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.getTableCellAlignment
import com.addzero.kmp.component.table.getTableTextAlign
import com.addzero.kmp.entity.low_table.EnumSortDirection

/**
 * 渲染表头列
 * @param column 列定义
 * @param sortDirection 排序方向
 * @param onChangeSorting 改变排序的回调
 * @param onColumnAdvSearchClick 点击高级搜索的回调
 * @param isFiltered 是否已过滤
 */
@Composable
fun RenderHeaderColumn(
    column: TableColumnType,
    sortDirection: EnumSortDirection,
    onChangeSorting: (String) -> Unit,
    onColumnAdvSearchClick: () -> Unit,
    isFiltered: Boolean,
) {
    val title = column.title
    val metaconfig = column.metaconfig
    val sortable = metaconfig.sortable
    val textAlign = getTableTextAlign(column)

    // 使用Box来确保表头列有正确的宽度和对齐方式
    Box(
        modifier = Modifier
            .width((column.metaconfig.widthRatio * 150).dp)
            .fillMaxHeight()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = getTableCellAlignment(column)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            //渲染标题
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = textAlign,
                modifier = Modifier.weight(1f)
            )

            // 排序图标 - 使用本地状态
            if (sortable) {
                when (sortDirection) {
                    EnumSortDirection.ASC -> {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "升序",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    EnumSortDirection.DESC -> {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "降序",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "排序",
                            tint = LocalContentColor.current.copy(alpha = 0.5f),
                            modifier = Modifier.alpha(0.5f).size(16.dp)
                        )
                    }
                }
            }

            // 渲染过滤图标
            RenderTableHeaderColumnFilterIcon(
                isFiltered = isFiltered,
                column = column,
                onColumnAdvSearchClick = onColumnAdvSearchClick
            )
        }
    }
}

