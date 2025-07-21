package com.addzero.kmp.demo

import androidx.compose.runtime.Composable
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.AddDataTable
import com.addzero.kmp.mock.mockkSysMunu


@Route
@Composable
fun daoisjdoaisjd(): Unit {
    val mockkSysMunu = mockkSysMunu()
    val values = mockkSysMunu.values
    val keys = mockkSysMunu.keys
    val data = values.toList()
    AddDataTable(
        data = data,
        columns = keys.toList(),
        getLabel = { it },
        getValue = {it.toString()},
    ) {

    }
}
