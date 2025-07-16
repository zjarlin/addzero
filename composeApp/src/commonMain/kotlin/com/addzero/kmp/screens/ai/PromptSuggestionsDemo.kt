package com.addzero.kmp.screens.ai

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.addzero.kmp.generated.isomorphic.SysAiPromptIso

/**
 * 🎨 提示词建议组件演示
 * 
 * 展示美化后的提示词建议组件效果
 */
@Composable
fun PromptSuggestionsDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "🎨 提示词建议组件演示",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "将鼠标悬浮在提示词卡片上查看完整内容",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // 模拟提示词数据
        val samplePrompts = listOf(
            SysAiPromptIso(
                id = 1,
                title = "代码优化专家",
                content = "请帮我优化这段代码，提高性能和可读性，并添加必要的注释说明。重点关注算法复杂度、内存使用和代码结构。"
            ),
            SysAiPromptIso(
                id = 2,
                title = "技术文档写手",
                content = "帮我写一份详细的技术文档，包括功能介绍、使用方法、API说明和示例代码。确保文档结构清晰，易于理解。"
            ),
            SysAiPromptIso(
                id = 3,
                title = "Bug分析师",
                content = "分析这个错误的原因，提供详细的解决方案和预防措施。包括错误复现步骤、根本原因分析和修复建议。"
            ),
            SysAiPromptIso(
                id = 4,
                title = "代码审查员",
                content = "对这段代码进行全面的审查，指出潜在问题、安全隐患和改进建议。关注代码质量、性能和最佳实践。"
            ),
            SysAiPromptIso(
                id = 5,
                title = "架构设计师",
                content = "设计一个可扩展、高性能的系统架构，考虑微服务、数据库设计、缓存策略、负载均衡和容错机制等方面。"
            ),
            SysAiPromptIso(
                id = 6,
                title = "测试工程师",
                content = "为这个功能编写完整的测试用例，包括单元测试、集成测试和端到端测试。确保测试覆盖率和测试质量。"
            )
        )
        
        // 使用LabubuPromptSuggestions组件
        LabubuPromptSuggestions(
            prompts = samplePrompts,
            onPromptSelected = { prompt ->
                println("选择了提示词: ${prompt.title}")
                println("内容: ${prompt.content}")
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 说明文字
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "✨ 功能特性",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                val features = listOf(
                    "🎨 JetBrains风格荧光卡片",
                    "📦 使用高阶组件AddMultiColumnContainer",
                    "🖱️ 悬浮显示完整文字内容",
                    "🎯 智能图标和副标题匹配",
                    "🌈 4种不同颜色的卡片类型",
                    "⚡ 流畅的动画和交互效果",
                    "📱 响应式双列布局",
                    "🎭 参考HackathonCard的设计风格"
                )
                
                features.forEach { feature ->
                    Text(
                        text = feature,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * 🧪 简单测试组件
 */
@Composable
fun SimplePromptTest() {
    val testPrompts = listOf(
        SysAiPromptIso(
            id = 1,
            title = "短提示",
            content = "这是一个短提示"
        ),
        SysAiPromptIso(
            id = 2,
            title = "长提示",
            content = "这是一个非常长的提示词内容，用于测试悬浮提示功能是否正常工作，当文字超过30个字符时应该显示悬浮提示框"
        )
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "🧪 提示词测试",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LabubuPromptSuggestions(
            prompts = testPrompts,
            onPromptSelected = { prompt ->
                println("测试选择: ${prompt.content}")
            }
        )
    }
}
