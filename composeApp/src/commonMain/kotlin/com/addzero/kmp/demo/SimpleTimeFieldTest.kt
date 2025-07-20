package com.addzero.kmp.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.form.date.AddTimeField
import kotlinx.datetime.LocalTime

@Composable
fun SimpleTimeFieldTest() {
    var selectedTime by remember { mutableStateOf<LocalTime?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "时间选择器测试",
            style = MaterialTheme.typography.headlineMedium
        )

        AddTimeField(
            value = selectedTime,
            onValueChange = { selectedTime = it },
            label = "选择时间",
            isRequired = true,
            placeholder = "请选择时间"
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
                        text = "选择的时间: $selectedTime",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
