package com.addzero.kmp.component.form.number

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route

/**
 * 数字输入框使用示例
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Route
fun NumberFieldExample() {
    var integerValue by remember { mutableStateOf("") }
    var positiveIntegerValue by remember { mutableStateOf("") }
    var decimalValue by remember { mutableStateOf("") }
    var moneyValue by remember { mutableStateOf("") }
    var percentageValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "数字输入框示例",
            style = MaterialTheme.typography.headlineMedium
        )

        // 整数输入框示例
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD),
                            Color(0xFFBBDEFB)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "整数输入框",
                    style = MaterialTheme.typography.titleMedium
                )

                // 基础整数输入
                AddIntegerField(
                    value = integerValue,
                    onValueChange = { integerValue = it },
                    label = "整数（允许负数）"
                )

                // 正整数输入
                AddIntegerField(
                    value = positiveIntegerValue,
                    onValueChange = { positiveIntegerValue = it },
                    label = "正整数",
                    allowNegative = false
                )
            }
        }

        // 小数输入框示例
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFF3E5F5),
                            Color(0xFFE1BEE7)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "小数输入框",
                    style = MaterialTheme.typography.titleMedium
                )

                // 基础小数输入
                AddDecimalField(
                    value = decimalValue,
                    onValueChange = { decimalValue = it },
                    label = "小数"
                )
            }
        }

        // 金额输入框示例
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFE8F5E8),
                            Color(0xFFC8E6C9)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "金额输入框",
                    style = MaterialTheme.typography.titleMedium
                )

                // 基础金额输入
                AddMoneyField(
                    value = moneyValue,
                    onValueChange = { moneyValue = it },
                    label = "金额",
                    currency = "¥"
                )
            }
        }

        // 百分比输入框示例
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFFFF3E0),
                            Color(0xFFFFCC80)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "百分比输入框",
                    style = MaterialTheme.typography.titleMedium
                )

                // 基础百分比输入
                AddPercentageField(
                    value = percentageValue,
                    onValueChange = { percentageValue = it },
                    label = "百分比"
                )
            }
        }

        // 当前值显示
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFF5F5F5),
                            Color(0xFFE0E0E0)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "当前值",
                    style = MaterialTheme.typography.titleMedium
                )

                Text("整数值: $integerValue")
                Text("正整数值: $positiveIntegerValue")
                Text("小数值: $decimalValue")
                Text("金额值: $moneyValue")
                Text("百分比值: $percentageValue")
            }
        }

        // 操作按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    integerValue = ""
                    positiveIntegerValue = ""
                    decimalValue = ""
                    moneyValue = ""
                    percentageValue = ""
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("清空所有")
            }

            Button(
                onClick = {
                    integerValue = "-123"
                    positiveIntegerValue = "456"
                    decimalValue = "123.45"
                    moneyValue = "999.99"
                    percentageValue = "85.50"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("填充示例")
            }
        }
    }
}
