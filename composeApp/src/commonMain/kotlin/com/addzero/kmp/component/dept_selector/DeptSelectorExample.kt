package com.addzero.kmp.component.dept_selector

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.isomorphic.SysDeptIso

/**
 * 🏢 部门选择器使用示例
 *
 * 展示 AddDeptSelector 组件的各种使用方式
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Route
fun DeptSelectorExample() {
    var selectedDepts1 by remember { mutableStateOf<List<SysDeptIso>>(emptyList()) }

    AddDeptSelector(
        selectedDepts = selectedDepts1,
        onValueChange = { selectedDepts1 = it },
    )

}
