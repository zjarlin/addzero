package com.addzero.kmp.screens.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.annotation.Route


/**
 * Labubu聊天界面新功能展示
 */
@Composable
@Route("界面演示", "Labubu聊天新功能")
fun LabubuChatFeatures() {
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
            text = "🎉 Labubu聊天界面新功能",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = LabubuColors.PrimaryPink
        )
        
        // 新聊天按钮功能
        NewChatButtonFeature()
        
        HorizontalDivider()
        
        // 圆角卡片优化
        RoundedCardOptimization()
        
        HorizontalDivider()
        
        // 动画效果展示
        AnimationShowcase()
        
        HorizontalDivider()
        
        // 使用指南
        UsageGuide()
    }
}

@Composable
private fun NewChatButtonFeature() {
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
                    Icons.Default.Add,
                    contentDescription = null,
                    tint = LabubuColors.PrimaryPink,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "新聊天按钮",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    ✨ 新增功能特性：
                    
                    • 🔄 一键清空聊天记录，开始全新对话
                    • 🎨 渐变背景设计，黄色到白色的径向渐变
                    • 🎪 动画反馈效果，点击时图标会切换并缩放
                    • 💫 智能状态管理，自动重置按钮状态
                    • 🎯 直观的用户体验，清晰的视觉反馈
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
            
            // 按钮预览
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    LabubuColors.AccentYellow.copy(alpha = 0.8f),
                                    Color.White.copy(alpha = 0.3f)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "新聊天",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RoundedCardOptimization() {
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
                    Icons.Default.CropFree,
                    contentDescription = null,
                    tint = LabubuColors.SoftBlue,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "圆角卡片优化",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    🎨 解决方案：
                    
                    • 🚫 移除Surface组件的默认白色背景
                    • 🎪 使用Box + clip + background实现圆角
                    • ✨ 保留阴影效果，增强视觉层次
                    • 🌈 渐变背景完美显示，无白底干扰
                    • 🎯 动画过渡更加流畅自然
                    
                    技术实现：
                    • shadow() - 添加阴影效果
                    • clip() - 裁剪为圆角形状
                    • background() - 应用渐变背景
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
            
            // 效果预览
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 修复前
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "修复前",
                        style = MaterialTheme.typography.bodySmall,
                        color = LabubuColors.LightText
                    )
                    Box(
                        modifier = Modifier
                            .size(60.dp, 40.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, LabubuColors.LightText, RoundedCornerShape(8.dp))
                    )
                }
                
                // 修复后
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "修复后",
                        style = MaterialTheme.typography.bodySmall,
                        color = LabubuColors.LightText
                    )
                    Box(
                        modifier = Modifier
                            .size(60.dp, 40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        LabubuColors.LightPink,
                                        Color.White
                                    )
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimationShowcase() {
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
                Text(
                    text = "✨",
                    fontSize = 24.sp
                )
                Text(
                    text = "动画效果展示",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = LabubuColors.DarkText
                )
            }
            
            Text(
                text = """
                    🎪 动画类型：
                    
                    • 🔄 图标切换动画：Add → Refresh
                    • 📏 缩放动画：点击时放大1.2倍
                    • 🎨 渐变背景：径向渐变效果
                    • ⏱️ 状态重置：300ms后自动恢复
                    • 🎯 流畅过渡：scaleIn/Out + fadeIn/Out
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
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
                    📖 如何使用新功能：
                    
                    1. 🤖 点击右上角机器人按钮打开AI聊天
                    2. ➕ 点击顶部栏的"+"按钮开始新聊天
                    3. 🗑️ 聊天记录会被清空，显示欢迎界面
                    4. 💬 开始全新的对话体验
                    5. ❌ 点击关闭按钮退出聊天界面
                    
                    💡 小贴士：
                    • 新聊天按钮有可爱的动画反馈
                    • 界面现在完全没有白底干扰
                    • 渐变背景在动画中保持完美显示
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = LabubuColors.DarkText,
                lineHeight = 20.sp
            )
        }
    }
}
