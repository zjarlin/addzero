package com.addzero.kmp.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.annotation.Route


/**
 * 键盘快捷键功能演示
 */
@Composable
@Route("界面演示", "键盘快捷键")
fun KeyboardShortcutsDemo() {
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
            text = "⌨️ 键盘快捷键功能",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.PrimaryPink
        )

        // 回车发送功能
        EnterToSendFeature()

        HorizontalDivider()

        // 快捷键列表
        ShortcutsList()

        HorizontalDivider()

        // 使用技巧
        UsageTips()

        HorizontalDivider()

        // 实际演示
        LiveDemo()
    }
}

@Composable
private fun EnterToSendFeature() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LabubuColors.LightPink
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.KeyboardReturn,
                    contentDescription = null,
                    tint = LabubuColors.PrimaryPink,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "回车发送消息",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }

            Text(
                text = """
                    🚀 新增功能特性：
                    
                    • ⌨️ 按回车键即可快速发送消息
                    • 📝 Shift+回车可以换行继续输入
                    • 🔧 Ctrl+回车也可以换行（兼容性）
                    • 🎯 只有在有内容时才能发送
                    • 💡 智能提示用户操作方式
                    
                    这个功能大大提升了聊天的便捷性，让用户可以像使用其他聊天应用一样自然地发送消息。
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ShortcutsList() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "⌨️ 快捷键列表",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LabubuColors.DarkText
            )

            val shortcuts = listOf(
                "Enter" to "发送消息",
                "Shift + Enter" to "换行",
                "Ctrl + Enter" to "换行（备选）",
                "Cmd + Shift + A" to "打开/关闭AI聊天",
                "Escape" to "关闭聊天界面"
            )

            shortcuts.forEach { (key, description) ->
                ShortcutItem(key, description)
            }
        }
    }
}

@Composable
private fun ShortcutItem(
    key: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 快捷键显示
        Box(
            modifier = Modifier
                .background(
                    LabubuColors.SoftGray,
                    RoundedCornerShape(8.dp)
                )
                .border(
                    1.dp,
                    LabubuColors.LightText.copy(alpha = 0.3f),
                    RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = key,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                ),
                color = LabubuColors.DarkText
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 功能描述
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = LabubuColors.LightText,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun UsageTips() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LabubuColors.SoftBlue.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = LabubuColors.SoftBlue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "使用技巧",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }

            Text(
                text = """
                    💡 高效使用技巧：
                    
                    1. 📝 输入消息后直接按回车发送，无需点击发送按钮
                    2. 📄 需要多行输入时，使用Shift+回车换行
                    3. ⚡ 空消息无法发送，确保输入有效内容
                    4. 🎯 输入框会显示操作提示，帮助新用户快速上手
                    5. 🔄 发送后输入框自动清空，准备下一条消息
                    
                    🎨 视觉反馈：
                    • 输入框边框会根据焦点状态变色
                    • 发送按钮会根据输入状态显示不同动画
                    • 占位符文字包含操作提示
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun LiveDemo() {
    var demoInput by remember { mutableStateOf("") }
    var demoMessages by remember { mutableStateOf(listOf<String>()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LabubuColors.MintGreen.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = LabubuColors.MintGreen,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "实际演示",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }

            Text(
                text = "在下面的输入框中试试回车发送功能：",
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText
            )

            // 演示消息列表
            if (demoMessages.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "发送的消息：",
                        style = MaterialTheme.typography.bodySmall,
                        color = LabubuColors.LightText
                    )
                    demoMessages.forEach { message ->
                        Text(
                            text = "• $message",
                            style = MaterialTheme.typography.bodyMedium,
                            color = LabubuColors.DarkText
                        )
                    }
                }
            }

            // 演示输入框
            LabubuInputArea(
                input = demoInput,
                onInputChange = { demoInput = it },
                onSend = {
                    if (demoInput.isNotBlank()) {
                        demoMessages = demoMessages + demoInput
                        demoInput = ""
                    }
                },
                enabled = demoInput.isNotBlank()
            )

            // 清空按钮
            if (demoMessages.isNotEmpty()) {
                TextButton(
                    onClick = { demoMessages = emptyList() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("清空演示消息")
                }
            }
        }
    }
}
