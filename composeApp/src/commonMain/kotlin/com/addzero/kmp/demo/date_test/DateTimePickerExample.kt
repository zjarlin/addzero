package com.addzero.kmp.demo.date_test

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.form.date.AddDateTimeField
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * 日期时间选择器使用示例
 */
@Composable
@Route("组件示例", "日期时间选择器示例")
fun DateTimePickerExample() {
    var selectedDateTime by remember { mutableStateOf<LocalDateTime?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "日期时间选择器示例",
            style = MaterialTheme.typography.headlineMedium
        )

        HorizontalDivider()

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "选择日期和时间",
                    style = MaterialTheme.typography.titleMedium
                )

                AddDateTimeField(
                    value = selectedDateTime,
                    onValueChange = { selectedDateTime = it },
                    label = "预约时间",
                    isRequired = true,
                    placeholder = "请选择预约的日期和时间"
                )

                if (selectedDateTime != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "已选择的时间：",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = selectedDateTime.toString().replace('T', ' '),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { selectedDateTime = null },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("清空")
                    }

                    Button(
                        onClick = {
                            val now = Clock.System.now()
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                            selectedDateTime = now
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("设为当前时间")
                    }
                }
            }
        }

        Text(
            text = "功能特点：",
            style = MaterialTheme.typography.titleMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("• 同时选择日期和时间", style = MaterialTheme.typography.bodyMedium)
                Text("• 使用 Material 3 设计规范", style = MaterialTheme.typography.bodyMedium)
                Text("• 支持24小时制时间格式", style = MaterialTheme.typography.bodyMedium)
                Text("• 响应式布局，适配不同屏幕尺寸", style = MaterialTheme.typography.bodyMedium)
                Text("• 支持必填字段标识", style = MaterialTheme.typography.bodyMedium)
                Text("• 友好的用户交互体验", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
