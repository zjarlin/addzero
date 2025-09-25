package site.addzero.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.android.annotation.KoinViewModel
import org.koin.compose.viewmodel.koinViewModel
import site.addzero.annotation.Route
import site.addzero.component.button.AddIconButton
import site.addzero.component.form.number.AddIntegerField
import site.addzero.component.form.text.AddTextField
import site.addzero.viewmodel.MqttMessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
//@Route("工具", "MQTT消息发送")
//@Preview
fun MqttMessageScreen() {
    val viewModel = koinViewModel<MqttMessageViewModel>()
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题
        Text(
            text = "MQTT消息发送器",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // Broker Host字段
        AddTextField(
            value = viewModel.brokerHost,
            onValueChange = { viewModel.brokerHost = it },
            label = "Broker Host",
            placeholder = "例如: broker.emqx.io",
            modifier = Modifier.fillMaxWidth()
        )

        // Broker Port字段
        AddIntegerField(
            value = viewModel.brokerPort.toString(),
            onValueChange = { viewModel.brokerPort = it.toInt() },
            label = "Broker Port",
            placeholder = "例如: 1883",
            modifier = Modifier.fillMaxWidth()
        )

        // Topic字段
        AddTextField(
            value = viewModel.topic.ifBlank { "sensor/2/temperature" },
            onValueChange = { viewModel.topic = it },
            label = "Topic",
            placeholder = "例如: sensor/2/temperature",
            modifier = Modifier.fillMaxWidth()
        )

        // Message JSON编辑器
        Text(
            text = "Message (JSON格式)",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedTextField(
            value = viewModel.message,
            onValueChange = { viewModel.message = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            placeholder = { Text("输入JSON格式的消息内容") },
            supportingText = { Text("请输入有效的JSON格式内容") }
        )

        // 发送按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            AddIconButton(
                text = "发送消息",
                imageVector = Icons.Default.Send,
                onClick = {
                    scope.launch {
                        viewModel.sendMessage()
                    }
                }
            )
        }

        // 发送结果
        if (viewModel.sendResult.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (viewModel.sendResult.startsWith("成功"))
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = viewModel.sendResult,
                    modifier = Modifier.padding(16.dp),
                    color = if (viewModel.sendResult.startsWith("成功"))
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}
