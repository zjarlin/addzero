package com.addzero.kmp.component.addzero_starter.addzero_table.controlled_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import com.addzero.kmp.component.drawer.AddDrawer
import com.addzero.kmp.component.form.DynamicForm

@Composable
fun  RenderDynamicFormDrawer(
    showForm: Boolean,
    currentItem: TableRowType?,
    onCloseForm: () -> Unit,
    columns: List<TableColumnType>,
    onFormSubmit: () -> Unit
) {
    if (!showForm) return
    val currentSelectItem = currentItem
    val isEdit = currentSelectItem != null
    val title = if (isEdit) "修改" else "新增"

    AddDrawer(
        visible = showForm,
        title = title,
        onClose = onCloseForm,
        onSubmit = onFormSubmit,
    ) {
        Column(
            modifier = Modifier.Companion.fillMaxWidth().height(600.dp) // 设置固定高度，避免无限高度限制
        ) {
            DynamicForm(
                columns = columns,
                item = currentItem,
//                modifier = TODO(),
//                howMuchColumn = TODO()
            )
        }
    }
}
