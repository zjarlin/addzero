package com.addzero.kmp.component.table.example

import com.addzero.kmp.component.table.viewmodel.*
import com.addzero.kmp.entity.low_table.EnumSortDirection

/**
 * 表格配置使用示例
 * 展示如何使用新的配置系统来创建表格配置
 */
object TableConfigExample {
    
    /**
     * 基础配置示例
     * 使用默认配置创建一个简单的表格
     */
    fun basicConfig(): TableConfig {
        return tableConfig {
            // 数据配置
            data {
                columns(emptyList()) // 实际使用时传入列定义
                getIdFun { it.hashCode() }
                onRowClick { /* 处理行点击 */ }
            }
            
            // API配置
            api {
                onLoadData { /* 加载数据逻辑 */ 
                    com.addzero.kmp.entity.low_table.SpecPageResult.Companion.empty()
                }
                onSave { /* 保存数据逻辑 */ true }
                onDelete { /* 删除数据逻辑 */ true }
            }
        }
    }
    
    /**
     * 完整配置示例
     * 展示所有可配置的参数
     */
    fun fullConfig(): TableConfig {
        return tableConfig {
            // 数据配置
            data {
                initData(emptyList())
                columns(emptyList())
                getIdFun { it.hashCode() }
                onRowClick { row -> 
                    println("点击了行: $row")
                }
            }
            
            // API配置
            api {
                onLoadData { input ->
                    // 实际的数据加载逻辑
                    println("加载数据: $input")
                    com.addzero.kmp.entity.low_table.SpecPageResult.Companion.empty()
                }
                onSave { item ->
                    println("保存数据: $item")
                    true
                }
                onDelete { id ->
                    println("删除数据: $id")
                    true
                }
                onEdit { item ->
                    println("编辑数据: $item")
                    true
                }
                onExport { param ->
                    println("导出数据: $param")
                    true
                }
                onCustomEvent { event, data ->
                    println("自定义事件: $event, 数据: $data")
                    true
                }
            }
            
            // UI配置
            ui {
                rowHeight(48)
                headerHeight(56)
                showActions(true)
                showPagination(true)
                showBatchOperations(true)
                showAdvancedSearch(true)
            }
            
            // 导入配置
            import {
                title("数据导入")
                description("请选择要导入的Excel文件")
                showDescription(true)
                acceptedFileTypes(listOf("xlsx", "xls"))
                maxFileSize(10) // 10MB
            }
            
            // ViewModel配置
            viewModel {
                // 分页配置
                pagination {
                    defaultPageSize(20)
                    pageSizeOptions(listOf(10, 20, 50, 100))
                    enablePagination(true)
                }
                
                // 搜索配置
                search {
                    enableKeywordSearch(true)
                    enableAdvancedSearch(true)
                    searchPlaceholder("输入关键词进行搜索...")
                    autoSearch(false)
                    autoSearchDelay(300)
                }
                
                // 选择配置
                selection {
                    enableMultiSelect(true)
                    enableSingleSelect(true)
                    defaultEditMode(false)
                }
                
                // 排序配置
                sort {
                    enableSort(true)
                    enableMultiSort(true)
                    defaultSortColumn("createTime")
                    defaultSortDirection(EnumSortDirection.DESC)
                }
            }
        }
    }
    
    /**
     * 只读表格配置示例
     * 禁用编辑、删除等操作
     */
    fun readOnlyConfig(): TableConfig {
        return tableConfig {
            data {
                columns(emptyList())
                getIdFun { it.hashCode() }
            }
            
            api {
                onLoadData { 
                    com.addzero.kmp.entity.low_table.SpecPageResult.Companion.empty()
                }
            }
            
            ui {
                showActions(false) // 不显示操作列
                showBatchOperations(false) // 不显示批量操作
            }
            
            viewModel {
                selection {
                    enableMultiSelect(false) // 禁用多选
                    defaultEditMode(false)
                }
            }
        }
    }
    
    /**
     * 简化分页配置示例
     * 使用较小的页面大小和有限的选项
     */
    fun simplePaginationConfig(): TableConfig {
        return tableConfig {
            data {
                columns(emptyList())
                getIdFun { it.hashCode() }
            }
            
            api {
                onLoadData { 
                    com.addzero.kmp.entity.low_table.SpecPageResult.Companion.empty()
                }
            }
            
            viewModel {
                pagination {
                    defaultPageSize(5)
                    pageSizeOptions(listOf(5, 10, 20))
                }
                
                search {
                    enableAdvancedSearch(false) // 只启用关键词搜索
                    autoSearch(true) // 启用自动搜索
                    autoSearchDelay(500)
                }
            }
        }
    }
    
    /**
     * 高性能配置示例
     * 针对大数据量优化的配置
     */
    fun highPerformanceConfig(): TableConfig {
        return tableConfig {
            data {
                columns(emptyList())
                getIdFun { it.hashCode() }
            }
            
            api {
                onLoadData { 
                    com.addzero.kmp.entity.low_table.SpecPageResult.Companion.empty()
                }
            }
            
            ui {
                rowHeight(40) // 较小的行高
                showBatchOperations(false) // 禁用批量操作以提高性能
            }
            
            viewModel {
                pagination {
                    defaultPageSize(50) // 较大的页面大小
                    pageSizeOptions(listOf(50, 100, 200))
                }
                
                search {
                    autoSearch(false) // 禁用自动搜索
                    enableAdvancedSearch(false) // 禁用高级搜索
                }
                
                sort {
                    enableMultiSort(false) // 禁用多列排序
                }
            }
        }
    }
}
