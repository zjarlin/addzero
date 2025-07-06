package com.addzero.kmp.component.tree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 默认树节点渲染函数
 * 根据节点信息渲染树节点，包括展开/折叠图标、节点类型图标、标签文本以及可选的类型标签
 *
 * @param nodeInfo 节点信息，包含标签、层级、是否可展开、是否已选中等
 * @param showType 是否显示节点类型标签，默认为false
 */
@Composable
fun <T> DefaultNodeRender(
    nodeInfo: TreeNodeInfo<T>,
    showType: Boolean = false
) {
    // 只在节点选中时设置背景颜色，其他情况下保持透明
    val backgroundColor = when {
        nodeInfo.isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else -> Color.Transparent
    }

    // 根据节点名称和是否有子节点推断节点类型
    val nodeName = nodeInfo.label
    val hasChildren = nodeInfo.hasChildren
    val nodeType = NodeType.guess(nodeName, hasChildren)

    // 节点容器 - 只占用实际节点需要的空间，不扩散到整个宽度
    Box(
        modifier = Modifier
            // 需要fillMaxWidth来确保显示全宽，但仅在节点内容区域添加背景色
            .fillMaxWidth()
            .clickable { nodeInfo.onNodeClick(nodeInfo.node) }
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                // 背景色只应用于实际Row内容，不扩展到整个宽度
                .background(backgroundColor)
                .padding(4.dp)
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
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { nodeInfo.onNodeClick(nodeInfo.node) }
                )
            } else {
                Spacer(modifier = Modifier.width(20.dp))
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 节点类型图标
            Icon(
                imageVector = nodeInfo.icon ?: nodeType.getIcon(nodeInfo.isExpanded),
                contentDescription = null,
                tint = nodeType.getColor(),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 节点标签
            Text(
                text = nodeInfo.label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (nodeInfo.isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (nodeInfo.isSelected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface
            )

            // 可选：显示节点类型
            if (showType) {
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = nodeType.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
