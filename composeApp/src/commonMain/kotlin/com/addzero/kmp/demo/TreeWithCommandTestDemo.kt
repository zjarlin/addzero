package com.addzero.kmp.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.addzero.kmp.annotation.Route
import com.addzero.kmp.component.tree_command.AddTreeWithCommand
import com.addzero.kmp.component.tree_command.TreeCommand

/**
 * 🧪 AddTreeWithCommand 重构测试演示
 * 
 * 验证重构后的组件是否正常工作：
 * - 基本树渲染
 * - 搜索功能
 * - 展开/收起控制
 * - 多选模式
 * - 命令系统
 */

// 简单的测试数据结构
data class TestNode(
    val id: String,
    val name: String,
    val type: String = "default",
    val children: List<TestNode> = emptyList()
)

@Composable
@Route
fun TreeWithCommandTestDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🧪 AddTreeWithCommand 重构测试",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "验证重构后的组件功能是否正常",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider()

        // 🧪 基础功能测试
        BasicFunctionalityTest()
    }
}

/**
 * 🧪 基础功能测试
 */
@Composable
fun BasicFunctionalityTest() {
    val testData = remember { createTestData() }
    var filteredCount by remember { mutableStateOf(testData.size) }
    var lastCommand by remember { mutableStateOf<String?>(null) }
    var selectedCount by remember { mutableStateOf(0) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "🎯 功能测试面板",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 📊 状态显示
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "📊 实时状态",
                    style = MaterialTheme.typography.titleSmall
                )
                
                Text(
                    text = "显示节点: $filteredCount 个",
                    style = MaterialTheme.typography.bodySmall
                )
                
                Text(
                    text = "选中节点: $selectedCount 个",
                    style = MaterialTheme.typography.bodySmall
                )
                
                lastCommand?.let { command ->
                    Text(
                        text = "最后命令: $command",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // 🌳 测试组件
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
        ) {
            AddTreeWithCommand(
                items = testData,
                getId = { it.id },
                getLabel = { it.name },
                getChildren = { it.children },
                getIcon = { getTestIcon(it.type) },
                initiallyExpandedIds = setOf("1", "2"),
                commands = setOf(
                    TreeCommand.SEARCH,
                    TreeCommand.EXPAND_ALL,
                    TreeCommand.COLLAPSE_ALL,
                    TreeCommand.MULTI_SELECT
                ),
                onNodeClick = { node ->
                    println("✅ 点击节点: ${node.name}")
                },
                onItemsChanged = { filteredItems ->
                    filteredCount = filteredItems.size
                    println("✅ 过滤结果: ${filteredItems.size} 个节点")
                    filteredItems.forEach { item ->
                        println("   - ${item.name}")
                    }
                },
                onCommandInvoke = { command, data ->
                    lastCommand = when (command) {
                        TreeCommand.SEARCH -> "搜索切换"
                        TreeCommand.EXPAND_ALL -> "展开全部 (${(data as? Set<*>)?.size ?: 0} 个)"
                        TreeCommand.COLLAPSE_ALL -> "收起全部"
                        TreeCommand.MULTI_SELECT -> "多选模式切换"
                        else -> command.toString()
                    }
                    println("✅ 执行命令: $lastCommand")
                },
                onSelectionChange = { selectedNodes ->
                    selectedCount = selectedNodes.size
                    println("✅ 选择变化: ${selectedNodes.size} 个节点")
                },
                modifier = Modifier.padding(8.dp)
            )
        }

        // 💡 测试说明
        TestInstructions()
    }
}

/**
 * 💡 测试说明
 */
@Composable
fun TestInstructions() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "💡 测试指南",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "1. 点击搜索按钮，输入 'Node' 测试搜索功能",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "2. 点击展开/收起按钮测试控制功能",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "3. 启用多选模式测试选择功能",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "4. 观察上方状态面板的实时更新",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "5. 查看控制台输出验证回调函数",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 获取测试图标
 */
@Composable
fun getTestIcon(type: String): ImageVector {
    return when (type) {
        "folder" -> Icons.Default.Folder
        "file" -> Icons.Default.InsertDriveFile
        "config" -> Icons.Default.Settings
        else -> Icons.Default.Circle
    }
}

/**
 * 创建测试数据
 */
fun createTestData(): List<TestNode> {
    return listOf(
        TestNode("1", "Root Folder", "folder", children = listOf(
            TestNode("2", "Sub Folder A", "folder", children = listOf(
                TestNode("3", "File A1", "file"),
                TestNode("4", "File A2", "file"),
                TestNode("5", "Config A", "config")
            )),
            TestNode("6", "Sub Folder B", "folder", children = listOf(
                TestNode("7", "File B1", "file"),
                TestNode("8", "Deep Folder", "folder", children = listOf(
                    TestNode("9", "Deep File 1", "file"),
                    TestNode("10", "Deep File 2", "file")
                ))
            ))
        )),
        TestNode("11", "Another Root", "folder", children = listOf(
            TestNode("12", "Simple File", "file"),
            TestNode("13", "Another Config", "config")
        )),
        TestNode("14", "Standalone File", "file")
    )
}
