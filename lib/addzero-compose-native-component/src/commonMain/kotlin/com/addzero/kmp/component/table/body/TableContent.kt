package com.addzero.kmp.component.table.body

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.addzero.kmp.component.button.AddEditDeleteButton
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.TableRowType
import com.addzero.kmp.component.table.header.column.RenderCell

/**
 * 表格行组件
 */
@Composable
fun TableRow(
    getidFun: (TableRowType) -> Any,
    item: TableRowType,
    rowNumber: Int,
    isSelected: Boolean,
    multiSelect: Boolean,
    showActions: Boolean = true,
    rowHeight: Int,
    backgroundColor: Color,
    onRowClick: ((TableRowType) -> Unit)?,
    onDeleteItem: ((Any) -> Unit)?,
    onCheckboxClick: () -> Unit,
    horizontalScrollState: ScrollState,
    columns: List<TableColumnType>,
    renderCustomActions: @Composable () -> Unit,
    onEditClick: (TableRowType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(rowHeight.dp).background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
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
                    // @RBAC_PERMISSION: table.row.select - 行选择权限
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
            // @RBAC_PERMISSION: table.row.click - 行点击权限
            modifier = Modifier.weight(1f).fillMaxHeight().horizontalScroll(horizontalScrollState)
                .clickable(enabled = onRowClick != null) { onRowClick?.invoke(item) }.padding(start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
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


                // @RBAC_PERMISSION: table.row.edit - 行编辑权限
                // @RBAC_PERMISSION: table.row.delete - 行删除权限
                // @RBAC_PERMISSION: table.row.actions - 行自定义操作权限
                AddEditDeleteButton(onEditClick = {
                    onEditClick(item)
                }, onDeleteClick = {
                    onDeleteItem?.invoke(getidFun(item))
                }, renderCustomActions = {
                    renderCustomActions()
                })


            }
        }
    }
}

