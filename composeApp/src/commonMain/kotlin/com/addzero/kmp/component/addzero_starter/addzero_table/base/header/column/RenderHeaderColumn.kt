package com.addzero.kmp.component.addzero_starter.addzero_table.base.header.column

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.assist_fun.getTableTextAlign
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
    val key = column.key
    val title = column.title

    val metaconfig = column.metaconfig
    val widthRatio = metaconfig.widthRatio
    val sortable = metaconfig.sortable

    // 使用本地状态跟踪此列的排序方向
//    var sortDirection by remember(column.key, forceRefresh) {
//        // 初始化为当前的排序方向
//        mutableStateOf(tableViewModel.getSortDirection(column.key))
//    }
//    val sortDirection = getSortDirection(key, stateSorts)

    val textAlign = getTableTextAlign(column)


    Box(
        modifier = Modifier.Companion.width((widthRatio * 150).dp)
            .fillMaxHeight().clickable(enabled = sortable) {
            if (sortable) {
                onChangeSorting(key)
            }
        }.padding(horizontal = 8.dp), contentAlignment = Alignment.Companion.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.Companion.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            //渲染标题
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Companion.Bold
                ),
                maxLines = 1,
                overflow = TextOverflow.Companion.Ellipsis,
                textAlign = textAlign,
                modifier = Modifier.Companion.weight(1f)
            )

            // 排序图标 - 使用本地状态
            if (sortable) {
                when (sortDirection) {
                    EnumSortDirection.ASC -> {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "升序",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.Companion.size(16.dp)
                        )
                    }

                    EnumSortDirection.DESC -> {
                        Icon(
                            imageVector = Icons.Default.ArrowDownward,
                            contentDescription = "降序",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.Companion.size(16.dp)
                        )
                    }

                    else -> {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "排序",
                            tint = LocalContentColor.current.copy(alpha = 0.5f),
                            modifier = Modifier.Companion.alpha(0.5f).size(16.dp)
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

