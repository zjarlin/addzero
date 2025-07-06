package com.addzero.kmp.screens.excel

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.processor.annotation.Route

/**
 * Excel元数据提取功能演示
 */
@Composable
@Route("界面演示", "元数据提取演示")
fun ExcelMetadataExtractionDemo() {
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
            text = "🔍 Excel元数据提取功能演示",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        
        // 功能概述
        FeatureOverview()
        
        HorizontalDivider()
        
        // 界面布局说明
        LayoutExplanation()
        
        HorizontalDivider()
        
        // 购物车功能
        ShoppingCartFeature()
        
        HorizontalDivider()
        
        // 使用流程
        UsageWorkflow()
        
        HorizontalDivider()
        
        // 技术实现
        TechnicalImplementation()
    }
}

@Composable
private fun FeatureOverview() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F9FF)
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
                    Icons.Default.Analytics,
                    contentDescription = null,
                    tint = Color(0xFF0EA5E9),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "功能概述",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF0C4A6E)
                )
            }
            
            Text(
                text = """
                    🎯 Excel元数据提取功能：
                    
                    • 🔍 智能提取：从多个Excel文件中提取元数据
                    • 🛒 购物车模式：类似电商购物车的批量处理体验
                    • 📊 状态跟踪：实时显示提取进度和状态
                    • 🎨 优化布局：调整设计区宽度，增加元数据面板
                    • ⚡ 批量处理：一次性处理多个相同格式的Excel文件
                    • 🔧 类型高亮：修复字段类型选择的视觉反馈
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF0C4A6E),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun LayoutExplanation() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0FDF4)
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
                    Icons.Default.ViewColumn,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "界面布局优化",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF14532D)
                )
            }
            
            // 布局示意图
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "📐 新的三栏布局 (35% + 35% + 30%):",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF14532D)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(0.35f).height(60.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFDCFCE7)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🎨 设计区\n(35%)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF14532D)
                                )
                            }
                        }
                        
                        Card(
                            modifier = Modifier.weight(0.35f).height(60.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1F2937)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "📄 JSON预览\n(35%)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White
                                )
                            }
                        }
                        
                        Card(
                            modifier = Modifier.weight(0.3f).height(60.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF8FAFC)
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🔍 元数据提取\n(30%)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF1E40AF)
                                )
                            }
                        }
                    }
                    
                    Text(
                        text = """
                            ✨ 布局优化说明：
                            • 设计区缩窄：只需要key-value输入，不占用过多空间
                            • JSON预览保持：实时预览生成的JSON格式
                            • 新增元数据面板：专门用于Excel文件的元数据提取
                        """.trimIndent(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF16A34A),
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun ShoppingCartFeature() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFEF3C7)
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
                    Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "购物车功能",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF92400E)
                )
            }
            
            val features = listOf(
                "📁 文件选择" to "从可选择的Excel文件列表中选择",
                "🛒 添加到购物车" to "点击购物车图标添加文件",
                "📊 状态显示" to "显示待处理、处理中、已完成状态",
                "🔄 批量处理" to "一键开始处理购物车中的所有文件",
                "🗑️ 移除功能" to "可以从购物车中移除不需要的文件",
                "✅ 完成标识" to "处理完成的文件有特殊标识"
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
                        color = Color(0xFF92400E),
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFD97706),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun UsageWorkflow() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3E8FF)
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
                    Icons.Default.Timeline,
                    contentDescription = null,
                    tint = Color(0xFF9333EA),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "使用流程",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF581C87)
                )
            }
            
            val steps = listOf(
                "1️⃣ 设计JSON结构" to "在左侧设计区域定义字段",
                "2️⃣ 预览JSON格式" to "中间区域实时查看JSON结构",
                "3️⃣ 添加Excel文件" to "在右侧面板添加要处理的Excel文件",
                "4️⃣ 选择文件到购物车" to "点击购物车图标添加文件",
                "5️⃣ 开始批量提取" to "点击播放按钮开始处理",
                "6️⃣ 查看处理状态" to "实时查看每个文件的处理进度",
                "7️⃣ 获取提取结果" to "处理完成后获取元数据结果"
            )
            
            steps.forEach { (step, description) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF581C87),
                        modifier = Modifier.width(120.dp)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF9333EA),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TechnicalImplementation() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFECFDF5)
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
                    Icons.Default.Code,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "技术实现",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF064E3B)
                )
            }
            
            val implementations = listOf(
                "🎨 类型高亮修复" to "FilterChip添加正确的颜色配置",
                "📊 状态管理" to "使用mutableStateListOf管理购物车状态",
                "🔄 异步处理" to "模拟元数据提取的异步处理过程",
                "🎯 布局优化" to "调整weight比例实现三栏布局",
                "💾 数据结构" to "新增MetadataExtractionItem数据类",
                "🛒 购物车逻辑" to "完整的添加、移除、清空功能",
                "📱 响应式UI" to "根据状态变化自动更新界面",
                "🔧 错误处理" to "完善的错误捕获和用户提示"
            )
            
            implementations.forEach { (title, description) ->
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
                        color = Color(0xFF064E3B),
                        modifier = Modifier.width(120.dp)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF059669),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
