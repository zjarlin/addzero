package com.addzero.kmp.component.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.TableRowType
import com.addzero.kmp.component.high_level.AddMultiColumnContainer

/**
 * 通用动态表单组件，根据字段元数据渲染不同类型的输入控件
 * @param [columns] 字段元数据列表
 * @param [item] 当前表单值
 * @param [modifier] 修饰符
 * @param [howMuchColumn]
 */
@Composable
fun DynamicForm(
    columns: List<TableColumnType>,
    item: TableRowType?,
    modifier: Modifier = Modifier,
    howMuchColumn: Int = 2,
) {
    AddMultiColumnContainer(
        howMuchColumn = howMuchColumn,
        modifier = modifier,
        items = columns.map { column ->
            {
//                val key = column.key
//                column.getFun(item, key)
                column.customFormRender(item)
            }
        }
    )
}
