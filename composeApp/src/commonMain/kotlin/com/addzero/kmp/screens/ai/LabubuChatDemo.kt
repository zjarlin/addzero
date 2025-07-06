package com.addzero.kmp.screens.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.processor.annotation.Route

/**
 * Labubu风格AI聊天界面演示
 */
@Composable
@Route("界面演示", "Labubu聊天风格")
fun LabubuChatDemo() {
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
            text = "🎨 Labubu风格AI聊天界面",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.PrimaryPink
        )
        
        // 设计说明
        DesignDescription()
        
        HorizontalDivider()
        
        // 颜色主题展示
        ColorThemeShowcase()
        
        HorizontalDivider()
        
        // 功能特性
        FeaturesList()
        
        HorizontalDivider()
        
        // 使用说明
        UsageInstructions()
    }
}

@Composable
private fun DesignDescription() {
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
            Text(
                text = "🌟 设计理念",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LabubuColors.DarkText
            )
            
            Text(
                text = """
                    Labubu风格的AI聊天界面采用了可爱、温馨的设计语言：
                    
                    • 🎨 渐变色彩：使用粉色、紫色、黄色等温暖色调
                    • 🔮 圆润造型：所有元素都采用圆角设计，营造柔和感
                    • ✨ 动画效果：心跳动画、脉冲效果、滑入动画等
                    • 😊 可爱元素：emoji表情、装饰点、渐变头像
                    • 💕 用户体验：直观的交互反馈和愉悦的视觉体验
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ColorThemeShowcase() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "🎨 Labubu色彩主题",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.DarkText
        )
        
        // 主要颜色
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorCard("主粉色", LabubuColors.PrimaryPink, Modifier.weight(1f))
            ColorCard("紫色", LabubuColors.SecondaryPurple, Modifier.weight(1f))
            ColorCard("黄色", LabubuColors.AccentYellow, Modifier.weight(1f))
        }
        
        // 辅助颜色
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorCard("蓝色", LabubuColors.SoftBlue, Modifier.weight(1f))
            ColorCard("薄荷绿", LabubuColors.MintGreen, Modifier.weight(1f))
            ColorCard("浅粉色", LabubuColors.LightPink, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ColorCard(
    name: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = if (color == LabubuColors.AccentYellow || color == LabubuColors.LightPink) {
                    LabubuColors.DarkText
                } else {
                    Color.White
                }
            )
        }
    }
}

@Composable
private fun FeaturesList() {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "✨ 功能特性",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.DarkText
        )
        
        val features = listOf(
            "🎭 可爱的头像系统" to "AI和用户都有独特的emoji头像，支持心跳动画",
            "💬 渐变聊天气泡" to "消息气泡采用渐变色彩，区分用户和AI消息",
            "🎪 入场动画效果" to "消息从侧边滑入，带有缓动效果",
            "🌈 渐变背景设计" to "整体背景采用柔和的渐变色彩",
            "💫 交互动画反馈" to "按钮脉冲、图标切换等丰富的交互动画",
            "🎨 圆润设计语言" to "所有元素都采用圆角设计，营造温馨感",
            "🎯 响应式布局" to "适配不同屏幕尺寸，保持最佳显示效果"
        )
        
        features.forEach { (title, description) ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = title.split(" ")[0],
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Column {
                        Text(
                            text = title.substringAfter(" "),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
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
            }
        }
    }
}

@Composable
private fun UsageInstructions() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = LabubuColors.SoftGray
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "📖 使用说明",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LabubuColors.DarkText
            )
            
            Text(
                text = """
                    1. 点击右上角的机器人按钮打开AI聊天界面
                    2. 界面会从右侧滑入，展现Labubu风格设计
                    3. 在输入框中输入消息，点击发送按钮
                    4. 享受可爱的动画效果和温馨的聊天体验
                    5. 点击关闭按钮或按ESC键关闭聊天界面
                    
                    💡 小贴士：
                    • 发送按钮会根据输入状态显示不同的动画
                    • 消息会带有入场动画，让对话更生动
                    • 头像会有心跳动画，增加可爱感
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}
