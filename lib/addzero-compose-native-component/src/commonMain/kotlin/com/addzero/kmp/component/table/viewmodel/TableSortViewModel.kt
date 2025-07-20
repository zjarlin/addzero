package com.addzero.kmp.component.table.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.component.table.config.TableSortConfig
import com.addzero.kmp.entity.low_table.EnumSortDirection
import com.addzero.kmp.entity.low_table.StateSort
import com.addzero.kmp.kt_util.removeIf
import org.koin.android.annotation.KoinViewModel

/**
 * 表格排序功能 ViewModel
 * 负责排序状态管理和排序操作
 */
@KoinViewModel
class TableSortViewModel : ViewModel() {

    private var config: TableSortConfig = TableSortConfig()

    // 排序状态集合
    var sortState by mutableStateOf<MutableSet<StateSort>>(mutableSetOf())
        private set

    /**
     * 初始化配置
     */
    fun initialize(sortConfig: TableSortConfig) {
        config = sortConfig

        // 设置默认排序
        if (config.defaultSortColumn != null && config.defaultSortDirection != EnumSortDirection.NONE) {
            sortState = mutableSetOf(StateSort(config.defaultSortColumn!!, config.defaultSortDirection))
        }
    }

    /**
     * 切换排序状态
     * 点击一次：升序(ASC)
     * 点击两次：降序(DESC)
     * 点击三次：无序(NONE)
     */
    fun changeSorting(columnKey: String) {
        // 查找当前是否已有该列的排序状态
        val existingSort = sortState.find { it.columnKey == columnKey }
        println("【排序处理】列: $columnKey, 当前排序状态: ${existingSort?.direction ?: "NONE"}")

        // 根据当前排序状态决定下一个状态
        val newDirection = when (existingSort?.direction) {
            EnumSortDirection.ASC -> EnumSortDirection.DESC
            EnumSortDirection.DESC -> EnumSortDirection.NONE
            else -> EnumSortDirection.ASC // null或NONE时设为ASC
        }
        println("【排序处理】列: $columnKey, 新排序状态: $newDirection")

        // 创建新的排序状态
        val newSortState = sortState.toMutableList()

        // 移除旧的排序状态
        newSortState.removeIf { it.columnKey == columnKey }

        // 只有非NONE状态才添加
        if (newDirection != EnumSortDirection.NONE) {
            newSortState.add(StateSort(columnKey, newDirection))
        }

        // 更新排序状态 - 强制创建新实例
        sortState = newSortState.toMutableSet()
    }

    /**
     * 设置指定列的排序方向
     */
    fun setSorting(columnKey: String, direction: EnumSortDirection) {
        val newSortState = sortState.toMutableList()

        // 移除旧的排序状态
        newSortState.removeIf { it.columnKey == columnKey }

        // 只有非NONE状态才添加
        if (direction != EnumSortDirection.NONE) {
            newSortState.add(StateSort(columnKey, direction))
        }

        sortState = newSortState.toMutableSet()
    }

    /**
     * 清除指定列的排序
     */
    fun clearSorting(columnKey: String) {
        val newSortState = sortState.toMutableList()
        newSortState.removeIf { it.columnKey == columnKey }
        sortState = newSortState.toMutableSet()
    }

    /**
     * 清除所有排序
     */
    fun clearAllSorting() {
        sortState = mutableSetOf()
    }

    /**
     * 判断某列是否有排序状态
     */
    fun isSorted(columnKey: String): Boolean {
        return sortState.any { it.columnKey == columnKey }
    }

    /**
     * 获取某列的排序方向
     */
    fun getSortDirection(columnKey: String): EnumSortDirection {
        return sortState.find { it.columnKey == columnKey }?.direction ?: EnumSortDirection.NONE
    }


    /**
     * 添加排序条件
     */
    fun addSort(columnKey: String, direction: EnumSortDirection) {
        if (direction != EnumSortDirection.NONE) {
            val newSortState = sortState.toMutableList()
            newSortState.removeIf { it.columnKey == columnKey } // 先移除旧的
            newSortState.add(StateSort(columnKey, direction))
            sortState = newSortState.toMutableSet()
        }
    }

    /**
     * 是否有任何排序条件
     */
    val hasAnySorting: Boolean
        get() = sortState.isNotEmpty()

    /**
     * 获取排序条件数量
     */
    val sortCount: Int
        get() = sortState.size
}
