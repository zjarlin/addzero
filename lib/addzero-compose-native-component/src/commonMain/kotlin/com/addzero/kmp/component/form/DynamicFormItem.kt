package com.addzero.kmp.component.form

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
import com.addzero.kmp.component.form.number.AddDecimalField
import com.addzero.kmp.component.form.number.AddIntegerField
import com.addzero.kmp.kt_util.containsAnyIgnoreCase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynamicFormItem(
    value: Any?,
    onValueChange: (Any?) -> Unit,
    title: String?,
    kmpType: String,
) {
    when {
//        // 整数类型
        kmpType.containsAnyIgnoreCase("Long", "Integer", "Int", "Short") -> {
            AddIntegerField(
                value = value.toString(),
                onValueChange = onValueChange,
                label = title ?: "",
            )
        }
//
//        // 浮点数类型
        kmpType.containsAnyIgnoreCase("Float", "Double", "BigDecimal") -> {
            AddDecimalField(
                value = value.toString(),
                onValueChange = onValueChange,
                label = title ?: "",
            )
        }
//
//
//
//
//
//        // 日期类型
//        kmpType.containsAnyIgnoreCase("Date") -> {
//            DatePickerField(
//                value = value.toString(),
//                onValueChange = onValueChange,
//                label = title ?: "",
//                modifier = Modifier.width(160.dp)
//            )
//        }
//
//        // 布尔类型
        kmpType.containsAnyIgnoreCase("Boolean") -> {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = value as? Boolean == true,
                    onCheckedChange = onValueChange
                )
                Text(
                    text = if (value as? Boolean == true) "是" else "否",
                    modifier = Modifier.width(40.dp)
                )
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

