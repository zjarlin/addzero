package com.addzero.kmp.component.form.selector

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.enums.EnumUtils.getEnumValues
import kotlin.reflect.KClass

/**
 * 枚举选择器组件
 * 基于 DropdownMenu 实现，提供枚举值选择功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
inline fun <reified T : Enum<T>> AddEnumSelector(
    value: T?,
    crossinline onValueChange: (T?) -> Unit,
    label: String,
    isRequired: Boolean = false,
    enabled: Boolean = true,
    placeholder: String = "请选择",
    enumClass: KClass<T>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    // 获取所有枚举值（KMP 兼容方式）
    val enumValues = remember(enumClass) {
        val enumValues = getEnumValues<T>()
        enumValues
    }

    Column(modifier = modifier) {
        // 标签
        if (label.isNotEmpty()) {
            Text(
                text = if (isRequired) "$label *" else label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        // 下拉选择框
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded && enabled }
        ) {
            OutlinedTextField(
                value = value?.name ?: "",
                onValueChange = { },
                readOnly = true,
                enabled = enabled,
                placeholder = { Text(placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                // 空选项（如果不是必需的）
                if (!isRequired) {
                    DropdownMenuItem(
                        text = { Text("无") },
                        onClick = {
                            onValueChange(null)
                            expanded = false
                        }
                    )
                }

                // 枚举值选项
                enumValues.forEach { enumValue ->
                    DropdownMenuItem(
                        text = { Text(enumValue.name) },
                        onClick = {
                            onValueChange(enumValue)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
