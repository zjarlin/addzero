package com.addzero.kmp.component.table.header

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.addzero.kmp.component.card.AddCard
import com.addzero.kmp.component.card.MellumCardType
import com.addzero.kmp.component.search_bar.AddSearchBar
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.header.column.RenderHeaderColumn
import com.addzero.kmp.component.table.getIsFiltered
import com.addzero.kmp.component.table.getSortDirection
import com.addzero.kmp.entity.low_table.StateSearchForm
import com.addzero.kmp.entity.low_table.StateSort

/**
 * 🎨 表格头部卡片组件
 *
 * 使用 JetBrains Mellum 风格的卡片来展示表格头部，
 * 提供更现代化的视觉效果和交互体验
 *
 * @param modifier 修饰符
 * @param renderButtons 按钮渲染函数
 * @param keyword 搜索关键词
 * @param onKeyWordChanged 关键词变化回调
 * @param onSearch 搜索回调
 * @param headerHeight 表头高度
 * @param enableEditMode 是否启用编辑模式
 * @param isPageAllSelected 是否全选当前页
 * @param onCheckedChange 选择状态变化回调
 * @param columns 表格列配置
 * @param onChangeSorting 排序变化回调
 * @param onColumnAdvSearchClick 列高级搜索点击回调
 * @param stateSorts 排序状态
 * @param filterStateMap 过滤状态映射
 * @param cardType 卡片背景类型
 * @param enableHoverEffect 是否启用悬浮效果
 */
@Composable
fun AddTableHeader(
    modifier: Modifier = Modifier,
    renderButtons: @Composable () -> Unit,
    keyword: String,
    onKeyWordChanged: (String) -> Unit,
    onSearch: () -> Unit,
    headerHeight: Int,
    enableEditMode: Boolean,
    isPageAllSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    columns: List<TableColumnType>,
    onChangeSorting: (String) -> Unit,
    onColumnAdvSearchClick: (TableColumnType) -> Unit,
    stateSorts: MutableSet<StateSort>,
    filterStateMap: Map<String, StateSearchForm>,
    cardType: MellumCardType = MellumCardType.Light,
    enableHoverEffect: Boolean = true
) {
    val horizontalScrollState = rememberScrollState()

    AddCard(
        onClick = if (enableHoverEffect) {
            {}
        } else null,
        modifier = modifier
            .fillMaxWidth()
            .zIndex(2f), // 确保表头在最上层
        backgroundType = cardType,
        cornerRadius = 12.dp,
        elevation = 4.dp,
        padding = 16.dp,
        animationDuration = 200
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 第一行：搜索栏和按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 按钮区域
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    renderButtons()
                }

                // 搜索栏
                AddSearchBar(
                    keyword = keyword,
                    onKeyWordChanged = onKeyWordChanged,
                    onSearch = onSearch,
                    modifier = Modifier.weight(1f)
                )

            }

            // 第二行：表格列头（与表格内容对齐）
            if (columns.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 固定列区域（复选框和行号）- 与表格内容保持一致的宽度
                    Row(
                        modifier = Modifier
                            .width(120.dp)  // 与表格内容的固定列宽度一致
                            .fillMaxHeight()
                            .padding(start = 16.dp, end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 多选模式下的全选复选框
                        if (enableEditMode) {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .width(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Checkbox(
                                    checked = isPageAllSelected,
                                    onCheckedChange = onCheckedChange
                                )
                            }
                        }

                        // 行号列标题
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .width(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "#",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // 可滚动的数据列区域 - 与表格内容保持一致
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .horizontalScroll(horizontalScrollState)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 只显示可见的列
                        val visibleColumns = columns.filter { it.metaconfig.showInList }
                        visibleColumns.forEach { column ->
                            RenderHeaderColumn(
                                column = column,
                                sortDirection = getSortDirection(column.key, stateSorts),
                                onChangeSorting = { onChangeSorting(column.key) },
                                onColumnAdvSearchClick = { onColumnAdvSearchClick(column) },
                                isFiltered = getIsFiltered(column.key, filterStateMap)
                            )
                        }
                    }

                    // 固定操作列区域 - 与表格内容保持一致的宽度
                    Box(
                        modifier = Modifier
                            .width(160.dp)  // 与表格内容的操作列宽度一致
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "操作",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

/**
 * 🎨 表格头部统计信息卡片
 *
 * 用于展示表格的统计信息，如总数、选中数量等
 */
@Composable
fun AddTableStatsCard(
    modifier: Modifier = Modifier,
    totalCount: Int,
    selectedCount: Int = 0,
    filteredCount: Int? = null,
    cardType: MellumCardType = MellumCardType.Blue
) {
    AddCard(
        modifier = modifier,
        backgroundType = cardType,
        cornerRadius = 8.dp,
        elevation = 2.dp,
        padding = 12.dp
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 总数
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = totalCount.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "总计",
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalContentColor.current.copy(alpha = 0.7f)
                )
            }

            // 选中数量（如果有选中项）
            if (selectedCount > 0) {
                Divider(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp),
                    color = LocalContentColor.current.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = selectedCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "已选",
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }

            // 过滤后数量（如果有过滤）
            if (filteredCount != null && filteredCount != totalCount) {
                Divider(
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp),
                    color = LocalContentColor.current.copy(alpha = 0.3f)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = filteredCount.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "筛选",
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalContentColor.current.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
