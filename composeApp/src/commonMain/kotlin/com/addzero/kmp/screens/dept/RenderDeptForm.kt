package com.addzero.kmp.screens.dept

import androidx.compose.runtime.Composable
// import com.addzero.kmp.generated.forms.SysDeptForm
// import com.addzero.kmp.generated.forms.rememberSysDeptFormState
import com.addzero.kmp.viewmodel.SysDeptViewModel

@Composable
fun RenderDeptForm(vm: SysDeptViewModel) {
    // TODO: 重新生成表单后取消注释
    // val state = rememberSysDeptFormState()
    // SysDeptForm(state = state, visible = vm.showForm, title = "部门表单", onClose = { vm.showForm = false }, onSubmit = { vm.onSave(state.value) }) {
    //     parent {
    //         val sysDeptViewModel = SysDeptViewModel()
    //         LeftCard(sysDeptViewModel)
    //     }
    //     children(true)
    //     sysUsers(true)
    // }
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

//@Composable
//private fun SysDeptFormDsl.renderParent(vm: MutableState<SysDeptIso>) {
//    var formTree by mutableStateOf(emptyList<SysDeptIso>())
//
//    var formKeyWord by remember { mutableStateOf("") }
//    LaunchedEffect(formKeyWord) {
//    }
//
//    parent {
////        var formKeyWord by remember { mutableStateOf("") }
////
////        LaunchedEffect(formKeyWord) {
////            vm.loadDeptTree()
////        }
////
//        Box(modifier = Modifier.fillMaxSize()) {
//            Column {
//                AddSearchBar(
//                    keyword = formKeyWord,
//                    onKeyWordChanged = { formKeyWord = it },
//                    onSearch = {
//                        getFormTree { formTree = it }
//                    }
//                )
//
//                AddTree(
//
//                    items = formTree,
//                    getId = { it.id!! },
//                    getLabel = { it.name },
//                    getChildren = { it.children },
//                    onCurrentNodeClick = {
//                        state.value = state.value.copy(parent = it)
//                    },
//                )
//            }
//        }
//    }
//}

//@Composable
//private fun getFormTree(onTreeChange: (List<SysDeptIso>) -> Unit): List<SysDeptIso> {
//    var formTree by mutableStateOf(emptyList<SysDeptIso>())
//
//    val rememberCoroutineScope = rememberCoroutineScope()
//    rememberCoroutineScope.launch {
//        val tree = sysDeptApi.tree("")
//        formTree = tree
//        onTreeChange(formTree)
//    }
//}
