package com.addzero.kmp.screens.dict

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.addzero.kmp.component.button.AddEditDeleteButton
import com.addzero.kmp.isomorphic.SysDictItemIso

/**
 * 字典项卡片
 */
@Composable
fun DictItemCard(
    dictItem: SysDictItemIso,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val statusColor = if (dictItem.status == 1L)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error

    val statusText = if (dictItem.status == 1L) "启用" else "禁用"

    Card(
        modifier = Modifier.Companion.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Row(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Companion.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.Companion.weight(1f),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 状态指示器
                Box(
                    modifier = Modifier.Companion
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(statusColor)
                )

                Column {
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = dictItem.itemText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Companion.Medium
                        )

                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = statusText,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = statusColor.copy(alpha = 0.1f),
                                labelColor = statusColor
                            ),
                            border = null
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "值: ${dictItem.itemValue}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = "排序: ${dictItem.sortOrder ?: 0}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }

                    if (dictItem.description != null) {
                        Text(
                            text = dictItem.description ?: "",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            maxLines = 1,
                            overflow = TextOverflow.Companion.Ellipsis,
                            modifier = Modifier.Companion.padding(top = 4.dp)
                        )
                    }
                }
            }

            // 操作按钮
            AddEditDeleteButton(
                onEditClick = onEditClick, onDeleteClick = onDeleteClick,
            )
        }
    }
}
