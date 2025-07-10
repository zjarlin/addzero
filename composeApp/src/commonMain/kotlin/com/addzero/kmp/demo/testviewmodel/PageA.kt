package com.addzero.kmp.demo.testviewmodel

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.addzero.kmp.annotation.Route
import org.koin.compose.viewmodel.koinViewModel

// PageA.kt
@Composable
fun PageA(vm: PageAViewModel = koinViewModel()) {
    TextField(
        value = "", // 实际应从 vm.searchVM.query 获取
        onValueChange = { vm.onQueryChanged(it) })
}

// PageB.kt
@Composable
fun PageB(vm: PageBViewModel = koinViewModel()) {
    val query by vm.query.collectAsState()
    Text("PageB Query: $query") // 会随 PageA 的输入变化！
}

@Composable
@Route("组件示例", "测试ViewModel")
fun jdoiajsdoida(
) {
    Column {
        PageA()
        PageB()
    }

}
