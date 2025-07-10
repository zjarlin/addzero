package com.addzero.kmp.screens.dept

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.addzero.kmp.component.button.AddIconButton
import com.addzero.kmp.component.search_bar.AddSearchBar
import com.addzero.kmp.component.tree.AddTree
import com.addzero.kmp.isomorphic.SysDeptIso
import com.addzero.kmp.viewmodel.SysDeptViewModel

@Composable
fun LeftCard(
    vm: SysDeptViewModel, onNodeClick: (SysDeptIso) -> Unit = {
        vm.currentDeptVO = it
    }
) {
    Column {
        // 标题和添加按钮
        AddIconButton(text = "添加部门") { vm.showForm = true }

        AddSearchBar(
            keyword = vm.keyword,
            onKeyWordChanged = { vm.keyword = it },
            onSearch = { vm.loadDeptTree() }
        )

        AddTree(
            items = vm.deptVos,
            getId = { it.id!! },
            getLabel = { it.name },
            getChildren = { it.children },
            onNodeClick = onNodeClick,
        )


//                AddFlatTree(
//                    items = vm.deptVos,
//                    getId = { it.id!! },
//                    getParentId = { it.parent?.id },
//                    getName = { it.name.toString() },
//                    onNodeClick = {
//                        vm.currentDeptVO = it
//                    },
//                )
    }
}
