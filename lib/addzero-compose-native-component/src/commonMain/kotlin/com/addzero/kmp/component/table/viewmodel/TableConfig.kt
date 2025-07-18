package com.addzero.kmp.component.table.viewmodel

import com.addzero.kmp.component.table.TableColumnType
import com.addzero.kmp.component.table.TableRowType
import com.addzero.kmp.entity.low_table.CommonTableDaTaInputDTO
import com.addzero.kmp.entity.low_table.ExportParam
import com.addzero.kmp.entity.low_table.SpecPageResult

/**
 * 表格API配置
 * 封装表格组件所需的所有API回调
 */
data class TableApiConfig(
    // 数据加载
    val onLoadData: (CommonTableDaTaInputDTO) -> SpecPageResult<TableRowType> = { SpecPageResult.Companion.empty() },
    // CRUD操作
    val onEdit: (TableRowType) -> Boolean = { false },
    val onDelete: (Any) -> Boolean = { false },
    val onSave: (TableRowType?) -> Boolean = { false },
    val onExport: (ExportParam) -> Boolean = { false },
    // 事件扩展点，用于自定义操作
    val onCustomEvent: (String, Any?) -> Boolean = { _, _ -> false }
)

/**
 * 表格数据配置
 * 封装表格组件所需的数据相关配置
 */
data class TableDataConfig(
    // 初始数据
    val initData: List<TableRowType> = emptyList(),
    // 列配置
    val columns: List<TableColumnType> = emptyList(),
    // ID获取函数
    val getIdFun: (TableRowType) -> Any = { it.hashCode() },
    // 点击事件
    val onRowClick: (TableRowType) -> Unit = {}
)

/**
 * 表格UI配置
 * 封装表格组件的UI相关配置
 */
data class TableUiConfig(
    // 表格行高
    val rowHeight: Int = 56,
    // 表头高度
    val headerHeight: Int = 56,
    // 是否显示操作列
    val showActions: Boolean = true,
    // 是否显示分页
    val showPagination: Boolean = true,
    // 是否显示批量操作
    val showBatchOperations: Boolean = true,
    // 是否显示高级搜索
    val showAdvancedSearch: Boolean = true
)

/**
 * 表格导入配置
 */
data class TableImportConfig(
    // 导入弹窗标题
    val title: String = "Excel文件上传",
    // 导入弹窗描述
    val description: String = "请上传Excel文件(.xlsx)，文件大小不超过5MB，支持批量上传。\n请确保使用正确的模板格式，否则可能导致导入失败。",
    // 是否显示描述
    val showDescription: Boolean = true,
    // 接受的文件类型
    val acceptedFileTypes: List<String> = listOf("xlsx"),
    // 最大文件大小(MB)
    val maxFileSize: Int = 5
)

/**
 * 表格分页配置
 * 封装分页相关的配置参数
 */
data class TablePaginationConfig(
    // 默认页面大小
    val defaultPageSize: Int = 10,
    // 可选的页面大小选项
    val pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
    // 是否启用分页
    val enablePagination: Boolean = true
)

/**
 * 表格搜索配置
 * 封装搜索相关的配置参数
 */
data class TableSearchConfig(
    // 是否启用关键词搜索
    val enableKeywordSearch: Boolean = true,
    // 是否启用高级搜索
    val enableAdvancedSearch: Boolean = true,
    // 搜索占位符文本
    val searchPlaceholder: String = "请输入关键词搜索...",
    // 是否自动搜索（输入时实时搜索）
    val autoSearch: Boolean = false,
    // 自动搜索延迟（毫秒）
    val autoSearchDelay: Long = 500
)

/**
 * 表格选择配置
 * 封装选择相关的配置参数
 */
data class TableSelectionConfig(
    // 是否启用多选
    val enableMultiSelect: Boolean = true,
    // 是否启用单选
    val enableSingleSelect: Boolean = true,
    // 默认是否开启编辑模式
    val defaultEditMode: Boolean = false
)

/**
 * 表格排序配置
 * 封装排序相关的配置参数
 */
data class TableSortConfig(
    // 是否启用排序
    val enableSort: Boolean = true,
    // 是否支持多列排序
    val enableMultiSort: Boolean = true,
    // 默认排序列
    val defaultSortColumn: String? = null,
    // 默认排序方向
    val defaultSortDirection: com.addzero.kmp.entity.low_table.EnumSortDirection = com.addzero.kmp.entity.low_table.EnumSortDirection.NONE
)

/**
 * ViewModel配置集合
 * 集成所有子ViewModel的配置参数
 */
data class TableViewModelConfig(
    val paginationConfig: TablePaginationConfig = TablePaginationConfig(),
    val searchConfig: TableSearchConfig = TableSearchConfig(),
    val selectionConfig: TableSelectionConfig = TableSelectionConfig(),
    val sortConfig: TableSortConfig = TableSortConfig()
)

/**
 * 完整表格配置
 * 集成所有子配置项
 */
data class TableConfig(
    val apiConfig: TableApiConfig = TableApiConfig(),
    val dataConfig: TableDataConfig = TableDataConfig(),
    val uiConfig: TableUiConfig = TableUiConfig(),
    val importConfig: TableImportConfig = TableImportConfig(),
    val viewModelConfig: TableViewModelConfig = TableViewModelConfig()
)