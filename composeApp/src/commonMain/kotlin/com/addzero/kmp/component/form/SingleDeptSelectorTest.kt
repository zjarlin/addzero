package com.addzero.kmp.component.form

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.isomorphic.SysDeptIso

/**
 * 🧪 单选部门选择器测试
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Route
fun SingleDeptSelectorTest() {
    var selectedDept by remember { mutableStateOf<SysDeptIso?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🧪 单选部门选择器测试",
            style = MaterialTheme.typography.headlineMedium
        )

        AddSingleDeptSelector(
            selectedDept = selectedDept,
            onValueChange = { selectedDept = it },
            placeholder = "请选择部门"
        )

        if (selectedDept != null) {
            Card {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("选择结果:")
                    Text("部门名称: ${selectedDept!!.name}")
                    Text("部门ID: ${selectedDept!!.id}")
                }
            }
        }

        Button(
            onClick = { selectedDept = null }
        ) {
            Text("清除选择")
        }
    }
}
