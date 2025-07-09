package com.addzero.kmp.component.tree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 默认树节点渲染函数
 * 参考 Android 官方文档样式，提供现代化的选中效果
 *
 * @param nodeInfo 节点信息，包含标签、层级、是否可展开、是否已选中等
 * @param showType 是否显示节点类型标签，默认为false
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> DefaultNodeRender(
    nodeInfo: TreeNodeInfo<T>,
    showType: Boolean = false
) {
    // 根据节点名称和是否有子节点推断节点类型
    val nodeName = nodeInfo.label
    val hasChildren = nodeInfo.hasChildren
    val nodeType = NodeType.guess(nodeName, hasChildren)

    // 外层容器，处理间距
    // 使用 Surface 提供现代化的选中效果
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { nodeInfo.onNodeClick(nodeInfo.node) },
        shape = RoundedCornerShape(8.dp),
        color = when {
            // 选中状态 - 参考 Android 官方文档的荧光绿效果
            nodeInfo.isSelected -> Color(0xFF4CAF50).copy(alpha = 0.15f) // 荧光绿背景
            else -> Color.Transparent
        },
        tonalElevation = if (nodeInfo.isSelected) 1.dp else 0.dp,
        shadowElevation = 0.dp,
        onClick = { nodeInfo.onNodeClick(nodeInfo.node) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // 缩进空间
            if (nodeInfo.level > 0) {
                Spacer(modifier = Modifier.width((nodeInfo.level * 16).dp))
            }

            // 展开/折叠图标
            if (nodeInfo.hasChildren) {
                Icon(
                    imageVector = if (nodeInfo.isExpanded)
                        Icons.Default.KeyboardArrowDown
                    else
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = if (nodeInfo.isExpanded) "折叠" else "展开",
                    modifier = Modifier.size(20.dp),
                    tint = if (nodeInfo.isSelected)
                        Color(0xFF2E7D32) // 深绿色，与荧光绿背景形成对比
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Spacer(modifier = Modifier.width(20.dp))
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 节点类型图标
            Icon(
                imageVector = nodeInfo.icon ?: nodeType.getIcon(nodeInfo.isExpanded),
                contentDescription = null,
                tint = if (nodeInfo.isSelected) {
                    // 选中状态使用深绿色，与荧光绿背景形成对比
                    Color(0xFF2E7D32)
                } else {
                    // 未选中状态使用节点类型的原始颜色
                    nodeType.getColor()
                },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 节点标签
            Text(
                text = nodeInfo.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (nodeInfo.isSelected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (nodeInfo.isSelected) {
                    // 选中状态使用深绿色文本，参考 Android 官方文档
                    Color(0xFF1B5E20)
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )

            // 可选：显示节点类型
            if (showType) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = nodeType.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (nodeInfo.isSelected) {
                        Color(0xFF2E7D32).copy(alpha = 0.8f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
