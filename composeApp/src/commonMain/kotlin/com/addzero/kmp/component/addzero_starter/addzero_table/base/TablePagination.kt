package com.addzero.kmp.component.addzero_starter.addzero_table.base

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.addzero.kmp.entity.low_table.StatePagination

/**
 * 表格分页控件组件
 */
@Composable
fun  TablePagination(
    modifier: Modifier = Modifier,
    statePagination: StatePagination,
    pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
    enablePagination: Boolean,
    onPageSizeChange: (Int) -> Unit,
    onGoFirstPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onGoToPage: (Int) -> Unit,
    onNextPage: () -> Unit,
    onGoLastPage: () -> Unit
) {

//    val defaultPageSize = tableConfig.defaultPageSize
//    var statePagination by mutableStateOf(statePagination)

    if (!enablePagination) return
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 分页信息
            Text(
                text = "显示 ${statePagination.startItem}-${statePagination.endItem} 共 ${statePagination.totalItems} 条",
                style = MaterialTheme.typography.bodyMedium
            )

            // 分页控件
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 每页显示条数选择器
                PageSizeSelector(
                    currentPageSize = statePagination.pageSize,
                    pageSizeOptions = pageSizeOptions,
                    onPageSizeChange = onPageSizeChange
                )

                Spacer(modifier = Modifier.width(16.dp))

                // 首页按钮
                IconButton(
                    onClick = onGoFirstPage,
                    enabled = statePagination.hasPreviousPage
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "首页"
                    )
                }

                // 上一页按钮
                IconButton(
                    onClick = onPreviousPage,
                    enabled = statePagination.hasPreviousPage
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "上一页"
                    )
                }

                // 页码选择器
                PageSelector(
                    currentPage = statePagination.currentPage,
                    totalPages = statePagination.totalPages,
                    onPageChange = onGoToPage
                )

                // 下一页按钮
                IconButton(
                    onClick = onNextPage,
                    enabled = statePagination.hasNextPage
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "下一页"
                    )
                }
                // 尾页按钮
                IconButton(
                    onClick = onGoLastPage,
                    enabled = statePagination.hasNextPage
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "尾页"
                    )
                }
            }
        }
    }
}

/**
 * 每页显示条数选择器
 */
@Composable
private fun PageSizeSelector(
    currentPageSize: Int,
    pageSizeOptions: List<Int>,
    onPageSizeChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "每页:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(end = 8.dp)
        )

        Box {
            OutlinedButton(
                onClick = { expanded = true },
                shape = RoundedCornerShape(4.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                modifier = Modifier.height(32.dp)
            ) {
                Text(
                    text = "$currentPageSize",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                pageSizeOptions.forEach { size ->
                    DropdownMenuItem(
                        text = { Text("$size") },
                        onClick = {
                            onPageSizeChange(size)
                            expanded = false
                        },
                        modifier = Modifier.height(40.dp)
                    )
                }
            }
        }
    }
}

/**
 * 页码选择器
 */
@Composable
private fun PageSelector(
    currentPage: Int,
    totalPages: Int,
    onPageChange: (Int) -> Unit
) {
    // 计算要显示的页码
    val pageNumbers = calculatePageNumbers(currentPage, totalPages)

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        pageNumbers.forEach { page ->
            if (page == -1) {
                // 省略号
                Text(
                    text = "...",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            } else {
                // 页码按钮
                PageButton(
                    page = page,
                    selected = page == currentPage,
                    onClick = { onPageChange(page) }
                )
            }
        }
    }
}

/**
 * 页码按钮
 */
@Composable
private fun PageButton(
    page: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .then(
                if (selected)
                    Modifier.background(MaterialTheme.colorScheme.primary)
                else
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = page.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 计算要显示的页码
 * 逻辑：显示首页、尾页，以及当前页附近的一些页码，其余用省略号代替
 */
private fun calculatePageNumbers(currentPage: Int, totalPages: Int): List<Int> {
    if (totalPages <= 7) {
        // 页数较少，全部显示
        return (1..totalPages).toList()
    }

    val result = mutableListOf<Int>()

    // 添加首页
    result.add(1)

    // 当前页靠近首页
    if (currentPage <= 4) {
        result.addAll((2..5).toList())
        result.add(-1) // 省略号
        result.add(totalPages)
    }
    // 当前页靠近尾页
    else if (currentPage >= totalPages - 3) {
        result.add(-1) // 省略号
        result.addAll((totalPages - 4..totalPages - 1).toList())
        result.add(totalPages)
    }
    // 当前页在中间
    else {
        result.add(-1) // 省略号
        result.addAll((currentPage - 1..currentPage + 1).toList())
        result.add(-1) // 省略号
        result.add(totalPages)
    }

    return result
}
