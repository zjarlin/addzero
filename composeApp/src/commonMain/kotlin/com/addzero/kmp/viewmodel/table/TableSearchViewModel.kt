package com.addzero.kmp.viewmodel.table

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.entity.low_table.StateSearchForm
import org.koin.android.annotation.KoinViewModel

/**
 * 表格搜索功能 ViewModel
 * 负责关键词搜索、高级搜索、过滤条件管理
 */
@KoinViewModel
class TableSearchViewModel : ViewModel() {
    
    // 关键词搜索
    var keyword by mutableStateOf("")
        private set
    
    // 高级搜索相关状态
    var showAdvancedSearch by mutableStateOf(true)
        private set
    
    var showFieldAdvSearch by mutableStateOf(false)
        private set
    
    var currentAdvSearchColumn by mutableStateOf(null as com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType?)
        private set
    
    // 过滤条件映射
    var filterStateMap by mutableStateOf(mapOf<String, StateSearchForm>())
        private set
    
    // 获取过滤条件集合
    val filterState: Set<StateSearchForm>
        get() = filterStateMap.values.toSet()
    
    /**
     * 更新关键词
     */
    fun updateKeyword(newKeyword: String) {
        keyword = newKeyword
    }
    
    /**
     * 清空关键词
     */
    fun clearKeyword() {
        keyword = ""
    }
    
    /**
     * 设置高级搜索显示状态
     */
    fun setAdvancedSearchVisible(visible: Boolean) {
        showAdvancedSearch = visible
    }
    
    /**
     * 显示字段高级搜索
     */
    fun showColumnAdvSearch(column: com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType) {
        currentAdvSearchColumn = column
        showFieldAdvSearch = true
    }
    
    /**
     * 隐藏字段高级搜索
     */
    fun hideColumnAdvSearch() {
        showFieldAdvSearch = false
    }
    
    /**
     * 更新搜索表单
     */
    fun updateSearchForm(
        columnKey: String,
        columnValue: Any?,
        operator: com.addzero.kmp.entity.low_table.EnumSearchOperator,
        logicType: com.addzero.kmp.entity.low_table.EnumLogicOperator
    ) {
        val newForm = StateSearchForm(
            columnKey = columnKey,
            operator = operator,
            columnValue = columnValue,
            logicType = logicType
        )
        filterStateMap = filterStateMap.toMutableMap().apply {
            this[columnKey] = newForm
        }
    }
    
    /**
     * 清除指定列的搜索条件
     */
    fun clearColumnFilter(columnKey: String) {
        filterStateMap = filterStateMap.toMutableMap().apply {
            remove(columnKey)
        }
    }
    
    /**
     * 清除所有搜索条件
     */
    fun clearAllFilters() {
        filterStateMap = emptyMap()
        keyword = ""
    }
    
    /**
     * 判断某列是否有过滤条件
     */
    fun isColumnFiltered(columnKey: String): Boolean {
        return filterStateMap.containsKey(columnKey)
    }
    
    /**
     * 获取指定列的搜索表单
     */
    fun getColumnSearchForm(columnKey: String): StateSearchForm? {
        return filterStateMap[columnKey]
    }
    
    /**
     * 是否有任何搜索条件
     */
    val hasAnyFilter: Boolean
        get() = keyword.isNotEmpty() || filterStateMap.isNotEmpty()
}
