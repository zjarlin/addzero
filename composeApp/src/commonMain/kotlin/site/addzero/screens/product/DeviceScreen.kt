package site.addzero.screens.product

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import site.addzero.component.table.biz.AddTable
import site.addzero.component.table.original.entity.ColumnConfig
import site.addzero.generated.forms.DeviceForm
import site.addzero.generated.forms.rememberDeviceFormState
import site.addzero.generated.isomorphic.DeviceIso
import site.addzero.screens.product.vm.DeviceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreen(viewModel: DeviceViewModel) {
    AddTable(
        getRowId = { it.id?:0L },

        data = viewModel.data,
        columns = listOf(
            ColumnConfig(
                key = "name",
                comment = "设备名称",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "code",
                comment = "设备编码",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "product",
                comment = "所属产品",
                kmpType = "site.addzero.generated.isomorphic.ProductIso"
            ),
            ColumnConfig(
                key = "authInfo",
                comment = "认证信息",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "status",
                comment = "设备状态",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "enabled",
                comment = "是否启用",
                kmpType = "kotlin.Boolean"
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
    val formState = rememberDeviceFormState(viewModel.selectedT)

    DeviceForm(
        formState,
        visible = viewModel.showForm,
        title = "产品分类表单",
        onClose = {
            viewModel.showForm = false
        },
        onSubmit = {
            viewModel.onSaveForm(formState.value)
            formState.value= DeviceIso()
        },
    )

}


