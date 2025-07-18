package com.addzero.kmp.component.addzero_starter.addzero_table.base

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import com.addzero.kmp.component.addzero_starter.addzero_table.base.header.column.RenderCell
import com.addzero.kmp.component.button.AddEditDeleteButton
import com.addzero.kmp.entity.low_table.EnumButtonColor
import com.addzero.kmp.entity.low_table.StateActionButton
import com.addzero.kmp.viewmodel.table.StatePagination

/**
 * 表格内容区组件
 */
@Composable
fun TableContent(
    getIdFun: (TableRowType) -> Any = { it.hashCode() }, data: List<TableRowType>, selectedItems:
    Set<Any>, columns: List<TableColumnType>, pageState: StatePagination,
    multiSelect:
    Boolean, showActions: Boolean, rowHeight: Int, onRowClick: (TableRowType) -> Unit = {}, onDeleteItem: ((Any) -> Unit)? = null, onCheckboxClick: (TableRowType) -> Unit = {}, onBatchDelete: () -> Unit = {}, onBatchExport: () -> Unit = {}, scrollState: ScrollState? = null, modifier: Modifier = Modifier, renderCustomActions: @Composable (() -> Unit), onEditClick: (TableRowType) -> Unit
) {
    val horizontalScrollState = scrollState ?: rememberScrollState()

    // 批量操作按钮
    if (multiSelect && selectedItems.isNotEmpty()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "已选择 ${selectedItems.size} 项" + ",已选择的id$selectedItems", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(end = 16.dp)
            )

            Button(onClick = onBatchDelete) {
                Text("批量删除")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = onBatchExport) {
                Text("导出选中")
            }
        }
    }

    Box(modifier = modifier.fillMaxWidth()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (data.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(200.dp).padding(16.dp), contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "没有数据", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                itemsIndexed(data) { index, item ->
                    val isSelected = selectedItems.contains(getIdFun(item))
                    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    else if (index % 2 == 0) MaterialTheme.colorScheme.surface
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

                    val rowNumber = (pageState.currentPage - 1) * pageState.pageSize + index + 1

                    TableRow(
                        getidFun = getIdFun, item = item, rowNumber = rowNumber, isSelected = isSelected, multiSelect = multiSelect, showActions = showActions, rowHeight = rowHeight, backgroundColor = backgroundColor, onRowClick = onRowClick, onDeleteItem = onDeleteItem, onCheckboxClick = { onCheckboxClick(item) }, horizontalScrollState = horizontalScrollState, columns = columns, renderCustomActions = renderCustomActions, onEditClick = onEditClick
                    )

                    if (index < data.size - 1) {
                        HorizontalDivider(
                            Modifier, DividerDefaults.Thickness, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * 表格行组件
 */
@Composable
private fun  TableRow(
    getidFun: (TableRowType) -> Any, item: TableRowType, rowNumber: Int, isSelected: Boolean, multiSelect: Boolean, showActions: Boolean = true, rowHeight: Int, backgroundColor: Color, onRowClick: ((TableRowType) -> Unit)?, onDeleteItem: ((Any) -> Unit)?, onCheckboxClick: () -> Unit, horizontalScrollState: ScrollState, columns: List<TableColumnType>, renderCustomActions: @Composable () -> Unit, onEditClick: (TableRowType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(rowHeight.dp).background(backgroundColor), verticalAlignment = Alignment.CenterVertically
    ) {
        // 固定列区域（复选框和行号）
        Row(
            modifier = Modifier.width(120.dp)  // 固定宽度
                .fillMaxHeight().background(backgroundColor).zIndex(1f), verticalAlignment = Alignment.CenterVertically
        ) {
            // 多选框列
            if (multiSelect) {
                Box(
                    modifier = Modifier.padding(horizontal = 4.dp).width(40.dp), contentAlignment = Alignment.Center
                ) {
                    Checkbox(
                        checked = isSelected, onCheckedChange = { onCheckboxClick() })
                }
            }

            // 行号列
            Box(
                modifier = Modifier.padding(horizontal = 4.dp).width(40.dp), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rowNumber", style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ), color = MaterialTheme.colorScheme.outline
                )
            }
        }

        // 可滚动的数据列区域
        Row(
            modifier = Modifier.weight(1f).fillMaxHeight().horizontalScroll(horizontalScrollState).clickable(enabled = onRowClick != null) { onRowClick?.invoke(item) }.padding(start = 8.dp, end = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            // 数据列
            val visibleColumns = columns.filter { it.metaconfig.showInList }
            for (column in visibleColumns) {
                RenderCell(
                    column, item
                )
            }
        }

        // 固定操作列区域
        if (showActions) {

            Box(
                modifier = Modifier.width(160.dp)  // 固定宽度
                    .fillMaxHeight().background(backgroundColor).zIndex(1f), contentAlignment = Alignment.Center
            ) {


                AddEditDeleteButton(
                    onEditClick = {
                        onEditClick(item)
                    }, onDeleteClick = {
                        onDeleteItem?.invoke(getidFun(item))
                    },
                    renderCustomActions = {
                        renderCustomActions()
                    }
                )


            }
        }
    }
}

@Composable
private fun <T> RenderActionMenus(
    visibleActions: List<StateActionButton<T>>, showMenu: Boolean, onClick: StateActionButton<T>, item: T, enabled: StateActionButton<T>
) {
    var showMenu1 = showMenu



    if (visibleActions.isNotEmpty()) {
        Box {
            IconButton(
                onClick = { showMenu1 = true }, modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert, contentDescription = "更多操作", modifier = Modifier.size(28.dp)
                )
            }

            DropdownMenu(
                expanded = showMenu1, onDismissRequest = { showMenu1 = false }) {
                visibleActions.forEach { action ->
                    DropdownMenuItem(
                        text = { Text(action.text) }, leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MoreVert, contentDescription = null, tint = when (action.color) {
                                    EnumButtonColor.PRIMARY -> MaterialTheme.colorScheme.primary
                                    EnumButtonColor.DANGER -> MaterialTheme.colorScheme.error
                                    EnumButtonColor.WARNING -> MaterialTheme.colorScheme.tertiary
                                    EnumButtonColor.SUCCESS -> MaterialTheme.colorScheme.secondary
                                    EnumButtonColor.DEFAULT -> LocalContentColor.current
                                }
                            )
                        }, onClick = {
                            showMenu1 = false
                            action.onClick(item)
                        }, enabled = action.enabled(item)
                    )
                }
            }
        }
    }
}
