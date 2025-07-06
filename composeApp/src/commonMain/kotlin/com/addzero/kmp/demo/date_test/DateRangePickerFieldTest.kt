@file:OptIn(ExperimentalMaterial3Api::class)

package com.addzero.kmp.demo.date_test

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.addzero.kmp.annotation.Route



/**
 * 日期范围选择
 */
@Composable
@Route("组件示例", "日期范围选择器")

fun DateRangePickerFieldTest() {


    val state = rememberDateRangePickerState().apply {
        displayMode = DisplayMode.Input
    }


    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
    ) {
        DateRangePicker(
            state = state
        )
        Text("已选择的起始时间戳${state.selectedStartDateMillis.toString()}")
        Text("已选择的结束时间戳${state.selectedEndDateMillis.toString()}")

    }


}
