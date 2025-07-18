package com.addzero.kmp.component.table.base.header

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.addzero.kmp.component.table.base.header.column.RenderHeaderColumn
import com.addzero.kmp.component.search_bar.AddSearchBar
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.getIsFiltered
import com.addzero.kmp.component.table.getSortDirection
import com.addzero.kmp.entity.low_table.StateSearchForm
import com.addzero.kmp.entity.low_table.StateSort


/**
 * 表格头部组件
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
    filterStateMap: Map<String, StateSearchForm>
) {
    val horizontalScrollState = rememberScrollState()
    Card(
        modifier = modifier.fillMaxWidth().zIndex(2f), // 确保表头在最上层
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        )
    ) {
        Column {
            // 搜索栏和工具按钮
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                renderButtons()

                AddSearchBar(
                    keyword = keyword,
                    onKeyWordChanged = onKeyWordChanged,
                    onSearch = onSearch,
                    modifier = Modifier.weight(1f),
                )
            }
        }


        // 列标题
        Row(
            modifier = Modifier.fillMaxWidth().height(headerHeight.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            // 固定列区域（复选框和行号）
            Row(
                modifier = Modifier.width(120.dp)  // 固定宽度
                    .fillMaxHeight().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    .padding(start = 16.dp, end = 8.dp).zIndex(1f), verticalAlignment = Alignment.CenterVertically
            ) {
                // 多选框列
                RenderHeaderMutiSelect(
                    enableEditMode, checked = isPageAllSelected, onCheckedChange = onCheckedChange
                )

                // 行号列标题
                Box(
                    modifier = Modifier.padding(horizontal = 4.dp).width(40.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "#", style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center
                    )
                }
            }

            // 数据列标题区域（可滚动）
            Row(
                modifier = Modifier.weight(1f).fillMaxHeight().horizontalScroll(horizontalScrollState)
                    .padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                // 数据列 - 只显示可见的列
                val visibleColumns = columns.filter { it.metaconfig.showInList }
                for (column in visibleColumns) {
                    RenderHeaderColumn(
                        column = column,
                        sortDirection = getSortDirection(column.key, stateSorts = stateSorts),
                        onChangeSorting = onChangeSorting,
                        onColumnAdvSearchClick = { onColumnAdvSearchClick(column) },
                        isFiltered = getIsFiltered(column.key, filterStateMap)
                    )
                }
            }

            // 固定操作列
            Box(
                modifier = Modifier.width(160.dp)  // 固定宽度
                    .fillMaxHeight().background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f))
                    .padding(horizontal = 16.dp).zIndex(1f), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "操作", style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ), textAlign = TextAlign.Center
                )
            }
        }

        // 分隔线
        HorizontalDivider(Modifier, DividerDefaults.Thickness, color = MaterialTheme.colorScheme.outlineVariant)
    }
}


