package com.addzero.kmp.demo.date_test

import DateTimePickerField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.addzero.kmp.processor.annotation.Route

@Composable
@Route("组件示例", "日期时间选择器", routePath = "/component/dateTimePickerField")
fun DateTimePickerField() {


    var dateTimeValue by remember { mutableStateOf<String?>(null) }

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
    ) {

        DateTimePickerField(
            value = dateTimeValue,
            onValueChange = { dateTimeValue = it },
            label = "选择日期时间",
//        modifier = Modifier,
//        timeZone = TimeZone.currentSystemDefault()
        )

        Text("已选择的日期时间: ${dateTimeValue ?: "未选择"}", modifier = Modifier)


    }

}