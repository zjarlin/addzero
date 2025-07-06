package com.addzero.kmp.viewmodel.table

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import org.koin.android.annotation.KoinViewModel

/**
 * 表格选择功能 ViewModel
 * 负责单选、多选、全选、批量操作等选择相关功能
 */
@KoinViewModel
class TableSelectionViewModel : ViewModel() {
    
    // 是否启用编辑模式（多选模式）
    var enableEditMode by mutableStateOf(false)
        private set
    
    // 已选中的项目ID集合
    var selectedItems by mutableStateOf(emptySet<Any>())
        private set
    
    // 当前选中的单个项目
    var currentSelectItem by mutableStateOf(null as TableRowType?)

    /**
     * 切换编辑模式
     */
    fun toggleEditMode() {
        enableEditMode = !enableEditMode
        if (!enableEditMode) {
            // 退出编辑模式时清空选择
            selectedItems = emptySet()
        }
    }
    
    /**
     * 设置编辑模式
     */
    fun setEditMode(enabled: Boolean) {
        enableEditMode = enabled
        if (!enabled) {
            selectedItems = emptySet()
        }
    }
    
    /**
     * 切换单个项目的选中状态
     */
    fun toggleItemSelection(item: TableRowType, getIdFun: (TableRowType) -> Any) {
        val itemId = getIdFun(item)
        
        selectedItems = if (selectedItems.contains(itemId)) {
            selectedItems.filter { it != itemId }.toSet()
        } else {
            selectedItems + itemId
        }
    }
    
    /**
     * 选中当前页所有项目
     */
    fun selectPageItems(data: List<TableRowType>, getIdFun: (TableRowType) -> Any) {
        val pageIds = data.map { getIdFun(it) }
        selectedItems = selectedItems + pageIds
    }
    
    /**
     * 取消选中当前页所有项目
     */
    fun unselectPageItems(data: List<TableRowType>, getIdFun: (TableRowType) -> Any) {
        val pageIds = data.map { getIdFun(it) }.toSet()
        selectedItems = selectedItems.filter { it !in pageIds }.toSet()
    }
    
    /**
     * 判断当前页是否全选
     */
    fun isPageAllSelected(data: List<TableRowType>, getIdFun: (TableRowType) -> Any): Boolean {
        val pageIds = data.map { getIdFun(it).toString() }.toSet()
        val selectedPageIds = selectedItems.map { it.toString() }.toSet().intersect(pageIds)
        return pageIds.isNotEmpty() && selectedPageIds.size == pageIds.size
    }
    

    /**
     * 清空所有选择
     */
    fun clearSelection() {
        selectedItems = emptySet()
        currentSelectItem = null
    }
    
    /**
     * 选中指定项目
     */
    fun selectItems(itemIds: Set<Any>) {
        selectedItems = selectedItems + itemIds
    }
    
    /**
     * 取消选中指定项目
     */
    fun unselectItems(itemIds: Set<Any>) {
        selectedItems = selectedItems.filter { it !in itemIds }.toSet()
    }
    
    /**
     * 判断项目是否被选中
     */
    fun isItemSelected(item: TableRowType, getIdFun: (TableRowType) -> Any): Boolean {
        val itemId = getIdFun(item)
        return selectedItems.contains(itemId)
    }
    
    /**
     * 获取选中项目数量
     */
    val selectedCount: Int
        get() = selectedItems.size
    
    /**
     * 是否有选中项目
     */
    val hasSelection: Boolean
        get() = selectedItems.isNotEmpty()
}
