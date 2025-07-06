package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import com.addzero.kmp.component.toast.ToastManager
import com.addzero.kmp.entity.low_table.*
import com.addzero.kmp.ext.toMultipartFile
import com.addzero.kmp.kt_util.removeIf
import io.github.vinceglb.filekit.core.PlatformFiles
import kotlinx.coroutines.launch

/**
 * 表格视图
 * @author zjarlin
 * @date 2025/05/10
 * @constructor 创建[GenericTableViewModel]
 * @param [config] 表格配置
 */
class GenericTableViewModel(
    val config: TableConfig
) : ViewModel() {

    var currentSelectItem by mutableStateOf(null as TableRowType?)

    //显示配置
    var showConfigDrawer by mutableStateOf(false)

    //初始表格
    var keyword by mutableStateOf("")

    var showForm by mutableStateOf(false)
    var showImportForm by mutableStateOf(false)

    var currentAdvSearchColumn by mutableStateOf(null as TableColumnType?)

    /////////////////////////增删改查实现

    fun queryPage() {
        viewModelScope.launch {
            val commonTableDaTaInputDTO = CommonTableDaTaInputDTO(
                tableName = "",
                keyword = keyword,
                stateSorts = sortState.toMutableSet(),
                stateSearchForms = filterState,
                pageNo = pageState.currentPage,
                pageSize = pageState.pageSize
            )
            val res = config.apiConfig.onLoadData(commonTableDaTaInputDTO)
            data = res.rows
            pageState = pageState.copy(totalItems = res.totalRowCount.toInt())
        }
    }


    /**
     * 导出数据
     * @param exportParam 导出参数
     */
    fun onExport(exportParam: ExportParam) {
        // 根据选择的导出类型和ID执行导出逻辑
        viewModelScope.launch {
            try {
                // 这里调用实际的导出API
                config.apiConfig.onExport(exportParam)
            } catch (e: Exception) {
                // 处理导出异常
                ToastManager.error("${e.message}")
            }
        }
    }

    /**
     * 导入数据
     * @param filePath 文件路径，可以是本地文件或网络URL
     * @param fileType 文件类型
     */
    fun importData() {
        viewModelScope.launch {
            try {
                queryPage()
            } catch (e: Exception) {
                ToastManager.error("${e.message}")
            }
        }
    }


    fun selectPageItems() {
        // 将当前页所有id添加到selectedItems中（去重）
        val pageIds = data.mapNotNull { config.dataConfig.getIdFun(it) }
        selectedItems = selectedItems + pageIds
    }

    fun unselectPageItems() {
        // 将当前页所有id从selectedItems中移除
        val pageIds = data.mapNotNull { config.dataConfig.getIdFun(it) }
        selectedItems = selectedItems.filter { it !in pageIds }.toSet()
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
        queryPage()
    }


    // 判断某列是否有排序状态
    fun isSorted(columnKey: String): Boolean {
        return sortState.any { it.columnKey == columnKey }
    }

    // 获取某列的排序方向
    fun getSortDirection(columnKey: String): EnumSortDirection {
        return sortState.find { it.columnKey == columnKey }?.direction ?: EnumSortDirection.NONE
    }

    // 判断某列是否有过滤条件
    fun isFiltered(columnKey: String): Boolean {
        return filterStateMap.containsKey(columnKey)
    }

    /** 是否全选 */
    val isPageAllSelected: Boolean
        get() {
            val pageIds = data.map { config.dataConfig.getIdFun(it).toString() }.toSet()
            val selectedPageIds = selectedItems.map { it.toString() }.toSet().intersect(pageIds)
            return pageIds.isNotEmpty() && selectedPageIds.size == pageIds.size
        }

    var enableEditMode by mutableStateOf(false)

    // 高级搜索相关状态
    //表格搜索
    val filterState
        get() = filterStateMap.values.toMutableSet()


    var filterStateMap by mutableStateOf(mapOf<String, StateSearchForm>())

    // 修改为使用List而不是Set，确保状态变化时触发重组
    var sortState by mutableStateOf<MutableSet<StateSort>>(mutableSetOf())

    var pageState by mutableStateOf(StatePagination())

    // 获取列定义，初始化使用配置的列
    var columns by mutableStateOf(config.dataConfig.columns)

    // 表格数据，初始化使用配置的数据
    var data by mutableStateOf(config.dataConfig.initData)

    // 新增：高级搜索图标按钮
    var showFieldAdvSearch by mutableStateOf(false)

    //显示导出下拉
    var showExportDropDownMenu by mutableStateOf(false)

    //表格体已选中的项
    var selectedItems by mutableStateOf(emptySet<Any>())

    //显示高级搜索框
    var showAdvancedSearch by mutableStateOf(config.uiConfig.showAdvancedSearch)

    // 显示操作列
    var showActions by mutableStateOf(config.uiConfig.showActions)

    fun previousPage() {
        if (pageState.hasPreviousPage) {
            pageState = pageState.copy(currentPage = pageState.currentPage - 1)
            queryPage()
        }
    }

    fun goToPage(it: Int) {
        pageState = pageState.copy(currentPage = it)
        queryPage()
    }

    fun nextPage() {
        if (pageState.hasNextPage) {
            pageState = pageState.copy(currentPage = pageState.currentPage + 1)
            queryPage()
        }
    }

    fun deleteBatch() {
        // 批量删除逻辑
        viewModelScope.launch {
            // 调用删除API
            // ...
            // 删除成功后重新加载数据
            queryPage()
        }
    }

    // 切换单个项目的选中状态
    fun toggleItemSelection(item: TableRowType) {
        val itemId = config.dataConfig.getIdFun(item)

        // 如果项目已被选中，则从selectedItems中移除
        if (selectedItems.contains(itemId)) {
            selectedItems = selectedItems.filter { it != itemId }.toSet()
        } else {
            // 否则添加到selectedItems中
            selectedItems = selectedItems + itemId
        }
    }

    fun onEdit() {
        // 批量删除逻辑
        viewModelScope.launch {
            // 调用删除API
            // 删除成功后重新加载数据
            currentSelectItem.let {
                config.apiConfig.onEdit(it!!)
                queryPage()
            }
        }
    }

    fun onDelete(id: Any) {
        viewModelScope.launch {
            // 调用删除API
            // 删除成功后重新加载数据
            currentSelectItem.let {
                config.apiConfig.onDelete(id)
                queryPage()
            }
        }
    }

    fun onSave() {
        viewModelScope.launch {
            // 调用删除API
            // 删除成功后重新加载数据
            currentSelectItem.let {
                config.apiConfig.onSave(it as TableRowType)
                queryPage()
            }
        }
    }

    fun onBatchExport() {
        if (selectedItems.isEmpty()) return

        viewModelScope.launch {
            try {
                val exportParam = ExportParam(
                    ids = selectedItems, enumExportType = EnumExportType.XLSX
                )
                config.apiConfig.onExport(exportParam)
            } catch (e: Exception) {
                ToastManager.error("批量导出失败: ${e.message}")
            }
        }
    }

    fun onBatchDelete() {
        if (selectedItems.isEmpty()) return

        viewModelScope.launch {
            try {
                // 逐个删除选中的项目
                var success = true
                for (itemId in selectedItems) {
                    val result = config.apiConfig.onDelete(itemId)
                    if (!result) {
                        success = false
                    }
                }

                if (success) {
                    ToastManager.info("批量删除成功")
                } else {
                    ToastManager.error("部分项目删除失败")
                }

                // 刷新数据
                selectedItems = emptySet()
                queryPage()
            } catch (e: Exception) {
                ToastManager.error("批量删除失败: ${e.message}")
            }
        }
    }

    fun onUpload(files: PlatformFiles?) {
        viewModelScope.launch {
            val map = files?.map { file ->
                // 创建MultiPartFormDataContent用于文件上传
                val content = file.toMultipartFile()
                val upload = config.apiConfig.onUpload(content)
                upload
            }?.toList()
            // 导入成功后刷新数据
            ToastManager.info("文件导入成功$map")
            queryPage()
        }
    }

    fun onDownloadTemplate() {
        viewModelScope.launch {
            try {
                ToastManager.info("模板下载成功")
                queryPage()
            } catch (e: Exception) {
                ToastManager.error("下载失败: ${e.message}")
            }
        }
    }

    /**
     * 触发自定义事件
     * @param eventType 事件类型标识
     * @param data 事件相关数据
     */
    fun triggerCustomEvent(eventType: String, data: Any? = null) {
        viewModelScope.launch {
            try {
                val result = config.apiConfig.onCustomEvent(eventType, data)
                if (result) {
                    queryPage()
                }
            } catch (e: Exception) {
                ToastManager.error("操作失败: ${e.message}")
            }
        }
    }

    init {
        // 只有在初始加载时查询一次数据，后续通过用户点击触发
        if (config.dataConfig.initData.isEmpty()) {
            queryPage()
        }
    }
}


