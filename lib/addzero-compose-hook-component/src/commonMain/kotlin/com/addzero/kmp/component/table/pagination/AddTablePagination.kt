package com.addzero.kmp.component.table.pagination

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.card.AddCard
import com.addzero.kmp.component.card.MellumCardType
import com.addzero.kmp.component.table.viewmodel.StatePagination

/**
 * 🎨 表格分页卡片组件
 * 
 * 使用 JetBrains Mellum 风格的卡片来展示分页控件，
 * 提供更现代化的视觉效果和交互体验
 * 
 * @param modifier 修饰符
 * @param statePagination 分页状态
 * @param pageSizeOptions 页面大小选项
 * @param enablePagination 是否启用分页
 * @param onPageSizeChange 页面大小变化回调
 * @param onGoFirstPage 跳转到首页回调
 * @param onPreviousPage 上一页回调
 * @param onGoToPage 跳转到指定页回调
 * @param onNextPage 下一页回调
 * @param onGoLastPage 跳转到末页回调
 * @param cardType 卡片背景类型
 * @param showPageSizeSelector 是否显示页面大小选择器
 * @param showPageInfo 是否显示页面信息
 * @param compactMode 是否使用紧凑模式
 */
@Composable
fun AddTablePagination(
    modifier: Modifier = Modifier,
    statePagination: StatePagination,
    pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
    enablePagination: Boolean,
    onPageSizeChange: (Int) -> Unit,
    onGoFirstPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onGoToPage: (Int) -> Unit,
    onNextPage: () -> Unit,
    onGoLastPage: () -> Unit,
    cardType: MellumCardType = MellumCardType.Light,
    showPageSizeSelector: Boolean = true,
    showPageInfo: Boolean = true,
    compactMode: Boolean = false
) {
    if (!enablePagination) return
    
    AddCard(
        modifier = modifier.fillMaxWidth(),
        backgroundType = cardType,
        cornerRadius = 12.dp,
        elevation = 2.dp,
        padding = if (compactMode) 12.dp else 16.dp,
        animationDuration = 200
    ) {
        if (compactMode) {
            // 紧凑模式：单行布局
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 页面信息
                if (showPageInfo) {
                    PaginationInfo(
                        statePagination = statePagination,
                        compact = true
                    )
                }
                
                // 分页控制按钮
                PaginationControls(
                    statePagination = statePagination,
                    onGoFirstPage = onGoFirstPage,
                    onPreviousPage = onPreviousPage,
                    onGoToPage = onGoToPage,
                    onNextPage = onNextPage,
                    onGoLastPage = onGoLastPage,
                    compact = true
                )
            }
        } else {
            // 标准模式：单行布局
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 左侧：页面信息
                if (showPageInfo) {
                    PaginationInfo(
                        statePagination = statePagination,
                        compact = false
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // 中间：分页控制按钮
                PaginationControls(
                    statePagination = statePagination,
                    onGoFirstPage = onGoFirstPage,
                    onPreviousPage = onPreviousPage,
                    onGoToPage = onGoToPage,
                    onNextPage = onNextPage,
                    onGoLastPage = onGoLastPage,
                    compact = false
                )

                // 右侧：页面大小选择器
                if (showPageSizeSelector) {
                    PageSizeSelector(
                        currentPageSize = statePagination.pageSize,
                        pageSizeOptions = pageSizeOptions,
                        onPageSizeChange = onPageSizeChange
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }
            }
        }
    }
}

/**
 * 分页信息组件
 */
@Composable
private fun PaginationInfo(
    statePagination: StatePagination,
    compact: Boolean
) {
    val startItem = (statePagination.currentPage - 1) * statePagination.pageSize + 1

    val endItem = minOf(statePagination.currentPage * statePagination.pageSize, statePagination.totalItems)
    
    if (compact) {
        Text(
            text = "${statePagination.currentPage}/${statePagination.totalPages}",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    } else {
        Column {
            Text(
                text = "第 $startItem-$endItem 项，共 ${statePagination.totalItems} 项",
                style = MaterialTheme.typography.bodyMedium,
                color = LocalContentColor.current.copy(alpha = 0.8f)
            )
            Text(
                text = "第 ${statePagination.currentPage} 页，共 ${statePagination.totalPages} 页",
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.6f)
            )
        }
    }
}

/**
 * 页面大小选择器
 */
@Composable
private fun PageSizeSelector(
    currentPageSize: Int,
    pageSizeOptions: List<Int>,
    onPageSizeChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.height(36.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = LocalContentColor.current.copy(alpha = 0.8f)
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = androidx.compose.ui.graphics.SolidColor(
                    LocalContentColor.current.copy(alpha = 0.3f)
                )
            )
        ) {
            Text(
                text = "$currentPageSize 条/页",
                style = MaterialTheme.typography.bodySmall
            )
        }
        
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            pageSizeOptions.forEach { size ->
                DropdownMenuItem(
                    text = { Text("$size 条/页") },
                    onClick = {
                        onPageSizeChange(size)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * 分页控制按钮组
 */
@Composable
private fun PaginationControls(
    statePagination: StatePagination,
    onGoFirstPage: () -> Unit,
    onPreviousPage: () -> Unit,
    onGoToPage: (Int) -> Unit,
    onNextPage: () -> Unit,
    onGoLastPage: () -> Unit,
    compact: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(if (compact) 4.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 首页按钮
        PaginationButton(
            onClick = onGoFirstPage,
            enabled = statePagination.currentPage > 1,
            compact = compact
        ) {
            Icon(
                imageVector = Icons.Default.SkipPrevious,
                contentDescription = "首页",
                modifier = Modifier.size(if (compact) 16.dp else 20.dp)
            )
        }
        
        // 上一页按钮
        PaginationButton(
            onClick = onPreviousPage,
            enabled = statePagination.currentPage > 1,
            compact = compact
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "上一页",
                modifier = Modifier.size(if (compact) 16.dp else 20.dp)
            )
        }
        
        // 页码按钮
        if (!compact) {
            PageNumberButtons(
                currentPage = statePagination.currentPage,
                totalPages = statePagination.totalPages,
                onGoToPage = onGoToPage
            )
        }
        
        // 下一页按钮
        PaginationButton(
            onClick = onNextPage,
            enabled = statePagination.currentPage < statePagination.totalPages,
            compact = compact
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "下一页",
                modifier = Modifier.size(if (compact) 16.dp else 20.dp)
            )
        }
        
        // 末页按钮
        PaginationButton(
            onClick = onGoLastPage,
            enabled = statePagination.currentPage < statePagination.totalPages,
            compact = compact
        ) {
            Icon(
                imageVector = Icons.Default.SkipNext,
                contentDescription = "末页",
                modifier = Modifier.size(if (compact) 16.dp else 20.dp)
            )
        }
    }
}

/**
 * 分页按钮
 */
@Composable
private fun PaginationButton(
    onClick: () -> Unit,
    enabled: Boolean,
    compact: Boolean,
    content: @Composable () -> Unit
) {
    val size = if (compact) 32.dp else 40.dp
    
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(
                if (enabled) {
                    LocalContentColor.current.copy(alpha = 0.1f)
                } else {
                    LocalContentColor.current.copy(alpha = 0.05f)
                }
            )
            .border(
                width = 1.dp,
                color = LocalContentColor.current.copy(alpha = if (enabled) 0.2f else 0.1f),
                shape = CircleShape
            )
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(
                alpha = if (enabled) 0.8f else 0.4f
            )
        ) {
            content()
        }
    }
}

/**
 * 页码按钮组
 */
@Composable
private fun PageNumberButtons(
    currentPage: Int,
    totalPages: Int,
    onGoToPage: (Int) -> Unit
) {
    // 计算显示的页码范围
    val visiblePages = 5
    val halfVisible = visiblePages / 2
    
    val startPage = maxOf(1, currentPage - halfVisible)
    val endPage = minOf(totalPages, startPage + visiblePages - 1)
    
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 显示页码按钮
        for (page in startPage..endPage) {
            val isCurrentPage = page == currentPage
            
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCurrentPage) {
                            LocalContentColor.current.copy(alpha = 0.2f)
                        } else {
                            Color.Transparent
                        }
                    )
                    .border(
                        width = if (isCurrentPage) 2.dp else 1.dp,
                        color = LocalContentColor.current.copy(
                            alpha = if (isCurrentPage) 0.6f else 0.2f
                        ),
                        shape = CircleShape
                    )
                    .clickable { onGoToPage(page) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = page.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (isCurrentPage) FontWeight.Bold else FontWeight.Normal,
                    color = LocalContentColor.current.copy(
                        alpha = if (isCurrentPage) 1f else 0.7f
                    )
                )
            }
        }
        
        // 如果有更多页面，显示省略号
        if (endPage < totalPages) {
            Text(
                text = "...",
                style = MaterialTheme.typography.bodySmall,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}
