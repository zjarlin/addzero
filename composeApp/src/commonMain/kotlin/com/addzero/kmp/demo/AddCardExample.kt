package com.addzero.kmp.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.card.AddCard
import com.addzero.kmp.component.card.AddCards
import com.addzero.kmp.component.card.MellumCardType

/**
 * 🎨 JetBrains Mellum卡片使用示例
 * 
 * 展示不同类型的卡片效果和使用方法
 */
@Composable
@Route
fun AddCardExample() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            Text(
                text = "🎨 JetBrains Mellum风格卡片",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "参考JetBrains官方设计的现代化材质卡片组件",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // 预设卡片示例
        item {
            Text(
                text = "预设样式卡片",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Koog Agent风格
                AddCards.KoogAgentCard(
                    onClick = { println("Koog Agent卡片被点击") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProductCardContent(
                        title = "Building Better Agents",
                        subtitle = "What's New in Koog 0.3.0",
                        icon = Icons.Default.Psychology,
                        description = "探索最新的AI Agent构建技术和最佳实践"
                    )
                }
                
                // Hackathon风格
                AddCards.HackathonCard(
                    onClick = { println("Hackathon卡片被点击") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProductCardContent(
                        title = "Google x JetBrains",
                        subtitle = "Hackathon '25",
                        icon = Icons.Default.Code,
                        description = "参与全球开发者盛会，展示你的创新项目"
                    )
                }
                
                // Deploy Mellum风格
                AddCards.DeployMellumCard(
                    onClick = { println("Deploy Mellum卡片被点击") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProductCardContent(
                        title = "Deploy JetBrains Mellum",
                        subtitle = "Your Way",
                        icon = Icons.Default.CloudUpload,
                        description = "灵活部署，随心所欲地管理你的开发环境"
                    )
                }
            }
        }
        
        // 所有类型展示
        item {
            Text(
                text = "所有卡片类型",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        
        items(MellumCardType.allTypes) { cardType ->
            AddCard(
                onClick = { println("${cardType.name}卡片被点击") },
                modifier = Modifier.fillMaxWidth(),
                backgroundType = cardType
            ) {
                SimpleCardContent(
                    title = "${cardType.name} Card",
                    description = "这是${cardType.name}类型的卡片示例，展示了不同的渐变背景效果。"
                )
            }
        }
        
        // 功能展示卡片
        item {
            Text(
                text = "功能展示",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 统计卡片
                AddCard(
                    onClick = { println("统计卡片被点击") },
                    modifier = Modifier.weight(1f),
                    backgroundType = MellumCardType.Purple
                ) {
                    StatCardContent(
                        value = "1,234",
                        label = "用户数量",
                        icon = Icons.Default.People,
                        trend = "+12%"
                    )
                }
                
                // 操作卡片
                AddCard(
                    onClick = { println("操作卡片被点击") },
                    modifier = Modifier.weight(1f),
                    backgroundType = MellumCardType.Blue
                ) {
                    ActionCardContent(
                        title = "快速部署",
                        icon = Icons.Default.RocketLaunch,
                        action = "立即开始"
                    )
                }
            }
        }
    }
}

/**
 * 产品卡片内容组件
 */
@Composable
fun ProductCardContent(
    title: String,
    subtitle: String,
    icon: ImageVector,
    description: String
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = LocalContentColor.current.copy(alpha = 0.9f),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LocalContentColor.current,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalContentColor.current.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = LocalContentColor.current.copy(alpha = 0.7f),
            lineHeight = 18.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 简单卡片内容组件
 */
@Composable
private fun SimpleCardContent(
    title: String,
    description: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = LocalContentColor.current
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = 0.8f),
            lineHeight = 20.sp
        )
    }
}

/**
 * 统计卡片内容组件
 */
@Composable
private fun StatCardContent(
    value: String,
    label: String,
    icon: ImageVector,
    trend: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = LocalContentColor.current.copy(alpha = 0.9f),
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = LocalContentColor.current
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = 0.8f)
        )
        Text(
            text = trend,
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF4ADE80), // 绿色表示增长
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * 操作卡片内容组件
 */
@Composable
private fun ActionCardContent(
    title: String,
    icon: ImageVector,
    action: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = LocalContentColor.current.copy(alpha = 0.9f),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = LocalContentColor.current
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = action,
            style = MaterialTheme.typography.bodyMedium,
            color = LocalContentColor.current.copy(alpha = 0.8f)
        )
    }
}
