package site.addzero.screens.product

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import site.addzero.component.table.biz.AddTable
import site.addzero.component.table.original.entity.ColumnConfig
import site.addzero.generated.forms.ThingModelForm
import site.addzero.generated.forms.rememberThingModelFormState
import site.addzero.generated.isomorphic.ThingModelIso
import site.addzero.screens.product.vm.ThingModelViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThingModelScreen(viewModel: ThingModelViewModel) {


    AddTable(
        getRowId = { it.id?:0L },

        data = viewModel.data,
        columns = listOf(
            ColumnConfig(
                key = "identifier",
                comment = "属性标识",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "name",
                comment = "属性名称",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "dataType",
                comment = "数据类型",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "dataPrecision",
                comment = "精度值",
                kmpType = "kotlin.Int"
            ),
            ColumnConfig(
                key = "accessMode",
                comment = "访问方式",
                kmpType = "kotlin.String"
            )
        ),
        getColumnKey = { it.key },
        getColumnLabel = {
            Text(
                it.comment
            )
        },
        onSearch = { keyword, serchState, stateSort, StatePagination ->
            viewModel.onSearch(keyword, serchState, stateSort, StatePagination)
        },
        onSaveClick = {
            viewModel.showForm = true
        },
        onImportClick = {},
        onExportClick = { keyword, serchState, stateSort, StatePagination ->
        },
        onBatchDelete = {
            viewModel.onBatchDelete(it)
        },
        onBatchExport = {},
        onEditClick = {
            viewModel.onEditClick(it)
        },
        onDeleteClick = {
            viewModel.onDeleteClick(it)
        }
    )
    val formState = rememberThingModelFormState(viewModel.selectedT)
    ThingModelForm(
        formState,
        visible = viewModel.showForm,
        title = "物模型属性表单",
        onClose = { viewModel.showForm = false },
        onSubmit = {
            viewModel.onSaveForm(formState.value)
            formState.value = ThingModelIso()
        },
    )
}
