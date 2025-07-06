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
import com.addzero.kmp.annotation.Route


/**
 * Excel模板设计器演示
 */
@Composable
@Route("界面演示", "Excel模板设计器")
fun ExcelTemplateDesignerDemo() {
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
            text = "📊 Excel模板设计器演示",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        
        // 设计理念
        DesignPhilosophy()
        
        HorizontalDivider()
        
        // 使用场景
        UsageScenarios()
        
        HorizontalDivider()
        
        // 数据格式说明
        DataFormatExplanation()
        
        HorizontalDivider()
        
        // 操作流程
        OperationFlow()
        
        HorizontalDivider()
        
        // 技术优势
        TechnicalAdvantages()
    }
}

@Composable
private fun DesignPhilosophy() {
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
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFF0EA5E9),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "设计理念",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF0C4A6E)
                )
            }
            
            Text(
                text = """
                    🎯 简单易用的Excel模板数据设计器：
                    
                    • 🎨 专注核心需求：只设计Excel模板填充所需的固定格式
                    • 📋 两种数据类型：一维区域(vo)和二维区域(dtos)
                    • 🔧 可视化配置：通过表单界面配置字段，无需手写JSON
                    • ⚡ 实时预览：修改字段立即看到JSON结果
                    • 📊 Excel集成：直接对接templateFill方法
                    • 💾 模板管理：支持Excel模板的上传和管理
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF0C4A6E),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun UsageScenarios() {
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
                    Icons.Default.BusinessCenter,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "使用场景",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF14532D)
                )
            }
            
            val scenarios = listOf(
                "📈 财务报表" to "月度/季度财务数据填充",
                "👥 员工信息" to "员工基本信息和考勤数据",
                "📦 库存管理" to "商品信息和库存明细",
                "🎓 学生成绩" to "学生基本信息和各科成绩",
                "🏥 医疗记录" to "患者信息和检查结果",
                "🏭 生产报告" to "生产计划和实际产量数据"
            )
            
            scenarios.forEach { (title, description) ->
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
                        color = Color(0xFF14532D),
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF16A34A),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun DataFormatExplanation() {
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
                    Icons.Default.DataObject,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "数据格式说明",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF92400E)
                )
            }
            
            // 一维区域说明
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
                        text = "🔹 一维区域 (vo: Map<String, Any>)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF059669)
                    )
                    
                    Text(
                        text = "用于填充单个值的字段，如报表标题、生成日期、总计等",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1F2937)
                        )
                    ) {
                        Text(
                            text = """
{
  "一维区域": {
    "报表标题": "月度销售报告",
    "生成日期": "2024-01-01",
    "总金额": 50000
  }
}
                            """.trimIndent(),
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            ),
                            color = Color(0xFF34D399),
                            fontSize = 10.sp
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 二维区域说明
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
                        text = "🔸 二维区域 (dtos: List<Map<String, Any>>)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF7C3AED)
                    )
                    
                    Text(
                        text = "用于填充表格数据，如商品列表、员工信息等",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF1F2937)
                        )
                    ) {
                        Text(
                            text = """
{
  "二维区域": [
    {"商品名称": "商品A", "数量": 100, "单价": 50.0},
    {"商品名称": "商品B", "数量": 200, "单价": 30.0}
  ]
}
                            """.trimIndent(),
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            ),
                            color = Color(0xFF34D399),
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OperationFlow() {
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
                    text = "操作流程",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF581C87)
                )
            }
            
            val steps = listOf(
                "1️⃣ 定义一维字段" to "添加报表标题、日期等单值字段",
                "2️⃣ 定义二维字段" to "添加表格列字段，如商品名称、数量等",
                "3️⃣ 设置字段类型" to "选择文本、数字或布尔类型",
                "4️⃣ 预览JSON格式" to "右侧实时查看生成的JSON结构",
                "5️⃣ 上传Excel模板" to "上传要填充的Excel模板文件",
                "6️⃣ 调用填充方法" to "使用生成的vo和dtos调用templateFill"
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
private fun TechnicalAdvantages() {
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
                    Icons.Default.Speed,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "技术优势",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF064E3B)
                )
            }
            
            val advantages = listOf(
                "🎯 专注性" to "专门为Excel模板填充设计，功能精准",
                "🚀 简单性" to "界面简洁，操作直观，学习成本低",
                "⚡ 高效性" to "快速生成标准格式，提高开发效率",
                "🔧 可扩展" to "支持字段类型扩展和模板管理",
                "💾 状态管理" to "完整的ViewModel状态管理",
                "🎨 用户体验" to "实时预览，所见即所得",
                "📱 响应式" to "适配不同屏幕尺寸",
                "🔄 数据同步" to "字段修改实时同步到JSON预览"
            )
            
            advantages.forEach { (title, description) ->
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
                        modifier = Modifier.width(80.dp)
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
