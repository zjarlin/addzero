package com.addzero.kmp.demo.date_test

import DatePickerField
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.core.ext.DateFormatPattern
import com.addzero.kmp.annotation.Route


@Composable
@Route("组件示例", "日期输入框", routePath = "/component/dateInputField")
fun DateInputFieldExample() {

    var dateStr by remember { mutableStateOf<String?>(null) }
    Column {

        DatePickerField(
            value = dateStr,
            onValueChange = { dateStr = it },
            label = "出生日期",
            dateFormat = DateFormatPattern.DATE_MEDIUM // yyyy年MM月dd日
        )
        Text(dateStr ?: "", modifier = Modifier.padding(16.dp))

    }


}