package com.addzero.kmp.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.addzero.kmp.generated.api.ApiProvider
import com.addzero.kmp.ext.api
import com.addzero.kmp.generated.isomorphic.SysDictIso
import com.addzero.kmp.generated.isomorphic.SysDictItemIso
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

/**
 * 字典管理视图模型
 * 负责字典类型和字典项的增删改查
 */
@KoinViewModel
class SysDictViewModel : ViewModel() {

    // 搜索关键字
    var keyword by mutableStateOf("")
    // 字典列表
    var dicts by mutableStateOf(listOf<SysDictIso>())

    // 当前选中的字典
    var selectedDict by mutableStateOf<SysDictIso?>(null)

    // 表单显示状态
    var showDictForm by mutableStateOf(false)
    var showItemForm by mutableStateOf(false)

    val dictItems by derivedStateOf {
        selectedDict?.sysDictItems ?: emptyList()
    }


    // 当前编辑的字典项
    var selectedDictItem by mutableStateOf<SysDictItemIso?>(null)


    // 初始化加载数据
    init {

        onSearch()
    }

    // 搜索字典
    fun onSearch() {
        api {
            val result = ApiProvider.sysDictApi.querydict(keyword)
            dicts = result
            selectedDict = dicts.firstOrNull()
        }
    }

    // 保存字典
    fun onSaveDict(dict: SysDictIso) {
        api {
            val saveDict = ApiProvider.sysDictApi.saveDict(dict)
            selectedDict = saveDict
            onSearch()
        }
    }

    // 保存字典项
    fun onSaveDictItem(item: SysDictItemIso) {
        api {
            ApiProvider.sysDictApi.saveDictItem(item)
            showItemForm = false
            onSearch()
        }

    }

    // 删除字典项
    fun onDeleteDictItem(id: Long) {
        viewModelScope.launch {
            ApiProvider.sysDictApi.deleteDictItem(id)
            // 刷新当前选中的字典
//            selectedDict?.let { dict ->
//                val updatedDict = dictService.querydict(dict.dictCode).firstOrNull()
//                if (updatedDict != null) {
//                    selectedDict = updatedDict
//                }
//            }
            onSearch()

        }
    }

    // 编辑字典项
    fun onEditDictItem(item: SysDictItemIso) {
        selectedDictItem = item
        showItemForm = true
    }

    // 新增字典项
    fun onAddDictItem() {
        selectedDictItem = null
        showItemForm = true
    }

    fun deleteDict(lng: Long) {
        viewModelScope.launch {
            ApiProvider.sysDictApi.deleteDict(lng)
            onSearch()

            selectedDict = dicts.first()

        }
    }
}
