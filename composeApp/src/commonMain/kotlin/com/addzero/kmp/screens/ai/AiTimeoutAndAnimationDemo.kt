package com.addzero.kmp.screens.ai

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.processor.annotation.Route

/**
 * AI超时配置和思考动画演示
 */
@Composable
@Route("界面演示", "AI超时和思考动画")
fun AiTimeoutAndAnimationDemo() {
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
            text = "⏰ AI超时配置和思考动画",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.PrimaryPink
        )
        
        // 超时配置说明
        TimeoutConfigurationCard()
        
        HorizontalDivider()
        
        // 思考动画展示
        ThinkingAnimationShowcase()
        
        HorizontalDivider()
        
        // 错误处理说明
        ErrorHandlingCard()
        
        HorizontalDivider()
        
        // 使用建议
        UsageRecommendations()
    }
}

@Composable
private fun TimeoutConfigurationCard() {
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
                    Icons.Default.Timer,
                    contentDescription = null,
                    tint = LabubuColors.PrimaryPink,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Ktor超时配置优化",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    🚀 针对AI接口的超时优化：
                    
                    • ⏱️ 请求超时：5分钟 (300秒)
                    • 🔌 连接超时：30秒
                    • 📡 Socket超时：5分钟 (300秒)
                    
                    这些设置专门针对AI接口的特点进行了优化，确保即使是复杂的AI推理任务也能正常完成。
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
            
            // 配置代码展示
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = LabubuColors.SoftGray
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "配置代码：",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = LabubuColors.DarkText
                    )
                    Text(
                        text = """
install(HttpTimeout) {
    requestTimeoutMillis = 5.minutes.inWholeMilliseconds
    connectTimeoutMillis = 30_000
    socketTimeoutMillis = 5.minutes.inWholeMilliseconds
}
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace
                        ),
                        color = LabubuColors.DarkText
                    )
                }
            }
        }
    }
}

@Composable
private fun ThinkingAnimationShowcase() {
    var showBasicAnimation by remember { mutableStateOf(false) }
    var showAdvancedAnimation by remember { mutableStateOf(false) }
    
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = LabubuColors.SoftBlue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "AI思考动画展示",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    🎪 动画特性：
                    
                    • 🤖 可爱的AI头像动画
                    • 💭 思考气泡效果
                    • ✨ 跳动的彩色点点
                    • 🎨 Labubu风格设计
                    • 📱 流畅的进入/退出动画
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
            
            // 基础动画演示
            Text(
                text = "基础思考动画：",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LabubuColors.DarkText
            )
            
            AiThinkingAnimation(
                isVisible = showBasicAnimation,
                modifier = Modifier.padding(8.dp)
            )
            
            Button(
                onClick = { showBasicAnimation = !showBasicAnimation },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LabubuColors.PrimaryPink
                )
            ) {
                Text(if (showBasicAnimation) "隐藏基础动画" else "显示基础动画")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 高级动画演示
            Text(
                text = "高级思考动画：",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LabubuColors.DarkText
            )
            
            AdvancedAiThinkingAnimation(
                isVisible = showAdvancedAnimation,
                thinkingText = "正在分析您的问题并生成最佳回答...",
                modifier = Modifier.padding(8.dp)
            )
            
            Button(
                onClick = { showAdvancedAnimation = !showAdvancedAnimation },
                colors = ButtonDefaults.buttonColors(
                    containerColor = LabubuColors.SecondaryPurple
                )
            ) {
                Text(if (showAdvancedAnimation) "隐藏高级动画" else "显示高级动画")
            }
        }
    }
}

@Composable
private fun ErrorHandlingCard() {
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
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = LabubuColors.AccentYellow,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "智能错误处理",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    🛡️ 错误处理机制：
                    
                    • ⏰ 超时错误：显示友好的超时提示
                    • 🔄 自动重试：可以重新发送消息
                    • 💬 用户友好：错误信息使用可爱的表情
                    • 🎯 状态管理：确保思考动画正确停止
                    • 📝 详细日志：开发者可以查看详细错误信息
                    
                    示例错误消息：
                    "抱歉，AI响应超时了，请稍后重试 ⏰"
                    "抱歉，发生了错误：网络连接失败 😔"
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun UsageRecommendations() {
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
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = LabubuColors.MintGreen,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "使用建议",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    💡 最佳实践建议：
                    
                    🎯 用户体验：
                    • 发送消息后立即显示思考动画
                    • 让用户知道AI正在处理请求
                    • 避免用户重复发送相同消息
                    
                    ⚙️ 技术配置：
                    • 根据AI服务的响应时间调整超时设置
                    • 监控API响应时间，优化用户体验
                    • 考虑添加取消请求的功能
                    
                    🎨 视觉设计：
                    • 思考动画应该足够明显但不干扰
                    • 保持与整体UI风格的一致性
                    • 考虑添加声音反馈（可选）
                    
                    📱 移动端优化：
                    • 确保动画在低性能设备上流畅运行
                    • 考虑电池消耗，适当优化动画复杂度
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}
