package com.addzero.kmp.component.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 🌟 荧光效果演示组件
 * 
 * 展示卡片悬浮时的荧光色边框效果
 */
@Composable
fun GlowEffectDemo() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "🌟 荧光效果演示",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "将鼠标悬浮在卡片上查看荧光边框效果",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // 展示所有卡片类型的荧光效果
        items(MellumCardType.allTypes) { cardType ->
            GlowCard(cardType = cardType)
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "🎨 荧光色配色方案",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // 显示每种卡片的荧光色
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MellumCardType.allTypes.forEach { cardType ->
                    ColorInfo(cardType = cardType)
                }
            }
        }
    }
}

/**
 * 荧光卡片组件
 */
@Composable
private fun GlowCard(cardType: MellumCardType) {
    AddJetBrainsMellumCard(
        onClick = { println("${cardType.name} 卡片被点击") },
        modifier = Modifier.fillMaxWidth(),
        backgroundType = cardType
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = when (cardType) {
                    MellumCardType.Purple -> Icons.Default.Psychology
                    MellumCardType.Blue -> Icons.Default.Code
                    MellumCardType.Teal -> Icons.Default.CloudUpload
                    MellumCardType.Orange -> Icons.Default.Warning
                    MellumCardType.Light -> Icons.Default.LightMode
                    MellumCardType.Dark -> Icons.Default.DarkMode
                    MellumCardType.Rainbow -> Icons.Default.Palette
                    else -> Icons.Default.Circle
                },
                contentDescription = cardType.name,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${cardType.name} 荧光卡片",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "悬浮查看${getGlowColorName(cardType)}荧光效果",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "这是${cardType.name}类型的卡片，具有独特的荧光边框效果。当鼠标悬浮时，卡片周围会出现${getGlowColorName(cardType)}的荧光色边框。",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

/**
 * 颜色信息组件
 */
@Composable
private fun ColorInfo(cardType: MellumCardType) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 颜色预览
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    color = cardType.hoverColor,
                    shape = CircleShape
                )
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column {
            Text(
                text = "${cardType.name} 荧光色",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = getColorHex(cardType.hoverColor),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * 获取荧光色名称
 */
private fun getGlowColorName(cardType: MellumCardType): String {
    return when (cardType) {
        MellumCardType.Purple -> "青色"
        MellumCardType.Blue -> "亮青色"
        MellumCardType.Teal -> "荧光绿"
        MellumCardType.Orange -> "荧光橙"
        MellumCardType.Light -> "蓝色"
        MellumCardType.Dark -> "白色"
        MellumCardType.Rainbow -> "荧光紫红"
        else -> "未知"
    }
}

/**
 * 获取颜色的十六进制值
 */
private fun getColorHex(color: Color): String {
    val red = (color.red * 255).toInt()
    val green = (color.green * 255).toInt()
    val blue = (color.blue * 255).toInt()
    return "#${red.toString(16).padStart(2, '0').uppercase()}" +
           "${green.toString(16).padStart(2, '0').uppercase()}" +
           "${blue.toString(16).padStart(2, '0').uppercase()}"
}

/**
 * 🎯 快速测试组件
 * 
 * 用于快速测试单个卡片的荧光效果
 */
@Composable
fun QuickGlowTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🎯 快速荧光测试",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        // 紫色卡片测试
        JetBrainsMellumCards.KoogAgentCard(
            onClick = { println("Koog Agent 卡片被点击") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = "AI",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Building Better Agents",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "What's New in Koog 0.3.0",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        
        // 蓝色卡片测试
        JetBrainsMellumCards.HackathonCard(
            onClick = { println("Hackathon 卡片被点击") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Code,
                    contentDescription = "Code",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Google x JetBrains",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Hackathon '25",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
        
        // 青绿色卡片测试
        JetBrainsMellumCards.DeployMellumCard(
            onClick = { println("Deploy Mellum 卡片被点击") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.CloudUpload,
                    contentDescription = "Deploy",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Deploy JetBrains Mellum",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your Way",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
