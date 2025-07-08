package com.addzero.kmp.component.form

import DatePickerField
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.kt_util.containsAnyIgnoreCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicFormItem(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    title: String?,
    kmpType: String,
) {
    val javaType = kmpType
    when {
        // 整数类型
//        javaType.containsAnyIgnoreCase("Long", "Integer", "Int", "Short") -> {
//            IntegerInputField(
//                value = value,
//                onValueChange = onValueChange,
//                label = title ?: "",
//                modifier = Modifier.width(160.dp)
//            )
//        }

        // 浮点数类型
//        javaType.containsAnyIgnoreCase("Float", "Double", "BigDecimal") -> {
//            DecimalInputField(
//                value = value,
//                onValueChange = onValueChange,
//                label = title ?: "",
//                modifier = Modifier.width(160.dp),
//                precision = 2  // 限制小数位数，可根据需要调整
//            )
//        }

        // 日期类型
        javaType.containsAnyIgnoreCase("Date") -> {
//            DatePickerField(
//                value = value.toString(),
//                onValueChange = onValueChange,
//                label = title ?: "",
//                modifier = Modifier.width(160.dp)
//            )
        }

        // 布尔类型
        javaType.containsAnyIgnoreCase("Boolean") -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = value as? Boolean == true,
                    onCheckedChange = onValueChange
                )
                Text("是/否", modifier = Modifier.width(40.dp))
            }
        }

        // 默认文本类型
        else -> {
            val textValue = remember(value) { mutableStateOf(value?.toString() ?: "") }

            OutlinedTextField(
                value = textValue.value,
                onValueChange = { newValue ->
                    textValue.value = newValue
                    onValueChange(newValue)
                },
                label = { Text(title ?: "") },
                singleLine = true,
                modifier = Modifier.width(160.dp)
            )
        }
    }
}

