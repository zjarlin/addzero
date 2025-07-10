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
 * 🚀 重构后的 AddTreeWithCommand 演示
 * 
 * 展示新的设计理念：
 * - 头部和尾部内容在外部声明
 * - 只保留必要的内部插槽
 * - 使用 AddSearchBar 增强搜索体验
 * - 智能展开匹配节点的父节点
 */

// 示例数据结构
data class ProjectFileNode(
    val id: String,
    val name: String,
    val type: FileNodeType,
    val description: String = "",
    val children: List<ProjectFileNode> = emptyList()
)

enum class FileNodeType {
    PROJECT, MODULE, PACKAGE, CLASS, FUNCTION, PROPERTY, TEST
}

@Composable
@Route
fun TreeWithCommandRefactoredDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🚀 重构后的 AddTreeWithCommand 演示",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "新设计理念：外部声明 + 内部插槽 + 智能搜索",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider()

        // 🚀 重构后的使用方式
        RefactoredUsageDemo()
    }
}

/**
 * 🚀 重构后的使用方式演示
 */
@Composable
fun RefactoredUsageDemo() {
    val sampleData = remember { createProjectData() }
    var filteredCount by remember { mutableStateOf(sampleData.size) }
    var lastCommand by remember { mutableStateOf<String?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "🎯 功能完整的树组件",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        // 📊 状态信息
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "显示 $filteredCount 个节点",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            lastCommand?.let { command ->
                Text(
                    text = "最后执行: $command",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        ) {
            // 🚀 重构后的 AddTreeWithCommand
            AddTreeWithCommand(
                items = sampleData,
                getId = { it.id },
                getLabel = { it.name },
                getChildren = { it.children },
                getIcon = { node -> getProjectFileIcon(node.type) },
                initiallyExpandedIds = setOf("1", "2"),
                commands = setOf(
                    TreeCommand.SEARCH,
                    TreeCommand.EXPAND_ALL,
                    TreeCommand.COLLAPSE_ALL,
                    TreeCommand.MULTI_SELECT
                ),
                onNodeClick = { node ->
                    println("点击了节点: ${node.name}")
                },
                onItemsChanged = { filteredItems ->
                    filteredCount = filteredItems.size
                },
                onCommandInvoke = { command, data ->
                    lastCommand = when (command) {
                        TreeCommand.SEARCH -> "搜索"
                        TreeCommand.EXPAND_ALL -> "展开全部 (${(data as? Set<*>)?.size ?: 0} 个节点)"
                        TreeCommand.COLLAPSE_ALL -> "收起全部"
                        TreeCommand.MULTI_SELECT -> "多选模式"
                        else -> command.toString()
                    }
                    println("执行命令: $lastCommand")
                },
                onSelectionChange = { selectedNodes ->
                    println("选择了 ${selectedNodes.size} 个节点")
                },
                modifier = Modifier.padding(16.dp)
            )
        }

        // 💡 功能说明
        FeatureExplanation()
    }
}

/**
 * 💡 功能说明
 */
@Composable
fun FeatureExplanation() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "🎯 重构亮点",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "✅ 外部声明：工具栏、搜索栏、状态栏都在外部声明",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "✅ 内部插槽：只保留 contextMenu 等无法外部实现的功能",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "✅ 智能搜索：使用 AddSearchBar，支持智能展开匹配父节点",
                style = MaterialTheme.typography.bodySmall
            )
            
            Text(
                text = "✅ 命令系统：统一的命令处理机制，易于扩展",
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = "✅ 性能优化：减少不必要的重组和计算",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/**
 * 获取项目文件对应的图标
 */
@Composable
fun getProjectFileIcon(type: FileNodeType): ImageVector {
    return when (type) {
        FileNodeType.PROJECT -> Icons.Default.AccountTree
        FileNodeType.MODULE -> Icons.Default.Folder
        FileNodeType.PACKAGE -> Icons.Default.FolderOpen
        FileNodeType.CLASS -> Icons.Default.Code
        FileNodeType.FUNCTION -> Icons.Default.Functions
        FileNodeType.PROPERTY -> Icons.Default.DataObject
        FileNodeType.TEST -> Icons.Default.BugReport
    }
}

/**
 * 创建示例项目数据
 */
fun createProjectData(): List<ProjectFileNode> {
    return listOf(
        ProjectFileNode("1", "addzero-project", FileNodeType.PROJECT, children = listOf(
            ProjectFileNode("2", "composeApp", FileNodeType.MODULE, children = listOf(
                ProjectFileNode("3", "com.addzero.kmp", FileNodeType.PACKAGE, children = listOf(
                    ProjectFileNode("4", "component", FileNodeType.PACKAGE, children = listOf(
                        ProjectFileNode("5", "tree", FileNodeType.PACKAGE, children = listOf(
                            ProjectFileNode("6", "AddTree.kt", FileNodeType.CLASS, children = listOf(
                                ProjectFileNode("7", "AddTree", FileNodeType.FUNCTION, "主要的树组件函数"),
                                ProjectFileNode("8", "TreeNodeInfo", FileNodeType.CLASS, "节点信息数据类"),
                                ProjectFileNode("9", "DefaultNodeRender", FileNodeType.FUNCTION, "默认节点渲染")
                            )),
                            ProjectFileNode("10", "TreeViewModel.kt", FileNodeType.CLASS, children = listOf(
                                ProjectFileNode("11", "TreeViewModel", FileNodeType.CLASS, children = listOf(
                                    ProjectFileNode("12", "searchQuery", FileNodeType.PROPERTY, "搜索查询"),
                                    ProjectFileNode("13", "filteredItems", FileNodeType.PROPERTY, "过滤项目"),
                                    ProjectFileNode("14", "performSearch", FileNodeType.FUNCTION, "执行搜索")
                                ))
                            ))
                        )),
                        ProjectFileNode("15", "tree_command", FileNodeType.PACKAGE, children = listOf(
                            ProjectFileNode("16", "AddTreeWithCommand.kt", FileNodeType.CLASS, children = listOf(
                                ProjectFileNode("17", "AddTreeWithCommand", FileNodeType.FUNCTION, "命令树组件"),
                                ProjectFileNode("18", "TreeCommand", FileNodeType.CLASS, "树命令枚举"),
                                ProjectFileNode("19", "CommandToolbar", FileNodeType.FUNCTION, "命令工具栏")
                            ))
                        ))
                    )),
                    ProjectFileNode("20", "demo", FileNodeType.PACKAGE, children = listOf(
                        ProjectFileNode("21", "TreeWithCommandRefactoredDemo.kt", FileNodeType.CLASS, children = listOf(
                            ProjectFileNode("22", "TreeWithCommandRefactoredDemo", FileNodeType.FUNCTION, "重构演示"),
                            ProjectFileNode("23", "RefactoredUsageDemo", FileNodeType.FUNCTION, "使用演示")
                        ))
                    ))
                ))
            )),
            ProjectFileNode("24", "shared", FileNodeType.MODULE, children = listOf(
                ProjectFileNode("25", "com.addzero.kmp.entity", FileNodeType.PACKAGE, children = listOf(
                    ProjectFileNode("26", "UserProfile.kt", FileNodeType.CLASS, children = listOf(
                        ProjectFileNode("27", "UserProfile", FileNodeType.CLASS, "用户配置接口"),
                        ProjectFileNode("28", "username", FileNodeType.PROPERTY, "用户名"),
                        ProjectFileNode("29", "email", FileNodeType.PROPERTY, "邮箱")
                    ))
                ))
            )),
            ProjectFileNode("30", "test", FileNodeType.MODULE, children = listOf(
                ProjectFileNode("31", "TreeTest.kt", FileNodeType.TEST, children = listOf(
                    ProjectFileNode("32", "testTreeRendering", FileNodeType.FUNCTION, "测试树渲染"),
                    ProjectFileNode("33", "testSearchFunction", FileNodeType.FUNCTION, "测试搜索功能")
                ))
            ))
        ))
    )
}
