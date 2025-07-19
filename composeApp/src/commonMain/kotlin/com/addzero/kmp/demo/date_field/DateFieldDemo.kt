package com.addzero.kmp.demo.date_field

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.form.date.AddDateField
import com.addzero.kmp.component.form.date.DateType
import kotlinx.datetime.LocalDate

/**
 * 日期字段组件演示
 *
 * 展示 AddDateField 的新功能：
 * - 支持手动输入日期
 * - 支持点击日历图标选择日期
 * - 实时显示输入内容
 * - 输入格式校验
 * - 最终输出 LocalDate 类型
 */
@Composable
fun DateFieldDemo() {
    var selectedDate1 by remember { mutableStateOf<LocalDate?>(null) }
    var selectedDate2 by remember { mutableStateOf<LocalDate?>(LocalDate(2024, 1, 15)) }
    var selectedDate3 by remember { mutableStateOf<LocalDate?>(null) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 标题
        Text(
            text = "📅 日期字段组件演示",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        // 功能说明
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "✨ 新功能特性",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                val features = listOf(
                    "📝 支持手动输入日期（格式：yyyy-MM-dd）",
                    "📅 支持点击日历图标选择日期",
                    "👀 实时显示输入内容",
                    "✅ 自动格式校验",
                    "🔄 最终输出 LocalDate 类型"
                )
                
                features.forEach { feature ->
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        
        // 示例1：基础日期选择
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "示例1：基础日期选择",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                AddDateField(
                    value = selectedDate1,
                    onValueChange = { selectedDate1 = it },
                    label = "选择日期",
                    placeholder = "请输入或选择日期",
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 显示当前值
                Text(
                    text = "当前值：${selectedDate1?.toString() ?: "未选择"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // 示例2：带默认值的日期选择
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "示例2：带默认值的日期选择",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                AddDateField(
                    value = selectedDate2,
                    onValueChange = { selectedDate2 = it },
                    label = "出生日期",
                    isRequired = true,
                    placeholder = "请输入或选择出生日期",
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 显示当前值
                Text(
                    text = "当前值：${selectedDate2?.toString() ?: "未选择"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        
        // 示例3：禁用状态
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "示例3：禁用状态",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                AddDateField(
                    value = selectedDate3,
                    onValueChange = { selectedDate3 = it },
                    label = "禁用的日期字段",
                    enabled = false,
                    placeholder = "此字段已禁用",
                    modifier = Modifier.fillMaxWidth()
                )
                
                // 显示当前值
                Text(
                    text = "当前值：${selectedDate3?.toString() ?: "未选择"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
        
        // 使用说明
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "📖 使用说明",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                
                val instructions = listOf(
                    "1. 直接在输入框中输入日期，格式为 yyyy-MM-dd（如：2024-01-15）",
                    "2. 点击右侧的日历图标打开日期选择器",
                    "3. 输入过程中会实时显示输入内容",
                    "4. 只有格式正确的日期才会更新到外部状态",
                    "5. 最终传递给后台的是 LocalDate 类型"
                )
                
                instructions.forEach { instruction ->
                    Text(
                        text = instruction,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        
        // 底部间距
        Spacer(modifier = Modifier.height(32.dp))
    }
}
