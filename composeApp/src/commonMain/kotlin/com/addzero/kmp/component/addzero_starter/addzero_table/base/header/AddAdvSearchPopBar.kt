package com.addzero.kmp.component.addzero_starter.addzero_table.base.header

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.drawer.AddDrawer
import com.addzero.kmp.component.dropdown.AddDropdownSelector
import com.addzero.kmp.component.form.DynamicFormItem
import com.addzero.kmp.entity.low_table.EnumLogicOperator
import com.addzero.kmp.entity.low_table.EnumSearchOperator
import com.addzero.kmp.entity.low_table.StateSearchForm

/**
 * 高级搜索抽屉组件
 * @param [column]
 * @param [advSearchTitle]
 * @param [showFieldAdvSearch]
 * @param [onAdvSearchSubmit] 提交
 * @param [onClose]
 * @param [searchForm]
 * @param [onAdvSearchCLean] 需要清除筛选条件
 * @param [onSearchFormChange] 搜索条件变化
 */
@Composable
fun AddAdvSearchPopBar(
    column: TableColumnType?,
    advSearchTitle: String = "高级搜索: ${column?.title}",
    showFieldAdvSearch: Boolean,
    onAdvSearchSubmit: () -> Unit,
    onClose: () -> Unit,
    searchForm: StateSearchForm?,
    onAdvSearchCLean: () -> Unit,
    onSearchFormChange: (Any?, EnumSearchOperator, EnumLogicOperator) -> Unit
) {
    column ?: return
    val key = column?.key
    val existingForm = searchForm

    val initialValue = existingForm?.columnValue
    val initialOperator = existingForm?.operator ?: EnumSearchOperator.LIKE
    val initialLogic = existingForm?.logicType ?: EnumLogicOperator.AND

    var inputValue by remember(key) { mutableStateOf(initialValue) }
    var selectedOperator by remember(key) { mutableStateOf(initialOperator) }
    var selectedLogic by remember(key) { mutableStateOf(initialLogic) }

    AddDrawer(
        visible = showFieldAdvSearch,
        title = advSearchTitle,
        onClose = onClose,
        onSubmit = onAdvSearchSubmit,
    ) {
        Column {

            // 逻辑操作符下拉选择
            AddDropdownSelector(
                title = "逻辑符",
                options = EnumLogicOperator.entries,
                getLabel = { it.displayName },
                initialValue = selectedLogic,
                value = selectedLogic,
                onValueChange = {
                    selectedLogic = it ?: EnumLogicOperator.AND
                    onSearchFormChange(inputValue, selectedOperator, selectedLogic)
                },
            )

            Spacer(modifier = Modifier.height(16.dp))


            // 操作符下拉选择
            AddDropdownSelector(
                title = "操作符",
                options = EnumSearchOperator.entries,
                getLabel = { it.displayName },
                initialValue = selectedOperator,
                onValueChange = {
                    selectedOperator = it ?: EnumSearchOperator.LIKE
                    onSearchFormChange(inputValue, selectedOperator, selectedLogic)
                })

            Spacer(modifier = Modifier.height(12.dp))


            // 输入框
            DynamicFormItem(
                value = inputValue, onValueChange = {
                    inputValue = it
                    onSearchFormChange(inputValue, selectedOperator, selectedLogic)
                }, title = column?.title, kmpType = column.metaconfig.kmpType
            )

            Spacer(modifier = Modifier.height(16.dp))



            AddIconButton(
                text = "清除条件", imageVector = Icons.Default.Close, onClick = onAdvSearchCLean,
            )
        }
    }
}
