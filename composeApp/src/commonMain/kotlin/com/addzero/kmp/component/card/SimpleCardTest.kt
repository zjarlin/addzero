package com.addzero.kmp.component.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.card.MellumCardType.*

/**
 * 🧪 简单的卡片测试组件
 * 
 * 用于测试卡片组件的基本功能和文字显示
 */
@Composable
fun SimpleCardTest() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🧪 卡片组件测试",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        // 测试不同类型的卡片
        MellumCardType.values().forEach { cardType ->
            TestCard(
                title = "${cardType.name} 卡片",
                description = "这是${cardType.name}类型的测试卡片，用于验证文字显示效果。",
                cardType = cardType
            )
        }
    }
}

/**
 * 测试卡片组件
 */
@Composable
private fun TestCard(
    title: String,
    description: String,
    cardType: MellumCardType
) {
    AddJetBrainsMellumCard(
        onClick = { println("$title 被点击") },
        modifier = Modifier.fillMaxWidth(),
        backgroundType = cardType
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = when (cardType) {
                    Purple -> Icons.Default.Psychology
                    Blue -> Icons.Default.Code
                    Teal -> Icons.Default.CloudUpload
                    Orange -> Icons.Default.Warning
                    MellumCardType.Light -> Icons.Default.LightMode
                    Dark -> Icons.Default.DarkMode
                    Rainbow -> Icons.Default.DarkMode
                },
                contentDescription = title,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
