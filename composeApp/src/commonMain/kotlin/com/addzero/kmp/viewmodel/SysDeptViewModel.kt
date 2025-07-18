package com.addzero.kmp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.addzero.kmp.generated.api.ApiProvider.sysDeptApi
import com.addzero.kmp.assist.api
import com.addzero.kmp.generated.isomorphic.SysDeptIso
import com.addzero.kmp.mock.sampleSysUserVos
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SysDeptViewModel(

) : ViewModel() {
    var showForm by mutableStateOf(false)
    var keyword by mutableStateOf("")

    var users by mutableStateOf(sampleSysUserVos)


    var deptVos by mutableStateOf(emptyList<SysDeptIso>())

    var currentDeptVO by mutableStateOf<SysDeptIso?>(null)


    init {
        loadDeptTree()
    }

    fun loadDeptTree() {
        api {
            deptVos = sysDeptApi.tree(keyword)
        }
    }

    fun saveDept(dept: SysDeptIso) {
        api {
            val userIds = users.map { it.id }
            sysDeptApi.save(dept)
            loadDeptTree()
            showForm = false

        }
    }

    fun deleteDept() {
        api {
            sysDeptApi.delete(currentDeptVO?.id!!)
            loadDeptTree()
        }
    }

    fun onSave(state: SysDeptIso) {
        api {
            val save = sysDeptApi.save(state)
            loadDeptTree()
            showForm=false
        }


    }


}
