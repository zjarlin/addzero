package site.addzero.screens.product

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import site.addzero.component.button.AddIconButton
import site.addzero.component.form.number.AddIntegerField
import site.addzero.component.form.text.AddTextField
import site.addzero.viewmodel.MqttMessageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MqttMessageScreen() {
    val viewModel = koinViewModel<MqttMessageViewModel>()
    val scope = rememberCoroutineScope()
    val receivedMessages by viewModel.receivedMessages.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 左侧：MQTT消息发送表单
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            MqttSenderForm(viewModel)
        }

        // 右侧：消息接收区域
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            MessageReceiverPanel(viewModel, receivedMessages)
        }
    }
}

@Composable
fun MqttSenderForm(viewModel: MqttMessageViewModel) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            value = viewModel.topic,
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

        // 按钮行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 订阅/取消订阅按钮
            AddIconButton(
                text = if (viewModel.isSubscribed) "取消订阅" else "订阅消息",
                imageVector = Icons.Default.Subscriptions,
                onClick = {
                    scope.launch {
                        viewModel.toggleSubscription()
                    }
                }
            )
            // 发送按钮
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

@Composable
fun MessageReceiverPanel(viewModel: MqttMessageViewModel, receivedMessages: List<String>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题
        Text(
            text = "消息接收",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        // 消息列表
        Card(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val scrollState = rememberScrollState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                if (receivedMessages.isEmpty()) {
                    Text(
                        text = "暂无消息",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    receivedMessages.forEach { message ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text(
                                text = message,
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}
