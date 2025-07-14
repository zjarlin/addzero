package com.addzero.kmp.component.form.selector

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.addzero.kmp.generated.api.ApiProvider.sysDeptApi
import com.addzero.kmp.generated.isomorphic.SysDeptIso

/**
 * 🏢 部门选择组件
 *
 * 基于 AddTreeWithCommand 封装的部门多选组件
 *
 * @param value 当前选中的部门列表
 * @param onValueChange 选择变化回调，返回选中的部门列表
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param enabled 是否启用
 * @param showConfirmButton 是否显示确认按钮
 * @param maxHeight 最大高度
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeptSelector(
    value: List<SysDeptIso> = emptyList(),
    onValueChange: (List<SysDeptIso>) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "请选择部门",
    enabled: Boolean = true,
    showConfirmButton: Boolean = true,
    maxHeight: Dp = 400.dp
) {
    var tempSelectedDepts by remember { mutableStateOf(value) }
    AddGenericMultiSelector(
        value = tempSelectedDepts,
        onValueChange = { tempSelectedDepts = it },
        dataProvider = { sysDeptApi.tree("") },
        getId = { it.id!! },
        getLabel = { it.name },
        getChildren = {it.children}
    )


}


