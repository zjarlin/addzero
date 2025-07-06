@file:OptIn(ExperimentalMaterial3Api::class)

package com.addzero.kmp.demo.date_test

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.addzero.kmp.processor.annotation.Route


/**
 * 日期范围选择
 */
@Composable
@Route("组件示例", "时间选择器")

fun TimePickerFieldTest() {

    val state = rememberTimePickerState().apply {
        is24hour = true
    }
    Column {
        TimePicker(
            state = state
        )
        Text("已选择的小时${state.hour}")
        Text("已选择的分钟${state.minute}")

    }


}
