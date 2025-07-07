package com.addzero.kmp.screens.dept

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.addzero.kmp.component.search_bar.AddSearchBar
import com.addzero.kmp.component.tree.AddTree
import com.addzero.kmp.core.ext.convertToByKtx
import com.addzero.kmp.forms.SysDeptForm
import com.addzero.kmp.forms.SysDeptFormDsl
import com.addzero.kmp.forms.rememberSysDeptFormState
import com.addzero.kmp.isomorphic.SysDeptIso
import com.addzero.kmp.mock.mockSysDepts
import com.addzero.kmp.viewmodel.SysDeptViewModel

@Composable
fun RenderDeptForm(vm: SysDeptViewModel) {

//    val vm = koinViewModel<SysDeptViewModel>()

    val rememberSysDeptFormState = rememberSysDeptFormState()
    SysDeptForm(
        rememberSysDeptFormState,
        visible = vm.showForm,
        title = "部门表单",
        onClose = { vm.showForm = false },
        onSubmit = { vm.onSave(rememberSysDeptFormState.value) }
    ) {
        renderParent(state)
        children(true)
        sysUsers(true)
    }
}

//private fun SysDeptFormDsl.renderParent(vm: SysDeptViewModel) {
//
//    parent {
//
//
//        LeftCard(
//            vm = vm,
//            onNodeClick = {
//                state.value = state.value.copy(parent = it)
//            }
//        )
//
//
//
////        //默认选中上级
////        state.value = state.value.copy(parent = vm.currentDeptVO)
////        var formKeyWord by remember { mutableStateOf("") }
////
////        LaunchedEffect(formKeyWord) {
////            vm.viewModelScope.launch {
////
////            }
////
////
////        }
////
////
////        Box(modifier = Modifier.Companion.fillMaxSize()) {
////            Column {
////                AddSearchBar(
////                    keyword = formKeyWord,
////                    onKeyWordChanged = { formKeyWord = it },
////                    onSearch = { vm.loadDeptTree() }
////                )
////
////                AddTree(
////                    items = vm.deptVos,
////                    getId = { it.id!! },
////                    getLabel = { it.name },
////                    getChildren = { it.children },
////                    onCurrentNodeClick = {
////                        state.value = state.value.copy(parent = it)
////                    },
////                )
////
////            }
////
////        }
//
//
//    }
//}

private fun SysDeptFormDsl.renderParent(vm: MutableState<SysDeptIso>) {
    parent {
//        var formKeyWord by remember { mutableStateOf("") }
//
//        LaunchedEffect(formKeyWord) {
//            vm.loadDeptTree()
//        }
//
//        Box(modifier = Modifier.fillMaxSize()) {
//            Column {
//                AddSearchBar(
//                    keyword = formKeyWord,
//                    onKeyWordChanged = { formKeyWord = it },
//                    onSearch = { vm.loadDeptTree() }
//                )
//
//                AddTree(
//
//                    items = vm.deptVos,
//                    getId = { it.id!! },
//                    getLabel = { it.name },
//                    getChildren = { it.children },
//                    onCurrentNodeClick = {
//                        state.value = state.value.copy(parent = it)
//                    },
//                )
//            }
//        }
    }
}
