package com.addzero.kmp.entity.low_table

/**
 * 表格元数据
 * 用于定义表格的结构、字段和行为
 */
data class DefaultTableConfig<T>(
    // 表格标题
    val title: String = "默认表格",
    // 是否支持多选
    val multiSelect: Boolean = true,
    // 是否支持分页
    val enablePagination: Boolean = true,
    // 每页默认行数
    val defaultPageSize: Int = 10,

    // 可选的每页行数
    val pageSizeOptions: List<Int> = listOf(10, 20, 50, 100),
    // 是否使用条纹样式
    val striped: Boolean = true,
    // 是否显示边框
    val bordered: Boolean = true,
    // 是否启用悬停效果
    val hoverable: Boolean = false,
    // 是否显示操作列
    val showActions: Boolean = true,
    // 操作列宽度
    val actionsColumnWidth: Float = 0.15f,
    // 表格行高
    val rowHeight: Int = 48,
    // 表格头部高度
    val headerHeight: Int = 56,
    // 自定义操作按钮
    val customActions: List<StateActionButton<T>> = emptyList(),
    // 表单配置
    val formConfig: List<StateFormField> = emptyList(),
    // ID字段，用于唯一标识行
    val idField: String = "id"
)

