package com.addzero.kmp.demo.date_test

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.form.date.AddTimeField
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Route("时间选择器示例")
@Composable
fun TimePickerExample() {
    var selectedTime by remember {
        mutableStateOf<LocalTime?>(
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "时间选择器示例",
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
                    text = "选择时间",
                    style = MaterialTheme.typography.titleMedium
                )

                AddTimeField(
                    value = selectedTime,
                    onValueChange = { selectedTime = it },
                    label = "营业时间",
                    isRequired = true,
                    placeholder = "请选择营业时间"
                )

                if (selectedTime != null) {
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
                                text = "选择的时间信息",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "时间: ${selectedTime}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "格式化: ${
                                    selectedTime!!.hour.toString().padStart(2, '0')
                                }:${selectedTime!!.minute.toString().padStart(2, '0')}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "多个时间选择器",
                    style = MaterialTheme.typography.titleMedium
                )

                var startTime by remember { mutableStateOf<LocalTime?>(null) }
                var endTime by remember { mutableStateOf<LocalTime?>(null) }

                AddTimeField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = "开始时间",
                    placeholder = "请选择开始时间"
                )

                AddTimeField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = "结束时间",
                    placeholder = "请选择结束时间"
                )

                if (startTime != null && endTime != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "时间段信息",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "开始: ${
                                    startTime!!.hour.toString().padStart(2, '0')
                                }:${startTime!!.minute.toString().padStart(2, '0')}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Text(
                                text = "结束: ${
                                    endTime!!.hour.toString().padStart(2, '0')
                                }:${endTime!!.minute.toString().padStart(2, '0')}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}
