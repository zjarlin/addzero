package com.addzero.kmp.component.addzero_starter.addzero_table

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.kmp.component.addzero_starter.addzero_table.base.TableContent
import com.addzero.kmp.component.addzero_starter.addzero_table.base.TablePagination
import com.addzero.kmp.component.addzero_starter.addzero_table.base.header.AddAdvSearchPopBar
import com.addzero.kmp.component.addzero_starter.addzero_table.base.header.AddTableHeader
import com.addzero.kmp.component.addzero_starter.addzero_table.controlled_components.RenderDynamicFormDrawer
import com.addzero.kmp.component.button.AddPermissionIconButton
import com.addzero.kmp.component.file_picker.AddFileUploadDrawer
import com.addzero.kmp.component.table.generictable.components.ExportButtonContent
import com.addzero.kmp.compose.icons.IconKeys
import com.addzero.kmp.viewmodel.table.TableConfig
import com.addzero.kmp.viewmodel.table.TableCompositeViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * 通用表格组件
 * 基于重构后的组合 ViewModel 实现的表格组件
 * @param [modifier] 修饰符
 * @param [config] 表格配置
 * @param [buttonSlot] 自定义按钮插槽
 * @param [actionSlot] 自定义操作列内容
 */
@Composable
fun  AddGenericTable(
    modifier: Modifier = Modifier,
    config: TableConfig,
    buttonSlot: @Composable (() -> Unit)? = null,
    actionSlot: @Composable () -> Unit = {}
) {

    val horizontalScrollState = rememberScrollState()
    val tableViewModel = koinViewModel<TableCompositeViewModel>()
    tableViewModel.initialize(config)


    val renderButtons: @Composable () -> Unit = {
        DefaultTableButtons(tableViewModel, buttonSlot)
    }

    Column(modifier = modifier) {
        // 表格头部
        AddTableHeader(
            renderButtons = renderButtons,
            keyword = tableViewModel.searchViewModel.keyword,
            onKeyWordChanged = { tableViewModel.searchViewModel.updateKeyword(it) },
            onSearch = { tableViewModel.search() },
            headerHeight = config.uiConfig.headerHeight,
            enableEditMode = tableViewModel.selectionViewModel.enableEditMode,
            isPageAllSelected = tableViewModel.selectionViewModel.isPageAllSelected(
                tableViewModel.dataViewModel.data,
                config.dataConfig.getIdFun
            ),
            onCheckedChange = { tableViewModel.togglePageSelection() },
            columns = tableViewModel.dataViewModel.columns,
            onChangeSorting = { tableViewModel.changeSorting(it) },
            onColumnAdvSearchClick = { column ->
                tableViewModel.showColumnAdvSearch(column)
            },
            stateSorts = tableViewModel.sortViewModel.sortState,
            filterStateMap = tableViewModel.searchViewModel.filterStateMap
        )

        // 表格内容
        TableContent(
            getIdFun = config.dataConfig.getIdFun,
            data = tableViewModel.dataViewModel.data,
            selectedItems = tableViewModel.selectionViewModel.selectedItems,
            columns = tableViewModel.dataViewModel.columns,
            pageState = tableViewModel.paginationViewModel.pageState,
            multiSelect = tableViewModel.selectionViewModel.enableEditMode,
            showActions = tableViewModel.uiStateViewModel.showActions,
            rowHeight = config.uiConfig.rowHeight,
            onRowClick = config.dataConfig.onRowClick,
            onDeleteItem = { id ->
                tableViewModel.deleteData(id)
            },
            scrollState = horizontalScrollState,
            modifier = Modifier.weight(1f),
            renderCustomActions = actionSlot,
            onCheckboxClick = {
                tableViewModel.toggleItemSelection(it)
            },
            onBatchDelete = { tableViewModel.batchDelete() },
            onBatchExport = { tableViewModel.batchExport() },
            onEditClick = {
                tableViewModel.showEditForm(it)
            },
        )

        // 分页控件
        if (config.uiConfig.showPagination) {
            TablePagination(
                statePagination = tableViewModel.paginationViewModel.pageState,
                enablePagination = true,
                onPageSizeChange = {
                    tableViewModel.changePageSize(it)
                },
                onGoFirstPage = {
                    tableViewModel.paginationViewModel.goToFirstPage()
                    tableViewModel.queryPage()
                },
                onPreviousPage = { tableViewModel.previousPage() },
                onGoToPage = { tableViewModel.goToPage(it) },
                onNextPage = { tableViewModel.nextPage() },
                onGoLastPage = {
                    tableViewModel.paginationViewModel.goToLastPage()
                    tableViewModel.queryPage()
                }
            )
        }
    }

    // 高级搜索
    if (tableViewModel.searchViewModel.showAdvancedSearch) {
        AddAdvSearchPopBar(
            column = tableViewModel.searchViewModel.currentAdvSearchColumn,
            showFieldAdvSearch = tableViewModel.searchViewModel.showFieldAdvSearch,
            onAdvSearchSubmit = { tableViewModel.submitAdvSearch() },
            onClose = { tableViewModel.searchViewModel.hideColumnAdvSearch() },
            searchForm = tableViewModel.searchViewModel.currentAdvSearchColumn?.let {
                tableViewModel.searchViewModel.filterStateMap[it.key]
            },
            onAdvSearchCLean = {
                tableViewModel.clearColumnFilter()
            },
            onSearchFormChange = { columnValue, operator, logicType ->
                tableViewModel.updateSearchForm(columnValue, operator, logicType)
            }
        )
    }

    // 动态表单
    RenderDynamicFormDrawer(
        showForm = tableViewModel.uiStateViewModel.showForm,
        currentItem = tableViewModel.selectionViewModel.currentSelectItem,
        onCloseForm = { tableViewModel.uiStateViewModel.hideFormDialog() },
        columns = tableViewModel.dataViewModel.columns,
        onFormSubmit = { tableViewModel.saveData(tableViewModel.selectionViewModel.currentSelectItem) }
    )

    // 上传抽屉
    AddFileUploadDrawer(
        visible = tableViewModel.uiStateViewModel.showImportForm,
        title = config.importConfig.title,
        onClose = { tableViewModel.uiStateViewModel.hideImportDialog() },
        onUpload = { files ->
            tableViewModel.uploadFiles(files)
        },
        onDownloadTemplate = { tableViewModel.downloadTemplate() },
        description = config.importConfig.description,
        showDescription = config.importConfig.showDescription,
        acceptedFileTypes = config.importConfig.acceptedFileTypes,
        maxFileSize = config.importConfig.maxFileSize
    )
}

/**
 * 默认的表格按钮
 */
@Composable
fun DefaultTableButtons(
    tableViewModel: TableCompositeViewModel,
    buttonSlot: @Composable (() -> Unit)? = null
) {
    buttonSlot?.invoke()

    // 添加多选模式按钮
    AddPermissionIconButton(
        permissionCode = "multiSelect",
        text = if (tableViewModel.selectionViewModel.enableEditMode) "退出多选" else "多选",
        icon = if (tableViewModel.selectionViewModel.enableEditMode) IconKeys.DESELECT else IconKeys.SELECTALL,
        onClick = { tableViewModel.toggleMultiSelectMode() }
    )

    AddPermissionIconButton(
        permissionCode = "add",
        text = "新增",
        icon = IconKeys.ADD,
        onClick = {
            tableViewModel.showAddForm()
        }
    )

    AddPermissionIconButton(
        permissionCode = "import",
        text = "导入",
        icon = IconKeys.UPLOAD,
        onClick = { tableViewModel.uiStateViewModel.showImportDialog() }
    )

    AddPermissionIconButton(
        permissionCode = "export",
        text = "导出",
        icon = IconKeys.FILEDOWNLOAD,
        onClick = { tableViewModel.uiStateViewModel.showExportDropDown() },
        content = {
            ExportButtonContent(
                selectedItems = tableViewModel.selectionViewModel.selectedItems,
                showExportMenu = tableViewModel.uiStateViewModel.showExportDropDownMenu,
                onExportButtonClick = { tableViewModel.uiStateViewModel.showExportDropDown() },
                onExportDropDownItemClick = { tableViewModel.exportData(it) },
                onDismissDropDownMenu = { tableViewModel.uiStateViewModel.hideExportDropDown() },
                permissionCode = "export"
            )
        }
    )
}

///**
// * 为向后兼容而提供的重载函数
// * 允许使用旧的参数列表来创建表格
// */
//@Composable
//fun <T> AddGenericTable(
//    modifier: Modifier = Modifier,
//    initData: List<T> = emptyList(),
//    initColumn: List<TableColumnType>,
//    tableConfig: DefaultTableConfig<T> = DefaultTableConfig(),
//    getIdFun: (T) -> Any = { it.hashCode() },
//    onRowClick: (T) -> Unit = {},
//    onApiEdit: (T) -> Boolean,
//    onLoadData: (CommonTableDaTaInputDTO) -> SpecPageResult<T>,
//    onApiexportData: (ExportParam) -> Boolean,
//    onApidelete: (Any) -> Boolean,
//    onApisave: (T?) -> Boolean,
//    onApiUpload: (MultiPartFormDataContent) -> String,
//    tableViewModel: GenericTableViewModel<T>? = null,
//    buttonSlot: @Composable (() -> Unit)? = null,
//    renderButtons: @Composable () -> Unit = {},
//    actionSlot: @Composable () -> Unit = {}
//) {
//    // 创建新的配置对象
//    val config = TableConfig(
//        apiConfig = TableApiConfig(
//            onLoadData = onLoadData,
//            onEdit = onApiEdit,
//            onDelete = onApidelete,
//            onSave = onApisave,
//            onUpload = onApiUpload,
//            onExport = onApiexportData
//        ),
//        dataConfig = TableDataConfig(
//            initData = initData,
//            columns = initColumn,
//            getIdFun = getIdFun,
//            onRowClick = onRowClick
//        ),
//        uiConfig = TableUiConfig(
//            rowHeight = tableConfig.rowHeight,
//            headerHeight = tableConfig.headerHeight
//        )
//    )
//
//    val viewModel = tableViewModel ?: remember { GenericTableViewModel(config) }
//
//    AddGenericTable(
//        modifier = modifier,
//        config = config,
//        tableViewModel = viewModel,
//        buttonSlot = buttonSlot,
//        renderButtons = if (renderButtons == {}) {
//            { DefaultTableButtons(viewModel, buttonSlot) }
//        } else {
//            renderButtons
//        },
//        actionSlot = actionSlot
//    )
//}


