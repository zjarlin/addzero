package com.addzero.kmp.screens.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.background.ChatBackground
import com.addzero.kmp.component.background.ChatBackgroundConfig
import com.addzero.kmp.component.background.ChatBackgroundPresets
import com.addzero.kmp.component.background.ChatBackgroundType


/**
 * 聊天背景功能演示
 */
@Composable
@Route("界面演示", "聊天背景系统")
fun ChatBackgroundDemo() {
    val scrollState = rememberScrollState()
    var selectedPreset by remember { mutableStateOf(ChatBackgroundPresets.LabubuDefault) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 标题
        Text(
            text = "🎨 聊天背景系统",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.PrimaryPink
        )

        // 功能介绍
        BackgroundSystemIntro()

        HorizontalDivider()

        // 背景预览
        BackgroundPreview(selectedPreset)

        HorizontalDivider()

        // 预设背景展示
        PresetBackgroundsShowcase(
            selectedPreset = selectedPreset,
            onPresetSelected = { selectedPreset = it }
        )

        HorizontalDivider()

        // 技术特性
        TechnicalFeatures()

        HorizontalDivider()

        // 使用指南
        UsageGuide()
    }
}

@Composable
private fun BackgroundSystemIntro() {
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
                    Icons.Default.Palette,
                    contentDescription = null,
                    tint = LabubuColors.PrimaryPink,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "背景系统介绍",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }

            Text(
                text = """
                    🎨 参考Avatar组件设计模式的独立背景系统：
                    
                    • 🔧 组件化设计：独立的背景组件，易于复用和维护
                    • 🎭 多种背景类型：渐变、图片、纯色、图案等
                    • ⚙️ 灵活配置：透明度、覆盖层、缩放模式等
                    • 🎪 预设主题：多种精美的预设背景
                    • 🖼️ 自定义支持：支持用户上传自定义背景图片
                    • 📱 响应式设计：适配不同屏幕尺寸
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun BackgroundPreview(config: ChatBackgroundConfig) {
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
                text = "🖼️ 背景预览",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = LabubuColors.DarkText
            )

            // 背景预览区域
            ChatBackground(
                config = config,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // 模拟聊天内容
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 模拟AI消息
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White.copy(alpha = 0.9f)
                            )
                        ) {
                            Text(
                                text = "你好！我是Labubu AI 🤖",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    // 模拟用户消息
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = LabubuColors.PrimaryPink.copy(alpha = 0.9f)
                            )
                        ) {
                            Text(
                                text = "背景很漂亮！",
                                modifier = Modifier.padding(12.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PresetBackgroundsShowcase(
    selectedPreset: ChatBackgroundConfig,
    onPresetSelected: (ChatBackgroundConfig) -> Unit
) {
    val presets = listOf(
        "Labubu默认" to ChatBackgroundPresets.LabubuDefault,
        "粉色梦幻" to ChatBackgroundPresets.PinkDream,
        "蓝色清新" to ChatBackgroundPresets.BlueFresh,
        "薄荷绿" to ChatBackgroundPresets.MintGreen,
        "纯白简约" to ChatBackgroundPresets.PureWhite
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "🌈 预设背景",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.DarkText
        )

        presets.forEach { (name, config) ->
            PresetBackgroundCard(
                name = name,
                config = config,
                isSelected = selectedPreset == config,
                onClick = { onPresetSelected(config) }
            )
        }
    }
}

@Composable
private fun PresetBackgroundCard(
    name: String,
    config: ChatBackgroundConfig,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) LabubuColors.PrimaryPink.copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, LabubuColors.PrimaryPink)
        } else null,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 背景预览
            ChatBackground(
                config = config,
                modifier = Modifier.size(60.dp)
            ) {}

            // 背景信息
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = LabubuColors.DarkText
                )
                Text(
                    text = when (config.type) {
                        ChatBackgroundType.GRADIENT -> "渐变背景"
                        ChatBackgroundType.SOLID_COLOR -> "纯色背景"
                        else -> "其他类型"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = LabubuColors.LightText
                )
            }

            // 选中指示器
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "已选择",
                    tint = LabubuColors.PrimaryPink,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun TechnicalFeatures() {
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = LabubuColors.SoftBlue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "技术特性",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }

            val features = listOf(
                "🔧 组件化架构" to "参考Avatar设计，独立可复用",
                "🎨 多种背景类型" to "渐变、图片、纯色、图案、动画",
                "⚙️ 灵活配置系统" to "透明度、覆盖层、缩放模式",
                "📱 响应式设计" to "适配不同屏幕尺寸",
                "🎪 预设主题库" to "精美的预设背景选择",
                "🖼️ 自定义支持" to "用户可上传自定义图片",
                "💾 状态管理" to "ViewModel管理背景状态",
                "🎯 性能优化" to "异步加载和缓存机制"
            )

            features.forEach { (title, description) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = LabubuColors.DarkText,
                        modifier = Modifier.width(120.dp)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = LabubuColors.LightText,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun UsageGuide() {
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
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = LabubuColors.MintGreen,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "使用指南",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }

            Text(
                text = """
                    📖 如何使用背景系统：
                    
                    1. 🎨 点击聊天界面顶部的调色板按钮
                    2. 🌈 在预设背景中选择喜欢的样式
                    3. 🖼️ 或者输入自定义图片URL
                    4. ⚙️ 调整透明度和覆盖层设置
                    5. ✅ 点击完成应用新背景
                    
                    💡 开发者使用：
                    • 使用ChatBackground组件包装内容
                    • 通过ChatBackgroundConfig配置背景
                    • 使用ChatBackgroundViewModel管理状态
                    • 参考预设创建自定义背景
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}
