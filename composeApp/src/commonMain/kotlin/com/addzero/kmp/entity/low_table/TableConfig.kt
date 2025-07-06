package com.addzero.kmp.entity.low_table

import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import io.ktor.client.request.forms.*

/**
 * 表格API配置
 * 封装表格组件所需的所有API回调
 */
data class TableApiConfig(
    // 数据加载
    val onLoadData: (CommonTableDaTaInputDTO) -> SpecPageResult<TableRowType> = { SpecPageResult.empty() },
    // CRUD操作
    val onEdit: (TableRowType) -> Boolean = { false },
    val onDelete: (Any) -> Boolean = { false },
    val onSave: (TableRowType?) -> Boolean = { false },
    // 导入导出
    val onUpload: (MultiPartFormDataContent) -> String = { "" },
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
 * 完整表格配置
 * 集成所有子配置项
 */

data class TableConfig(
    val apiConfig: TableApiConfig = TableApiConfig(),
    val dataConfig: TableDataConfig = TableDataConfig(),
    val uiConfig: TableUiConfig = TableUiConfig(),
    val importConfig: TableImportConfig = TableImportConfig()
) 