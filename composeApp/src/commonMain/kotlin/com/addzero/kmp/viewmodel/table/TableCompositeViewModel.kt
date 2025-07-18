package com.addzero.kmp.viewmodel.table

import androidx.lifecycle.ViewModel
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import org.koin.android.annotation.KoinViewModel

/**
 * 表格组合 ViewModel
 * 协调各个子 ViewModel 的交互，提供统一的接口
 */
@KoinViewModel
class TableCompositeViewModel(
    val searchViewModel: TableSearchViewModel ,
    val selectionViewModel: TableSelectionViewModel ,
    val paginationViewModel: TablePaginationViewModel,
    val sortViewModel: TableSortViewModel ,
    val uiStateViewModel: TableUIStateViewModel,
    val dataViewModel: TableDataViewModel ,
    ) : ViewModel() {

    private lateinit var config: TableConfig

    //
    /**
     * 初始化配置
     */
    fun initialize(tableConfig: TableConfig) {
        config = tableConfig
        dataViewModel.initialize(config)

        // 初始化UI状态
        uiStateViewModel.setActionsVisible(config.uiConfig.showActions)
        searchViewModel.setAdvancedSearchVisible(config.uiConfig.showAdvancedSearch)

        // 如果没有初始数据，则加载数据
        if (config.dataConfig.initData.isEmpty()) {
            queryPage()
        }
    }

    /**
     * 查询分页数据
     * 协调搜索、排序、分页等条件
     */
    fun queryPage() {
        dataViewModel.queryPage(
            keyword = searchViewModel.keyword,
            sortState = sortViewModel.sortState,
            filterState = searchViewModel.filterState,
            pageNo = paginationViewModel.pageState.currentPage,
            pageSize = paginationViewModel.pageState.pageSize
        ) { result ->
            paginationViewModel.setTotalItems(result.totalRowCount.toInt())
        }
    }

    /**
     * 搜索并重置到第一页
     */
    fun search() {
        paginationViewModel.goToFirstPage()
        queryPage()
    }

    /**
     * 清空搜索条件并刷新
     */
    fun clearSearch() {
        searchViewModel.clearAllFilters()
        search()
    }

    /**
     * 改变排序并刷新数据
     */
    fun changeSorting(columnKey: String) {
        sortViewModel.changeSorting(columnKey)
        search()
    }

    /**
     * 切换页面并刷新数据
     */
    fun goToPage(page: Int) {
        paginationViewModel.goToPage(page)
        queryPage()
    }

    /**
     * 改变页面大小并刷新数据
     */
    fun changePageSize(pageSize: Int) {
        paginationViewModel.setPageSize(pageSize)
        queryPage()
    }

    /**
     * 上一页
     */
    fun previousPage() {
        paginationViewModel.goToPreviousPage()
        queryPage()
    }

    /**
     * 下一页
     */
    fun nextPage() {
        paginationViewModel.goToNextPage()
        queryPage()
    }

    /**
     * 保存数据并刷新
     */
    fun saveData(item: TableRowType?) {
        dataViewModel.saveData(item) {
            uiStateViewModel.hideFormDialog()
            selectionViewModel.currentSelectItem=null
            queryPage()
        }
    }

    /**
     * 删除数据并刷新
     */
    fun deleteData(id: Any) {
        dataViewModel.deleteData(id) {
            queryPage()
        }
    }

    /**
     * 批量删除并刷新
     */
    fun batchDelete() {
        dataViewModel.batchDeleteData(selectionViewModel.selectedItems) {
            selectionViewModel.clearSelection()
            queryPage()
        }
    }

    /**
     * 批量导出
     */
    fun batchExport() {
        dataViewModel.batchExportData(selectionViewModel.selectedItems)
    }

    /**
     * 导出数据
     */
    fun exportData(exportParam: com.addzero.kmp.entity.low_table.ExportParam) {
        dataViewModel.exportData(exportParam)
    }

    /**
     * 上传文件并刷新
     */
    fun uploadFiles(files: io.github.vinceglb.filekit.core.PlatformFiles?) {
        dataViewModel.uploadFiles(files) {
            uiStateViewModel.hideImportDialog()
            queryPage()
        }
    }

    /**
     * 下载模板
     */
    fun downloadTemplate() {
        dataViewModel.downloadTemplate()
    }

    /**
     * 显示新增表单
     */
    fun showAddForm() {
        selectionViewModel.currentSelectItem=null
        uiStateViewModel.showFormDialog()
    }

    /**
     * 显示编辑表单
     */
    fun showEditForm(item: TableRowType) {
        selectionViewModel.currentSelectItem=null
        uiStateViewModel.showFormDialog()
    }

    /**
     * 切换多选模式
     */
    fun toggleMultiSelectMode() {
        selectionViewModel.toggleEditMode()
    }

    /**
     * 选中/取消选中当前页所有项目
     */
    fun togglePageSelection() {
        if (selectionViewModel.isPageAllSelected(dataViewModel.data, config.dataConfig.getIdFun)) {
            selectionViewModel.unselectPageItems(dataViewModel.data, config.dataConfig.getIdFun)
        } else {
            selectionViewModel.selectPageItems(dataViewModel.data, config.dataConfig.getIdFun)
        }
    }

    /**
     * 切换项目选择状态
     */
    fun toggleItemSelection(item: TableRowType) {
        selectionViewModel.toggleItemSelection(item, config.dataConfig.getIdFun)
    }

    /**
     * 显示列的高级搜索
     */
    fun showColumnAdvSearch(column: TableColumnType) {
        searchViewModel.showColumnAdvSearch(column)
    }

    /**
     * 提交高级搜索
     */
    fun submitAdvSearch() {
        searchViewModel.hideColumnAdvSearch()
        search()
    }

    /**
     * 清除列的搜索条件
     */
    fun clearColumnFilter() {
        searchViewModel.currentAdvSearchColumn?.key?.let { key ->
            searchViewModel.clearColumnFilter(key)
            search()
        }
    }

    /**
     * 更新搜索表单
     */
    fun updateSearchForm(
        columnValue: Any?,
        operator: com.addzero.kmp.entity.low_table.EnumSearchOperator,
        logicType: com.addzero.kmp.entity.low_table.EnumLogicOperator
    ) {
        searchViewModel.currentAdvSearchColumn?.key?.let { key ->
            searchViewModel.updateSearchForm(key, columnValue, operator, logicType)
        }
    }
}
