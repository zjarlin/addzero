package com.addzero.kmp.viewmodel.table

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addzero.kmp.component.addzero_starter.addzero_table.TableColumnType
import com.addzero.kmp.component.addzero_starter.addzero_table.TableRowType
import com.addzero.kmp.component.toast.ToastManager
import com.addzero.kmp.entity.low_table.*
import com.addzero.kmp.ext.api
import com.addzero.kmp.ext.toMultipartFile
import io.github.vinceglb.filekit.core.PlatformFiles
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * 表格数据管理 ViewModel
 * 负责数据的CRUD操作、数据加载、导入导出等
 */
@KoinViewModel
class TableDataViewModel : ViewModel() {

    private lateinit var config: TableConfig

    // 表格数据
    var data by mutableStateOf(emptyList<TableRowType>())

    // 列定义
    var columns by mutableStateOf(emptyList<TableColumnType>())

    /**
     * 初始化配置
     */
    fun initialize(tableConfig: TableConfig) {
        config = tableConfig
        data = config.dataConfig.initData
        columns = config.dataConfig.columns
    }

    /**
     * 查询分页数据
     */
    fun queryPage(
        keyword: String,
        sortState: Set<StateSort>,
        filterState: Set<StateSearchForm>,
        pageNo: Int,
        pageSize: Int,
        onSuccess: (SpecPageResult<TableRowType>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val commonTableDaTaInputDTO = CommonTableDaTaInputDTO(
                    tableName = "",
                    keyword = keyword,
                    stateSorts = sortState.toMutableSet(),
                    stateSearchForms = filterState.toMutableSet(),
                    pageNo = pageNo,
                    pageSize = pageSize
                )
                val result = config.apiConfig.onLoadData(commonTableDaTaInputDTO)
                data = result.rows
                onSuccess(result)
            } catch (e: Exception) {
                ToastManager.error("数据加载失败: ${e.message}")
            }
        }
    }

    /**
     * 保存数据（新增或编辑）
     */
    fun saveData(item: TableRowType?, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = config.apiConfig.onSave(item)
                if (result) {
                    ToastManager.info("保存成功")
                    onSuccess()
                } else {
                    ToastManager.error("保存失败")
                }
            } catch (e: Exception) {
                ToastManager.error("保存失败: ${e.message}")
            }
        }
    }

    /**
     * 删除单个数据
     */
    fun deleteData(id: Any, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = config.apiConfig.onDelete(id)
                if (result) {
                    ToastManager.info("删除成功")
                    onSuccess()
                } else {
                    ToastManager.error("删除失败")
                }
            } catch (e: Exception) {
                ToastManager.error("删除失败: ${e.message}")
            }
        }
    }

    /**
     * 批量删除数据
     */
    fun batchDeleteData(selectedItems: Set<Any>, onSuccess: () -> Unit = {}) {
        if (selectedItems.isEmpty()) {
            api {
                ToastManager.warning("请选择要删除的项目")
            }
            return
        }

        viewModelScope.launch {
            try {
                var successCount = 0
                var failCount = 0

                for (itemId in selectedItems) {
                    val result = config.apiConfig.onDelete(itemId)
                    if (result) {
                        successCount++
                    } else {
                        failCount++
                    }
                }

                when {
                    failCount == 0 -> ToastManager.info("批量删除成功，共删除 $successCount 项")
                    successCount == 0 -> ToastManager.error("批量删除失败")
                    else -> ToastManager.warning("部分删除成功：成功 $successCount 项，失败 $failCount 项")
                }

                onSuccess()
            } catch (e: Exception) {
                ToastManager.error("批量删除失败: ${e.message}")
            }
        }
    }

    /**
     * 编辑数据
     */
    fun editData(item: TableRowType, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = config.apiConfig.onEdit(item)
                if (result) {
                    ToastManager.info("编辑成功")
                    onSuccess()
                } else {
                    ToastManager.error("编辑失败")
                }
            } catch (e: Exception) {
                ToastManager.error("编辑失败: ${e.message}")
            }
        }
    }

    /**
     * 导出数据
     */
    fun exportData(exportParam: ExportParam, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = config.apiConfig.onExport(exportParam)
                if (result) {
                    ToastManager.info("导出成功")
                    onSuccess()
                } else {
                    ToastManager.error("导出失败")
                }
            } catch (e: Exception) {
                ToastManager.error("导出失败: ${e.message}")
            }
        }
    }

    /**
     * 批量导出数据
     */
    fun batchExportData(selectedItems: Set<Any>, onSuccess: () -> Unit = {}) {
        if (selectedItems.isEmpty()) {
            api {
                ToastManager.warning("请选择要导出的项目")
            }
            return
        }

        val exportParam = ExportParam(
            ids = selectedItems,
            enumExportType = EnumExportType.XLSX
        )
        exportData(exportParam, onSuccess)
    }

    /**
     * 上传文件
     */
    fun uploadFiles(files: PlatformFiles?, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val results = files?.map { file ->
                    val content = file.toMultipartFile()
                    config.apiConfig.onUpload(content)
                }?.toList()

                ToastManager.info("文件上传成功: $results")
                onSuccess()
            } catch (e: Exception) {
                ToastManager.error("文件上传失败: ${e.message}")
            }
        }
    }

    /**
     * 下载模板
     */
    fun downloadTemplate(onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                ToastManager.info("模板下载成功")
                onSuccess()
            } catch (e: Exception) {
                ToastManager.error("模板下载失败: ${e.message}")
            }
        }
    }

    /**
     * 触发自定义事件
     */
    fun triggerCustomEvent(eventType: String, eventData: Any? = null, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val result = config.apiConfig.onCustomEvent(eventType, eventData)
                if (result) {
                    onSuccess()
                }
            } catch (e: Exception) {
                ToastManager.error("操作失败: ${e.message}")
            }
        }
    }


}
