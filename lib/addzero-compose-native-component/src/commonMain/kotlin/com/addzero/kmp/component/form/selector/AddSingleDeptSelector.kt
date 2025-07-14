package com.addzero.kmp.component.form.selector

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.addzero.kmp.generated.api.ApiProvider.sysDeptApi
import com.addzero.kmp.generated.isomorphic.SysDeptIso

/**
 * 🏢 单选部门选择组件
 *
 * 基于 AddDeptSelector 派生的单选版本，选择一个部门后立即关闭并确认
 *
 * @param value 当前选中的部门
 * @param onValueChange 选择变化回调，返回选中的部门（可为null）
 * @param modifier 修饰符
 * @param placeholder 占位符文本
 * @param enabled 是否启用
 * @param maxHeight 最大高度
 * @param allowClear 是否允许清除选择
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSingleDeptSelector(
    value: SysDeptIso? = null,
    onValueChange: (SysDeptIso?) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "请选择部门",
    enabled: Boolean = true,
    maxHeight: Dp = 400.dp,
    allowClear: Boolean = true
) {
    var selectedDept by remember { mutableStateOf(value) }


    AddGenericSingleSelector(
        value = selectedDept,
        onValueChange = { selectedDept = it },
        dataProvider = { sysDeptApi.tree("") },
        getId = { it.id!! },
        getLabel = { it.name },
        getChildren = { it.children }
    )


}
