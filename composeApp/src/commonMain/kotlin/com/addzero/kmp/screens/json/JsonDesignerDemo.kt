package com.addzero.kmp.screens.json

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.addzero.kmp.annotation.Route


/**
 * JSON设计器功能演示
 */
@Composable
@Route("界面演示", "JSON设计器演示")
fun JsonDesignerDemo() {
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
            text = "🔧 JSON设计器功能演示",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )

        // 功能介绍
        FeatureIntroduction()

        HorizontalDivider()

        // 使用示例
        UsageExamples()

        HorizontalDivider()

        // 双向编辑说明
        BidirectionalEditingInfo()

        HorizontalDivider()

        // Excel模板管理
        ExcelTemplateManagement()

        HorizontalDivider()

        // 技术特性
        TechnicalFeatures()
    }
}

@Composable
private fun FeatureIntroduction() {
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
                    Icons.Default.Build,
                    contentDescription = null,
                    tint = Color(0xFF0EA5E9),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "功能介绍",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF0C4A6E)
                )
            }

            Text(
                text = """
                    🎯 JSON设计器是一个可视化的JSON构建工具：
                    
                    • 🎨 可视化构建：通过图形界面构建复杂的JSON结构
                    • 🔄 双向编辑：左侧树形结构和右侧文本编辑器实时同步
                    • 📊 Excel集成：支持上传Excel模板，管理常用模板
                    • 🎯 类型支持：支持对象、数组、字符串、数字、布尔值、空值
                    • 💾 状态管理：使用ViewModel保存所有状态
                    • 🔧 实时预览：修改即时反映在JSON预览中
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF0C4A6E),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun UsageExamples() {
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
                    Icons.Default.Code,
                    contentDescription = null,
                    tint = Color(0xFF16A34A),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "使用示例",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF14532D)
                )
            }

            // 对象示例
            ExampleCard(
                title = "创建对象",
                description = "构建Map<String,Any>结构",
                example = """
{
  "name": "张三",
  "age": 25,
  "isActive": true
}
                """.trimIndent(),
                steps = listOf(
                    "点击'添加对象'按钮",
                    "添加字符串字段：name = 张三",
                    "添加数字字段：age = 25",
                    "添加布尔字段：isActive = true"
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 数组示例
            ExampleCard(
                title = "创建数组",
                description = "构建List<Map<String,Any>>结构",
                example = """
[
  {"天气": "晴天", "温度": "25°C"},
  {"天气": "雨天", "温度": "18°C"}
]
                """.trimIndent(),
                steps = listOf(
                    "点击'添加数组'按钮",
                    "在数组中添加对象",
                    "为每个对象添加字段",
                    "重复添加更多对象"
                )
            )
        }
    }
}

@Composable
private fun ExampleCard(
    title: String,
    description: String,
    example: String,
    steps: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF16A34A).copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF14532D)
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF16A34A)
            )

            // JSON示例
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1F2937)
                )
            ) {
                Text(
                    text = example,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    ),
                    color = Color(0xFF10B981)
                )
            }

            // 操作步骤
            Column {
                Text(
                    text = "操作步骤：",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF14532D)
                )
                steps.forEachIndexed { index, step ->
                    Text(
                        text = "${index + 1}. $step",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF16A34A),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BidirectionalEditingInfo() {
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
                    Icons.Default.SwapHoriz,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "双向编辑功能",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF92400E)
                )
            }

            Text(
                text = """
                    🔄 左右两侧实时同步：
                    
                    • 🌳 左侧树形结构：可视化编辑JSON结构
                      - 点击展开/收起节点
                      - 直接编辑键名和值
                      - 拖拽添加新元素
                      - 删除不需要的节点
                    
                    • 📝 右侧文本编辑器：直接编辑JSON文本
                      - 支持语法高亮
                      - 实时语法检查
                      - 格式化显示
                      - 错误提示
                    
                    • ⚡ 实时同步：任一侧的修改都会立即反映到另一侧
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF92400E),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ExcelTemplateManagement() {
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
                    Icons.Default.TableChart,
                    contentDescription = null,
                    tint = Color(0xFF9333EA),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Excel模板管理",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF581C87)
                )
            }

            Text(
                text = """
                    📊 Excel文件管理功能：
                    
                    • 📤 多文件上传：支持同时上传多个Excel文件
                    • ⭐ 常用模板：可以将常用的Excel保存为模板
                    • 🗂️ 模板分类：区分普通上传和常用模板
                    • 🗑️ 删除管理：可以删除不需要的文件
                    • 💾 状态持久化：所有上传状态都保存在ViewModel中
                    
                    📝 使用场景：
                    • 数据导入模板管理
                    • 报表格式保存
                    • 批量数据处理
                    • 模板复用
                """.trimIndent(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF581C87),
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TechnicalFeatures() {
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
                    Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "技术特性",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF064E3B)
                )
            }

            val features = listOf(
                "🏗️ MVVM架构" to "使用ViewModel管理状态，清晰的数据流",
                "🔄 响应式UI" to "Compose响应式编程，状态变化自动更新UI",
                "📱 组件化设计" to "模块化组件，易于维护和扩展",
                "🎯 类型安全" to "Kotlin类型系统，编译时错误检查",
                "💾 状态管理" to "完整的状态持久化和恢复机制",
                "🔧 可扩展性" to "易于添加新的JSON类型和功能",
                "🎨 Material Design" to "遵循Material Design设计规范",
                "⚡ 性能优化" to "LazyColumn等优化组件，流畅的用户体验"
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
