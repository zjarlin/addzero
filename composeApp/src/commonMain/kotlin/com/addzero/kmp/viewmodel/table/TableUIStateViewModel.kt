package com.addzero.kmp.viewmodel.table

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel

/**
 * 表格UI状态管理 ViewModel
 * 负责各种弹窗、抽屉、下拉菜单等UI状态的管理
 */
@KoinViewModel
class TableUIStateViewModel : ViewModel() {
    
    // 表单相关状态
    var showForm by mutableStateOf(false)
        private set
    
    var showImportForm by mutableStateOf(false)
        private set
    
    var showConfigDrawer by mutableStateOf(false)
        private set
    
    // 导出相关状态
    var showExportDropDownMenu by mutableStateOf(false)
        private set
    
    // UI配置状态
    var showActions by mutableStateOf(true)
        private set
    
    /**
     * 显示/隐藏表单
     */
    fun setFormVisible(visible: Boolean) {
        showForm = visible
    }
    
    /**
     * 显示表单
     */
    fun showFormDialog() {
        showForm = true
    }
    
    /**
     * 隐藏表单
     */
    fun hideFormDialog() {
        showForm = false
    }
    
    /**
     * 显示/隐藏导入表单
     */
    fun setImportFormVisible(visible: Boolean) {
        showImportForm = visible
    }
    
    /**
     * 显示导入表单
     */
    fun showImportDialog() {
        showImportForm = true
    }
    
    /**
     * 隐藏导入表单
     */
    fun hideImportDialog() {
        showImportForm = false
    }
    
    /**
     * 显示/隐藏配置抽屉
     */
    fun setConfigDrawerVisible(visible: Boolean) {
        showConfigDrawer = visible
    }
    
    /**
     * 显示配置抽屉
     */
    fun showConfigDrawerDialog() {
        showConfigDrawer = true
    }
    
    /**
     * 隐藏配置抽屉
     */
    fun hideConfigDrawerDialog() {
        showConfigDrawer = false
    }
    
    /**
     * 显示/隐藏导出下拉菜单
     */
    fun setExportDropDownVisible(visible: Boolean) {
        showExportDropDownMenu = visible
    }
    
    /**
     * 显示导出下拉菜单
     */
    fun showExportDropDown() {
        showExportDropDownMenu = true
    }
    
    /**
     * 隐藏导出下拉菜单
     */
    fun hideExportDropDown() {
        showExportDropDownMenu = false
    }
    
    /**
     * 设置操作列显示状态
     */
    fun setActionsVisible(visible: Boolean) {
        showActions = visible
    }
    
    /**
     * 切换操作列显示状态
     */
    fun toggleActionsVisible() {
        showActions = !showActions
    }
    
    /**
     * 关闭所有弹窗和下拉菜单
     */
    fun closeAllDialogs() {
        showForm = false
        showImportForm = false
        showConfigDrawer = false
        showExportDropDownMenu = false
    }
    
    /**
     * 重置所有UI状态
     */
    fun resetUIState() {
        closeAllDialogs()
        showActions = true
    }
}
