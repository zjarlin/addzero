package com.addzero.kmp.viewmodel.table

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.koin.android.annotation.KoinViewModel

/**
 * 分页状态
 */
data class StatePagination(
    var currentPage: Int = 1,
    var pageSize: Int = 10,
    var totalItems: Int = 0
) {
    val totalPages: Int get() = if (totalItems == 0) 1 else (totalItems + pageSize - 1) / pageSize
    val hasPreviousPage: Boolean get() = currentPage > 1
    val hasNextPage: Boolean get() = currentPage < totalPages
    val startItem: Int get() = (currentPage - 1) * pageSize + 1
    val endItem: Int get() = minOf(currentPage * pageSize, totalItems)
}



/**
 * 表格分页功能 ViewModel
 * 负责分页状态管理和分页操作
 */
@KoinViewModel
class TablePaginationViewModel : ViewModel() {
    
    // 分页状态
    var pageState by mutableStateOf(StatePagination())
        private set

    
    /**
     * 更新当前页
     */
    fun setCurrentPage(page: Int) {
        pageState = pageState.copy(currentPage = page)
    }
    
    /**
     * 更新页面大小
     */
    fun setPageSize(size: Int) {
        pageState = pageState.copy(pageSize = size, currentPage = 1) // 重置到第一页
    }
    
    /**
     * 更新总条目数
     */
    fun setTotalItems(total: Int) {
        pageState = pageState.copy(totalItems = total)
    }
    
    /**
     * 跳转到第一页
     */
    fun goToFirstPage() {
        pageState = pageState.copy(currentPage = 1)
    }
    
    /**
     * 跳转到上一页
     */
    fun goToPreviousPage() {
        if (pageState.hasPreviousPage) {
            pageState = pageState.copy(currentPage = pageState.currentPage - 1)
        }
    }
    
    /**
     * 跳转到下一页
     */
    fun goToNextPage() {
        if (pageState.hasNextPage) {
            pageState = pageState.copy(currentPage = pageState.currentPage + 1)
        }
    }
    
    /**
     * 跳转到最后一页
     */
    fun goToLastPage() {
        pageState = pageState.copy(currentPage = pageState.totalPages)
    }
    
    /**
     * 跳转到指定页
     */
    fun goToPage(page: Int) {
        if (page in 1..pageState.totalPages) {
            pageState = pageState.copy(currentPage = page)
        }
    }
    
    /**
     * 重置分页状态
     */
    fun resetPagination() {
        pageState = StatePagination()
    }
    
    /**
     * 获取当前页的起始条目索引（从0开始）
     */
    val startIndex: Int
        get() = (pageState.currentPage - 1) * pageState.pageSize
    
    /**
     * 获取当前页的结束条目索引（从0开始，不包含）
     */
    val endIndex: Int
        get() = minOf(startIndex + pageState.pageSize, pageState.totalItems)
    
    /**
     * 是否显示分页控件
     */
    fun shouldShowPagination(): Boolean {
        return pageState.totalItems > pageState.pageSize
    }
}
