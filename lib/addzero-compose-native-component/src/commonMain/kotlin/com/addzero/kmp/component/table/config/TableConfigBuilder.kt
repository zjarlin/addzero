package com.addzero.kmp.component.table.config

import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.TableRowType
import com.addzero.kmp.entity.low_table.CommonTableDaTaInputDTO
import com.addzero.kmp.entity.low_table.EnumSortDirection
import com.addzero.kmp.entity.low_table.ExportParam
import com.addzero.kmp.entity.low_table.SpecPageResult

/**
 * 表格配置构建器
 * 提供链式调用的方式来构建TableConfig
 */
class TableConfigBuilder {

    private var apiConfig = TableApiConfig()
    private var dataConfig = TableDataConfig()
    private var uiConfig = TableUiConfig()
    private var importConfig = TableImportConfig()
    private var viewModelConfig = TableViewModelConfig()

    /**
     * 配置API相关参数
     */
    fun api(block: TableApiConfigBuilder.() -> Unit): TableConfigBuilder {
        apiConfig = TableApiConfigBuilder().apply(block).build()
        return this
    }

    /**
     * 配置数据相关参数
     */
    fun data(block: TableDataConfigBuilder.() -> Unit): TableConfigBuilder {
        dataConfig = TableDataConfigBuilder().apply(block).build()
        return this
    }

    /**
     * 配置UI相关参数
     */
    fun ui(block: TableUiConfigBuilder.() -> Unit): TableConfigBuilder {
        uiConfig = TableUiConfigBuilder().apply(block).build()
        return this
    }

    /**
     * 配置导入相关参数
     */
    fun import(block: TableImportConfigBuilder.() -> Unit): TableConfigBuilder {
        importConfig = TableImportConfigBuilder().apply(block).build()
        return this
    }

    /**
     * 配置ViewModel相关参数
     */
    fun viewModel(block: TableViewModelConfigBuilder.() -> Unit): TableConfigBuilder {
        viewModelConfig = TableViewModelConfigBuilder().apply(block).build()
        return this
    }

    /**
     * 构建最终的TableConfig
     */
    fun build(): TableConfig {
        return TableConfig(
            apiConfig = apiConfig,
            dataConfig = dataConfig,
            uiConfig = uiConfig,
            importConfig = importConfig,
            viewModelConfig = viewModelConfig
        )
    }
}

/**
 * API配置构建器
 */
class TableApiConfigBuilder {
    private var onLoadData: (CommonTableDaTaInputDTO) -> SpecPageResult<TableRowType> =
        { SpecPageResult.Companion.empty() }
    private var onEdit: (TableRowType) -> Boolean = { false }
    private var onDelete: (Any) -> Boolean = { false }
    private var onSave: (TableRowType?) -> Boolean = { false }
    private var onExport: (ExportParam) -> Boolean = { false }
    private var onCustomEvent: (String, Any?) -> Boolean = { _, _ -> false }

    fun onLoadData(handler: (CommonTableDaTaInputDTO) -> SpecPageResult<TableRowType>) = apply { onLoadData = handler }
    fun onEdit(handler: (TableRowType) -> Boolean) = apply { onEdit = handler }
    fun onDelete(handler: (Any) -> Boolean) = apply { onDelete = handler }
    fun onSave(handler: (TableRowType?) -> Boolean) = apply { onSave = handler }
    fun onExport(handler: (ExportParam) -> Boolean) = apply { onExport = handler }
    fun onCustomEvent(handler: (String, Any?) -> Boolean) = apply { onCustomEvent = handler }

    fun build() = TableApiConfig(onLoadData, onEdit, onDelete, onSave, onExport, onCustomEvent)
}

/**
 * 数据配置构建器
 */
class TableDataConfigBuilder {
    private var initData: List<TableRowType> = emptyList()
    private var columns: List<TableColumnType> = emptyList()
    private var getIdFun: (TableRowType) -> Any = { it.hashCode() }
    private var onRowClick: (TableRowType) -> Unit = {}

    fun initData(data: List<TableRowType>) = apply { initData = data }
    fun columns(cols: List<TableColumnType>) = apply { columns = cols }
    fun getIdFun(func: (TableRowType) -> Any) = apply { getIdFun = func }
    fun onRowClick(handler: (TableRowType) -> Unit) = apply { onRowClick = handler }

    fun build() = TableDataConfig(initData, columns, getIdFun, onRowClick)
}

/**
 * UI配置构建器
 */
class TableUiConfigBuilder {
    private var rowHeight: Int = 56
    private var headerHeight: Int = 56
    private var showActions: Boolean = true
    private var showPagination: Boolean = true
    private var showBatchOperations: Boolean = true
    private var showAdvancedSearch: Boolean = true

    fun rowHeight(height: Int) = apply { rowHeight = height }
    fun headerHeight(height: Int) = apply { headerHeight = height }
    fun showActions(show: Boolean) = apply { showActions = show }
    fun showPagination(show: Boolean) = apply { showPagination = show }
    fun showBatchOperations(show: Boolean) = apply { showBatchOperations = show }
    fun showAdvancedSearch(show: Boolean) = apply { showAdvancedSearch = show }

    fun build() =
        TableUiConfig(rowHeight, headerHeight, showActions, showPagination, showBatchOperations, showAdvancedSearch)
}

/**
 * 导入配置构建器
 */
class TableImportConfigBuilder {
    private var title: String = "Excel文件上传"
    private var description: String =
        "请上传Excel文件(.xlsx)，文件大小不超过5MB，支持批量上传。\n请确保使用正确的模板格式，否则可能导致导入失败。"
    private var showDescription: Boolean = true
    private var acceptedFileTypes: List<String> = listOf("xlsx")
    private var maxFileSize: Int = 5

    fun title(text: String) = apply { title = text }
    fun description(text: String) = apply { description = text }
    fun showDescription(show: Boolean) = apply { showDescription = show }
    fun acceptedFileTypes(types: List<String>) = apply { acceptedFileTypes = types }
    fun maxFileSize(size: Int) = apply { maxFileSize = size }

    fun build() = TableImportConfig(title, description, showDescription, acceptedFileTypes, maxFileSize)
}

/**
 * ViewModel配置构建器
 */
class TableViewModelConfigBuilder {
    private var paginationConfig = TablePaginationConfig()
    private var searchConfig = TableSearchConfig()
    private var selectionConfig = TableSelectionConfig()
    private var sortConfig = TableSortConfig()

    fun pagination(block: TablePaginationConfigBuilder.() -> Unit) = apply {
        paginationConfig = TablePaginationConfigBuilder().apply(block).build()
    }

    fun search(block: TableSearchConfigBuilder.() -> Unit) = apply {
        searchConfig = TableSearchConfigBuilder().apply(block).build()
    }

    fun selection(block: TableSelectionConfigBuilder.() -> Unit) = apply {
        selectionConfig = TableSelectionConfigBuilder().apply(block).build()
    }

    fun sort(block: TableSortConfigBuilder.() -> Unit) = apply {
        sortConfig = TableSortConfigBuilder().apply(block).build()
    }

    fun build() = TableViewModelConfig(paginationConfig, searchConfig, selectionConfig, sortConfig)
}

/**
 * 分页配置构建器
 */
class TablePaginationConfigBuilder {
    private var defaultPageSize: Int = 10
    private var pageSizeOptions: List<Int> = listOf(10, 20, 50, 100)
    private var enablePagination: Boolean = true

    fun defaultPageSize(size: Int) = apply { defaultPageSize = size }
    fun pageSizeOptions(options: List<Int>) = apply { pageSizeOptions = options }
    fun enablePagination(enable: Boolean) = apply { enablePagination = enable }

    fun build() = TablePaginationConfig(defaultPageSize, pageSizeOptions, enablePagination)
}

/**
 * 搜索配置构建器
 */
class TableSearchConfigBuilder {
    private var enableKeywordSearch: Boolean = true
    private var enableAdvancedSearch: Boolean = true
    private var searchPlaceholder: String = "请输入关键词搜索..."
    private var autoSearch: Boolean = false
    private var autoSearchDelay: Long = 500

    fun enableKeywordSearch(enable: Boolean) = apply { enableKeywordSearch = enable }
    fun enableAdvancedSearch(enable: Boolean) = apply { enableAdvancedSearch = enable }
    fun searchPlaceholder(placeholder: String) = apply { searchPlaceholder = placeholder }
    fun autoSearch(enable: Boolean) = apply { autoSearch = enable }
    fun autoSearchDelay(delay: Long) = apply { autoSearchDelay = delay }

    fun build() =
        TableSearchConfig(enableKeywordSearch, enableAdvancedSearch, searchPlaceholder, autoSearch, autoSearchDelay)
}

/**
 * 选择配置构建器
 */
class TableSelectionConfigBuilder {
    private var enableMultiSelect: Boolean = true
    private var enableSingleSelect: Boolean = true
    private var defaultEditMode: Boolean = false

    fun enableMultiSelect(enable: Boolean) = apply { enableMultiSelect = enable }
    fun enableSingleSelect(enable: Boolean) = apply { enableSingleSelect = enable }
    fun defaultEditMode(enable: Boolean) = apply { defaultEditMode = enable }

    fun build() = TableSelectionConfig(enableMultiSelect, enableSingleSelect, defaultEditMode)
}

/**
 * 排序配置构建器
 */
class TableSortConfigBuilder {
    private var enableSort: Boolean = true
    private var enableMultiSort: Boolean = true
    private var defaultSortColumn: String? = null
    private var defaultSortDirection: EnumSortDirection = EnumSortDirection.NONE

    fun enableSort(enable: Boolean) = apply { enableSort = enable }
    fun enableMultiSort(enable: Boolean) = apply { enableMultiSort = enable }
    fun defaultSortColumn(column: String?) = apply { defaultSortColumn = column }
    fun defaultSortDirection(direction: EnumSortDirection) = apply { defaultSortDirection = direction }

    fun build() = TableSortConfig(enableSort, enableMultiSort, defaultSortColumn, defaultSortDirection)
}

/**
 * 便捷的构建函数
 */
fun tableConfig(block: TableConfigBuilder.() -> Unit): TableConfig {
    return TableConfigBuilder().apply(block).build()
}
