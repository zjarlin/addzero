package com.addzero.kmp.component.table

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.table.base.TableContent
import com.addzero.kmp.component.table.base.TablePagination
import com.addzero.kmp.component.table.base.header.AddAdvSearchPopBar
import com.addzero.kmp.component.table.base.header.AddTableHeader
import com.addzero.kmp.component.table.viewmodel.TableCompositeViewModel
import com.addzero.kmp.component.table.viewmodel.TableConfig
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

    // 上传抽屉
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
    
    // 多选模式按钮
    AddIconButton(
        text = if (tableViewModel.selectionViewModel.enableEditMode) "退出多选" else "多选",
        imageVector = if (tableViewModel.selectionViewModel.enableEditMode) Icons.Default.Deselect else Icons.Default.SelectAll,
        onClick = {
            tableViewModel.selectionViewModel.toggleEditMode()
        }
    )
    
    // 新增按钮
    AddIconButton(
        text = "新增",
        imageVector = Icons.Default.Add,
        onClick = { tableViewModel.showAddForm() }
    )

    // 导入按钮
    AddIconButton(
        text = "导入",
        imageVector = Icons.Default.UploadFile,
        onClick = { tableViewModel.uiStateViewModel.showImportDialog() }
    )

    // 导出按钮
    AddIconButton(
        text = "导出",
        imageVector = Icons.Default.DownloadForOffline,
        onClick = { tableViewModel.uiStateViewModel.showExportDropDown() }
    )
}

