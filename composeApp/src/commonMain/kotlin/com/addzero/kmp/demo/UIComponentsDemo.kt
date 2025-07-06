package com.addzero.kmp.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.processor.annotation.Route

@Composable
@Route("组件示例", "UI组件测试")
fun UIComponentsDemo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 标题
        Text(
            text = "UI组件测试页面",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // 描述
        Text(
            text = "这个页面展示了各种Material 3组件的示例",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // 按钮部分
        SectionTitle("按钮")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {}) {
                Text("主按钮")
            }

            ElevatedButton(onClick = {}) {
                Text("凸起按钮")
            }

            FilledTonalButton(onClick = {}) {
                Text("色调按钮")
            }

            OutlinedButton(onClick = {}) {
                Text("轮廓按钮")
            }

            TextButton(onClick = {}) {
                Text("文本按钮")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {}) {
                Icon(Icons.Default.Favorite, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("带图标按钮")
            }

            ExtendedFloatingActionButton(
                onClick = {},
                icon = { Icon(Icons.Default.Add, contentDescription = null) },
                text = { Text("扩展FAB") }
            )

            FloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = null)
            }

            SmallFloatingActionButton(onClick = {}) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // 文本输入部分
        SectionTitle("文本输入")

        var text1 by remember { mutableStateOf("") }
        var text2 by remember { mutableStateOf("") }
        var text3 by remember { mutableStateOf("") }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("标准输入框") },
                modifier = Modifier.fillMaxWidth()
            )

            TextField(
                value = text2,
                onValueChange = { text2 = it },
                label = { Text("填充输入框") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = text3,
                onValueChange = { text3 = it },
                label = { Text("带图标输入框") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                trailingIcon = { Icon(Icons.Default.Clear, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // 选择控件部分
        SectionTitle("选择控件")

        var checked1 by remember { mutableStateOf(false) }
        var checked2 by remember { mutableStateOf(true) }
        var radioOption by remember { mutableStateOf(1) }
        var sliderValue by remember { mutableStateOf(0.5f) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checked1, onCheckedChange = { checked1 = it })
                Spacer(Modifier.width(4.dp))
                Text("复选框1")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = checked2, onCheckedChange = { checked2 = it })
                Spacer(Modifier.width(4.dp))
                Text("复选框2")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = checked1, onCheckedChange = { checked1 = it })
                Spacer(Modifier.width(4.dp))
                Text("开关")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = radioOption == 1, onClick = { radioOption = 1 })
            Spacer(Modifier.width(4.dp))
            Text("选项1")
            Spacer(Modifier.width(16.dp))

            RadioButton(selected = radioOption == 2, onClick = { radioOption = 2 })
            Spacer(Modifier.width(4.dp))
            Text("选项2")
            Spacer(Modifier.width(16.dp))

            RadioButton(selected = radioOption == 3, onClick = { radioOption = 3 })
            Spacer(Modifier.width(4.dp))
            Text("选项3")
        }

        Column {
            Text("滑块: ${(sliderValue * 100).toInt()}%")
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                modifier = Modifier.fillMaxWidth()
            )
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // 卡片部分
        SectionTitle("卡片")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "标准卡片",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "这是一个标准卡片示例",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            ElevatedCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "凸起卡片",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "这是一个凸起卡片示例",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            OutlinedCard(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "轮廓卡片",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "这是一个轮廓卡片示例",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // 进度指示器部分
        SectionTitle("进度指示器")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator()

            CircularProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier.size(60.dp),
                strokeWidth = 6.dp
            )

            LinearProgressIndicator(
                modifier = Modifier.width(200.dp)
            )

            LinearProgressIndicator(
                progress = { 0.4f },
                modifier = Modifier.width(200.dp)
            )
        }

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

        // 徽章部分
        SectionTitle("徽章")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BadgedBox(
                badge = {
                    Badge { Text("8") }
                }
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }

            BadgedBox(
                badge = {
                    Badge { Text("99+") }
                }
            ) {
                Icon(
                    Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }

            BadgedBox(
                badge = {
                    Badge()
                }
            ) {
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}
