package site.addzero.screens.product

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import site.addzero.component.table.biz.AddTable
import site.addzero.component.table.original.entity.ColumnConfig
import site.addzero.generated.forms.ProductCategoryForm
import site.addzero.generated.forms.rememberProductCategoryFormState
import site.addzero.generated.isomorphic.ProductCategoryIso
import site.addzero.screens.product.vm.ProductCategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCategoryScreen(viewModel: ProductCategoryViewModel) {
    AddTable(
        getRowId = { it.id?:0L },


        data = viewModel.data,
        columns = listOf(
            ColumnConfig(
                key = "name",
                comment = "分类名称",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "parent",
                comment = "上级分类",
                kmpType = "site.addzero.generated.isomorphic.ProductCategoryIso"
            ),
            ColumnConfig(
                key = "description",
                comment = "分类描述",
                kmpType = "kotlin.String"
            ),
            ColumnConfig(
                key = "enabled",
                comment = "是否启用",
                kmpType = "kotlin.Boolean"
            ),
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
    val formState = rememberProductCategoryFormState(viewModel.selectedT)
    ProductCategoryForm(
        formState,
        visible = viewModel.showForm,
        title = "产品分类表单",
        onClose = { viewModel.showForm = false },
        onSubmit = {
            viewModel.onSaveForm(formState.value)
//            formState.value = ProductCategoryIso()

        },
    ) {
//        products(hidden = true)
    }

}
