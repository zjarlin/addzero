package com.addzero.kmp.screens.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.annotation.Route


/**
 * 回车键发送功能测试演示
 */
@Composable
@Route("界面演示", "回车发送测试")
fun EnterKeyTestDemo() {
    val scrollState = rememberScrollState()
    var testInput by remember { mutableStateOf("") }
    var testMessages by remember { mutableStateOf(listOf<String>()) }
    var debugInfo by remember { mutableStateOf("等待输入...") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题
        Text(
            text = "⌨️ 回车发送功能测试",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.PrimaryPink
        )
        
        // 问题说明
        ProblemDescription()
        
        HorizontalDivider()
        
        // 解决方案说明
        SolutionDescription()
        
        HorizontalDivider()
        
        // 测试区域
        TestArea(
            testInput = testInput,
            onInputChange = { 
                testInput = it
                debugInfo = "输入内容: '$it' (长度: ${it.length})"
            },
            onSend = {
                if (testInput.isNotBlank()) {
                    testMessages = testMessages + testInput
                    debugInfo = "✅ 发送成功: '$testInput'"
                    testInput = ""
                } else {
                    debugInfo = "❌ 输入为空，无法发送"
                }
            },
            enabled = testInput.isNotBlank(),
            debugInfo = debugInfo,
            testMessages = testMessages
        )
        
        HorizontalDivider()
        
        // 键盘快捷键说明
        KeyboardShortcuts()
    }
}

@Composable
private fun ProblemDescription() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LabubuColors.AccentYellow.copy(alpha = 0.1f)
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
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = LabubuColors.AccentYellow,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "问题描述",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    🐛 遇到的问题：
                    
                    • 按回车键时没有触发onSend函数
                    • 而是在输入框中换行了
                    • 键盘事件处理逻辑有问题
                    • onKeyEvent可能被其他事件处理器覆盖
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun SolutionDescription() {
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
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = LabubuColors.MintGreen,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "解决方案",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    🔧 修复措施：
                    
                    • 使用onPreviewKeyEvent替代onKeyEvent
                    • 优先处理回车键事件，阻止默认换行
                    • 改进事件消费逻辑，确保正确处理
                    • 添加焦点管理，提升用户体验
                    • 保持Shift+Enter的换行功能
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TestArea(
    testInput: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    enabled: Boolean,
    debugInfo: String,
    testMessages: List<String>
) {
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
                text = "🧪 测试区域",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LabubuColors.DarkText
            )
            
            // 调试信息
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = LabubuColors.SoftGray
                )
            ) {
                Text(
                    text = "🐛 调试信息: $debugInfo",
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = LabubuColors.DarkText
                )
            }
            
            // 测试消息列表
            if (testMessages.isNotEmpty()) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = LabubuColors.LightPink.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📝 发送的消息:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = LabubuColors.DarkText
                        )
                        testMessages.forEachIndexed { index, message ->
                            Text(
                                text = "${index + 1}. $message",
                                style = MaterialTheme.typography.bodySmall,
                                color = LabubuColors.DarkText
                            )
                        }
                    }
                }
            }
            
            // 测试输入区域
            LabubuInputArea(
                input = testInput,
                onInputChange = onInputChange,
                onSend = onSend,
                enabled = enabled
            )
            
            // 清空按钮
            if (testMessages.isNotEmpty()) {
                OutlinedButton(
                    onClick = { 
                        testMessages.toMutableList().clear()
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("清空测试消息")
                }
            }
        }
    }
}

@Composable
private fun KeyboardShortcuts() {
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
                    Icons.Default.Keyboard,
                    contentDescription = null,
                    tint = LabubuColors.SoftBlue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "键盘快捷键测试",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            val shortcuts = listOf(
                "Enter" to "发送消息 (应该触发onSend)",
                "Shift + Enter" to "换行 (应该在输入框中换行)",
                "Ctrl + Enter" to "换行 (备选换行方式)"
            )
            
            shortcuts.forEach { (key, description) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = key,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = LabubuColors.DarkText
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = LabubuColors.LightText
                    )
                }
            }
            
            Text(
                text = """
                    💡 测试说明：
                    1. 在上面的输入框中输入一些文字
                    2. 按回车键，应该看到消息被发送到列表中
                    3. 按Shift+回车，应该在输入框中换行
                    4. 观察调试信息了解事件处理情况
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall,
                color = LabubuColors.LightText,
                lineHeight = 18.sp
            )
        }
    }
}
